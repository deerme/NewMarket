package com.market.service.camel;

import com.market.model.standard.Execution;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionMessageConverter {
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution execution);
}
