package camel;

import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by pizmak on 2016-04-25.
 */
public class OrderProcessor {
    private ArrayBlockingQueue arrayBlockingQueueWithOrders;
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

    public OrderProcessor(ArrayBlockingQueue arrayBlockingQueueWithOrders) {
        this.arrayBlockingQueueWithOrders = arrayBlockingQueueWithOrders;
    }

    @Handler
    public void addNewOrderToArrayBlockingQueueWithWaitingOrders(String order) throws InterruptedException {
        try {
            arrayBlockingQueueWithOrders.put(order);
        } catch (InterruptedException e) {
          logger.error("Problems with putting new Order "+e.getMessage(),e);
            throw e;
        }
    }
}
