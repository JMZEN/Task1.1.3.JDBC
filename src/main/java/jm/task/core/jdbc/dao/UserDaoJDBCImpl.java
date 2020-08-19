package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Util util = new Util();

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        String createSQLTable = "CREATE TABLE IF NOT EXISTS Users.user (id bigint not null auto_increment, age tinyint, lastName varchar(20), name varchar(15), primary key (id))";
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute(createSQLTable);
            System.out.println("Таблица создана с использованием JDBC");
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DROP TABLE IF EXISTS Users.user")) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица удалена с использованием JDBC");
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = util.getTransactedConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO Users.user (name, lastName, age) VALUES( ?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount == 1) {
                connection.commit();
            } else {
                connection.rollback();
            }
            System.out.printf("Пользователь c именем %s добавлен в базу данных с использованием JDBC\n", name);
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DELETE FROM Users.user WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("Пользователь с id %d был удалён с использованием JDBC\n", id);
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("SELECT * FROM Users.user");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString(3);
                Byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DELETE FROM Users.user")) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица очищена с использованием JDBC");
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
