package twitter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 6/30/11
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class DBConfig {

    public static final String DRIVER   = "com.mysql.jdbc.Driver";
    public static final String JDBC_URL = "jdbc:mysql://localhost/twimini";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    @Bean
    public SimpleJdbcTemplate simpleJdbcTemplate(){
        DataSource dataSource = getDataSource();
        SimpleJdbcTemplate db = new SimpleJdbcTemplate( dataSource );
        return db;
    }

    private static DataSource getDataSource() {
        //
        // Creates a new instance of DriverManagerDataSource and sets
        // the required parameters such as the Jdbc Driver class,
        // Jdbc URL, database user name and password.
        //
        System.out.println( "Connected to db" );
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DBConfig.DRIVER);
        dataSource.setUrl(DBConfig.JDBC_URL);
        dataSource.setUsername(DBConfig.USERNAME);
        dataSource.setPassword(DBConfig.PASSWORD);
        return dataSource;
    }

}
