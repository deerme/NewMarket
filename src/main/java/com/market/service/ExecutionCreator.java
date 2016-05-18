package com.market.service;

import com.market.model.Execution;
import com.market.model.Order;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionCreator {
    public List<Execution> process(Order order);
}
