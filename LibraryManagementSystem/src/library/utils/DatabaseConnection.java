package library.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://librarymanagementserver.mysql.database.azure.com:3306/librarydb?useSSL=true";
            connection = DriverManager.getConnection(url, "libraryadmin", "Manager@2024");
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return connection;
    }
}
