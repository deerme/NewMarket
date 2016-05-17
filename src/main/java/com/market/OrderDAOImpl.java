package com.market;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-05-17.
 */
public class OrderDAOImpl implements OrderDAO {
    private JdbcTemplate jdbcTemplate;

    public OrderDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Order2 saveOrder(Order2 order) {
        final String insertSql = "INSERT INTO ORDERINMARKET(TYPE,QUANTITY) VALUES(?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertSql);
                    ps.setString(1, order.getType());
                    ps.setInt(2, order.getQuantity());
                    return ps;
                },
                keyHolder
        );

        return new Order2(
                Optional.of(keyHolder.getKey().intValue()),
                order.getType(),
                order.getQuantity()
        );
    }

    @Override
    public List<Order2> getAllOpenOrders() {
        final String sqlGetAllOpenOrdersQuery =
                "SELECT * FROM orderinmarket WHERE quantity > 0";

        return this.jdbcTemplate.query(
                sqlGetAllOpenOrdersQuery,
                (rs, rowNum) -> new Order2(
                        Optional.of(rs.getInt("id")),
                        rs.getString("type"),
                        rs.getInt("quantity")
                )
        );
    }
}
