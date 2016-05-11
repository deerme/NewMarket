package camel;

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

        String order = exchange.getIn().getBody().toString();
        String[] splittedOrders = order.split(" ");

        List<String> body = new ArrayList<>();
        for(int i=0;i<splittedOrders.length;i+=2) {
            body.add("type=" + splittedOrders[i] + " quantity=" + splittedOrders[i + 1]);
        }

        message.setBody(body);
        exchange.getOut().setBody(message);
    }
}
