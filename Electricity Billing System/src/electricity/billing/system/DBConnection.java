/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.billing.system;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author danny
 */
public class DBConnection {
    
    Connection conn; 
    Statement stmt;
    
    // Removed 'throws SQLException' because we are catching the exception internally
    DBConnection() { 
        try {
            // 1. Load the JDBC Driver (Recommended best practice) but this is Optional
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 2. Establish Connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/electricityBillingSystem_DB", "root", "abc123");
            
            // 3. Create Statement
            stmt = conn.createStatement();
            
        } catch(Exception e) {
            // Inform the user if the connection fails
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Failed! Check Server/Driver/Credentials.");
        }
    }
}