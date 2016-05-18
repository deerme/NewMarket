package com.market.camel;

import com.market.service.*;
import com.market.web.MarketWebAppInitializer;
import com.market.database.ExecutionDAO;
import com.market.database.ExecutionDAOImpl;
import com.market.database.OrderDAO;
import com.market.database.OrderDAOImpl;
import com.market.web.MainController;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

/**
 * Created by pizmak on 2016-05-17.
 */
@EnableWebMvc
@PropertySource("classpath:/jdbc.properties")
@Configuration
public class MarketSpringContext extends CamelConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

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
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

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
        return new MainController(orderDAO(),executionDAO());
    }

    @Bean
    public MarketWebAppInitializer marketInitializer(){
        return new MarketWebAppInitializer();
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
//        dataSource.setUrl(env.getProperty("jdbc.url"));
//        dataSource.setUsername(env.getProperty("jdbc.username"));
//        dataSource.setPassword(env.getProperty("jdbc.password"));

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/market2");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return  dataSource;
    }
}
