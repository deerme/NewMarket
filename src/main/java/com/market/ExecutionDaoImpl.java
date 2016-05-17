package com.market;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Optional;

/**
 * Created by pizmak on 2016-05-17.
 */
public class ExecutionDaoImpl implements ExecutionDao {
    private JdbcTemplate jdbcTemplate;

    public ExecutionDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Execution2 saveExecution(Execution2 execution) {
        final String insertSql = "INSERT INTO EXECUTION(QUANTITY,ID_ORDER_SELLER,ID_ORDER_BUYER) VALUES(?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(
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
}
