package com.market.camel;

import com.market.database.ExecutionDAO;
import com.market.database.ExecutionDAOImpl;
import com.market.database.OrderDAO;
import com.market.database.OrderDAOImpl;
import com.market.service.*;
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

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

/**
 * Created by pizmak on 2016-05-17.
 */

@PropertySource("classpath:/jdbc.properties")
@Configuration
public class DbSpringContext extends CamelConfiguration {
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
