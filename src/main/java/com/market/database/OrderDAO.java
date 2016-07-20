package com.market.database;

import com.market.model.standard.Execution;
import com.market.model.standard.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
@Transactional
public interface OrderDAO {
    final String SAVE_ORDER_METHOD_NAME = "saveOrder";
    final String UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME="updateQuantityOfOrdersAfterDoneExecution";

    @Transactional
    Order saveOrder(Order order);
    @Transactional
    List<Order> getAllOpenOrdersByType(String type);
    @Transactional
    Execution updateQuantityOfOrdersAfterDoneExecution(Execution execution);
    @Transactional
    List<Order> getAllOrders();
    @Transactional
    int getQuantityOfOrderById(int id);
}
