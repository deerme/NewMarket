package com.market;

import exception.GeneralException;
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
@ContextConfiguration(classes = TransactionalityTest.FirstIntegrationTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class TransactionalityTest {
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
    @Autowired
    private ExecutionMessageConverter executionMessageConverter;

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
        Assert.assertEquals(0, executionDAO.getListOfAllExecutions().size());
        GeneralException exception = new GeneralException(null);
        Mockito.when(executionMessageConverter.convertMessageAboutExecutionToFormatForSendingToQueue(Mockito.any()))
                .thenThrow(exception);

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 1");
        try {
            producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "BUY 1");
        } catch (Exception ex) {
//            Assert.assertTrue(exception.equals(ex));
            Assert.assertEquals(0, orderDAO.getAllOrders().size());
            Assert.assertEquals(0, executionDAO.getListOfAllExecutions().size());
            return;
        }
        Assert.assertEquals(0, orderDAO.getAllOrders().size());
        Assert.assertEquals(0, executionDAO.getListOfAllExecutions().size());
        Assert.fail();
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

        @Bean
        @Primary
        public ExecutionMessageConverter executionMessageConverter() {
            return Mockito.mock(ExecutionMessageConverter.class);
        }

    }
}
