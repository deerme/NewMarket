package com.market.database;

import com.market.model.Execution2;
import com.market.model.Order2;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface OrderDAO {
    final String SAVE_ORDER_METHOD_NAME = "saveOrder";
    final String UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME="updateQuantityOfOrdersAfterDoneExecution";

    @Transactional
    Order2 saveOrder(Order2 order);
    @Transactional
    List<Order2> getAllOpenOrdersByType(String type);
    @Transactional
    Execution2 updateQuantityOfOrdersAfterDoneExecution(Execution2 execution);
    @Transactional
    List<Order2> getAllOrders();
    @Transactional
    int getQuantityOfOrderById(int id);
}
