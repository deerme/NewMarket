package camel;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.apache.activemq.broker.region.policy.DeadLetterStrategy;
import org.apache.activemq.broker.region.policy.IndividualDeadLetterStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultRouteContext;
import org.apache.camel.processor.RedeliveryPolicy;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.spi.TransactionErrorHandlerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.jms.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.context.transaction.TestTransaction.end;

/**
 * Created by pizmak on 2016-04-21.
 */
@Configuration
@ComponentScan(basePackages = "bean.configuration")
public class ServerConfig  extends CamelConfiguration {
    @Autowired
    private OrderProcessor orderProcessor;

    @Bean
    RouteBuilder routeBuilder(){
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
               // from("jms:topic:ActiveMQ.Advisory.Consumer.Queue.ActiveMQ.DLQ").bean(MainLogger.class);

               // RedeliveryPolicy policyEntry = new RedeliveryPolicy();
                //ErrorHandlerBuilder errorHandlerBuilder = new TransactionErrorHandlerBuilder();
               // camelContext().setErrorHandlerBuilder(new TransactionErrorHandlerBuilder());
                //setupCamelContext();
                //setErrorHandlerBuilder(errorHandlerBuilder);
                errorHandler(new TransactionErrorHandlerBuilder());
                        //errorHandler(deadLetterChannel("jms:queue:dead")));

                        from("direct:errors")
                                .log(LoggingLevel.INFO, "In process to dead queue")
                                .to("jms:queueWithNotPassedOrders");
                from("direct:mainRoute")
                       // .onException(Exception.class).handled(true).to("direct:errors").end()

                        //.onException(Exception.class).markRollbackOnlyLast().end()
                       // .doTry()
                      // .onException(Exception.class).maximumRedeliveries(3)
                        .transacted()
                       // .doTry()

                        .log(LoggingLevel.INFO,"111")
                        .bean(orderProcessor, "addNewOrderToArrayBlockingQueueWithWaitingOrders");
                       // .endDoTry()
                        //.doCatch(Exception.class).to("jms:queueWithNotPassedOrders")
                        //.end();
                       //  .to("jms:test");
                        //doCatch(Exception.class)
                         //.bean(MainLogger.class)
                       // end();

                from("jms:testQueueWithNewOrders")
                    .transacted()
                    .bean(MainLogger.class)
                    .to("direct:mainRoute");
            }

        };
    }

    @Bean
    ConnectionFactory connectionFactory(){

     //   ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//
//       RedeliveryPolicyMap map = connectionFactory.getRedeliveryPolicyMap();
//        RedeliveryPolicy policyEntry = new RedeliveryPolicy();
//
//        IndividualDeadLetterStrategy individualDeadLetterStrategy = new IndividualDeadLetterStrategy();
//        individualDeadLetterStrategy.setQueuePrefix(("DLQ."));
//        individualDeadLetterStrategy.setUseQueueForQueueMessages(true);
//
//        policyEntry.

//        individualDeadLetterStrategy.se
//
//        policyEntry.setQueue("DLQ.QUEUEFATAL");
//        connectionFactory.setRedeliveryPolicy(policyEntry);
//        List<RedeliveryPolicy> policyEntries = new ArrayList<>();
//        policyEntries.add(policyEntry);
//        map.setRedeliveryPolicyEntries(policyEntries);
//        connectionFactory.setRedeliveryPolicyMap(map);
//
//
//        connectionFactory.getRedeliveryPolicyMap().setRedeliveryPolicyEntries(policyEntries);


        //return connectionFactory;
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
}
