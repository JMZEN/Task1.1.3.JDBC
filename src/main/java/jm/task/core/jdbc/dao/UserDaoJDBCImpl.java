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
        String createSQLTable = "CREATE TABLE User (id MEDIUMINT NOT NULL AUTO_INCREMENT, name VARCHAR(15), lastName VARCHAR(15), age TINYINT, PRIMARY KEY (id))";
        try (Statement statement = util.getConnection().createStatement()) {
            statement.execute(createSQLTable);
            System.out.println("Таблица создана");
        } catch (SQLException | CommandAcceptanceException ignored) {
        }
    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DROP TABLE IF EXISTS User")) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица удалена");
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = util.getTransactedConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO User (name, lastName, age) VALUES( ?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.printf("Пользователь c именем %s добавлен в базу данных\n", name);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DELETE FROM User WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("Пользователь с id %d был удалён\n", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("SELECT * FROM User");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                Byte age = resultSet.getByte(4);
                users.add(new User(name, lastName, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = util.getConnection()
                .prepareStatement("DELETE FROM User")) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
