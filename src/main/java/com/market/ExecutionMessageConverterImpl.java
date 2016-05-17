package com.market;

/**
 * Created by pizmak on 2016-05-17.
 */
public class ExecutionMessageConverterImpl implements ExecutionMessageConverter {
    @Override
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution2 execution) {
        return new StringBuilder()
                   .append("exec_id= ")
                   .append(execution.getId())
                   .append("sell_id= ")
                   .append(execution.getIdSeller())
                   .append("buy_id= ")
                   .append(execution.getIdSeller())
                   .append("qty= ")
                   .append(execution.getQuantity())
                   .toString() ;
    }
}
