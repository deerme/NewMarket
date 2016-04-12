package beanTestConfiguration;

import database.ExecutionDAOImpl;
import database.OrderDAOImpl;
import mq.Receiver;
import mq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import service.ExecutionManager;
import service.OrderManager;
import service.OrderReader;
import service.OrderReceiver;

import javax.jms.JMSException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by pizmak on 2016-04-11.
 */
@Configuration
@PropertySource("classpath:/jdbc.properties")
public class BeanTestConfiguration  {

        @Autowired
        private Environment env;



    @Bean
    public ExecutionDAOImpl testExecutionDAO(DataSource dataSource){
        return new ExecutionDAOImpl(dataSource);
    }

    @Bean
    public OrderDAOImpl testOrderDAO(DataSource dataSource){
        return new OrderDAOImpl(dataSource);
    }

    @Bean
    public OrderManager testOrderManager(ExecutionManager testExecutionManager, OrderDAOImpl testOrderDAO){
        return new OrderManager(testExecutionManager,testOrderDAO);
    }

    @Bean
        ExecutionManager testExecutionManager(ExecutionDAOImpl testExecutionDAO){
            return new ExecutionManager(testExecutionDAO);
        }
//
//    @Lazy
//        @Bean( destroyMethod="closeConnection")
//        public Receiver testReceiver(){
//            return new Receiver(env.getProperty("jdbc.queueName"),env.getProperty("jdbc.serverName"));
//        }
//
//        @Lazy
//        @Bean(initMethod = "initSender" ,destroyMethod="closeConnection")
//        public Sender testSender() throws JMSException {
//            return new Sender(env.getProperty("jdbc.executionqueueName"),env.getProperty("jdbc.serverName"));
//        }
//
//
//
//        @Bean(initMethod = "initReceiver")
//        public OrderReceiver testOrderReceiver(Receiver testReceiver, ArrayBlockingQueue testBlockingQueueWithOrders){
//            return new OrderReceiver(testReceiver,testBlockingQueueWithOrders);
//        }
//
//        @Bean
//        public ArrayBlockingQueue testBlockingQueueWithOrders(){
//            return new ArrayBlockingQueue(1000);
//        }
//
//        @Bean(initMethod = "initOrderReader")
//        public OrderReader testOrderReader(OrderManager testOrderManager , ArrayBlockingQueue testBlockingQueueWithOrders){
//            return new OrderReader(testOrderManager,testBlockingQueueWithOrders);
//        }
//
//
//        @Bean
//        ExecutionManager testExecutionManager(Sender testSender,ExecutionDAOImpl testExecutionDAO){
//            return new ExecutionManager(testSender,testExecutionDAO);
//        }
//
//        @Bean
//        public  PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
//            return new PropertySourcesPlaceholderConfigurer();
//        }

        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            EmbeddedDatabase db = builder
                    .setType(EmbeddedDatabaseType.H2) //.H2 or .DERBY
                    .addScript("schema.sql")
                    .addScript("data.sql")
                    .build();
            return db;
        }
}
