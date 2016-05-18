package com.market;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */

public interface ExecutionDAO {
   @Transactional
   Execution2 saveExecution(Execution2 execution);

   @Transactional
   List<Execution2> getListOfAllExecutions();
}
