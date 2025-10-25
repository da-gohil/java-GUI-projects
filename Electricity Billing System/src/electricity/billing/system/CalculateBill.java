package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math; // For rounding functions

// This class is now properly focused on calculating and saving a customer's monthly bill.
public class CalculateBill extends JFrame implements ActionListener {

    // Declare all components globally
    Choice cMeterNumber, cMonth;
    JTextField tfUnits;
    JButton btnCalculate, btnCancel;
    JLabel lblName, lblAddress;

    CalculateBill() {
        super("Calculate Electricity Bill");
        setSize(700, 500);
        setLocation(400, 200);

        // --- Panel Setup ---
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173, 216, 230)); // Light Blue background
        add(p);

        // --- Heading ---
        JLabel heading = new JLabel("Calculate Electricity Bill");
        heading.setBounds(180, 10, 350, 25);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        p.add(heading);

        // --- Meter Number Selection ---
        JLabel lblmeterno = new JLabel("Meter Number");
        lblmeterno.setBounds(100, 80, 120, 20);
        p.add(lblmeterno);

        cMeterNumber = new Choice();
        // 1. Populate Meter Numbers from the database
        try {
            DBConnection c = new DBConnection();
            // Fetch all meter numbers from the customer table (as per DDL)
            ResultSet rs = c.stmt.executeQuery("SELECT meter_no FROM customer");
            while(rs.next()) {
                cMeterNumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: Could not load meter numbers.");
        }
        
        cMeterNumber.setBounds(240, 80, 200, 20);
        
        // 2. Add Listener to update customer details when meter number changes
        cMeterNumber.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                updateCustomerDetails();
            }
        });
        p.add(cMeterNumber);

        // --- Customer Name (Display Field) ---
        JLabel lblCustomerName = new JLabel("Customer Name");
        lblCustomerName.setBounds(100, 120, 120, 20);
        p.add(lblCustomerName);

        lblName = new JLabel(); // Will be updated by updateCustomerDetails()
        lblName.setBounds(240, 120, 200, 20);
        p.add(lblName);
        
        // --- Address (Display Field) ---
        JLabel lblCustomerAddress = new JLabel("Address");
        lblCustomerAddress.setBounds(100, 160, 100, 20);
        p.add(lblCustomerAddress);

        lblAddress = new JLabel(); // Will be updated by updateCustomerDetails()
        lblAddress.setBounds(240, 160, 200, 20);
        p.add(lblAddress);
        
        // Call this once to load initial data for the first meter number
        updateCustomerDetails();

        // --- Units Consumed (Input Field) ---
        JLabel lblUnitsConsumed = new JLabel("Units Consumed");
        lblUnitsConsumed.setBounds(100, 200, 120, 20);
        p.add(lblUnitsConsumed);

        tfUnits = new JTextField();
        tfUnits.setBounds(240, 200, 200, 20);
        p.add(tfUnits);


        // --- Month Selection (Dropdown) ---
        JLabel lblMonth = new JLabel("Billing Month");
        lblMonth.setBounds(100, 240, 100, 20);
        p.add(lblMonth);

        cMonth = new Choice();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            cMonth.add(month);
        }
        cMonth.setBounds(240, 240, 200, 20);
        p.add(cMonth);

        // --- Buttons ---
        btnCalculate = new JButton("Calculate & Submit");
        btnCalculate.setBounds(120, 350, 160, 30);
        btnCalculate.setBackground(Color.BLACK);
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.addActionListener(this);
        p.add(btnCalculate);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(300, 350, 120, 30);
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(this);
        p.add(btnCancel);

        // --- Layout and Image ---
        setLayout(new BorderLayout());
        add(p, "Center");

        // The image loading path assumes an 'icon' folder in the classpath.
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
        Image i2 = i1.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image, "West");

        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
    }
    
    // Helper method to fetch and display customer details based on the selected meter
    private void updateCustomerDetails() {
        String selectedMeter = cMeterNumber.getSelectedItem();
        if (selectedMeter != null && !selectedMeter.isEmpty()) {
            try {
                DBConnection c = new DBConnection();
                // Query customer table for name and address
                String query = "SELECT customer_name, address FROM customer WHERE meter_no = '" + selectedMeter + "'";
                ResultSet rs = c.stmt.executeQuery(query);
                
                if (rs.next()) {
                    lblName.setText(rs.getString("customer_name"));
                    lblAddress.setText(rs.getString("address"));
                } else {
                    lblName.setText("No Customer Found");
                    lblAddress.setText("N/A");
                }
            } catch (Exception e) {
                e.printStackTrace();
                lblName.setText("DB Error");
                lblAddress.setText("DB Error");
            }
        } else {
            lblName.setText("N/A");
            lblAddress.setText("N/A");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnCalculate) {
            
            // 1. Retrieve essential form data
            String meterNumber = cMeterNumber.getSelectedItem();
            String month = cMonth.getSelectedItem();
            String unitsConsumedStr = tfUnits.getText();

            if (unitsConsumedStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter units consumed.");
                return;
            }

            try {
                int units = Integer.parseInt(unitsConsumedStr);
                
                DBConnection c = new DBConnection();
                
                // --- Start Dynamic Rate Calculation Logic ---
                
                // 2. Fetch Tax Rates from the 'tax' table
                double unitRate = 0, meterRent = 0, serviceCharge = 0, serviceTax = 0, swacchCess = 0, fixedTax = 0;
                
                String taxQuery = "SELECT * FROM tax";
                ResultSet taxRs = c.stmt.executeQuery(taxQuery);
                
                if (taxRs.next()) {
                    // All values are stored as VARCHAR, so parsing is required
                    unitRate = Double.parseDouble(taxRs.getString("cost_per_unit"));
                    meterRent = Double.parseDouble(taxRs.getString("meter_rent"));
                    serviceCharge = Double.parseDouble(taxRs.getString("service_charge"));
                    serviceTax = Double.parseDouble(taxRs.getString("service_tax"));
                    swacchCess = Double.parseDouble(taxRs.getString("swacch_bharat_cess"));
                    fixedTax = Double.parseDouble(taxRs.getString("fixed_tax"));
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Error: No tax rates found in the database ('tax' table). Bill cannot be calculated.");
                    return;
                }
                
                // 3. Perform Final Calculation
                double unitsCost = units * unitRate;
                double totalBill = unitsCost + meterRent + serviceCharge + serviceTax + swacchCess + fixedTax;
                
                // Round to two decimal places
                totalBill = Math.round(totalBill * 100.0) / 100.0;
                
                // --- End Dynamic Rate Calculation Logic ---


                // 4. Construct SQL Query to insert the calculated bill into the 'bill' table
                String status = "Unpaid"; // Initial status of the bill
                
                // Using PreparedStatement is safer and preferred, but sticking to Statement for consistency with previous files.
                String query = "INSERT INTO bill VALUES('" + meterNumber + "', '" + month + "', '" + units + "', '" + totalBill + "', '" + status + "')";
                
                // 5. Execute the query
                c.stmt.executeUpdate(query);
                
                JOptionPane.showMessageDialog(null, "Bill for Meter " + meterNumber + " calculated and saved successfully.\nTotal amount: $" + String.format("%.2f", totalBill));
                setVisible(false);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Units Consumed or Tax Rates must be valid numbers.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: Failed to save bill data. Check logs for details: " + e.getMessage());
            }
        } else {
            // Cancel button action
            setVisible(false);
        }
    }

    // Main method to test the frame
    public static void main(String[] args) {
        new CalculateBill();
    }
}
