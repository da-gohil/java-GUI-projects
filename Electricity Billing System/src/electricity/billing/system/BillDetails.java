package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils; // IMPORTANT: This relies on the rs2xml.jar library

public class BillDetails extends JFrame{

    /**
     * BillDetails constructor fetches and displays all bills for a given meter number.
     * @param meter The meter number for which to display bills.
     */
    BillDetails(String meter) {
        
        // Set up the main frame properties
        setSize(700, 650);
        setLocation(400, 150);
        setLayout(new BorderLayout()); // Use BorderLayout for better structure
        
        getContentPane().setBackground(Color.WHITE);
        
        JTable table = new JTable();
        
        try {
            // Using the consistent DBConnection class
            DBConnection c = new DBConnection(); 
            // Query to fetch all bill records for the specific meter
            String query = "SELECT * FROM bill WHERE meter_no = '"+meter+"'";
            
            // Execute query using the statement object (stmt)
            ResultSet rs = c.stmt.executeQuery(query);
            
            // Use DbUtils to convert the ResultSet into a TableModel
            table.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (Exception e) {
            e.printStackTrace();
            // Display error message to the user
            JOptionPane.showMessageDialog(this, "Error loading bill data: " + e.getMessage());
        }
        
        // Add the table to a scroll pane
        JScrollPane sp = new JScrollPane(table);
        
        // Add the scroll pane to the center of the frame
        add(sp, BorderLayout.CENTER);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        // Example usage, pass a meter number when calling this frame
        new BillDetails("100123"); 
    }
}
