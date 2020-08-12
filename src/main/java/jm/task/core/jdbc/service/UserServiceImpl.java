package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    public void createUsersTable() throws SQLException {
        Util util = new Util();
        String createSQLTable = "CREATE TABLE User (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(15), " +
                "lastName VARCHAR(15), age TINYINT, PRIMARY KEY (id))";
        Statement statement = util.getConnection().createStatement();
        statement.execute(createSQLTable);
        statement.close();
        System.out.println("Table created");
    }

    public void dropUsersTable() throws SQLException {
        Util util = new Util();
        PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DROP TABLE User");
        preparedStatement.executeUpdate();
        System.out.println("Table dropped");
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        Util util = new Util();
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("INSERT INTO User (name, lastName, age) VALUES( ?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User c именем %s добавлен в базу данных\n", name);
        }
    }

    public void removeUserById(long id) throws SQLException {
        Util util = new Util();
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DELETE FROM User WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("User with id %d deleted", id);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = null;
        Util util = new Util();
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("SELECT * FROM User")) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                Byte age = resultSet.getByte(4);
                users.add(new User(name, lastName, age));
            }
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        Util util = new Util();
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DELETE FROM User")) {
            preparedStatement.executeUpdate();
            System.out.println("Table cleaned");
        }
    }
}
