package pe.edu.vallegrande.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://database-1.c9kaeqiwud9r.us-east-1.rds.amazonaws.com:3306/bd_incidencias";
    private static final String USER = "admin";
    private static final String PASSWORD = "12345678";

    public static Connection getConnection() throws SQLException {
        // Con Java 6+, no necesitas Class.forName() si el JAR está en el classpath
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ CONEXIÓN EXITOSA a la base de datos");
        } catch (SQLException e) {
            System.err.println("❌ ERROR de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}