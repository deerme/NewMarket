package camel;

import database.ExecutionDAO;
import database.OrderDAO;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Created by pizmak on 2016-05-11.
 */
public class ExecutionManager implements Processor {
    @Autowired
    ExecutionDAO executionDAO;

    @Autowired
    OrderDAO orderDAO;

    @Override
    public void process(Exchange exchange) throws Exception {
        String dateAboutExecution = (String) exchange.getIn().getBody();

        exchange.setOut(createExecutionAndCamelMessageAboutExecution(dateAboutExecution));
    }

    public Message createExecutionAndCamelMessageAboutExecution(String dateAboutExecution){
        String [] splittedMessage = dateAboutExecution.split(" ");
        String [] splittedMessageWithIdBuyer = splittedMessage[0].split("idBuyer=");
        String [] splittedMessageWithIdSeller = splittedMessage[1].split("idSeller=");
        String [] splittedMessageWithQuantity = splittedMessage[2].split("quantityOfExecution=");
        String [] splittedMessageWithNewBuyOrderQuantity = splittedMessage[3].split("newQuantityBuyer=");
        String [] splittedMessageWithNewSellOrderQuantity = splittedMessage[3].split("newQuantitySeller=");


        KeyHolder keyHolderWithKeyOfExecution = executionDAO.addExecutionToDatabaseAndReturnAutoGeneratedKey(Integer.valueOf(splittedMessageWithIdBuyer[1]),Integer.valueOf(splittedMessageWithIdSeller[1]),Integer.valueOf(splittedMessageWithQuantity[1]));
        Message messageAboutExecution = new DefaultMessage();
        messageAboutExecution.setBody("exec_id"+keyHolderWithKeyOfExecution.getKey().intValue()+"sell_id"+splittedMessageWithIdSeller[1]+"buy_id"+splittedMessageWithIdBuyer[1]+"qty"+splittedMessageWithQuantity[1]);

        updateOrderToDatabase(Integer.valueOf(splittedMessageWithIdBuyer[1]),Integer.valueOf(splittedMessageWithNewBuyOrderQuantity[1]));
        updateOrderToDatabase(Integer.valueOf(splittedMessageWithIdSeller[1]),Integer.valueOf(splittedMessageWithNewSellOrderQuantity[1]));

        return messageAboutExecution;
    }

    public void updateOrderToDatabase(int idOfOrder, int quantityOfDoneExecution){
        orderDAO.updateOrderToDatabase(idOfOrder,quantityOfDoneExecution);
    }
}
