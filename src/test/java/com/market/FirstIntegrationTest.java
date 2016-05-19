package com.market;

import com.market.camel.MarketSpringContext;
import com.market.database.ExecutionDAO;
import com.market.database.OrderDAO;
import com.market.model.Execution;
import com.market.model.Order;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.market.camel.MarketRouteBuilder.*;

/**
 * Created by pizmak on 2016-05-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FirstIntegrationTest.FirstIntegrationTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class FirstIntegrationTest {
    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";

    @Autowired
    private ModelCamelContext camelContext;

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ExecutionDAO executionDAO;

    @EndpointInject(uri = "mock:" + JMS_EXECUTION_INFO)
    private MockEndpoint jmsEndPoint;

    @EndpointInject(uri = "mock:" + JMS_NEW_ORDERS)
    private MockEndpoint jmsStartPoint;

    private ProducerTemplate producerTemplate;



    @Before
    public void cleanDataBase() throws Exception {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
        camelContext.getRouteDefinition(MAIN_ROUTE_ENTRY).adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip(JMS_EXECUTION_INFO);
            }
        });
        jmsEndPoint.reset();

        camelContext.getRouteDefinition(WEB_ROUTE_ENTRY).adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip(JMS_NEW_ORDERS);
            }
        });
        jmsStartPoint.reset();
    }

    @Before
    public void setUpProducerTemplate() throws Exception {
        producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.start();
    }

    @Test
    public void testOrderSaved() {
        Assert.assertEquals(0, orderDAO.getAllOrders().size());

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 147");

        List<Order> allOpenOrders = orderDAO.getAllOrders();
        Assert.assertEquals(1,  allOpenOrders.size());
        Order order = allOpenOrders.get(0);
        Assert.assertEquals("SELL", order.getType());
        Assert.assertEquals(147, order.getQuantity());
    }

    @Test
    public void testExecutionCreated() {
        Assert.assertEquals(0, orderDAO.getAllOrders().size());

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 20");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 35");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 75");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 60");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 100");
    }

    @Test
    public void testExecutionSavedShouldDoOneExecution() throws InterruptedException {
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());
        jmsEndPoint.expectedMessageCount(1);

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 100");

        List<Execution> listOfAllExecutions = executionDAO.getListOfAllExecutions();
        Assert.assertEquals(1,listOfAllExecutions.size());
        Execution execution = listOfAllExecutions.get(0);

        final int quantityOfExecution = 100;
        Assert.assertEquals(quantityOfExecution,execution.getQuantity());

        jmsEndPoint.assertIsSatisfied();
    }

    @Test
    public void testExecutionShouldDoTwoExecutions(){
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 80");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 40");

        List<Integer> listOfAllQuantitiesOfAllExecutions = executionDAO.getListOfAllExecutions()
                .stream().map(e -> e.getQuantity()).collect(Collectors.toList());
        Assert.assertEquals(2,listOfAllQuantitiesOfAllExecutions.size());

        final int quantityOfFirstExecution = 80;
        final int quantityOfSecondExecution = 40;

        Assert.assertTrue(listOfAllQuantitiesOfAllExecutions.contains(quantityOfFirstExecution));
        Assert.assertTrue(listOfAllQuantitiesOfAllExecutions.contains(quantityOfSecondExecution));
    }

    @Test
    public void testUpdatingQuantityOfMachingOrdersAfterDoneExecution(){
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());
        Assert.assertEquals(0,orderDAO.getAllOrders().size());

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 80");

        List<Integer> listOfAllQuantitiesOfAllOrders = orderDAO.getAllOrders()
                .stream().map(e->e.getQuantity()).collect(Collectors.toList());

        final int quantityOfFirstOrderAfterExecution = 40;
        final int quantityOfSecondOrderAfterExecution = 0;

        Assert.assertTrue(listOfAllQuantitiesOfAllOrders.contains(quantityOfFirstOrderAfterExecution));
        Assert.assertTrue(listOfAllQuantitiesOfAllOrders.contains(quantityOfSecondOrderAfterExecution));

    }

    @Test
    public void testCorrectnessMessageAboutExecution() throws InterruptedException {
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());
        Assert.assertEquals(0,orderDAO.getAllOrders().size());

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 80");

        String templateOfGoodEndedMessageAboutExecutionWhichIsSentToJmsEndQueue="exec_id=Optional\\[[0-9]{1,}\\]sell_id=[0-9]{1,}buy_id=[0-9]{1,}qty=[0-9]{1,}";

        jmsEndPoint.expectedMessageCount(1);
        jmsEndPoint.message(0).body().regex(templateOfGoodEndedMessageAboutExecutionWhichIsSentToJmsEndQueue);
        jmsEndPoint.assertIsSatisfied();

    }

    @Configuration
    @Import({MarketSpringContext.class})
    public static class FirstIntegrationTestConfig {
        public FirstIntegrationTestConfig() {
        }

        @Primary
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .addScript("schema.sql")
                    .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource());
        }
    }
}
