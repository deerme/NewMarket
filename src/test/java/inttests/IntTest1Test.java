package inttests;

import bean.configuration.BeanConfiguration;
import camel.ServerConfig;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.jms.ConnectionFactory;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {BeanConfiguration.class, ServerConfig.class},
        loader = AnnotationConfigContextLoader.class
)
public class IntTest1Test {

    @Autowired
    private ModelCamelContext camelContext;

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    RouteBuilder routeBuilder;

    @Test
    public void test1() throws Exception {
        System.out.println("routeBuilder = " + routeBuilder);
        camelContext
                .getRouteDefinition("testQueueWithNewOrders")
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpoints("jms:testQueueWithNewOrders");
            }
        });
        camelContext.createProducerTemplate().sendBody("jms:testQueueWithNewOrders", "SELL 100");
    }

}
