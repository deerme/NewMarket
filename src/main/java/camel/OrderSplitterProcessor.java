package camel;

import model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pizmak on 2016-05-11.
 */
public class OrderSplitterProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = new DefaultMessage();
        List<Order> body = new ArrayList<>();

        String order = exchange.getIn().getBody().toString();
        String[] splittedOrders = order.split(" ");

        for(int i=0;i<splittedOrders.length;i+=2) {
            Order newOrder = new Order();
            newOrder.setType(splittedOrders[i]);
            newOrder.setQuantity(Integer.valueOf(splittedOrders[i + 1]));
            body.add(newOrder);
        }
        message.setBody(body);
        exchange.getOut().setBody(message);
    }
}
