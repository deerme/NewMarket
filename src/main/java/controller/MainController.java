package controller;

/**
 * Created by pizmak on 2016-04-07.
 */

import database.ExecutionDAOImpl;
import database.OrderDAOImpl;
import model.Execution;
import model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


@Controller
@RequestMapping("/")
public class MainController {
    private OrderDAOImpl orderDAO;
    private ExecutionDAOImpl executionDAO;
    private ArrayBlockingQueue arrayBlockingQueueWithOrders;
    private static final Logger logger= LoggerFactory.getLogger (MainController.class);

    public MainController(OrderDAOImpl orderDAO,ExecutionDAOImpl executionDAO,ArrayBlockingQueue blockingQueueWithOrders) {
        this.orderDAO = orderDAO;
        this.executionDAO = executionDAO;
        this.arrayBlockingQueueWithOrders = blockingQueueWithOrders;
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
        List<Execution> listOfAllExecutions = executionDAO.getAllExecutions();
        model.addAttribute("listOfAllExecutions", listOfAllExecutions);
        return "executions";
    }

    @RequestMapping(value = "/addNewOrder", method = RequestMethod.GET)
    public String addNewOrder(ModelMap model) {
        model.addAttribute("addNewOrder", "Here you will add new order");
        return "addOrder";
    }

    @RequestMapping( value = "/addNewOrder",method =  RequestMethod.POST)
    public String addNewOrderToDatabase(@RequestParam("quantity") int quantityOfOrder, @RequestParam("typeOfOrder") String typeOfOrder, ModelMap model){
        putNewOrderToArrayBlockingQueueWithOrders(typeOfOrder,quantityOfOrder);

        model.addAttribute("typeOfOrder",typeOfOrder);
        model.addAttribute("quantityOfOrder",quantityOfOrder);

        return "afterAddedNewOrder";
    }

    public void putNewOrderToArrayBlockingQueueWithOrders(String typeOfOrder,int quantityOfOrder){
        try {
            arrayBlockingQueueWithOrders.put(typeOfOrder+" "+ quantityOfOrder);
        } catch (InterruptedException e) {
            logger.error("Couldn`t add new order", e);
        }
    }
}