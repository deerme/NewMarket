package com.market;

import model.Order;

/**
 * Created by pizmak on 2016-05-17.
 */
public class OrderConverterImpl implements OrderConverter {
    @Override
    public Order convert(String orderStr) {
        String[] splitStr = orderStr.split(" ");
        Order order = new Order();
        order.setType(splitStr[0].trim());
        order.setQuantity(Integer.valueOf(splitStr[1].trim()));
        System.out.println(">>>order = " + order);
        return order;
    }
}
