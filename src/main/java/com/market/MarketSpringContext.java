package com.market;

import bean.configuration.MarketWebAppInitializer;
import controller.MainController;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.jms.ConnectionFactory;
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
        return new MarketRouteBuilder();
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

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

//    @Bean(initMethod = "createProducerTemplateFromCamelContext")
//    public MainController mainController() {
//        return new MainController(orderDAO(),executionDAO());
//    }

    @Bean
    public MarketWebAppInitializer marketInitializer(){
        return new MarketWebAppInitializer();
    }
}
