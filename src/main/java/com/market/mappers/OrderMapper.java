package com.market.mappers;

import com.market.model.changed.OrderTemp;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pizmak on 2016-07-06.
 */
public interface OrderMapper {
    @Select("SELECT * FROM orderinmarket WHERE id = #{orderId}")
    public OrderTemp getOrderTemp(int orderId);

    @Insert("INSERT INTO orderinmarket (type, quantity) VALUES(#{type}, #{quantity})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public int addOrderTemp(OrderTemp order);

    @Select("SELECT * FROM orderinmarket WHERE quantity>0 AND type=#{type}")
    @ResultType(ArrayList.class)
    public List<OrderTemp> getAllTempOrdersByType(String type);

    @Update("UPDATE orderinmarket SET quantity=quantity - #{quantity} WHERE id=#{id}")
    public void updateOrder(OrderTemp order);

    @Select("SELECT * FROM orderinmarket WHERE quantity>0")
    @ResultType(ArrayList.class)
    public List<OrderTemp> getAllOpenTempOrders();

    @Select("SELECT * FROM orderinmarket")
    @ResultType(ArrayList.class)
    public List<OrderTemp> getAllTempOrders();

}
