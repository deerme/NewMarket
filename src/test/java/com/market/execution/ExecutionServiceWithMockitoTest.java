package com.market.execution;

import com.market.FirstIntegrationTest;
import com.market.TestConfiguration;
import com.market.mappers.ExecutionMapper;
import com.market.mappers.OrderMapper;
import com.market.model.changed.ExecutionTemp;
import com.market.model.changed.OrderTemp;
import com.market.model.standard.Execution;
import com.market.model.standard.Order;
import com.market.service.myBatis.ExecutionDAOService;
import com.market.service.myBatis.ExecutionDAOServiceImpl;
import com.market.service.myBatis.OrderDAOService;
import com.market.service.myBatis.OrderDAOServiceImpl;
import com.market.service.myBatis.converters.ExecutionConverter;
import com.market.service.myBatis.converters.OrderConverter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by pizmak on 2016-07-20.
 */

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class ExecutionServiceWithMockitoTest {
    private static final int DEFAULT_ID = 0;

    private static final String BUY_ORDER_TYPE = "BUY";
    private static final int BUY_ORDER_QUANTITY = 120;
    private static final int BUY_ORDER_GENERATED_ID = 10;

    private static final String SELL_ORDER_TYPE = "SELL";
    private static final int SELL_ORDER_QUANTITY = 110;
    private static final int SELL_ORDER_GENERATED_ID = 11;

    private static final int EXECUTION_ID = 23;
    private static final int EXECUTION_QUANTITY = Math.min(BUY_ORDER_QUANTITY, SELL_ORDER_QUANTITY);
    private static final int EXECUTION_ID_SELLER = SELL_ORDER_GENERATED_ID;
    private static final int EXECUTION_ID_BUYER = BUY_ORDER_GENERATED_ID;

    @Mock
    private ExecutionMapper executionMapper;

    @Mock
    private ExecutionConverter executionConverter;

    @InjectMocks
    private ExecutionDAOServiceImpl executionDAOService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderConverter orderConverter;

    @InjectMocks
    private OrderDAOServiceImpl orderDAOService;


    @Test
    public void testAddExecution() {
        Order buyOrderToAdd = new Order(Optional.empty(), BUY_ORDER_TYPE, BUY_ORDER_QUANTITY);
        OrderTemp buyOrderTemp = new OrderTemp(DEFAULT_ID, BUY_ORDER_TYPE, BUY_ORDER_QUANTITY);
        Order buyOrderAfterAdd = new Order(Optional.of(BUY_ORDER_GENERATED_ID), BUY_ORDER_TYPE, BUY_ORDER_QUANTITY);

        Order sellOrderToAdd = new Order(Optional.empty(), SELL_ORDER_TYPE, SELL_ORDER_QUANTITY);
        OrderTemp sellOrderTemp = new OrderTemp(DEFAULT_ID, SELL_ORDER_TYPE, SELL_ORDER_QUANTITY);
        Order sellOrderAfterAdd = new Order(Optional.of(SELL_ORDER_GENERATED_ID), SELL_ORDER_TYPE, SELL_ORDER_QUANTITY);


        when(orderConverter.createOrderTempFromOrder(Matchers.refEq(buyOrderToAdd)))
                .thenReturn(buyOrderTemp);

        when(orderConverter.createOrderFromOrderTemp(Matchers.refEq(buyOrderTemp)))
                .thenReturn(buyOrderAfterAdd);

        when(orderMapper.addOrderTemp(Matchers.refEq(buyOrderTemp)))
                .thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                        OrderTemp order = (OrderTemp) invocationOnMock.getArguments()[0];
                        order.setId(BUY_ORDER_GENERATED_ID);
                        return 0;
                    }
                });


        when(orderConverter.createOrderTempFromOrder(Matchers.refEq(sellOrderToAdd)))
                .thenReturn(sellOrderTemp);
        when(orderConverter.createOrderFromOrderTemp(Matchers.refEq(sellOrderTemp)))
                .thenReturn(sellOrderAfterAdd);

        when(orderMapper.addOrderTemp(Matchers.refEq(sellOrderTemp)))
                .thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                        OrderTemp order = (OrderTemp) invocationOnMock.getArguments()[0];
                        order.setId(SELL_ORDER_GENERATED_ID);
                        return 0;
                    }
                });


        int buyOrderId = orderDAOService.addOrder(buyOrderToAdd).getId().get();
        int sellOrderId = orderDAOService.addOrder(sellOrderToAdd).getId().get();

        ArgumentCaptor<Execution> executionArgumentCaptor = ArgumentCaptor.forClass(Execution.class);
        ArgumentCaptor<ExecutionTemp> executionTempArgumentCaptor = ArgumentCaptor.forClass(ExecutionTemp.class);

        verify(orderMapper, times(2)).addOrderTemp(any(OrderTemp.class));
        verify(orderConverter, times(2)).createOrderTempFromOrder(any(Order.class));

        verify(orderConverter, never()).createOrderListFromOrderTempList(anyList());
        verify(orderConverter, times(2)).createOrderFromOrderTemp(any(OrderTemp.class));
        verify(orderMapper, never()).getOrderTemp(anyInt());
        verify(orderMapper, never()).getAllTempOrders();
        verify(orderMapper, never()).updateOrder(any(OrderTemp.class));


        Execution execution = new Execution(Optional.empty(), buyOrderId, sellOrderId, EXECUTION_QUANTITY);
        ExecutionTemp executionTemp = new ExecutionTemp(DEFAULT_ID, buyOrderId, sellOrderId, EXECUTION_QUANTITY);
        Execution executionAfterAdd = new Execution(Optional.of(EXECUTION_ID), buyOrderId, sellOrderId, EXECUTION_QUANTITY);

        when(executionConverter.createExecutionTempFromExecution(Matchers.refEq(execution))).
                thenReturn(executionTemp);

        when(executionConverter.createExecutionFromExecutionTemp(Matchers.refEq(executionTemp))).
                thenReturn(executionAfterAdd);

        when(executionMapper.addExecution(Matchers.refEq(executionTemp))).then(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                ExecutionTemp exec = (ExecutionTemp) invocationOnMock.getArguments()[0];
                exec.setId(EXECUTION_ID);
                return 0;
            }
        });

        executionDAOService.addExecution(execution);

        verify(executionConverter).createExecutionTempFromExecution(executionArgumentCaptor.capture());
        verify(executionMapper).addExecution(executionTempArgumentCaptor.capture());


        assertEquals(Optional.empty(), executionArgumentCaptor.getValue().getId());
        assertEquals(EXECUTION_ID_BUYER, executionArgumentCaptor.getValue().getIdBuyer());
        assertEquals(EXECUTION_ID_SELLER, executionArgumentCaptor.getValue().getIdSeller());
        assertEquals(EXECUTION_QUANTITY, executionArgumentCaptor.getValue().getQuantity());

        assertEquals(EXECUTION_ID, executionTempArgumentCaptor.getValue().getId());
        assertEquals(EXECUTION_ID_BUYER, executionTempArgumentCaptor.getValue().getIdBuyer());
        assertEquals(EXECUTION_ID_SELLER, executionTempArgumentCaptor.getValue().getIdSeller());
        assertEquals(EXECUTION_QUANTITY, executionArgumentCaptor.getValue().getQuantity());
    }
}