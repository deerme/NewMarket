package com.market.web;

/**
 * Created by pizmak on 2016-04-07.
 */

import com.market.model.Execution;
import com.market.database.ExecutionDAO;
import com.market.model.Order;
import com.market.database.OrderDAO;
import com.market.exception.GeneralException;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
@RequestMapping("/")
public class MainController {
    private OrderDAO orderDAO;
    private ExecutionDAO executionDAO;

    @Autowired
    private CamelContext camelContext;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private ProducerTemplate producerTemplate;

//    public MainController(OldOrderDAO oldOrderDAO, OldExecutionDAO oldExecutionDAO) {
//        this.oldOrderDAO = oldOrderDAO;
//        this.oldExecutionDAO = oldExecutionDAO;
//    }

    public MainController(OrderDAO orderDAO, ExecutionDAO executionDAO) {
         this.orderDAO = orderDAO;
         this.executionDAO = executionDAO;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String sayWelcomeOnMarket(ModelMap model) {
        model.addAttribute("greeting", "Welcome on our market");
        return "welcome";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        List<Order> listOfAllOrders = orderDAO.getAllOrders();

        model.addAttribute("listOfAllOrders", listOfAllOrders);
        model.addAllAttributes(listOfAllOrders);
        return "orders";
    }

    @RequestMapping(value = "/executions", method = RequestMethod.GET)
    public String seeAllExecutions(ModelMap model) {
        List<Execution> listOfAllExecutions = executionDAO.getListOfAllExecutions();
        model.addAttribute("listOfAllExecutions", listOfAllExecutions);
        return "executions";
    }

    @RequestMapping(value = "/addNewOrder", method = RequestMethod.GET)
    public String addNewOrder(ModelMap model) {
        model.addAttribute("addNewOrder", "Here you will add new order");
        return "addOrder";
    }

    @RequestMapping(value = "/addNewOrder", method = RequestMethod.POST)
    public String addNewOrderToDatabase(@RequestParam("quantity") int quantityOfOrder, @RequestParam("typeOfOrder") String typeOfOrder, ModelMap model) throws GeneralException {
        putNewOrderToQueueWithWaitingOrders(typeOfOrder, quantityOfOrder);

        model.addAttribute("typeOfOrder", typeOfOrder);
        model.addAttribute("quantityOfOrder", quantityOfOrder);

        return "afterAddedNewOrder";
    }

    public void putNewOrderToQueueWithWaitingOrders(String typeOfOrder, int quantityOfOrder) {
        logger.info("Before sending to testQueueWithNewOrders by producerTemplate");
        producerTemplate.requestBody("jms:testQueueWithNewOrders", typeOfOrder + " " + quantityOfOrder);
        logger.info("After sending to testQueueWithNewOrders by producerTemplate");
    }

    public void createProducerTemplateFromCamelContext(){
        this.producerTemplate = camelContext.createProducerTemplate();
    }
}