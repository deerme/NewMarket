package com.market.camel;

import com.market.database.ExecutionDAO;
import com.market.database.ExecutionDAOImpl;
import com.market.database.OrderDAO;
import com.market.database.OrderDAOImpl;
import com.market.service.camel.*;
import com.market.service.camel.OrderMessageConverter;
import com.market.service.myBatis.ExecutionDAOService;
import com.market.service.myBatis.ExecutionDAOServiceImpl;
import com.market.service.myBatis.OrderDAOService;
import com.market.service.myBatis.OrderDAOServiceImpl;
import com.market.service.myBatis.converters.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

/**
 * Created by pizmak on 2016-05-17.
 */
@MapperScan(basePackages = "com.market.mappers")
@PropertySource("classpath:/jdbc.properties")
@Configuration
@EnableTransactionManagement

public class MarketSpringContext extends CamelConfiguration {
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Bean
    public OrderMessageConverter myBatisOrderConverter() {
        return new OrderMessageConverterImpl();
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
        return new ExecutionCreatorImpl(orderDAOService());
    }

    @Bean
    public ExecutionDAO executionDAO(){
        return new ExecutionDAOImpl(dataSource);
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
    public ExecutionMessageConverter executionMessageConverter(){
        return new ExecutionMessageConverterImpl();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public OrderDAOService orderDAOService(){
        return new OrderDAOServiceImpl();
    }

    @Bean
    public ExecutionDAOService executionDAOService(){
        return new ExecutionDAOServiceImpl();
    }

    @Bean
    public ExecutionConverter executionConverter(){
        return new ExecutionConverter();
    }

    @Bean
    public OrderConverter orderConverter(){
        return new OrderConverter();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        return sessionFactory.getObject();
    }
}
