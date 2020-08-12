package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private final String url1 = "jdbc:mysql://localhost:3306/Users?serverTimezone=UTC";
    private final String userName = "root";
    private final String password = "vhyrx4fsg";
    private final Connection connection = DriverManager.getConnection(url1, userName, password);
    private final Connection connection1 = DriverManager.getConnection(url1, userName, password);

    public Util() throws SQLException {
    }


    public Connection getConnection() {
        return connection;
    }

    public Connection getTransactedConnection() throws SQLException {
        connection1.setAutoCommit(false);
        connection1.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return connection1;
    }
}
