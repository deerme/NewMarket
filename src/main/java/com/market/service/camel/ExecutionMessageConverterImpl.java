package com.market.service.camel;

import com.market.model.standard.Execution;

/**
 * Created by pizmak on 2016-05-17.
 */
public class ExecutionMessageConverterImpl implements ExecutionMessageConverter {
    @Override
    public String convertMessageAboutExecutionToFormatForSendingToQueue(Execution execution) {
        return new StringBuilder()
                   .append("exec_id=")
                   .append(execution.getId())
                   .append("sell_id=")
                   .append(execution.getIdSeller())
                   .append("buy_id=")
                   .append(execution.getIdSeller())
                   .append("qty=")
                   .append(execution.getQuantity())
                   .toString() ;
    }
}
