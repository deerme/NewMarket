package com.market.mappers;

import com.market.model.changed.ExecutionTemp;
import org.apache.ibatis.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pizmak on 2016-07-06.
 */

public interface ExecutionMapper {
    @Insert("INSERT INTO EXECUTION(QUANTITY,ID_ORDER_SELLER,ID_ORDER_BUYER) VALUES(#{quantity},#{idSeller},#{idBuyer})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public int addExecution(ExecutionTemp execution);

    @Select("SELECT MAX(ID) FROM EXECUTION")
    public int getLastExecutionId();

    @Select("SELECT * FROM EXECUTION")
    @ResultType(ArrayList.class)
    @Results(value = {
            @Result(property="id" ,column = "ID"),
            @Result(property= "quantity",column = "QUANTITY"),
            @Result(property = "idBuyer",column = "ID_ORDER_BUYER"),
            @Result(property = "idSeller",column = "ID_ORDER_SELLER")
    })
    public List<ExecutionTemp> getAllExecutions();
}