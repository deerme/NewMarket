package com.market.service.camel;

import com.market.model.standard.Order;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface OrderMessageConverter {
    Order convert(String orderStr);
}
