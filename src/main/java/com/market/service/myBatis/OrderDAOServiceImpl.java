package com.market.service.myBatis;


import com.market.mappers.OrderMapper;
import com.market.model.changed.OrderTemp;
import com.market.model.standard.Execution;
import com.market.model.standard.Order;
import com.market.service.myBatis.converters.OrderConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-07-06.
 */
public class OrderDAOServiceImpl implements OrderDAOService {
    private Logger logger = LoggerFactory.getLogger("auditLogger");

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderConverter orderConverter;

    @Override
    public Order addOrder(Order orderToAdd) {
        OrderTemp orderTemp = orderConverter.createOrderTempFromOrder(orderToAdd);
        orderMapper.addOrderTemp(orderTemp);

        logger.info("Added order to database.Auto-generated id: " + orderTemp.getQuantity() +" Type of order: "+orderToAdd.getType()+" Quantity of order: " +orderTemp.getQuantity());

        return orderConverter.createOrderFromOrderTemp(orderTemp);
    }

    @Override
    public Order getOrder(int userId) {
        return orderConverter.createOrderFromOrderTemp(
                                orderMapper.getOrderTemp(userId));
    }

    @Override
    public List<Order> getListAllOrdersByType(String type){
        return orderConverter.createOrderListFromOrderTempList(
                                orderMapper.getAllTempOrdersByType(type));
    }

    @Override
    public Execution updateOrder(Execution execution){
        int quantityOfExecution = execution.getQuantity();

        orderMapper.updateOrder(orderConverter.createOrderTempFromOrder(new Order(
                Optional.of(execution.getIdBuyer()),
                        "BUY",quantityOfExecution)));

        orderMapper.updateOrder(orderConverter.createOrderTempFromOrder((new Order(
                Optional.of(execution.getIdSeller()),
                "SELL",quantityOfExecution))));

        logger.info("Update order to database.Id of order:" + execution.getIdBuyer()+" New quantity is:" + orderMapper.getOrderTemp(execution.getIdBuyer()).getQuantity());
        logger.info("Update order to database.Id of order:" + execution.getIdSeller()+" New quantity is:" + orderMapper.getOrderTemp(execution.getIdSeller()).getQuantity());

        return execution;
    }

    @Override
    public List<Order> getListAllOpenOrders() {
        return orderConverter.createOrderListFromOrderTempList(
                                orderMapper.getAllOpenTempOrders());
    }

    @Override
    public List<Order> getListAllOrders() {
        return orderConverter.createOrderListFromOrderTempList(
                                orderMapper.getAllTempOrders());
    }
}
