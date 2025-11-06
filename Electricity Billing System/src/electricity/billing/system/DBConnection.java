package electricity.billing.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * DBConnection handles the MySQL database connection.
 * Provides a reusable Connection and Statement object.
 *
 * @author danny
 */
public class DBConnection {

    // ===== Database Configuration =====
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "electricityBillingSystem_DB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "abc123"; // Ideally, move to encrypted config

    // JDBC objects
    private Connection conn;
    Statement stmt;

    /**
     * Constructor attempts to establish a database connection and initialize Statement.
     */
    public DBConnection() {
        try {
            // Load JDBC driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Build JDBC URL dynamically
            String dbUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                           + "?useSSL=false&serverTimezone=UTC";

            // Establish connection
            conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);

            // Initialize statement
            stmt = conn.createStatement();

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "JDBC Driver not found. Contact Admin.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database Connection Failed! Check server or credentials.");
        }
    }

    /**
     * Returns the active Connection object.
     * @return Connection or null if not connected.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Returns the initialized Statement object.
     * @return Statement or null if connection failed.
     */
    public Statement getStatement() {
        return stmt;
    }
}
