package com.market;

import model.Order;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface OrderConverter {
    Order2 convert(String orderStr);
}
