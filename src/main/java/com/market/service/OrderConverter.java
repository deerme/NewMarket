package com.market.service;

import com.market.model.Order2;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface OrderConverter {
    Order2 convert(String orderStr);
}
