package com.market;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */
public interface ExecutionDAO {
   public Execution2 saveExecution(Execution2 execution);

   public List<Execution2> getListOfAllExecutions();
}
