package com.market.order;

import com.market.mappers.OrderMapper;
import com.market.model.changed.OrderTemp;
import com.market.model.standard.Order;
import com.market.service.myBatis.OrderDAOService;
import com.market.service.myBatis.OrderDAOServiceImpl;
import com.market.service.myBatis.converters.OrderConverter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * Created by pizmak on 2016-07-20.
 */

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceWithMockitoTest {
    private static final String TYPE_147 = "type147";
    private static final int QUANTITY_123 = 123;
    private static final int DEFAULT_ID = 0;
    private static final int GENERATED_ID = 49;

    private Order orderToAdd;
    private OrderTemp tmpOrder;
    private Order orderAfterAdd;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderConverter orderConverter;

    @InjectMocks
    private OrderDAOServiceImpl orderDAOService;


    @Before
    public void createOrderAndOrderTemp() {
        //given
        orderToAdd = new Order(Optional.empty(), TYPE_147, QUANTITY_123);
        tmpOrder = new OrderTemp(DEFAULT_ID, TYPE_147, QUANTITY_123);
        orderAfterAdd = new Order(Optional.of(GENERATED_ID),TYPE_147,QUANTITY_123);

    }

    @Test
    public void testAddOrder() {
        when(orderConverter.createOrderTempFromOrder(Matchers.refEq(orderToAdd)))
                .thenReturn(tmpOrder);

        when(orderConverter.createOrderFromOrderTemp(Matchers.refEq(tmpOrder)))
                .thenReturn(orderAfterAdd);

        when(orderMapper.addOrderTemp(Matchers.refEq(tmpOrder)))
                .thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                        OrderTemp arg = (OrderTemp) invocationOnMock.getArguments()[0];
                        arg.setId(GENERATED_ID);
                        return 0;
                    }
                });

        //when
       int newId = orderDAOService.addOrder(orderToAdd).getId().get();
        //then
        ArgumentCaptor<Order> argument1 = ArgumentCaptor.forClass(Order.class);
        verify(orderConverter).createOrderTempFromOrder(argument1.capture());
        assertEquals(Optional.empty(), argument1.getValue().getId());
        assertEquals(TYPE_147, argument1.getValue().getType());
        assertEquals(QUANTITY_123, argument1.getValue().getQuantity());

        ArgumentCaptor<OrderTemp> argument2 = ArgumentCaptor.forClass(OrderTemp.class);
        verify(orderMapper).addOrderTemp(argument2.capture());
        assertEquals(TYPE_147, argument2.getValue().getType());
        assertEquals(QUANTITY_123, argument2.getValue().getQuantity());

        assertEquals(GENERATED_ID, newId);
    }

    @Test
    public void testInvocationOfMethodsDuringAddingNewOrder() {
        InOrder inOrder = inOrder(orderMapper, orderConverter);


        when(orderConverter.createOrderTempFromOrder(Matchers.refEq(orderToAdd)))
                .thenReturn(tmpOrder);

        when(orderConverter.createOrderFromOrderTemp(Matchers.refEq(tmpOrder)))
                .thenReturn(orderAfterAdd);

        when(orderMapper.addOrderTemp(Matchers.refEq(tmpOrder)))
                .thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                        OrderTemp arg = (OrderTemp) invocationOnMock.getArguments()[0];
                        arg.setId(GENERATED_ID);
                        System.out.println(arg);
                        return 0;
                    }
                });

        Order order = orderDAOService.addOrder(orderToAdd);

        
        inOrder.verify(orderConverter).createOrderTempFromOrder(orderToAdd);
        inOrder.verify(orderMapper).addOrderTemp(tmpOrder);


        verify(orderConverter, never()).createOrderListFromOrderTempList(anyList());
        verify(orderMapper, never()).getOrderTemp(anyInt());
        verify(orderMapper, never()).getAllTempOrders();
        verify(orderMapper, never()).updateOrder(any(OrderTemp.class));
        verify(orderMapper, only()).addOrderTemp(any(OrderTemp.class));
    }

}