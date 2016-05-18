package com.market;

import org.apache.activemq.ActiveMQConnectionFactory;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.market.MarketRouteBuilder.JMS_EXECUTION_INFO;
import static com.market.MarketRouteBuilder.MAIN_ROUTE_ENTRY;

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
    }

    @Test
    public void testOrderSaved() {
        Assert.assertEquals(0, orderDAO.getAllOrders().size());

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 147");

        List<Order2> allOpenOrders = orderDAO.getAllOrders();
        Assert.assertEquals(1,  allOpenOrders.size());
        Order2 order = allOpenOrders.get(0);
        Assert.assertEquals("SELL", order.getType());
        Assert.assertEquals(147, order.getQuantity());
    }

    @Test
    public void testExecutionCreated() {
        Assert.assertEquals(0, orderDAO.getAllOrders().size());

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 20");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 35");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 75");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 60");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 100");
    }

    @Test
    public void testExecutionSavedShouldDoOneExecution() throws InterruptedException {
        //given
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());
        jmsEndPoint.expectedMessageCount(1);
//        jmsEndPoint.expectedBodiesReceived("---000");

        //when
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 100");

        //then
        List<Execution2> listOfAllExecutions = executionDAO.getListOfAllExecutions();
        Assert.assertEquals(1,listOfAllExecutions.size());
        Execution2 execution = listOfAllExecutions.get(0);

        final int quantityOfExecution = 100;
        Assert.assertEquals(quantityOfExecution,execution.getQuantity());

        jmsEndPoint.assertIsSatisfied();
//        Assert.assertEquals("ddd", jmsEndPoint.);
//        jmsEndPoint.expectedBodyReceived();

    }

    @Test
    public void testExecutionShouldDoTwoExecutions(){
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 80");
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 40");

        List<Integer> listOfAllExecutions = executionDAO.getListOfAllExecutions()
                .stream().map(e -> e.getQuantity()).collect(Collectors.toList());
        Assert.assertEquals(2,listOfAllExecutions.size());

        final int quantityOfFirstExecution = 80;
        final int quantityOfSecondExecution = 40;

        Assert.assertTrue(listOfAllExecutions.contains(quantityOfFirstExecution));
        Assert.assertTrue(listOfAllExecutions.contains(quantityOfSecondExecution));
    }

    @Test
    public void testUpdatingQuantityOfMachingOrdersAfterDoneExecution(){
        Assert.assertEquals(0,executionDAO.getListOfAllExecutions().size());
        Assert.assertEquals(0,orderDAO.getAllOrders().size());

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"BUY 120");//
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY,"SELL 80");//

        List<Order2> listOfAllOrders = orderDAO.getAllOrders();
        Order2 firstOrder = listOfAllOrders.get(0);
        Order2 secondOrder = listOfAllOrders.get(1);
        final int quantityOfFirstOrderAfterExecution = 40;//how should it be written ???
        final int quantityOfSecondOrderAfterExecution = 0;//

        Assert.assertEquals(quantityOfFirstOrderAfterExecution,firstOrder.getQuantity());
        Assert.assertEquals(quantityOfSecondOrderAfterExecution,secondOrder.getQuantity());

    }
    @Configuration
    @Import({MarketSpringContext.class})
    public static class FirstIntegrationTestConfig {
        public FirstIntegrationTestConfig() {
        }

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
