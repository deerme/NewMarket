package com.market.service;

import com.market.model.Order2;

import java.util.Optional;

/**
 * Created by pizmak on 2016-05-17.
 */
public class OrderConverterImpl implements OrderConverter {
    @Override
    public Order2 convert(String orderStr) {
        String[] splitStr = orderStr.split(" ");
        Order2 order = new Order2(
                Optional.empty(),
                splitStr[0].trim(),
                Integer.valueOf(splitStr[1].trim())
        );
        System.out.println(">>>order = " + order);
        return order;
    }
}
