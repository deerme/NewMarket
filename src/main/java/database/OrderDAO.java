package database;


import model.Order;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.jdbc.support.KeyHolder;
import java.util.List;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public interface OrderDAO {
    public KeyHolder addOrderToDatabase(String typeOfOrder, int quantity);

    public void updateOrderToDatabase(int idOrder,int quantity);

    public List<ImmutablePair<Order,Order>> getPairsOfMatchingOrders();

    public List<Order> getAllOrders();
}
