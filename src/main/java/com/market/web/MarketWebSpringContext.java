package com.market.web;

import com.market.camel.MarketSpringContext;
import com.market.database.ExecutionDAO;
import com.market.database.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by pizmak on 2016-05-18.
 */
@EnableWebMvc
@Component
@Import(MarketSpringContext.class)
public class MarketWebSpringContext {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ExecutionDAO executionDAO;

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Bean(initMethod = "createProducerTemplateFromCamelContext")
    public MainController mainController() {
        return new MainController(orderDAO,executionDAO);
    }

    @Bean
    public MarketWebAppInitializer marketInitializer(){
        return new MarketWebAppInitializer();
    }
}
