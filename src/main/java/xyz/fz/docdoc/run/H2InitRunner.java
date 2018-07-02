package xyz.fz.docdoc.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import xyz.fz.docdoc.util.SpringContextHelper;

import java.util.ArrayList;
import java.util.List;

public class H2InitRunner implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2InitRunner.class);

    private JdbcTemplate jdbcTemplate;

    @Override
    public void run() {
        JdbcTemplate jdbcTemplate = SpringContextHelper.getBean("jdbcTemplate", JdbcTemplate.class);
        this.jdbcTemplate = jdbcTemplate;
        Assert.notNull(jdbcTemplate, "jdbcTemplate is null");
        try {
            String testSql = "SELECT * FROM t_test ";
            jdbcTemplate.execute(testSql);
            // already init
            LOGGER.debug("Db already init");
        } catch (BadSqlGrammarException badSqlGrammarException) {
            LOGGER.debug("Do db init");
            // do init
            dbInit();
        }
    }

    private void dbInit() {
        initTestTable();
        initUserTable();
    }

    private void initTestTable() {
        List<String> fieldList = new ArrayList<>();
        fieldList.add("id BIGINT AUTO_INCREMENT(0, 1) PRIMARY KEY, ");
        initTable("t_test", fieldList);
    }

    private void initUserTable() {
        List<String> fieldList = new ArrayList<>();
        fieldList.add("id BIGINT AUTO_INCREMENT(0, 1) PRIMARY KEY, ");
        fieldList.add("userName VARCHAR(255), ");
        fieldList.add("passWord VARCHAR(255) ");
        initTable("t_user", fieldList);
    }

    private void initTable(String tableName, List<String> fieldList) {
        String createTableSql = "CREATE TABLE " + tableName + "(#{fieldSql});";
        StringBuilder fieldSql = new StringBuilder();
        for (String field : fieldList) {
            fieldSql.append(field);
        }
        jdbcTemplate.execute((createTableSql).replace("#{fieldSql}", fieldSql.toString()));
    }
}
