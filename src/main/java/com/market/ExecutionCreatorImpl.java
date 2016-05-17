package com.market;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.market.Constants.ORD_TYPE_BUY;
import static com.market.Constants.ORD_TYPE_SELL;

/**
 * Created by pizmak on 2016-05-17.
 */
public class ExecutionCreatorImpl implements ExecutionCreator {
    private OrderDAO orderDAO;

    public ExecutionCreatorImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public List<Execution2> process(Order2 order) {
        List<Order2> openedOrdersOrdered = orderDAO.getAllOpenOrdersByType(inverseType(order.getType()));
        Collections.sort(
                openedOrdersOrdered,
                (o1, o2) -> o1.getQuantity() < o2.getQuantity() ? -1
                        : o1.getQuantity() == o2.getQuantity() ? 0
                        : 1
        );

        List<Execution2> result = new ArrayList<>();
        for (Pair<Order2,Integer> execPair: getFirstOrdersBySummQty(openedOrdersOrdered, order.getQuantity())) {
            result.add(new Execution2(
                    Optional.empty(),
                    getIdBuyer(order, execPair.getLeft()),
                    getIdSeller(order, execPair.getLeft()),
                    execPair.getRight()
            ));
        }
        for (Execution2 exec : result) {
            System.out.println(">>>Exec = "  + exec);
        }
        return result;
    }

    private int getIdBuyer(Order2 o1, Order2 o2) {
        if (ORD_TYPE_BUY.equals(o1.getType())) {
            return o1.getId().get();
        } else {
            return o2.getId().get();
        }
    }

    private int getIdSeller(Order2 o1, Order2 o2) {
        if (ORD_TYPE_SELL.equals(o1.getType())) {
            return o1.getId().get();
        } else {
            return o2.getId().get();
        }
    }

    private List<Pair<Order2, Integer>> getFirstOrdersBySummQty(List<Order2> orders, int sumQty) {
        List<Pair<Order2, Integer>> result = new ArrayList<>();
        int leftQty = sumQty;
        for (Order2 oppositeOrder : orders) {
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
