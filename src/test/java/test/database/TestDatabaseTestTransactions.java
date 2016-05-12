package test.database;

import bean.test.configuration.BeanTestConfiguration;
import database.ExecutionDAO;
import database.OrderDAO;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import javax.sql.DataSource;



/**
 * Created by PBanasiak on 3/30/2016.
 */

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
@Ignore
public class TestDatabaseTestTransactions extends CamelTestSupport {
    private static final Logger logger = LoggerFactory.getLogger(TestDatabaseTestTransactions.class);
    @Autowired
    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
//    @Autowired
//    private OrderManager orderManager;
    @Autowired
    @Qualifier("mockTestOrderDAO")
    private OrderDAO orderDAO;
    @Autowired
    private ExecutionDAO executionDAO;

    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception{
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("bean:testOrderReader").to("jms:test");
            }
        };
    }


    @Before
    public void initJdbcTemplate(){
        jdbcTemplate = new JdbcTemplate(datasource);
    }

//    @Test
//    public void testTransaction1() throws JMSException {
//        Mockito.doThrow(new ExceptionFromOrderDao("exception from spy")).when(orderDAO).updateOrderToDatabase(Mockito.anyInt(), Mockito.anyInt());
//
//        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
//
////        try {
////            orderManager.takeMessageWithOrder("BUY 200");
////            orderManager.takeMessageWithOrder("SELL 80");
////            orderManager.takeMessageWithOrder("SELL 80");
////        } catch (ExceptionFromOrderDao ex) {
////            Assert.assertEquals(new Integer(0), jdbcTemplate.queryForObject("select count(*) from execution", Integer.class));
////            logger.info("Thrown exeception by mock",ex,ex.getMessage());
////        }
//    }
}


