package service;

import database.OrderDAO;
import database.OrderDAOImpl;
import model.Order;
import mq.Receiver;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.jms.JMSException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by PBanasiak on 3/23/2016.
 */
public class OrderManager {
    private OrderDAO orderDAO;
    private ExecutionManager executionManager;

    private static final Logger logger= LoggerFactory.getLogger (OrderManager.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("auditLogger");

    public OrderManager(ExecutionManager executionManager, OrderDAO orderDAO) {
        this.executionManager = executionManager;
        this.orderDAO = orderDAO;
    }

    public void setExecutionManager (ExecutionManager executionManager) {
        this.executionManager = executionManager;
    }


   @Transactional
    public void takeMessageWithOrder (String messageFromQueue) throws JMSException {
        String[] splittedMessage = messageFromQueue.split (" ");

        for (int i = 0; i < splittedMessage.length; i += 2) {
            logger.info ("Splitted message with order" + splittedMessage[i] + "  " + splittedMessage[i + 1]);

            Order order = new Order ();
            order.setType (splittedMessage[i]);
            order.setQuantity (Integer.valueOf (splittedMessage[i + 1]));

            logger.info ("Splitted message  order.Type is " + order.getType () + "Qantity is" + order.getQuantity ());
            KeyHolder idOfOrder = orderDAO.addOrderToDatabase (order.getType (), order.getQuantity ());
            auditLogger.info ("Added order to database with: Id: " + idOfOrder.getKey ().intValue () + ("type of Order is: ") + order.getType () + ("quantity of Order is: ") + order.getQuantity ());

            checkIfMatchesSellAndBuyOrders ();
        }
    }

    public void checkIfMatchesSellAndBuyOrders () throws JMSException {
        List<ImmutablePair<Order, Order>> listOfAllAvailablePairsOfOrders = orderDAO.getPairsOfMatchingOrders ();
        Set<Integer> setOfAllUsedIdOfBuyer = new HashSet<> ();
        Set<Integer> setOfAllUsedIdOfSeller = new HashSet<> ();

        for (ImmutablePair<Order, Order> pairOfOrders : listOfAllAvailablePairsOfOrders) {
            Order sellOrder;
            Order buyOrder;

            if ((! setOfAllUsedIdOfSeller.contains (pairOfOrders.getRight ().getId ())) && (! setOfAllUsedIdOfBuyer.contains (pairOfOrders.getLeft ().getId ()))) {
                buyOrder = pairOfOrders.getLeft ();
                sellOrder = pairOfOrders.getRight ();
                setOfAllUsedIdOfBuyer.add (buyOrder.getId ());
                setOfAllUsedIdOfSeller.add (sellOrder.getId ());

                int quantityOfOrder = Math.min(sellOrder.getQuantity (),buyOrder.getQuantity ());

                executionManager.createExecution (sellOrder, buyOrder);
                orderDAO.updateOrderToDatabase (buyOrder.getId (), buyOrder.getQuantity() - quantityOfOrder);
                orderDAO.updateOrderToDatabase (sellOrder.getId (), sellOrder.getQuantity () - quantityOfOrder);
            }
        }
    }
}
