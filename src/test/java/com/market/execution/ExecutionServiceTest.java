package com.market.execution;

import com.market.camel.MarketSpringContext;
import com.market.model.standard.Execution;
import com.market.model.standard.Order;
import com.market.service.myBatis.ExecutionDAOService;
import com.market.service.myBatis.OrderDAOService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by pizmak on 2016-07-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ExecutionServiceTest.ExecutionServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class ExecutionServiceTest {
    private static final String NAME_OF_TABLE_WITH_EXECUTIONS = "execution";
    private static final String NAME_OF_TABLE_WITH_ORDERS = "orderinmarket";

    private static final String BUY_ORDER_TYPE = "BUY";
    private static final int BUY_ORDER_QUANTITY = 20;

    private static final String SELL_ORDER_TYPE = "SELL";
    private static final int SELL_ORDER_QUANTITY = 110;

    private static final int EXECUTION_QUANTITY = Math.min(BUY_ORDER_QUANTITY, SELL_ORDER_QUANTITY);



    @Autowired
    private OrderDAOService orderDAOService;

    @Autowired
    private ExecutionDAOService executionDAOService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private int buyOrderId;
    private int sellOrderId;

    @Before
    public void createAndAddTwoOrdersToDatabase(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS,NAME_OF_TABLE_WITH_ORDERS);

        Order buyOrder = orderDAOService.addOrder(new Order(Optional.empty(),BUY_ORDER_TYPE,BUY_ORDER_QUANTITY));
        Order sellOrder = orderDAOService.addOrder(new Order(Optional.empty(),SELL_ORDER_TYPE,SELL_ORDER_QUANTITY));

        buyOrderId = buyOrder.getId().get();
        sellOrderId = sellOrder.getId().get();
    }

    @Test
    public void  testGettingListOfExecutionsShouldReturnOneExecution() {
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS));

        executionDAOService.addExecution(new Execution(Optional.empty(), buyOrderId,sellOrderId,EXECUTION_QUANTITY));

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,NAME_OF_TABLE_WITH_EXECUTIONS));
        assertEquals(1, executionDAOService.getAllExecutions().size());
    }
    @Configuration
    @Import({MarketSpringContext.class})
    public static class ExecutionServiceTestConfig {
        public ExecutionServiceTestConfig() {
        }

        @Primary
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .addScript("schema.sql")
                    .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource());
        }
    }

}