package service;

import database.ExecutionDAO;
import database.ExecutionDAOImpl;
import model.Execution;
import model.Order;
import mq.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import javax.jms.JMSException;


/**
 * Created by PBanasiak on 3/24/2016.
 */
public class ExecutionManager {
    private Sender sender;
    private ExecutionDAO executionDAO;
    private static final Logger logger = LoggerFactory.getLogger(ExecutionManager.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("auditLogger");

    public ExecutionManager(Sender sender, ExecutionDAO executionDAO) {
        this.sender = sender;
        this.executionDAO = executionDAO;
    }

    public void setExecutionDAO(ExecutionDAOImpl executionDAO) {
        this.executionDAO = executionDAO;
    }

    public void createExecution(Order sellOrder, Order buyOrder) throws JMSException {
        KeyHolder keyWithIdOfExecution = executionDAO.addExecutionToDatabaseAndReturnAutoGeneratedKey(buyOrder.getId(),sellOrder.getId(),Math.min(sellOrder.getQuantity (),buyOrder.getQuantity ()));
        Execution execution = new Execution(buyOrder.getId(), sellOrder.getId(), keyWithIdOfExecution.getKey().intValue() , buyOrder.getQuantity());
        auditLogger.info("Added execution to database with: Id of execution:" + execution.getId() + "Id of buyer: "+ execution.getIdBuyer() + "Id of seller: " + execution.getIdSeller() + "quantity of execution" + execution.getQuantityOfExecution());
        createMessage(execution);
    }

    public void createMessage(Execution execution) throws JMSException {
        sender.sendMessage("exec_id"+execution.getId()+"sell_id"+execution.getIdSeller()+"buy_id"+execution.getIdBuyer()+"qty"+execution.getQuantityOfExecution());
        logger.debug ("sended message to queue"+"exec_id"+execution.getId()+"sell_id"+execution.getIdSeller()+"buy_id+"+execution.getIdBuyer()+"qty"+execution.getQuantityOfExecution());
    }
}
