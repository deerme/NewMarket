package com.market.database;

import com.market.model.Execution;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by pizmak on 2016-05-17.
 */

public interface ExecutionDAO {
   @Transactional
   Execution saveExecution(Execution execution);

   @Transactional
   List<Execution> getListOfAllExecutions();
}
