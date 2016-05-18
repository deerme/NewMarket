package com.market.camel;


import com.market.service.ExecutionCreator;
import com.market.service.ExecutionMessageConverter;
import com.market.service.OrderConverter;
import com.market.database.ExecutionDAO;
import com.market.database.OrderDAO;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pizmak on 2016-05-17.
 */
public class MarketRouteBuilder extends RouteBuilder {
    public static final  String MAIN_ROUTE_ENTRY = "direct:mainRoute";
    public static final  String JMS_EXECUTION_INFO = "jms:testExecutionsInformationQueue";
    public static final  String JMS_NEW_ORDERS = "jms:testQueueWithNewOrders";
    public static final  String WEB_ROUTE_ENTRY = "direct:webRoute";

    @Autowired
    private OrderConverter orderConverter;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ExecutionCreator executionCreator;
    @Autowired
    private ExecutionDAO executionDAO;
    @Autowired
    private ExecutionMessageConverter executionMessageConverter;

    @Override
    public void configure() throws Exception {
        from(MAIN_ROUTE_ENTRY)
                .id(MAIN_ROUTE_ENTRY)
                .transacted()
//                .onException(Throwable.class).handled(false)
               // .markRollbackOnly()
                .bean(orderConverter)
                .bean(orderDAO, OrderDAO.SAVE_ORDER_METHOD_NAME)
                .bean(executionCreator)
                .split(body())
                    .bean(executionDAO)
                    .bean(orderDAO,OrderDAO.UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME)
                    .bean(executionMessageConverter)
                    .to(JMS_EXECUTION_INFO);

        from(WEB_ROUTE_ENTRY)
                .id(WEB_ROUTE_ENTRY)
                .to(JMS_NEW_ORDERS);


        from(JMS_NEW_ORDERS)
                .errorHandler(deadLetterChannel("jms:testQueueWithNewOrders.Dead").useOriginalMessage())
                .to(MAIN_ROUTE_ENTRY);

    }
}
