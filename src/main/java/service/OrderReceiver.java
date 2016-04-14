package service;

import exception.GeneralException;
import mq.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import javax.jms.JMSException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by PBanasiak on 3/23/2016.
 */

public class OrderReceiver implements Runnable {
    private Receiver receiver;
    private static final Logger logger = LoggerFactory.getLogger(OrderReceiver.class);
    private BlockingQueue blockingQueueWithOrders;
    private PlatformTransactionManager transactionManager;

    // single TransactionTemplate shared amongst all methods in this instance
    private TransactionTemplate transactionTemplate;

    public OrderReceiver(Receiver receiver, ArrayBlockingQueue arrayBlockingQueue,PlatformTransactionManager transactionManager) {
        this.receiver = receiver;
        this.blockingQueueWithOrders = arrayBlockingQueue;
        this.transactionManager = transactionManager;
    }

    public void setTransactionTemplate () {
            Assert.notNull(transactionManager, "The ''transactionManager'' argument must not be null.");
            this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void service() {
        transactionTemplate.execute (new TransactionCallbackWithoutResult () {
            protected void doInTransactionWithoutResult (TransactionStatus status) {
                run ();
                try {
                    runReceiver ();
                } catch (GeneralException e) {
                    logger.error("Exception",e);
                }
            }
        });
    }

    public void run()  {
        logger.info("After init OrderReceiver");
        try {
            runReceiver();
        } catch (GeneralException e) {
           logger.error("Exception"+e);
        }
    }

    public void runReceiver () throws GeneralException {
        try {
            receiver.createConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        while (true) {
            logger.info ("In threadloop");
            String messageFromQueue;

            logger.info ("before trying getting message");
            try {
                messageFromQueue = receiver.getMessage ();
            } catch (JMSException e) {
                logger.error ("JMSException: " + e.getMessage (), e);
                throw new GeneralException (e);
            }
            try {
                blockingQueueWithOrders.put (messageFromQueue);
            } catch (InterruptedException e) {
                logger.error ("Problems with messageFromQueue" + e);
                throw new GeneralException (e);
            }
            logger.info ("after trying getting message");
        }
    }

    public void initReceiver(){
        setTransactionTemplate();
       // new Thread (this).start ();
    }
}
