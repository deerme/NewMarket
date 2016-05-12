package camel;

import database.OrderDAO;
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
    private List<String> listOfAllExecutionsWhichCanHappen ;

    public OrderPairsMatcher() {
        listOfAllExecutionsWhichCanHappen = new ArrayList<>();
    }

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = new DefaultMessage();
        message.setBody(checkIfMatchesSellAndBuyOrders());
        exchange.setOut(message);
    }


    public List<String> checkIfMatchesSellAndBuyOrders () throws JMSException {
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

                listOfAllExecutionsWhichCanHappen.add(createMessageAboutExecution(buyOrder, sellOrder));
            }
        }

        return listOfAllExecutionsWhichCanHappen;
    }

    public String createMessageAboutExecution(Order buyOrder, Order sellOrder){
        int quantityOfExecution = Math.min(sellOrder.getQuantity(), buyOrder.getQuantity());

        return new StringBuilder()
                    .append("idBuyer=")
                    .append(buyOrder.getId())
                    .append(" idSeller=")
                    .append(sellOrder.getId())
                    .append(" quantityOfExecution=")
                    .append(quantityOfExecution)
                    .append(" newQuantityBuyer=")
                    .append(buyOrder.getQuantity() - quantityOfExecution)
                    .append(" newQuantitySeller=")
                    .append(sellOrder.getQuantity() - quantityOfExecution)
                    .toString();
    }

}
