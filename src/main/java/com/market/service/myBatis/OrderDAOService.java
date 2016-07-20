package com.market.service.myBatis;

import com.market.model.standard.Execution;
import com.market.model.standard.Order;

import java.util.List;

/**
 * Created by pizmak on 2016-07-06.
 */
public interface OrderDAOService {
    public static final String SAVE_ORDER_METHOD_NAME = "addOrder";
    public static final String UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME="updateOrder";
    public static final String GET_ALL_ORDERS_METHOD_NAME="getListAllOrdersByType";

    public Order addOrder(Order orderToAdd);

    public Order getOrder(int userId);

    public List<Order> getListAllOrdersByType(String type);

    public Execution updateOrder(Execution execution);

    public List<Order> getListAllOpenOrders();

    public List<Order> getListAllOrders();
}
