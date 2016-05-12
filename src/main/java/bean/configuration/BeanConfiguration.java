package bean.configuration;

import camel.MainLogger;
import camel.ServerConfig;
import controller.MainController;
import database.ExecutionDAOImpl;
import database.OrderDAO;
import database.OrderDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


/**
 * Created by pizmak on 2016-04-06.
 */

@EnableWebMvc
@Configuration
@PropertySource("classpath:/jdbc.properties")
@ComponentScan(basePackages = "camel")
@Import(ServerConfig.class)
public class BeanConfiguration {
    @Autowired
    private Environment env;


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
    MainLogger mainLogger(){
        return new MainLogger();
    }

    @Bean
    public ExecutionDAOImpl executionDAO(){
        return new ExecutionDAOImpl(dataSource());
    }

    @Bean
    public OrderDAO orderDAO(){
        return new OrderDAOImpl(dataSource());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
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
