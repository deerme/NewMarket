package test.database;



import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.jms.JMSException;
import javax.sql.DataSource;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

/**
 * Created by PBanasiak on 3/30/2016.
 */
@Ignore
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = BeanTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TestDatabase /*extends CamelSpringTestSupport*/{
//    private DataSource datasource;
//    private JdbcTemplate jdbcTemplate;
//    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
//    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";
//    private static final String NAME_OF_JMS_QUEUE_TO_SEND_ORDERS = "jms:incomingOrders";
//    private MockEndpoint mockEndpointWithExecutions;
//    @Autowired
//    private OldExecutionDAO oldExecutionDAO;
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
//                        .end()
//                        .bean(OrderPairsMatcher.class,"process")
//                        .split(body())
//                        .choice()
//                            .when(body().isNull())
//                                .log(LoggingLevel.INFO,"It wasn`t any matched orders to create execution")
//
//                            .when(body().isNotNull())
//                                .log(LoggingLevel.INFO,"Starting from execution: ${body}" )
//                                .bean(ExecutionManager.class)
//                                .to("mock:testExecutionsInformationQueue")
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
//    public void testDatabaseShouldReturnNoRowsInExecutionTable() throws JMSException {
//        final int numberOfExecutions = 0;
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS);
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 200");
//
//        assertEquals(numberOfExecutions,countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS));
//    }
//
//    @Test
//    public void testDatabaseShouldReturnNoRowsInExecutionTableAfterAddingTwoOrdersWithTheSameType() throws JMSException {
//        final int numberOfExecutions = 0;
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS);
//
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 200");
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 100");
//
//        assertEquals(numberOfExecutions,countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS));
//    }
//    @Ignore
//    @Test
//    public void testDatabaseShouldReturnOneMoreRowInExecutionTable(){
//        final int numberOfExecutionsWhichShouldBeAfterInsertingNew = 1;
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS);
//
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 100");
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"SELL 200");
//
//        assertEquals(numberOfExecutionsWhichShouldBeAfterInsertingNew,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS));
//    }
//
//    @Test
//    public void testRouteShouldNotSendInformationAboutExecutionToExecutionsQueueReplacedByMock() throws InterruptedException {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS);
//
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 200");
//
//        mockEndpointWithExecutions.expectedMessageCount(0);
//        mockEndpointWithExecutions.assertIsSatisfied();
//    }
//
//    @Test
//    public void testMessageSentToQueueAfterDoneExecutionFromTwoMatchingOrdersShouldBeOneMessage() throws InterruptedException {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS);
//
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 100");
//        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"SELL 100");
//
//        mockEndpointWithExecutions.expectedMessageCount(1);
//       // mockEndpointWithExecutions.assertIsSatisfied();
//    }
//
//
////    @Test
////    public void testUpdatingTwoOrdersAfterAddingTwoMatchingOrdersToDatabaseAndMakingExecutionShouldUpdateTwoOrders() throws JMSException {
////        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
////
////        final int quantityOfBuyOrderBeforeMatchingToSellOrder=300;
////        int quantityOfSellOrderBeforeMatchingToBuyOrder=400;
////
////        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"BUY 300");
////        template.sendBody(NAME_OF_JMS_QUEUE_TO_SEND_ORDERS,"SELL 400");
////
////        int quantityOfExecution = Math.min (quantityOfBuyOrderBeforeMatchingToSellOrder,quantityOfSellOrderBeforeMatchingToBuyOrder);
////
////        int quantityOfBuyOrderAfterUpdatingAfterDoneExecution = jdbcTemplate.queryForObject("select quantity from orderinmarket where id = ?", Integer.class, idOfBuyOrder);
////        int quantityOfSellOrderAfterUpdatingAfterDoneExecution = jdbcTemplate.queryForObject("select quantity from orderinmarket where id = ?", Integer.class, idOfSellOrder);
////
////        assertEquals (quantityOfBuyOrderBeforeMatchingToSellOrder-quantityOfExecution,quantityOfBuyOrderAfterUpdatingAfterDoneExecution);
////        assertEquals (quantityOfSellOrderBeforeMatchingToBuyOrder-quantityOfExecution,quantityOfSellOrderAfterUpdatingAfterDoneExecution);
////    }
}


