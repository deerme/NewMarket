package beanTestConfiguration;

import database.ExecutionDAO;
import database.ExecutionDAOImpl;
import database.OrderDAO;
import database.OrderDAOImpl;
import mq.Receiver;
import mq.Sender;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import service.ExecutionManager;
import service.OrderManager;
import service.OrderReader;
import service.OrderReceiver;

import javax.jms.JMSException;
import javax.sql.DataSource;
import java.util.concurrent.ArrayBlockingQueue;

import static org.mockito.Mockito.spy;


/**
 * Created by pizmak on 2016-04-11.
 */
@Configuration
@EnableTransactionManagement
public class BeanTestConfigurationWithMocks {

//        @Autowired
//        private Environment env;
//
//
        @Bean
        public ExecutionDAO testExecutionDAO(){
            return new ExecutionDAOImpl(dataSource());
        }
//
        @Bean
        public OrderDAO testOrderDAO(DataSource dataSource){
            return spy(new OrderDAOImpl(dataSource));
        }
//
        @Bean
        public OrderManager testOrderManager(){
            return new OrderManager(testExecutionManager(), testOrderDAO(dataSource()));
        }
//
        @Bean
        public Sender testSender() {
            return Mockito.mock(Sender.class);
        }
//
//        @Bean
//        public ArrayBlockingQueue testBlockingQueueWithOrders(){
//            return new ArrayBlockingQueue(1000);
//        }
//
        @Bean
        public ExecutionManager testExecutionManager(){
            return new ExecutionManager(testSender(), testExecutionDAO());
        }
//
        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            EmbeddedDatabase db = builder
                    .setType(EmbeddedDatabaseType.H2)
                    .addScript("schema.sql")
                    .addScript("data.sql")
                    .build();
            return db;
        }
//
//
//
//    @Bean(initMethod = "initReceiver")
//    public OrderReceiver testOrderReceiver(Receiver testReceiver, ArrayBlockingQueue testArrayBlockingQueue, DataSourceTransactionManager dataSourceTransactionManager){
//        return new OrderReceiver(testReceiver,testArrayBlockingQueue,dataSourceTransactionManager);
//    }
//
//    @Bean( destroyMethod="closeConnection")
//    public Receiver testReceiver(){
//        return new Receiver("testQueueWithNewOrders","tcp://localhost:61616");
//    }
//
//    @Bean(initMethod = "initOrderReader")
//    public OrderReader orderReader(OrderManager orderManager , ArrayBlockingQueue arrayBlockingQueue){
//        return new OrderReader(orderManager,arrayBlockingQueue);
//    }
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }
}
