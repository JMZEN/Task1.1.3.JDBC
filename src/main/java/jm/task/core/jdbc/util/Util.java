package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private String url1 = "jdbc:mysql://localhost:3306/Users?serverTimezone=UTC";
    private String userName = "root";
    private String password = "vhyrx4fsg";
    private Connection connection = DriverManager.getConnection(url1, userName, password);

    public Util() throws SQLException {
    }


    public Connection getConnection() {
        return connection;
    }
}
