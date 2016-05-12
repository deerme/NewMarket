package database;

import model.Order;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;


/**
 * Created by PBanasiak on 3/21/2016.
 */

public class OrderDAOImpl implements OrderDAO {
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger("auditLogger");

    public OrderDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public KeyHolder addOrderToDatabase(final String typeOfOrder, final int quantity) {
        final String insertSql = "INSERT INTO ORDERINMARKET(TYPE,QUANTITY) VALUES(?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertSql);
                    ps.setString(1, typeOfOrder);
                    ps.setInt(2,quantity);

                    return ps;
                },
                keyHolder);
        return keyHolder;
    }

    public List<ImmutablePair<Order,Order>> getPairsOfMatchingOrders(){
        final String sqlGetPairsOfMatchingOrders = "SELECT p1.id as idSell , p1.type as typeSell , p1.quantity quantitySell, p2.id as idBuy , p2.type as typeBuy , p2.quantity as quantityBuy FROM orderinmarket p1 JOIN orderinmarket  p2 ON (p1.type  != p2.type)WHERE (p1.type='SELL' AND p2.type='BUY' AND p1.quantity>0 AND p2.quantity>0);";

        return this.jdbcTemplate.query(
                sqlGetPairsOfMatchingOrders,
                (rs, rowNum) -> new ImmutablePair<>(
                        new Order(
                                rs.getInt("idBuy"),
                                rs.getString("typeBuy"),
                                rs.getInt("quantityBuy")
                        ),
                        new Order(
                                rs.getInt("idSell"),
                                rs.getString("typeSell"),
                                rs.getInt("quantitySell")
                        )
                )
        );
    }

    public List<Order> getAllOrders(){
        return this.jdbcTemplate.query("SELECT * FROM ORDERINMARKET",
                (rs, rowNum) -> new Order(rs.getInt("id"),rs.getString("type"),rs.getInt("quantity")));
    }

    public void updateOrderToDatabase(int idOrder, int quantity) {
        this.jdbcTemplate.update("UPDATE ORDERINMARKET SET QUANTITY=? WHERE ID=?;",quantity, idOrder);
        logger.info("Updating into database where id of Order is :" + idOrder + "new quantity is:" + quantity);
    }

    public int getQuantityOfOrderWithConcreteId(int idOrder){
      return this.jdbcTemplate.queryForObject("SELECT QUANTITY FROM ORDERINMARKET WHERE ID=?",Integer.class,idOrder);
    }
}

