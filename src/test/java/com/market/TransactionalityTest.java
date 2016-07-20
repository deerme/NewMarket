package com.market;

import com.market.camel.MarketSpringContext;
import com.market.database.ExecutionDAO;
import com.market.database.OrderDAO;
import com.market.service.camel.ExecutionCreator;
import com.market.service.camel.ExecutionMessageConverter;
import com.market.service.myBatis.ExecutionDAOService;
import com.market.service.myBatis.OrderDAOService;
import exceptions.ExceptionFromOrderDao;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import static com.market.camel.MarketRouteBuilder.*;

/**
 * Created by pizmak on 2016-05-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TransactionalityTest.FirstIntegrationTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class TransactionalityTest {
    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";

    @Autowired
    private ModelCamelContext camelContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ExecutionMessageConverter executionMessageConverter;
    @Autowired
    private ExecutionCreator executionCreator;
    @Autowired
    private OrderDAOService orderDAOService;
    @Autowired
    private ExecutionDAOService executionDAOService;

    @EndpointInject(uri = "mock:" + JMS_EXECUTION_INFO)
    private MockEndpoint jmsEndPoint;

    @EndpointInject(uri = "mock:" + JMS_NEW_ORDERS)
    private MockEndpoint jmsStartPoint;


    @Before
    public void cleanDatabase() throws Exception {
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

    @Test
    public void testExecutionShouldBeRoledBack() {
        Assert.assertEquals(0, orderDAOService.getListAllOrders().size());
        Assert.assertEquals(0, executionDAOService.getAllExecutions().size());

        ExceptionFromOrderDao exception = new ExceptionFromOrderDao("transactionException");
        Mockito.when(executionMessageConverter.convertMessageAboutExecutionToFormatForSendingToQueue(Mockito.any()))
                .thenThrow(exception);

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 1");
        try {
            producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 1");
        } catch (Exception ex) {
            Assert.assertTrue(ex.getCause().getCause().equals(exception));
            Assert.assertEquals(1, orderDAOService.getListAllOrders().size());
            Assert.assertEquals(0, executionDAOService.getAllExecutions().size());
            System.out.println("In catch");
            return;
        }
        Assert.fail();
    }

    @Configuration
    @Import(MarketSpringContext.class)
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

        @Primary
        @Bean
        public ExecutionMessageConverter executionMessageConverter() {
            return Mockito.mock(ExecutionMessageConverter.class);
        }

    }
}
