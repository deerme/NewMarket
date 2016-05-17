package camel;

import database.OrderDAO;
import model.Execution;
import model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pizmak on 2016-05-11.
 */
public class OrderPairsMatcher implements Processor {
    private List<Execution> listOfAllExecutionsWhichCanHappen ;

    @Autowired
    private OrderDAO orderDAO;

    public OrderPairsMatcher() {
        listOfAllExecutionsWhichCanHappen = new ArrayList<>();
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = new DefaultMessage();
        if(checkIfMatchesSellAndBuyOrders().isEmpty())  message.setBody("No executions available");
        message.setBody(checkIfMatchesSellAndBuyOrders());
        exchange.setOut(message);
    }


    public List<Execution> checkIfMatchesSellAndBuyOrders () throws JMSException {
        List<ImmutablePair<Order, Order>> listOfAllAvailablePairsOfOrders = orderDAO.getPairsOfMatchingOrders();
        Set<Integer> setOfAllUsedIdOfBuyer = new HashSet<>();
        Set<Integer> setOfAllUsedIdOfSeller = new HashSet<>();

        for (ImmutablePair<Order, Order> pairOfOrders : listOfAllAvailablePairsOfOrders) {
            Order sellOrder;
            Order buyOrder;

            if ((!setOfAllUsedIdOfSeller.contains(pairOfOrders.getRight().getId())) && (!setOfAllUsedIdOfBuyer.contains(pairOfOrders.getLeft().getId()))) {
                buyOrder = pairOfOrders.getLeft();
                sellOrder = pairOfOrders.getRight();
                setOfAllUsedIdOfBuyer.add(buyOrder.getId());
                setOfAllUsedIdOfSeller.add(sellOrder.getId());

                int quantityOfExecution = Math.min(sellOrder.getQuantity(), buyOrder.getQuantity());

                listOfAllExecutionsWhichCanHappen.add(new Execution.ExecutionBuilder(quantityOfExecution).idBuyer(buyOrder.getId()).idSeller(sellOrder.getId()).quantityOfBuyer(buyOrder.getQuantity()).quantityOfSeller(sellOrder.getQuantity()).build());
            }
        }

        return listOfAllExecutionsWhichCanHappen;
    }
}
