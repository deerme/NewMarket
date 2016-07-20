package com.market.service.myBatis.converters;

import com.market.model.changed.OrderTemp;
import com.market.model.standard.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-07-06.
 */
public class OrderConverter {
    private static final int DEFAULT_ORDER_ID = 0;

    public OrderTemp createOrderTempFromOrder(Order order){
        return new OrderTemp(
                order.getId().orElse(DEFAULT_ORDER_ID),
                order.getType(),
                order.getQuantity()
        );
    }

    public Order createOrderFromOrderTemp(OrderTemp orderTemp){
        return new Order(
                orderTemp.getId() != DEFAULT_ORDER_ID ? Optional.of(orderTemp.getId()) : Optional.empty(),
                orderTemp.getType(),
                orderTemp.getQuantity()
        );
    }

    public List<Order> createOrderListFromOrderTempList(List<OrderTemp> orderTempList){
        List<Order> orderList = new ArrayList<>();

        orderTempList.forEach(
                    order -> {
                         orderList.add(
                                 createOrderFromOrderTemp(order));});

        return orderList;
    }
}
