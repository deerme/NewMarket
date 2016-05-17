package com.market;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * Created by pizmak on 2016-05-17.
 */
@Configuration
public class MarketSpringContext extends CamelConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    public OrderConverter orderConverter() {
        return new OrderConverterImpl();
    }

    @Bean
    public RouteBuilder routeBuilder() {
        return new MarketRouteBuilder(orderConverter(), orderDAO(), executionCreator(),executionDAO(),executionMessageConverter());
    }

    @Bean
    public OrderDAO orderDAO() {
        return new OrderDAOImpl(dataSource);
    }

    @Bean
    public ExecutionCreator executionCreator() {
        return new ExecutionCreatorImpl(orderDAO());
    }

    @Bean
    public ExecutionDAO executionDAO(){
        return new ExecutionDAOImpl(dataSource);
    }

    @Bean
    public ExecutionMessageConverter executionMessageConverter(){
        return new ExecutionMessageConverterImpl();
    }
}
