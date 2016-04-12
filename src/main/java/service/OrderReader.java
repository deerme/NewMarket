package service;

import exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by PBanasiak on 3/24/2016.
 */

public class OrderReader implements Runnable{
    private BlockingQueue blockingQueueWithOrders;
    private OrderManager orderManager;
    private static final  Logger logger = LoggerFactory.getLogger(OrderReader.class);

    public OrderReader(OrderManager orderManager, ArrayBlockingQueue arrayBlockingQueue) {
        this.blockingQueueWithOrders = arrayBlockingQueue;
        this.orderManager = orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public void setBlockingQueueWithOrders(java.util.concurrent.ArrayBlockingQueue blockingQueueWithOrders) {
        this.blockingQueueWithOrders = blockingQueueWithOrders;
    }

    public void run() {
        logger.info("After init OrderReader");

        while(true){
            try {
                getMessageFromQueueAndSendIntoOrderManager ();
            } catch (GeneralException e) {
                logger.error("GeneralExeception:"+e);
            }
        }
    }

    public void getMessageFromQueueAndSendIntoOrderManager() throws GeneralException {
        try {
            String orderMessage = (String) blockingQueueWithOrders.take();
            logger.info("Message from queue:"+orderMessage);
            sendMessageWithOrderToExecutionManager(orderMessage);
        } catch (InterruptedException e) {
            logger.error ("GeneralException: " + e.getMessage (), e);
            throw new GeneralException (e);
        } catch (JMSException e) {
            logger.error("JMSEcveption"+ e.getMessage (), e);
            throw new GeneralException (e);
        }

    }

    public void initOrderReader() {
        new Thread(this).start();
    }

    public void sendMessageWithOrderToExecutionManager(String messageWithOrder) throws JMSException {
        orderManager.takeMessageWithOrder(messageWithOrder);
        logger.trace("Sended messaget to execution manager"+messageWithOrder);
    }
}
