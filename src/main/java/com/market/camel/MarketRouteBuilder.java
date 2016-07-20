package com.market.camel;

import com.market.service.camel.ExecutionCreator;
import com.market.service.camel.ExecutionMessageConverter;
import com.market.service.camel.OrderMessageConverter;
import com.market.database.ExecutionDAO;
import com.market.database.OrderDAO;
import com.market.service.myBatis.ExecutionDAOService;
import com.market.service.myBatis.OrderDAOService;
import org.apache.camel.spring.SpringRouteBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import javax.sql.DataSource;


/**
 * Created by pizmak on 2016-05-17.
 */
@EnableWebMvc
//@MapperScan(basePackages = "com.market.mappers")
public class MarketRouteBuilder extends SpringRouteBuilder {
    public static final  String MAIN_ROUTE_ENTRY = "direct:mainRoute";
    public static final  String JMS_EXECUTION_INFO = "jms:testExecutionsInformationQueue";
    public static final  String JMS_NEW_ORDERS = "jms:testQueueWithNewOrders";
    public static final  String WEB_ROUTE_ENTRY = "direct:webRoute";

    @Autowired
    private OrderMessageConverter orderMessageConverter;
    @Autowired
    private ExecutionCreator executionCreator;
    @Autowired
    private ExecutionDAOService executionDAOService;
    @Autowired
    private ExecutionMessageConverter executionMessageConverter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private OrderDAOService orderDAOService;

    @Override
    public void configure() throws Exception {
//        onException(IOException.class)
//                .useOriginalMessage();
//                .handled(false);
               // .maximumRedeliveries(2).redeliveryDelay(0);

        from(MAIN_ROUTE_ENTRY)
                .id(MAIN_ROUTE_ENTRY)
                .transacted()
                .bean(orderMessageConverter)
                //.bean(orderDAO, OrderDAO.SAVE_ORDER_METHOD_NAME)
                .bean(orderDAOService,OrderDAOService.SAVE_ORDER_METHOD_NAME)
                .bean(executionCreator)
                .split(body())
                    .bean(executionDAOService,ExecutionDAOService.SAVE_EXECUTION_METHOD_NAME)
                    .bean(orderDAOService,OrderDAOService.UPDATE_ORDER_AFTER_EXECUTION_METHOD_NAME)
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
