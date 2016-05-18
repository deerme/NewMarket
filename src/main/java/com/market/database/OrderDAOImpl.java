package com.market.database;

import com.market.model.Execution2;
import com.market.model.Order2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-05-17.
 */
public class OrderDAOImpl implements OrderDAO {
    private JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger("auditLogger");

    public OrderDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Order2 saveOrder(Order2 order) {
        final String insertSql = "INSERT INTO ORDERINMARKET(TYPE,QUANTITY) VALUES(?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertSql);
                    ps.setString(1, order.getType());
                    ps.setInt(2, order.getQuantity());
                    return ps;
                },
                keyHolder
        );

        logger.info("Added order to database.Auto-generated id:" +  Optional.of(keyHolder.getKey().intValue()) +" Type of order"+order.getType()+" Quantity of order" +order.getQuantity());

        return new Order2(
                Optional.of(keyHolder.getKey().intValue()),
                order.getType(),
                order.getQuantity()
        );
    }

    @Override
    public List<Order2> getAllOpenOrdersByType(String type) {
        final String sqlGetAllOpenOrdersQuery =
                "SELECT * FROM orderinmarket WHERE quantity > 0 and type = ?";

        return jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sqlGetAllOpenOrdersQuery);
                    ps.setString(1, type);
                    return ps;
                },
                (rs, rowNum) -> createOrderFromResultSet(rs)
        );
    }

    @Override
    public Execution2 updateQuantityOfOrdersAfterDoneExecution(Execution2 execution) {
        final String sqlUpdateQuantityOfOrderAfterExecution="UPDATE orderinmarket SET quantity=quantity-? WHERE ID=?";

        jdbcTemplate.update(sqlUpdateQuantityOfOrderAfterExecution,execution.getQuantity(),execution.getIdBuyer());
        jdbcTemplate.update(sqlUpdateQuantityOfOrderAfterExecution,execution.getQuantity(),execution.getIdSeller());

        logger.info("Update order to database.Id of order:" + execution.getIdBuyer()+" New quantity is:" + getQuantityOfOrderById(execution.getIdBuyer()));
        logger.info("Update order to database.Id of order:" + execution.getIdSeller()+" New quantity is:" + getQuantityOfOrderById(execution.getIdSeller()));

        return execution;
    }

    @Override
    public List<Order2> getAllOrders() {
        final String sqlGetAllOrdersQuery = "SELECT * FROM orderinmarket;";

        return jdbcTemplate.query(
                sqlGetAllOrdersQuery,
                (rs, rowNum) -> createOrderFromResultSet(rs)
        );

    }

    @Override
    public int getQuantityOfOrderById(int id) {
        final String sqlGetAllOpenOrdersQuery = "SELECT quantity FROM orderinmarket WHERE id= ?;";

        return jdbcTemplate.queryForObject(sqlGetAllOpenOrdersQuery,Integer.class,id);
    }

    private Order2 createOrderFromResultSet(ResultSet rs) throws SQLException {
        return new Order2(
                Optional.of(rs.getInt("id")),
                rs.getString("type"),
                rs.getInt("quantity")
        );
    }

}
