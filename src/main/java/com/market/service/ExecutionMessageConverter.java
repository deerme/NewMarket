package com.market.service;

import com.market.model.Execution;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionMessageConverter {
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution execution);
}
