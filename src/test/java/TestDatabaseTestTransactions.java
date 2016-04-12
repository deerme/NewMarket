
import beanTestConfiguration.BeanTestConfigurationWithMocks;
import database.OrderDAOImpl;
import exceptions.ExceptionFromOrderDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;
import service.OrderManager;
import javax.jms.JMSException;
import javax.sql.DataSource;



/**
 * Created by PBanasiak on 3/30/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanTestConfigurationWithMocks.class, loader = AnnotationConfigContextLoader.class)
public class TestDatabaseTestTransactions {
    @Autowired
    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderDAOImpl orderDAO;

    @Before
    public void initJdbcTemplate(){
         jdbcTemplate = new JdbcTemplate(datasource);
    }
    @Ignore
    @Test
    public void testTransaction1() throws JMSException {
        Mockito.doThrow(new ExceptionFromOrderDao("exception from spy")).when(orderDAO).updateOrderToDatabase(Mockito.anyInt(), Mockito.anyInt());

        JdbcTestUtils.deleteFromTables (jdbcTemplate,"execution","orderinmarket");

        try {
            orderManager.takeMessageWithOrder("BUY 200");
            orderManager.takeMessageWithOrder("SELL 80");
        } catch (ExceptionFromOrderDao ex) {
            Assert.assertEquals(new Integer(0), jdbcTemplate.queryForObject("select count(1) from execution", Integer.class));
        }
    }
}


