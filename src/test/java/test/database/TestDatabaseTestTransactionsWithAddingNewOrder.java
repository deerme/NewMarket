package test.database;

import bean.test.configuration.BeanTestConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import javax.jms.JMSException;
import javax.sql.DataSource;



/**
 * Created by PBanasiak on 3/30/2016.
 */
@Ignore
public class TestDatabaseTestTransactionsWithAddingNewOrder /*extends CamelSpringTestSupport */{
    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";
    private static final String NAME_OF_JMS_QUEUE_TO_SEND_ORDERS = "jms:incomingOrders";
    private MockEndpoint mockEndpointWithExecutions;
//    @Autowired
//    private OldExecutionDAO oldExecutionDAO;
//
//    @Autowired
//    private OldOrderDAO oldOrderDAO;
//    private Logger logger = LoggerFactory.getLogger(TestDatabaseTestTransactionsWithAddingNewOrder.class);
//
//    @Before
//    public void setupDatabase() throws Exception {
//        datasource = context.getRegistry().lookupByNameAndType("testDataSource", DataSource.class);
//        jdbcTemplate = new JdbcTemplate(datasource);
//    }
//
//    @Before
//    public void createMockEndpoints(){
//        mockEndpointWithExecutions = getMockEndpoint("mock:testExecutionsInformationQueue");
//    }
//    @Override
//    protected AbstractApplicationContext createApplicationContext() {
//        return new AnnotationConfigApplicationContext(BeanTestConfiguration.class);
//    }
//
//    @Override
//    protected RouteBuilder createRouteBuilder() throws Exception {
//        return new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("direct:mainRoute")
//                        .transacted()
//                        .bean(OrderSplitterProcessor.class)
//                        .split(body())
//                        .log(LoggingLevel.INFO,"Starting from order : {$body}"  )
//                        .bean(OrderDAOSave.class)
//                        .process(new Processor() {
//                            @Override
//                            public void process(Exchange exchange) throws Exception {
//                                throw new Exception();
//                            }
//                        })
//                        .end()
//                        .bean(OrderPairsMatcher.class,"process")
//                        .split(body())
//                        .choice()
//                        .when(body().contains("No executions available"))
//                        .log(LoggingLevel.INFO,"It wasn`t any matched orders to create execution")
//
//                        .when(body().isNotNull())
//                        .log(LoggingLevel.INFO,"Starting from execution: ${body}" )
//                        .bean(ExecutionManager.class)
//                        .to("mock:testExecutionsInformationQueue")
//                        .endChoice();
//
//                from("jms:incomingOrders")
//                        .errorHandler(deadLetterChannel("jms:testQueueWithNewOrders.Dead").useOriginalMessage())
//                        .bean(MainLogger.class)
//                        .to("direct:mainRoute");
//            }
//        };
//    }
//
//    @Test
//    public void testTransactionShouldNotAddNewOrderToDatabase() throws JMSException {
//        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
//        try {
//            template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS, "BUY 100");
//        }catch (Exception ex) {
//            Assert.assertEquals(new Integer(0),new Integer(JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS)));
//            logger.info("Thrown exeception by mock",ex,ex.getMessage());
//        }
//    }
}


