package beanConfig;

import database.ExecutionDAOImpl;
import database.OrderDAOImpl;
import mq.Receiver;
import mq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import service.ExecutionManager;
import service.OrderManager;
import service.OrderReader;
import service.OrderReceiver;
import javax.jms.JMSException;
import javax.sql.DataSource;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by pizmak on 2016-04-06.
 */
@Configuration
@PropertySource("classpath:/jdbc.properties")
//@Lazy
public class BeanConfiguration {
    @Autowired
    private Environment env;

    @Lazy
    @Bean( destroyMethod="closeConnection")
    public Receiver receiver(){
        return new Receiver(env.getProperty("jdbc.queueName"),env.getProperty("jdbc.serverName"));
    }

    @Lazy
    @Bean(initMethod = "initSender" ,destroyMethod="closeConnection")
    public Sender sender() throws JMSException {
        return new Sender(env.getProperty("jdbc.executionqueueName"),env.getProperty("jdbc.serverName"));
    }

    @Bean
    public ExecutionDAOImpl executionDAO(DriverManagerDataSource dataSource){
        return new ExecutionDAOImpl(dataSource);
    }

    @Bean
    public OrderDAOImpl orderDAO(DriverManagerDataSource dataSource){
        return new OrderDAOImpl(dataSource);
    }

    @Bean(initMethod = "initReceiver")
    public OrderReceiver orderReceiver(Receiver receiver,ArrayBlockingQueue arrayBlockingQueue,DataSourceTransactionManager dataSourceTransactionManager){
        return new OrderReceiver(receiver,arrayBlockingQueue,dataSourceTransactionManager);
    }

    @Bean
    public ArrayBlockingQueue blockingQueueWithOrders(){
        return new ArrayBlockingQueue(1000);
    }

    @Bean(initMethod = "initOrderReader")
    public OrderReader orderReader(OrderManager orderManager , ArrayBlockingQueue arrayBlockingQueue){
        return new OrderReader(orderManager,arrayBlockingQueue);
    }

    @Bean
    public OrderManager orderManager(ExecutionManager executionManager, OrderDAOImpl orderDAO){
        return new OrderManager(executionManager,orderDAO);
    }

    @Bean
    ExecutionManager executionManager(Sender sender,ExecutionDAOImpl executionDAO){
        return new ExecutionManager(sender,executionDAO);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        return  dataSource;
    }
}
