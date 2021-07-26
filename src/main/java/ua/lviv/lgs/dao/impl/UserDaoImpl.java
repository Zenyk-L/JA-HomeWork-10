package ua.lviv.lgs.dao.impl;

import org.apache.log4j.Logger;
import ua.lviv.lgs.dao.UserDao;
import ua.lviv.lgs.domain.User;
import ua.lviv.lgs.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static String READ_ALL = "select * from users";
    private static String CREATE = "insert into users (email,  first_name, last_name, role, password ) values (?,?,?,?,?)";
    private static String READ_BY_ID = "select * from users where id =?";
    private static String READ_BY_EMAIL = "select * from users where email =?";
    private static String UPDATE_BY_ID = "update users set email =?, first_name = ?, last_name = ?, role =?, password =? where id = ?";
    private static String DELETE_BY_ID = "delete from users where id =?";

    private static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

    private Connection connection;
    private PreparedStatement preparedStatement;

    public UserDaoImpl() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        connection = ConnectionUtils.openConnection();
    }

    @Override
    public User create(User user) {
        try {
            preparedStatement = connection.prepareStatement(CREATE, java.sql.Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        return user;
    }

    @Override
    public User read(Integer id) {
        User user = null;
        try {
            preparedStatement = connection.prepareStatement(READ_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            result.next();

            Integer userId = result.getInt("id");
            String email = result.getString("email");
            String firstName = result.getString("first_name");
            String lastName = result.getString("last_name");
            String role = result.getString("role");
            String password = result.getString("password");

            user = new User(userId, email, firstName, lastName, role, password);

        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return user;
    }

    @Override
    public User update(User user) {
        try {
            preparedStatement = connection.prepareStatement(UPDATE_BY_ID);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        return user;
    }

    @Override
    public void delete(Integer id) {
        try {
            preparedStatement = connection.prepareStatement(DELETE_BY_ID);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public List<User> readAll() {
        List<User> userRecords = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(READ_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer userId = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");

                userRecords.add(new User(userId,email,firstName,lastName,role,password));
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        return userRecords;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;
        try {
            preparedStatement = connection.prepareStatement(READ_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();
            result.next();

            Integer userId = result.getInt("id");
            String firstName = result.getString("first_name");
            String lastName = result.getString("last_name");
            String role = result.getString("role");
            String password = result.getString("password");

            user = new User(userId, email, firstName, lastName, role, password);

        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return user;
    }


}