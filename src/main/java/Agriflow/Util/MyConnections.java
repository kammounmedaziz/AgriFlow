package Agriflow.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnections {

    private static MyConnections instance;
    private Connection connection;

    private String url = "jdbc:mysql://localhost:3306/agriflow";  // Replace with your database URL
    private String username = "";       // Replace with your database username
    private String password = "";       // Replace with your database password

    private MyConnections() {
        try {
            // Load the MySQL driver (this is no longer always required, but explicit loading can help in some environments)
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }
    }

    public static MyConnections getInstance() {
        if (instance == null) {
            instance = new MyConnections();
        }
        return instance;
    }

    public Connection getMyConnections() {
        return connection;
    }

}
