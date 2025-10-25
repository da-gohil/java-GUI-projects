package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

/**
 * The CustomerDetails class displays a comprehensive list of all customer records
 * stored in the database. This is typically an Admin-only function.
 */
public class CustomerDetails extends JFrame implements ActionListener {

    // JTable for displaying all customer data from the 'customer' table
    JTable table;
    // Button to trigger the print action
    JButton print;
    
    CustomerDetails(){
        super("All Customer Details");
        
        // Setting up the frame dimensions and position
        setSize(1200, 650);
        setLocation(200, 150);
        
        // Initialize the table
        table = new JTable();
        
        try {
            // Establish connection and fetch all data from the customer table
            // IMPORTANT CHANGE: Now using the centralized DBConnection class
            DBConnection db = new DBConnection();
            
            // Assuming 'customer' table columns are: name, meterno, address, city, state, email, phone
            // Accessing the Statement object through the getStatement() method
            ResultSet rs = db.getStatement().executeQuery("select * from customer");
            
            // Use DbUtils to convert the ResultSet into a JTable model
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details. Check DB connection/table.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Wrap the table in a JScrollPane to enable scrolling if data exceeds display area
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER); // Add scroll pane to the center of the frame
        
        // Setup the Print button
        print = new JButton("Print");
        print.addActionListener(this);
        // Add the button to the bottom (South) of the frame using BorderLayout
        add(print, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes this window only
        setVisible(true);
    }
    
    /**
     * Handles the Print button action.
     * @param ae The action event.
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == print) {
            try {
                // Use the built-in JTable print function
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not print the table.", "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new CustomerDetails();
    }
}
