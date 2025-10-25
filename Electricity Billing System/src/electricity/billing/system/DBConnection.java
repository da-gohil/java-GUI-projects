package electricity.billing.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * DBConnection class handles the connection to the MySQL database.
 * This class uses the standard JDBC approach and provides access to the 
 * Connection and Statement objects.
 *
 * IMPORTANT: The credentials (DB name, user, password) are hardcoded here 
 * and must be updated for actual use.
 *
 * @author danny
 */
public class DBConnection {
    
    // Instance variables to hold the connection and statement objects
    Connection conn;    
    Statement stmt;

    /**
     * Constructor attempts to establish a connection to the MySQL database 
     * and create a Statement object.
     */
    public DBConnection() {    
        try {
            // 1. Load the JDBC Driver (Recommended best practice)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 2. Establish Connection
            // NOTE: Change "abc123" to your actual MySQL root password.
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/electricityBillingSystem_DB", "root", "abc123");
            
            // 3. Create Statement (Kept for compatibility with simple queries)
            stmt = conn.createStatement();
            
        } catch(Exception e) {
            // Inform the user if the connection fails
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Failed! Check Server/Driver/Credentials.");
            // Set connection to null if it failed
            conn = null;
            stmt = null;
        }
    }
    
    /**
     * Provides the established database connection object.
     * This is essential for classes like Login and SignUp to create a 
     * PreparedStatement for secure query execution.
     * @return The active java.sql.Connection, or null if connection failed.
     */
    public Connection getConnection() {
        return conn;
    }
    
    /**
     * Provides the initialized Statement object.
     * @return The active java.sql.Statement, or null if connection failed.
     */
    public Statement getStatement() {
        return stmt;
    }
}
