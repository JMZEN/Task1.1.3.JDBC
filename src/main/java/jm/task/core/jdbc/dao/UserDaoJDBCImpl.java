package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        Util util;
        try {
            util = new Util();
            String createSQLTable = "CREATE TABLE User (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(15), " +
                    "lastName VARCHAR(15), age TINYINT, PRIMARY KEY (id))";
            Statement statement = util.getConnection().createStatement();
            statement.execute(createSQLTable);
            statement.close();
            System.out.println("Table created");
        } catch (SQLException ignored) {
        }

    }

    @Override
    public void dropUsersTable() {
        Util util;
        try {
            util = new Util();
            PreparedStatement preparedStatement = util.getConnection()
                    .prepareStatement("DROP TABLE User");
            preparedStatement.executeUpdate();
            System.out.println("Table dropped");
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Util util;
        try {
            util = new Util();
            try (PreparedStatement preparedStatement = util.getConnection()
                    .prepareStatement("INSERT INTO User (name, lastName, age) VALUES( ?, ?, ?)")) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
                System.out.printf("User c именем %s добавлен в базу данных\n", name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeUserById(long id) {
        Util util;
        try {
            util = new Util();
            try (PreparedStatement preparedStatement = util.getConnection()
                    .prepareStatement("DELETE FROM User WHERE id = ?")) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                System.out.printf("Пользователь с id %d был удалён\n", id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        ResultSet resultSet;
        Util util;
        try {
            util = new Util();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Util util;
        try {
            util = new Util();
            try (PreparedStatement preparedStatement = util.getConnection()
                    .prepareStatement("DELETE FROM User")) {
                preparedStatement.executeUpdate();
                System.out.println("Table cleaned");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
