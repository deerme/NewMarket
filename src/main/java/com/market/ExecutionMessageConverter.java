package com.market;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionMessageConverter {
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution2 execution2);
}
