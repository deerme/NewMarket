package com.market;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface OrderDAO {
    public final String SAVE_ORDER_METHOD_NAME = "saveOrder";
    public final String UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME="updateQuantityOfOrdersAfterDoneExecution";

    public Order2 saveOrder(Order2 order);

    public List<Order2> getAllOpenOrders();

    public List<Order2> getAllOpenOrdersByType(String type);

    public Execution2 updateQuantityOfOrdersAfterDoneExecution(Execution2 execution);

    public List<Order2> getAllOrders();
}
