package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.table.JTableHeader;

/**
 * PaymentHistory frame displays a table of all generated bills and their payment status
 * for a specific customer identified by meter number.
 */
public class PaymentHistory extends JFrame implements ActionListener {

    JTable table;
    JButton btnBack;
    String meter;
    
    // Define the primary blue accent color and light background
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color LIGHT_BG = new Color(230, 240, 250);

    /**
     * Constructor for the PaymentHistory frame.
     * @param meterNumber The meter number of the customer.
     */
    public PaymentHistory(String meterNumber) {
        this.meter = meterNumber;
        setTitle("Payment History for Meter No. " + meter);
        setLayout(null);
        setBounds(350, 150, 850, 600);
        getContentPane().setBackground(LIGHT_BG);
        
        // --- Header ---
        JLabel lblHeading = new JLabel("Customer Bill History");
        lblHeading.setFont(new Font("SanSerif", Font.BOLD, 22));
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(300, 20, 300, 30);
        add(lblHeading);

        // --- Data Fetch and Table Setup ---
        
        // Prepare data structure for JTable (using a 2D array of Strings)
        String[] columnNames = {"Month", "Units Consumed", "Total Bill (Rs)", "Payment Status"};
        String[][] data = new String[0][4]; // Start with empty data array
        int rowCount = 0;
        
        try {
            DBConnection c = new DBConnection();
            
            // 1. First query to count rows for array initialization
            ResultSet rsCount = c.stmt.executeQuery("SELECT COUNT(*) AS total FROM bill WHERE meter_no = '"+meter+"'");
            if (rsCount.next()) {
                rowCount = rsCount.getInt("total");
                data = new String[rowCount][4];
            }
            
            // 2. Second query to fetch the actual data
            ResultSet rs = c.stmt.executeQuery("SELECT month, units_consumed, total_bill, status FROM bill WHERE meter_no = '"+meter+"'");
            
            int i = 0;
            while(rs.next()) {
                data[i][0] = rs.getString("month");
                data[i][1] = rs.getString("units_consumed");
                data[i][2] = rs.getString("total_bill");
                data[i][3] = rs.getString("status");
                i++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill history: " + e.getMessage());
        }

        // --- Table Creation and Styling ---
        table = new JTable(data, columnNames);
        table.setFont(new Font("SanSerif", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(200, 220, 255)); // Light blue selection
        
        // Style the table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SanSerif", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 80, 790, 400);
        add(scrollPane);
        
        // --- Back Button ---
        btnBack = new JButton("Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(PRIMARY_BLUE);
        btnBack.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnBack.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1));
        btnBack.setBounds(375, 500, 100, 35);
        btnBack.addActionListener(this);
        add(btnBack);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBack) {
            setVisible(false);
            // In a real application, you would navigate back to the main customer frame (e.g., Project_ViewCustomer)
        }
    }

    public static void main(String[] args) {
        // Example usage
        new PaymentHistory("100123"); 
    }
}
