package com.market.service.camel;

import com.market.model.standard.Execution;
import com.market.model.standard.Order;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionCreator {
    public List<Execution> process(Order order);
}
