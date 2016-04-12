package database;


import org.springframework.jdbc.support.KeyHolder;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public interface OrderDAO {
    public KeyHolder addOrderToDatabase(String typeOfOrder, int quantity);
    public void updateOrderToDatabase(int idOrder,int quantity);
}
