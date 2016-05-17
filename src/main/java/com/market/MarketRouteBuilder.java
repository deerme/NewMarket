package com.market;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by pizmak on 2016-05-17.
 */
public class MarketRouteBuilder extends RouteBuilder {
    public static final  String MAIN_ROUTE_ENTRY = "direct:mainRoute";
    public static final String JMS_EXECUTION_INFO = "jms:testExecutionsInformationQueue";

    private OrderConverter orderConverter;
    private OrderDAO orderDAO;
    private ExecutionCreator executionCreator;
    private ExecutionDAO executionDAO;
    private ExecutionMessageConverter executionMessageConverter;

    public MarketRouteBuilder(
            OrderConverter orderConverter,
            OrderDAO orderDAO,
            ExecutionCreator executionCreator,
            ExecutionDAO executionDAO,
            ExecutionMessageConverter executionMessageConverter
    ) {
        this.orderConverter = orderConverter;
        this.orderDAO = orderDAO;
        this.executionCreator = executionCreator;
        this.executionDAO = executionDAO;
        this.executionMessageConverter = executionMessageConverter;
    }

    @Override
    public void configure() throws Exception {
        from(MAIN_ROUTE_ENTRY)
                .bean(orderConverter)
                .bean(orderDAO, OrderDAO.SAVE_ORDER_METHOD_NAME)
                .bean(executionCreator)
                .split(body())
                    .bean(executionDAO)
                    .bean(orderDAO,OrderDAO.UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME)
                    .bean(executionMessageConverter)
                    .to(JMS_EXECUTION_INFO);

    }
}
