package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

/**
 * CustomerDetails class displays all customer records dynamically from the database.
 * Designed for Admin users, with robust database handling and optional printing.
 */
public class CustomerDetails extends JFrame implements ActionListener {

    private JTable table;
    private JButton print;

    public CustomerDetails() {
        super("All Customer Details");

        // Frame setup
        setSize(1200, 650);
        setLocationRelativeTo(null); // center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize table
        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Print button
        print = new JButton("Print");
        print.setFont(new Font("Tahoma", Font.BOLD, 14));
        print.setBackground(new Color(0, 122, 255));
        print.setForeground(Color.WHITE);
        print.addActionListener(this);
        add(print, BorderLayout.SOUTH);

        // Load customer data
        loadCustomerData();

        setVisible(true);
    }

    /**
     * Dynamically loads customer data from the database.
     * Uses try-with-resources to avoid resource leaks.
     */
    private void loadCustomerData() {
        String query = "SELECT * FROM customer"; // Adjust columns as per your DB
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error fetching customer details. Please check your database connection and table structure.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Could not print the table.",
                        "Print Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new CustomerDetails();
    }
}
