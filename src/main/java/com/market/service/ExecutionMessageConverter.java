package com.market.service;

import com.market.model.Execution2;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionMessageConverter {
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution2 execution2);
}
