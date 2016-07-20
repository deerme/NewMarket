package com.market;

import com.market.camel.MarketRouteBuilder;
import com.market.camel.MarketSpringContext;
import com.market.database.ExecutionDAO;
import com.market.database.ExecutionDAOImpl;
import com.market.database.OrderDAO;
import com.market.database.OrderDAOImpl;
import com.market.service.camel.*;
import com.market.service.myBatis.ExecutionDAOService;
import com.market.service.myBatis.ExecutionDAOServiceImpl;
import com.market.service.myBatis.OrderDAOService;
import com.market.service.myBatis.OrderDAOServiceImpl;
import com.market.service.myBatis.converters.ExecutionConverter;
import com.market.service.myBatis.converters.OrderConverter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

/**
 * Created by pizmak on 2016-07-20.
 */
@Import({MarketSpringContext.class})
public class TestConfiguration {
    @Bean
    public OrderMessageConverter myBatisOrderConverter() {
        return new OrderMessageConverterImpl();
    }

    @Bean
    public RouteBuilder routeBuilder() {
        return new MarketRouteBuilder();
    }

    @Bean
    public ExecutionCreator executionCreator() {
        return new ExecutionCreatorImpl(orderDAOService());
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
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public OrderDAOServiceImpl orderDAOService(){
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
    @Primary
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
