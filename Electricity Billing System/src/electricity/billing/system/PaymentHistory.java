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

    private JTable table;
    private JButton btnBack;
    private String meter;

    // Define consistent primary blue accent color and light background
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

        // --- Heading ---
        JLabel lblHeading = new JLabel("Customer Bill History");
        lblHeading.setFont(new Font("SanSerif", Font.BOLD, 22));
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(300, 20, 300, 30);
        add(lblHeading);

        // --- Table Columns ---
        String[] columnNames = {"Month", "Units Consumed", "Total Bill (Rs)", "Payment Status"};

        // --- Fetch Data from DB ---
        String[][] data = new String[0][4];

        try {
            DBConnection c = new DBConnection();

            // Count rows first to initialize the array
            ResultSet rsCount = c.stmt.executeQuery("SELECT COUNT(*) AS total FROM bill WHERE meter_no = '" + meter + "'");
            int rowCount = 0;
            if (rsCount.next()) {
                rowCount = rsCount.getInt("total");
                data = new String[rowCount][4];
            }

            // Fetch actual data
            ResultSet rs = c.stmt.executeQuery("SELECT month, units_consumed, total_bill, status FROM bill WHERE meter_no = '" + meter + "'");
            int i = 0;
            while (rs.next()) {
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

        // --- Create JTable ---
        table = new JTable(data, columnNames);
        table.setFont(new Font("SanSerif", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(200, 220, 255));

        // --- Table Header Styling ---
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBack) {
            setVisible(false);
            // Optional: Navigate back to MainHomePage or PayBill frame if you pass a reference
        }
    }

    public static void main(String[] args) {
        new PaymentHistory("100123"); // Example test
    }
}
