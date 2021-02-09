package ru.geekbrains.servercloud.authService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geekbrains.CloudAuthMessage;

import java.sql.*;

public class AuthService implements AuthServiceInterface{


    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;



    public CloudAuthMessage fillUserData(String name, String pass) throws SQLException {
        CloudAuthMessage userData = null;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user_data");
        while (resultSet.next()){
            String log = resultSet.getString("user_login");
            String pas = resultSet.getString("user_pass");
            String folder = resultSet.getString("user_folder");
            System.out.println(log + " " + pas + " " + folder);

            if(log.equals(name) && pas.equals(pass)){
                boolean res = true;
                userData = new CloudAuthMessage("AUTH", log, folder,res);

            }
        }
        return userData;
    }



    @Override
    public void startService() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:user_storage.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Сервис авторизации запущен");
        LOG.debug("Подключение к БД");

    }

    @Override
    public void stopServices() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Сервис авторизации остановлен");
        LOG.debug("Отключение БД");
    }
}
