package com.market;

import exception.GeneralException;

/**
 * Created by pizmak on 2016-05-17.
 */
public class ExecutionMessageConverterImpl implements ExecutionMessageConverter {
    @Override
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution2 execution) {
//        if (1 == 1) {
//            throw new GeneralException(null);
//        }
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
