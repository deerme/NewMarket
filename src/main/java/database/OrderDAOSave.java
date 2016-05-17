package database;

import model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pizmak on 2016-05-11.
 */
public class OrderDAOSave implements Processor{
    @Autowired
    private  OrderDAO orderDAO;

    @Override
    public void process(Exchange exchange) throws Exception {
       Order bodyWithOrder = (Order) exchange.getIn().getBody();

       orderDAO.addOrderToDatabase(bodyWithOrder.getType(),bodyWithOrder.getQuantity());
    }
}
