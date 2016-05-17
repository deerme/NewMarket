package com.market;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by pizmak on 2016-05-17.
 */
public class MarketRouteBuilder extends RouteBuilder {
    public final static String MAIN_ROUTE_ENTRY = "direct:mainRoute";

    private OrderConverter orderConverter;
    private OrderDAO orderDAO;

    public MarketRouteBuilder(OrderConverter orderConverter, OrderDAO orderDAO) {
        this.orderConverter = orderConverter;
        this.orderDAO = orderDAO;
    }

    @Override
    public void configure() throws Exception {
        from(MAIN_ROUTE_ENTRY)
                .bean(orderConverter)
                .bean(orderDAO, OrderDAO.SAVE_ORDER_METHOD_NAME);

    }
}
