package bean.test.configuration;

import database.ExecutionDAOImpl;
import database.OrderDAOImpl;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;
import static org.mockito.Mockito.spy;


/**
 * Created by pizmak on 2016-04-11.
 */
@Ignore
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
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();

            return builder.setType(EmbeddedDatabaseType.H2).addScript("schema.sql").addScript("data.sql").build();
        }

        @Bean
        public DataSourceTransactionManager dataSourceTransactionManager(){
            return new DataSourceTransactionManager(dataSource());
        }
}
