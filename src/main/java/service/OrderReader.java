package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.JMSException;

/**
 * Created by PBanasiak on 3/24/2016.
 */

public class OrderReader {
    private OrderManager orderManager;
    private static final  Logger logger = LoggerFactory.getLogger(OrderReader.class);

    public OrderReader(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public void sendMessageWithOrderToOrderManager(String messageWithOrder) throws JMSException {
        orderManager.takeMessageWithOrder(messageWithOrder);
        logger.trace("Sended messaget to execution manager"+messageWithOrder);
    }
}
