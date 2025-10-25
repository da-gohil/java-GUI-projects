package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

/**
 * DepositDetails class displays and allows filtering of all bill records,
 * acting as an administrative view of payment/deposit history.
 */
public class DepositDetails extends JFrame implements ActionListener{

    Choice cMeterNumber, cMonth;
    JTable table;
    JButton btnSearch, btnPrint;
    
    DepositDetails(){
        super("Bill and Deposit Details");
        
        // --- Frame Setup ---
        setSize(700, 700);
        setLocation(400, 100);
        
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        // --- Meter Number Filter ---
        JLabel lblMeterNumber = new JLabel("Search By Meter Number");
        lblMeterNumber.setBounds(20, 20, 150, 20);
        lblMeterNumber.setFont(new Font("SanSerif", Font.PLAIN, 14));
        add(lblMeterNumber);
        
        cMeterNumber = new Choice();
        cMeterNumber.setBounds(180, 20, 150, 20);
        add(cMeterNumber);
        
        // Populate Meter Numbers from 'customer' table
        try {
            DBConnection c  = new DBConnection();
            ResultSet rs = c.stmt.executeQuery("SELECT meter_no FROM customer");
            while(rs.next()) {
                cMeterNumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading meter numbers: " + e.getMessage());
        }
        
        // --- Month Filter ---
        JLabel lblMonth = new JLabel("Search By Month");
        lblMonth.setBounds(400, 20, 100, 20);
        lblMonth.setFont(new Font("SanSerif", Font.PLAIN, 14));
        add(lblMonth);
        
        cMonth = new Choice();
        cMonth.setBounds(520, 20, 150, 20);
        cMonth.add("January");
        cMonth.add("February");
        cMonth.add("March");
        cMonth.add("April");
        cMonth.add("May");
        cMonth.add("June");
        cMonth.add("July");
        cMonth.add("August");
        cMonth.add("September");
        cMonth.add("October");
        cMonth.add("November");
        cMonth.add("December");
        add(cMonth);
        
        // --- JTable Setup ---
        table = new JTable();
        
        // Initial Table Load (Show all bills)
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.stmt.executeQuery("SELECT * FROM bill");
            
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill details: " + e.getMessage());
        }
        
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 100, 700, 600);
        add(sp);
        
        // --- Buttons ---
        
        // Primary Action Button (Blue background, White Text)
        btnSearch = new JButton("Search");
        btnSearch.setBounds(20, 70, 100, 25);
        btnSearch.setBackground(new Color(0, 122, 255)); // Primary Accent Blue
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnSearch.addActionListener(this);
        add(btnSearch);
        
        // Secondary Action Button (White background, Dark Blue Text, with border implied by default look)
        btnPrint = new JButton("Print");
        btnPrint.setBounds(140, 70, 100, 25);
        btnPrint.setBackground(Color.WHITE);
        btnPrint.setForeground(new Color(0, 122, 255)); // Primary Accent Blue for text
        btnPrint.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnPrint.addActionListener(this);
        add(btnPrint);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSearch) {
            // Query to filter by selected meter number and month
            String query = "SELECT * FROM bill WHERE meter_no = '"+cMeterNumber.getSelectedItem()+"' AND month = '"+cMonth.getSelectedItem()+"'";
            
            try {
                DBConnection c = new DBConnection();
                ResultSet rs = c.stmt.executeQuery(query);
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(this, "Error during search: " + e.getMessage());
            }
        } else if (ae.getSource() == btnPrint) {
            try {
                // Java built-in print functionality for JTable
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error printing table: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new DepositDetails();
    }
}
