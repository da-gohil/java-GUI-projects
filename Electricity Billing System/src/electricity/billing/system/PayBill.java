package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/**
 * PayBill frame allows the customer to view their bill details for a selected month
 * and mark the bill as paid in the database.
 */
public class PayBill extends JFrame implements ActionListener {

    // Naming convention: c for Choice, btn for Button, lbl for JLabel
    Choice cMonth;
    JButton btnPay, btnBack;
    String meter;
    
    // Labels that hold dynamic data fetched from the database
    JLabel lblMeterNumber, lblCustomerName, lblUnitsConsumed, lblTotalBill, lblStatus;
    
    // Define the primary blue accent color
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color LIGHT_BG = new Color(230, 240, 250);

    PayBill(String meter) {
        this.meter = meter;
        setLayout(null);
        setBounds(300, 150, 900, 600);
        getContentPane().setBackground(LIGHT_BG);

        // --- Panel for Content ---
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(Color.WHITE);
        p.setBounds(30, 30, 500, 500);
        add(p);
        
        Font headerFont = new Font("SanSerif", Font.BOLD, 24);
        Font labelFont = new Font("SanSerif", Font.PLAIN, 14);
        Font dataFont = new Font("SanSerif", Font.BOLD, 15);
        
        // --- Header ---
        JLabel lblHeading = new JLabel("Electricity Bill Payment");
        lblHeading.setFont(headerFont);
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(100, 20, 400, 30);
        p.add(lblHeading);
        
        int y_pos = 90;
        int y_increment = 50;

        // --- 1. Meter Number ---
        JLabel lblMeterNumberLabel = new JLabel("Meter Number:");
        lblMeterNumberLabel.setBounds(35, y_pos, 200, 20);
        lblMeterNumberLabel.setFont(labelFont);
        p.add(lblMeterNumberLabel);
        
        lblMeterNumber = new JLabel(meter); // Display the passed meter number
        lblMeterNumber.setBounds(250, y_pos, 200, 20);
        lblMeterNumber.setFont(dataFont);
        p.add(lblMeterNumber);
        y_pos += y_increment;
        
        // --- 2. Name ---
        JLabel lblCustomerNameLabel = new JLabel("Customer Name:");
        lblCustomerNameLabel.setBounds(35, y_pos, 200, 20);
        lblCustomerNameLabel.setFont(labelFont);
        p.add(lblCustomerNameLabel);
        
        lblCustomerName = new JLabel("Loading..."); // Placeholder label
        lblCustomerName.setBounds(250, y_pos, 200, 20);
        lblCustomerName.setFont(dataFont);
        p.add(lblCustomerName);
        y_pos += y_increment;
        
        // --- 3. Month Choice ---
        JLabel lblMonthLabel = new JLabel("Select Month:");
        lblMonthLabel.setBounds(35, y_pos, 200, 20);
        lblMonthLabel.setFont(labelFont);
        p.add(lblMonthLabel);
        
        cMonth = new Choice();
        cMonth.setBounds(250, y_pos, 200, 20);
        cMonth.setFont(labelFont);
        // Add all months to the Choice component
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            cMonth.add(month);
        }
        p.add(cMonth);
        y_pos += y_increment;
        
        // --- 4. Units Consumed ---
        JLabel lblUnitsConsumedLabel = new JLabel("Units Consumed:");
        lblUnitsConsumedLabel.setBounds(35, y_pos, 200, 20);
        lblUnitsConsumedLabel.setFont(labelFont);
        p.add(lblUnitsConsumedLabel);
        
        lblUnitsConsumed = new JLabel("N/A");
        lblUnitsConsumed.setBounds(250, y_pos, 200, 20);
        lblUnitsConsumed.setFont(dataFont);
        p.add(lblUnitsConsumed);
        y_pos += y_increment;
        
        // --- 5. Total Bill ---
        JLabel lblTotalBillLabel = new JLabel("Total Bill Amount (Rs.):");
        lblTotalBillLabel.setBounds(35, y_pos, 200, 20);
        lblTotalBillLabel.setFont(labelFont);
        p.add(lblTotalBillLabel);
        
        lblTotalBill = new JLabel("N/A");
        lblTotalBill.setBounds(250, y_pos, 200, 20);
        lblTotalBill.setFont(new Font("SanSerif", Font.BOLD, 18));
        lblTotalBill.setForeground(new Color(204, 51, 0)); // Dark Orange/Red for amount
        p.add(lblTotalBill);
        y_pos += y_increment;
        
        // --- 6. Status ---
        JLabel lblStatusLabel = new JLabel("Payment Status:");
        lblStatusLabel.setBounds(35, y_pos, 200, 20);
        lblStatusLabel.setFont(labelFont);
        p.add(lblStatusLabel);
        
        lblStatus = new JLabel("Unknown");
        lblStatus.setBounds(250, y_pos, 200, 20);
        lblStatus.setFont(dataFont);
        lblStatus.setForeground(Color.RED);
        p.add(lblStatus);
        y_pos += y_increment;
        
        // --- Database Fetch for Initial Load (Customer Name and Default Month Bill) ---
        try {
            DBConnection c = new DBConnection();
            
            // 1. Fetch Customer Name
            ResultSet rs = c.stmt.executeQuery("SELECT * FROM customer WHERE meter_no = '"+meter+"'");
            if(rs.next()) {
                lblCustomerName.setText(rs.getString("customer_name"));
            }
            
            // 2. Fetch Default Bill (January or first month in the Choice)
            rs = c.stmt.executeQuery("SELECT * FROM bill WHERE meter_no = '"+meter+"' AND month = '"+cMonth.getSelectedItem()+"'");
            if(rs.next()) {
                lblUnitsConsumed.setText(rs.getString("units_consumed"));
                lblTotalBill.setText("Rs. " + rs.getString("total_bill"));
                lblStatus.setText(rs.getString("status"));
                if ("Paid".equalsIgnoreCase(rs.getString("status"))) {
                    lblStatus.setForeground(new Color(34, 139, 34)); // Forest Green
                } else {
                    lblStatus.setForeground(Color.RED);
                }
            } else {
                lblUnitsConsumed.setText("N/A");
                lblTotalBill.setText("N/A");
                lblStatus.setText("Bill Not Calculated");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching initial data: " + e.getMessage());
        }
        
        // --- Item Listener for Month Change ---
        cMonth.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie) {
                try {
                    DBConnection c = new DBConnection();
                    ResultSet rs = c.stmt.executeQuery("SELECT * FROM bill WHERE meter_no = '"+meter+"' AND month = '"+cMonth.getSelectedItem()+"'");
                    if(rs.next()) {
                        lblUnitsConsumed.setText(rs.getString("units_consumed"));
                        lblTotalBill.setText("Rs. " + rs.getString("total_bill"));
                        lblStatus.setText(rs.getString("status"));
                         if ("Paid".equalsIgnoreCase(rs.getString("status"))) {
                            lblStatus.setForeground(new Color(34, 139, 34)); // Forest Green
                        } else {
                            lblStatus.setForeground(Color.RED);
                        }
                    } else {
                        // Reset fields if bill for selected month is not found
                        lblUnitsConsumed.setText("N/A");
                        lblTotalBill.setText("N/A");
                        lblStatus.setText("Bill Not Calculated");
                        lblStatus.setForeground(Color.BLUE); // Indicate no calculation yet
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PayBill.this, "Error fetching bill details: " + e.getMessage());
                }
            }
        });
        
        // --- Buttons (Aligned with Apple aesthetic) ---
        
        // Primary Action Button (Blue Background, White Text)
        btnPay = new JButton("Pay Bill Now");
        btnPay.setBackground(PRIMARY_BLUE);
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnPay.setBounds(100, 440, 140, 30);
        btnPay.addActionListener(this);
        p.add(btnPay);
        
        // Secondary Action Button (White Background, Blue Text, Border)
        btnBack = new JButton("Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(PRIMARY_BLUE);
        btnBack.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnBack.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE));
        btnBack.setBounds(270, 440, 140, 30);
        btnBack.addActionListener(this);
        p.add(btnBack);
        
        // --- Image ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bill.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 450, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(550, 50, 300, 450);
        add(image);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnPay) {
            String selectedMonth = cMonth.getSelectedItem();
            
            // 1. Check if a valid bill exists for the month and is not already paid
            if ("Bill Not Calculated".equals(lblStatus.getText()) || "N/A".equals(lblTotalBill.getText())) {
                JOptionPane.showMessageDialog(this, "Bill for " + selectedMonth + " has not been calculated yet or is invalid.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Paid".equalsIgnoreCase(lblStatus.getText())) {
                 JOptionPane.showMessageDialog(this, "The bill for " + selectedMonth + " is already marked as Paid.", "Already Paid", JOptionPane.INFORMATION_MESSAGE);
                 return;
            }

            try {
                // In a real application, this is where you'd navigate to a payment gateway (e.g., Paytm)
                // Since we don't have that integration, we simulate the payment by marking the bill as Paid.
                
                DBConnection c = new DBConnection();
                // SQL to update the bill status to 'Paid'
                String updateQuery = "UPDATE bill SET status = 'Paid' WHERE meter_no = '"+meter+"' AND month='"+selectedMonth+"'";
                c.stmt.executeUpdate(updateQuery);
                
                // Show confirmation message
                JOptionPane.showMessageDialog(this, "Payment Successful!\nBill for " + selectedMonth + " marked as Paid.", "Payment Confirmed", JOptionPane.INFORMATION_MESSAGE);
                
                // Immediately update the status label in the UI to reflect the change
                lblStatus.setText("Paid");
                lblStatus.setForeground(new Color(34, 139, 34)); // Forest Green
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
        }
    }

    public static void main(String[] args){
        // Example usage (replace with actual meter number passed from login)
        new PayBill("100123"); 
    }
}
