package com.market.service.myBatis.converters;

import com.market.model.changed.ExecutionTemp;
import com.market.model.standard.Execution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-07-06.
 */
public class ExecutionConverter {
    private static final int DEFAULT_EXECUTION_ID = 0;

    public List<Execution> createListExecutionsFromListExecutionsTemp(List<ExecutionTemp> executionTempList){
        List<Execution> executionList= new ArrayList<>();
        executionTempList.forEach(
                                a->{executionList.add(
                                        createExecutionFromExecutionTemp(a));});

        return executionList;
    }

    public Execution createExecutionFromExecutionTemp(ExecutionTemp executionTemp){
        return new Execution( executionTemp.getId()!=DEFAULT_EXECUTION_ID ? Optional.of(executionTemp.getId()) :  Optional.empty(),
                executionTemp.getIdBuyer(),
                executionTemp.getIdSeller(),
                executionTemp.getQuantity()
        );
    }

    public ExecutionTemp createExecutionTempFromExecution(Execution execution){
        return new ExecutionTemp( execution.getId().orElse(DEFAULT_EXECUTION_ID),
                execution.getIdBuyer(),
                execution.getIdSeller(),
                execution.getQuantity()
        );
    }
}
