package beanConfig;

import controller.MainController;
import database.ExecutionDAOImpl;
import database.OrderDAOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by pizmak on 2016-04-07.
 */

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = {"beanConfig","controller"})
    public class MarketWebConfiguration {

        @Bean
        public ViewResolver viewResolver() {
            InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
            viewResolver.setViewClass(JstlView.class);
            viewResolver.setPrefix("/WEB-INF/views/");
            viewResolver.setSuffix(".jsp");

            return viewResolver;
        }

        @Bean
        public MainController mainController(OrderDAOImpl orderDAO, ExecutionDAOImpl executionDAO, ArrayBlockingQueue blockingQueueWithOrders){
            return new MainController(orderDAO,executionDAO,blockingQueueWithOrders);
        }

        @Bean
        public MarketWebAppInitializer marketInitializer(){
            return new MarketWebAppInitializer();
        }
    }
