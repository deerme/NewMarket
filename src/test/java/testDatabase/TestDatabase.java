package testDatabase;

import bean.test.configuration.BeanTestConfiguration;
import database.OrderDAOImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;
import service.OrderManager;
import javax.jms.JMSException;
import javax.sql.DataSource;
import static org.junit.Assert.assertEquals;


/**
 * Created by PBanasiak on 3/30/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TestDatabase {
    @Autowired
    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    @Qualifier("testOrderDAO")
    private OrderDAOImpl orderDAO;

    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";

    @Before
    public void initJdbcTemplate(){
        jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Test
    public void testDatabaseShouldReturnNoRows() throws JMSException {
        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
        assertEquals (0,JdbcTestUtils.countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));
    }

    @Test
    public void testInsertingOrderToDatabaseShouldReturnOneMoreRowFromOrderTable(){
        int rowsInOrderTableBeforeInsertion = JdbcTestUtils.countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS);
        orderDAO.addOrderToDatabase ("BUY",300);
        assertEquals (rowsInOrderTableBeforeInsertion+1,JdbcTestUtils.countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));
    }

    @Test
    public void testInsertingOrderToatabaseFromStrinMessageShouldReturnOneMoreRowFromOrderTable() throws JMSException {
        int rowsInOrderTableBeforeInsertion = JdbcTestUtils.countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS);
        orderManager.takeMessageWithOrder ("BUY 2000");
        assertEquals (rowsInOrderTableBeforeInsertion+1,JdbcTestUtils.countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));
    }

    @Test
    public void testAddingTwoMatchingOrdersToDatabaseShouldAddNewExecutionToDatabase() throws JMSException {
        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
        orderManager.takeMessageWithOrder ("BUY 200");
        orderManager.takeMessageWithOrder ("SELL 200");
        assertEquals (1,JdbcTestUtils.countRowsInTable (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS));
    }

    @Test
    public void testUpdatingTwoOrdersAfterAddingTwoMatchingOrdersToDatabaseAndMakingExecutionShouldUpdateTwoOrders() throws JMSException {
        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);

        final int QUANTITY_OF_BUY_ORDER_BEFORE_MATCHING_TO_SELL_ORDER=300;
        int quantityOfSellOrderBeforeMatchingToBuyOrder=400;
        int idOfBuyOrder = orderDAO.addOrderToDatabase ("BUY" , QUANTITY_OF_BUY_ORDER_BEFORE_MATCHING_TO_SELL_ORDER).getKey ().intValue ();
        int idOfSellOrder = orderDAO.addOrderToDatabase ("SELL" , quantityOfSellOrderBeforeMatchingToBuyOrder).getKey ().intValue ();
        orderManager.checkIfMatchesSellAndBuyOrders ();
        int quantityOfExecution = Math.min (QUANTITY_OF_BUY_ORDER_BEFORE_MATCHING_TO_SELL_ORDER,quantityOfSellOrderBeforeMatchingToBuyOrder);

        int quantityOfBuyOrderAfterUpdatingAfterDoneExecution = this.jdbcTemplate.queryForObject("select quantity from orderinmarket where id = ?", Integer.class, idOfBuyOrder);
        int quantityOfSellOrderAfterUpdatingAfterDoneExecution = this.jdbcTemplate.queryForObject("select quantity from orderinmarket where id = ?", Integer.class, idOfSellOrder);

        assertEquals (QUANTITY_OF_BUY_ORDER_BEFORE_MATCHING_TO_SELL_ORDER-quantityOfExecution,quantityOfBuyOrderAfterUpdatingAfterDoneExecution);
        assertEquals (quantityOfSellOrderBeforeMatchingToBuyOrder-quantityOfExecution,quantityOfSellOrderAfterUpdatingAfterDoneExecution);
    }

    @Test
    public void testUpdatingTwoOrdersAfterAddingTwoMatchingOrdersToDatabaseAndMakingExecutionShouldUpdateTwoOrders2() throws JMSException {
        JdbcTestUtils.deleteFromTables (jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);

        orderManager.takeMessageWithOrder("BUY 200");
        orderManager.takeMessageWithOrder("SELL 80");

        Assert.assertEquals(new Integer(1), jdbcTemplate.queryForObject("select count(1) from execution", Integer.class));
        Assert.assertEquals(new Integer(1), jdbcTemplate.queryForObject("select count(1) from orderinmarket where QUANTITY in (0)", Integer.class));
    }
}


