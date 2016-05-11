package database;

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
       String bodyWithOrder = (String) exchange.getIn().getBody();

       String [] splitted = bodyWithOrder.split("type=");
       String [] splittedWithType = splitted[1].split(" ");
       String [] splittedWithQuantity = splittedWithType[1].split("quantity=");

       orderDAO.addOrderToDatabase(splittedWithType[0],Integer.valueOf(splittedWithQuantity[1]));
    }
}
