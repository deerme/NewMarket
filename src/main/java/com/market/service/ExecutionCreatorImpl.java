package com.market.service;

import com.market.database.OrderDAO;
import com.market.model.Execution;
import com.market.model.Order;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.market.service.Constants.ORD_TYPE_BUY;
import static com.market.service.Constants.ORD_TYPE_SELL;

/**
 * Created by pizmak on 2016-05-17.
 */
public class ExecutionCreatorImpl implements ExecutionCreator {
    private OrderDAO orderDAO;

    public ExecutionCreatorImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public List<Execution> process(Order order) {
        List<Order> openedOrdersOrdered = orderDAO.getAllOpenOrdersByType(inverseType(order.getType()));
        Collections.sort(
                openedOrdersOrdered,
                (o1, o2) -> o1.getQuantity() < o2.getQuantity() ? -1
                        : o1.getQuantity() == o2.getQuantity() ? 0
                        : 1
        );

        List<Execution> result = new ArrayList<>();
        for (Pair<Order,Integer> execPair: getFirstOrdersBySummQty(openedOrdersOrdered, order.getQuantity())) {
            result.add(new Execution(
                    Optional.empty(),
                    getIdBuyer(order, execPair.getLeft()),
                    getIdSeller(order, execPair.getLeft()),
                    execPair.getRight()
            ));
        }
        for (Execution exec : result) {
            System.out.println(">>>Exec = "  + exec);
        }
        return result;
    }

    private int getIdBuyer(Order o1, Order o2) {
        if (ORD_TYPE_BUY.equals(o1.getType())) {
            return o1.getId().get();
        } else {
            return o2.getId().get();
        }
    }

    private int getIdSeller(Order o1, Order o2) {
        if (ORD_TYPE_SELL.equals(o1.getType())) {
            return o1.getId().get();
        } else {
            return o2.getId().get();
        }
    }

    private List<Pair<Order, Integer>> getFirstOrdersBySummQty(List<Order> orders, int sumQty) {
        List<Pair<Order, Integer>> result = new ArrayList<>();
        int leftQty = sumQty;
        for (Order oppositeOrder : orders) {
            if (leftQty <= 0) {
                break;
            }
            result.add(Pair.of(oppositeOrder, min(leftQty, oppositeOrder.getQuantity())));
            leftQty -= oppositeOrder.getQuantity();
        }
        return result;
    }

    private int min(int i1, int i2) {
        return i1 < i2 ? i1 : i2;
    }

    private String inverseType(String type) {
        if (ORD_TYPE_BUY.equals(type)) {
            return Constants.ORD_TYPE_SELL;
        } else {
            return ORD_TYPE_BUY;
        }
    }
}
