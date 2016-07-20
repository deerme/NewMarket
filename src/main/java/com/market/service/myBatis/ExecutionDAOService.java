package com.market.service.myBatis;

import com.market.model.standard.Execution;

import java.util.List;

/**
 * Created by pizmak on 2016-07-06.
 */
public interface ExecutionDAOService {
    public static final String SAVE_EXECUTION_METHOD_NAME = "addExecution";
    public static final String UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME="updateQuantityOfOrdersAfterDoneExecution";

    public Execution addExecution(Execution execution);

    public List<Execution> getAllExecutions();
}
