package com.market.service.myBatis;

import com.market.mappers.ExecutionMapper;
import com.market.model.changed.ExecutionTemp;
import com.market.model.standard.Execution;
import com.market.service.myBatis.converters.ExecutionConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-07-06.
 */
public class ExecutionDAOServiceImpl implements ExecutionDAOService {
    @Autowired
    ExecutionMapper executionMapper;

    @Autowired
    ExecutionConverter executionConverter;

    @Override
    public Execution addExecution(Execution execution){
        ExecutionTemp executionTemp = executionConverter.createExecutionTempFromExecution(execution);
        executionMapper.addExecution(executionTemp);

        return new Execution(Optional.of(executionTemp.getId()),
                            execution.getIdBuyer(),
                            execution.getIdSeller(),
                            execution.getQuantity());
    }

    @Override
    public List<Execution> getAllExecutions(){
        return executionConverter.createListExecutionsFromListExecutionsTemp
                (executionMapper.getAllExecutions());
    }
}
