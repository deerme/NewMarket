package com.market.database;


import com.market.model.Execution2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Created by pizmak on 2016-05-17.
 */
@Transactional
public class ExecutionDAOImpl implements ExecutionDAO {
    private JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger("auditLogger");

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

        logger.info("Added order to database.Auto-generated id:" + Optional.of(keyHolder.getKey().intValue()) + " Quantity of order: " +execution.getQuantity()+ " Id seller: " + execution.getIdSeller() +" Id buyer: "+execution.getIdBuyer());
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
