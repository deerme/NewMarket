package com.market;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.sql.DataSource;

import java.util.List;

import static com.market.MarketRouteBuilder.MAIN_ROUTE_ENTRY;

/**
 * Created by pizmak on 2016-05-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Int1Test.Int1TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class Int1Test {
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private OrderDAO orderDAO;

    @Test
    public void test1() {
        Assert.assertEquals(0, orderDAO.getAllOpenOrders().size());
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody(MAIN_ROUTE_ENTRY, "SELL 147");
        List<Order2> allOpenOrders = orderDAO.getAllOpenOrders();
        Assert.assertEquals(1,  allOpenOrders.size());
        Order2 order = allOpenOrders.get(0);
        Assert.assertEquals("SELL", order.getType());
        Assert.assertEquals(147, order.getQuantity());
    }

    @Configuration
    @Import({MarketSpringContext.class})
    public static class Int1TestConfig {
        public Int1TestConfig() {
        }

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .addScript("schema.sql")
                    .build();
        }
    }
}
