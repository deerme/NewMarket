package camel;

import database.OrderDAOSave;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.jms.ConnectionFactory;


/**
 * Created by pizmak on 2016-04-21.
 */
@Configuration
@ComponentScan(basePackages = "bean.configuration")
public class ServerConfig  extends CamelConfiguration {

    @Bean
    RouteBuilder routeBuilder(){
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:mainRoute")
                        .transacted()
                        .bean(OrderSplitterProcessor.class)
                        .split(body())
                        .log(LoggingLevel.INFO,"Starting from order : ${body}" )
                        .bean(OrderDAOSave.class)
                        .end()
                        .bean(OrderPairsMatcher.class,"process")
                        .split(body())
                        .bean(ExecutionManager.class)
                        .to("jms:testExecutionsInformationQueue");

                from("jms:testQueueWithNewOrders")
                        .errorHandler(deadLetterChannel("jms:testQueueWithNewOrders.Dead").useOriginalMessage())
                        .bean(MainLogger.class)
                        .to("direct:mainRoute");
            }

        };
    }

    @Bean
    ConnectionFactory connectionFactory(){
     return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean
    OrderSplitterProcessor orderSplitterProcessor(){
        return new OrderSplitterProcessor();
    }

    @Bean
    OrderDAOSave orderDAOSave(){
        return new OrderDAOSave();
    }

    @Bean
    OrderPairsMatcher orderPairsMatcher(){
        return  new OrderPairsMatcher();
    }

    @Bean
    ExecutionManager executionManager2(){
        return new ExecutionManager();
    }
}
