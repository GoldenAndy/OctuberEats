package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/OctuberEats";
    private static final String USER = "root";
    private static final String PASSWORD = "midas1987";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No fue posible conectarse a la base de datos", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
