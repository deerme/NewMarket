package com.market;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface OrderDAO {
    public final String SAVE_ORDER_METHOD_NAME = "saveOrder";

    Order2 saveOrder(Order2 order);
    List<Order2> getAllOpenOrders();
    List<Order2> getAllOpenOrdersByType(String type);
}
