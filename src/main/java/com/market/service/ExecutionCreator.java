package com.market.service;

import com.market.model.Execution2;
import com.market.model.Order2;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionCreator {
    public List<Execution2> process(Order2 order);
}
