package com.market;

import model.Execution;
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
public class ExecutionDAOImpl implements ExecutionDAO {
    private JdbcTemplate jdbcTemplate;

    public ExecutionDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Execution2 saveExecution(Execution2 execution) {
        final String insertSql = "INSERT INTO EXECUTION(QUANTITY,ID_ORDER_SELLER,ID_ORDER_BUYER) VALUES(?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertSql);
                    ps.setInt(1, execution.getQuantity());
                    ps.setInt(2, execution.getIdSeller());
                    ps.setInt(3, execution.getIdBuyer());
                    return ps;
                },
                keyHolder
        );

        return new Execution2(
                Optional.of(keyHolder.getKey().intValue()),
                execution.getIdBuyer(),
                execution.getIdSeller(),
                execution.getQuantity()
        );
    }

    @Override
    public List<Execution2> getListOfAllExecutions() {
        final String sqlGetAllExecutions = "SELECT * FROM EXECUTION";
        return  jdbcTemplate.query(sqlGetAllExecutions,
                    (rs, rowNum) -> new Execution2(
                            Optional.of(rs.getInt("id")),
                            rs.getInt("id_order_buyer"),
                            rs.getInt("id_order_seller"),
                            rs.getInt("quantity")));
    }
}
