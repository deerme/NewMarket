package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

/**
 * Created by PBanasiak on 3/21/2016.
 */
public class TableCreator {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public void createOrderTable(){
        dropOrderTableIfExists();
        this.jdbcTemplate.execute("CREATE TABLE ORDERINMARKET(ID INT AUTO_INCREMENT NOT NULL, TYPE VARCHAR(255), QUANTITY INT,PRIMARY KEY(ID));");
    }

    public void dropOrderTableIfExists()  {
        this.jdbcTemplate.update("DROP TABLE IF EXISTS ORDERINMARKET;");
    }

    public void createExecutionTable(){
        dropExecutionTableIfExists();
        this.jdbcTemplate.execute("CREATE TABLE EXECUTION(ID INT AUTO_INCREMENT NOT NULL,QUANTITY VARCHAR(255),ID_ORDER_SELLER INT,ID_ORDER_BUYER INT,FOREIGN KEY( ID_ORDER_SELLER) REFERENCES ORDERINMARKET(ID),FOREIGN KEY( ID_ORDER_BUYER) REFERENCES ORDERINMARKET(ID),PRIMARY KEY(ID));");
    }

    public void dropExecutionTableIfExists() {
        this.jdbcTemplate.update("DROP TABLE IF EXISTS EXECUTION;");
    }
}
