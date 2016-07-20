package com.market.order;

import com.market.TestConfiguration;
import com.market.model.standard.Order;
import com.market.service.myBatis.OrderDAOService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by pizmak on 2016-07-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class OrderServiceTest {
    @Autowired
    OrderDAOService orderDAOService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";

    private static final String BUY_ORDER_TYPE = "BUY";
    private static final int BUY_ORDER_QUANTITY = 20;

    private static final String SELL_ORDER_TYPE = "SELL";
    private static final int SELL_ORDER_QUANTITY = 110;

    private Order firstOrder;
    private Order secondOrder;

    @Before
    public void createNewOrders(){
        firstOrder = new Order(Optional.empty(),BUY_ORDER_TYPE,BUY_ORDER_QUANTITY);
        secondOrder = new Order(Optional.empty(),SELL_ORDER_TYPE,SELL_ORDER_QUANTITY);
    }

    @Before
    public void cleanDatabase(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);
    }

    @Test
    public void testAddingOneOrderToDatabaseShouldAddOneOrder(){
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));
        orderDAOService.addOrder(firstOrder);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));
    }

    @Test
    public void testAddingTwoOrdersToDatabaseShouldAddTwoOrders(){
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));

        orderDAOService.addOrder(firstOrder);
        orderDAOService.addOrder(secondOrder);

        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_ORDERS));
    }
}