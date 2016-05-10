package camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import service.OrderManager;
import service.OrderReader;

import javax.jms.ConnectionFactory;


/**
 * Created by pizmak on 2016-04-21.
 */
@Configuration
//@ComponentScan(basePackages = "bean.configuration")
public class ServerConfig extends CamelConfiguration {
    @Autowired
    private OrderReader orderReader;

    @Bean
    public RouteBuilder routeBuilder(){
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:mainRoute")
                        .transacted()
                        .bean(MainLogger.class)
                        .bean(orderReader);

                from("jms:testQueueWithNewOrders")
                        .routeId("testQueueWithNewOrders")
                        .errorHandler(deadLetterChannel("jms:testQueueWithNewOrders.Dead").useOriginalMessage())
                        .bean(MainLogger.class)
                        .to("direct:mainRoute");
            }

        };
    }

    @Bean
    public ConnectionFactory connectionFactory(){
     return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
}
