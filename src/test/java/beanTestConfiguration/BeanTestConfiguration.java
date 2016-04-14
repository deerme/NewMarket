package beanTestConfiguration;

import database.ExecutionDAOImpl;
import database.OrderDAOImpl;
import mq.Sender;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import service.ExecutionManager;
import service.OrderManager;
import javax.jms.JMSException;
import javax.sql.DataSource;
import java.util.concurrent.ArrayBlockingQueue;

import static org.mockito.Mockito.spy;


/**
 * Created by pizmak on 2016-04-11.
 */
@Configuration
public class BeanTestConfiguration  {

        @Autowired
        private Environment env;




        @Bean
        public ExecutionDAOImpl testExecutionDAO(){
            return new ExecutionDAOImpl(dataSource());
        }

        @Bean
        public OrderDAOImpl testOrderDAO(){
            return new OrderDAOImpl(dataSource());
        }

        @Bean
        public OrderDAOImpl mockTestOrderDAO(){
            return spy(new OrderDAOImpl(dataSource()));
        }

        @Bean
        public OrderManager testOrderManager() throws JMSException {
            return new OrderManager(testExecutionManager(),testOrderDAO());
        }

        @Bean
        public Sender testSender() throws JMSException {
            return Mockito.mock(Sender.class);
        }

        @Bean
        public ArrayBlockingQueue testBlockingQueueWithOrders(){
            return new ArrayBlockingQueue(1000);
        }

        @Bean
        ExecutionManager testExecutionManager() throws JMSException {
            return new ExecutionManager(testSender(),testExecutionDAO());
        }

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

        @Bean
        public DataSourceTransactionManager dataSourceTransactionManager(){
            return new DataSourceTransactionManager(dataSource());
        }
}
