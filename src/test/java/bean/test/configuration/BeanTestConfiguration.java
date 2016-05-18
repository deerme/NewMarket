package bean.test.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import static org.mockito.Mockito.spy;


/**
 * Created by pizmak on 2016-04-11.
 */
@Ignore
@Configuration
public class BeanTestConfiguration  {

//        @Autowired
//        private Environment env;
//
//
//        @Bean
//        public OldExecutionDAOImpl testExecutionDAO(){
//            return new OldExecutionDAOImpl(testDataSource());
//        }
//
//        @Bean
//        public OldOrderDAOImpl testOrderDAO(){
//            return new OldOrderDAOImpl(testDataSource());
//        }
//
//        @Bean
//        public OldOrderDAOImpl mockTestOrderDAO(){
//            return spy(new OldOrderDAOImpl(testDataSource()));
//        }
//
//
//        @Bean
//        public DataSource testDataSource() {
//            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//
//            return builder.setType(EmbeddedDatabaseType.H2).addScript("schema.sql").addScript("data.sql").build();
//        }
//
//        @Bean
//        public DataSourceTransactionManager dataSourceTransactionManager(){
//            return new DataSourceTransactionManager(testDataSource());
//        }
//
//        @Bean
//        ConnectionFactory connectionFactory(){
//        return new ActiveMQConnectionFactory("tcp://localhost:61616");
//    }
//
//        @Bean
//        OrderSplitterProcessor orderSplitterProcessor(){
//            return new OrderSplitterProcessor();
//        }
//
//        @Bean
//        OrderDAOSave orderDAOSave(){
//            return new OrderDAOSave();
//        }
//
//        @Bean
//        OrderPairsMatcher orderPairsMatcher(){
//            return  new OrderPairsMatcher();
//        }
//
//        @Bean
//        ExecutionManager executionManager2(){
//            return new ExecutionManager();
//        }
//
//        @Bean
//        public OldExecutionDAOImpl executionDAO(){
//            return new OldExecutionDAOImpl(testDataSource());
//        }
//
//        @Bean
//        public OldOrderDAO orderDAO(){
//            return new OldOrderDAOImpl(testDataSource());
//        }
}
