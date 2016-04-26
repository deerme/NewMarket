package database;


import model.Order;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public interface OrderDAO {
    @Transactional(propagation = Propagation.REQUIRED)
    public KeyHolder addOrderToDatabase(String typeOfOrder, int quantity);
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderToDatabase(int idOrder,int quantity);
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ImmutablePair<Order,Order>> getPairsOfMatchingOrders();
    public List<Order> getAllOrders();
}
