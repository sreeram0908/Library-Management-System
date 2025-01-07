package library.management;

import library.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin {
    public static boolean validate(String username, String password) {
        boolean status = false;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM admin_credentials WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            status = resultSet.next();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return status;
    }
}
