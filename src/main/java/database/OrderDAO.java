package database;


import model.Order;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.List;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public interface OrderDAO {
    public int addOrderToDatabase(String typeOfOrder, int quantity);

    public void updateOrderToDatabase(int idOrder,int quantity);

    public List<ImmutablePair<Order,Order>> getPairsOfMatchingOrders();

    public List<Order> getAllOrders();
}
