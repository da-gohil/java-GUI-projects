package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
// import java.sql.*; // Needed if you were to use PreparedStatement or Connection

public class NewCustomer extends JFrame implements ActionListener {

    // Declare all components globally
    JTextField tfname, tfaddress, tfstate, tfcity, tfemail, tfphone;
    JButton next, cancel;
    JLabel lblmeter;

    NewCustomer() {
        super("New Customer");
        setSize(700, 500);
        setLocation(400, 200);

        // --- Panel Setup ---
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173, 216, 230));
        add(p);

        // --- Heading ---
        JLabel heading = new JLabel("New Customer");
        heading.setBounds(180, 10, 200, 25);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 24));
        p.add(heading);

        // --- Customer Name ---
        JLabel lblname = new JLabel("Customer Name");
        lblname.setBounds(100, 80, 120, 20);
        p.add(lblname);

        tfname = new JTextField();
        tfname.setBounds(240, 80, 200, 20);
        p.add(tfname);

        // --- Meter Number Label ---
        JLabel lblmeterno = new JLabel("Meter Number");
        lblmeterno.setBounds(100, 120, 120, 20);
        p.add(lblmeterno);

        lblmeter = new JLabel("");
        lblmeter.setBounds(240, 120, 200, 20);
        p.add(lblmeter);

        // --- Random Meter Number Generation ---
        // Generates a random number up to 6 digits.
        Random ran = new Random();
        long number = ran.nextLong() % 1000000;
        // Format to ensure at least 6 digits for consistency (e.g., 001234)
        lblmeter.setText(String.format("%06d", Math.abs(number)));

        // --- Address ---
        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(100, 160, 100, 20);
        p.add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(240, 160, 200, 20);
        p.add(tfaddress);

        // --- City ---
        JLabel lblcity = new JLabel("City");
        lblcity.setBounds(100, 200, 100, 20);
        p.add(lblcity);

        tfcity = new JTextField();
        tfcity.setBounds(240, 200, 200, 20);
        p.add(tfcity);

        // --- State ---
        JLabel lblstate = new JLabel("State");
        lblstate.setBounds(100, 240, 100, 20);
        p.add(lblstate);

        tfstate = new JTextField();
        tfstate.setBounds(240, 240, 200, 20);
        p.add(tfstate);

        // --- Email ---
        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(100, 280, 100, 20);
        p.add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(240, 280, 200, 20);
        p.add(tfemail);

        // --- Phone Number ---
        JLabel lblphone = new JLabel("Phone Number");
        lblphone.setBounds(100, 320, 120, 20);
        p.add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(240, 320, 200, 20);
        p.add(tfphone);

        // --- Buttons ---
        next = new JButton("Next");
        next.setBounds(120, 390, 100, 25);
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.addActionListener(this);
        p.add(next);

        cancel = new JButton("Cancel");
        cancel.setBounds(250, 390, 100, 25);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        p.add(cancel);

        // --- Layout and Image ---
        setLayout(new BorderLayout());
        add(p, "Center");

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
        Image i2 = i1.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image, "West");

        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
    }

// ----------------------------------------------------------------------------------------------------------------------
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == next) {
            
            // 1. Retrieve all form data
            String name = tfname.getText();
            String meter = lblmeter.getText();
            String address = tfaddress.getText();
            String city = tfcity.getText();
            String state = tfstate.getText();
            String email = tfemail.getText();
            String phone = tfphone.getText();
            
            // 2. Define derived/fixed values for the LOGIN table (5 columns)
            // DDL: meter_no, username, password, userType, name
            String loginUsername = meter;         
            String loginPassword = name;          
            String userType = "Customer";         
            
            // 3. Construct SQL Queries using String Concatenation
            
            // Query for 'customer' table (7 columns)
            String query1 = "INSERT INTO customer VALUES('" + name + "', '" + meter + "', '" + address + "', '" + city + "', '" + state + "', '" + email + "', '" + phone + "')";

            // Query for 'Login' table (5 columns - all NOT NULL fields must be populated)
            // Order: meter_no, username, password, userType, name
            String query2 = "INSERT INTO Login VALUES('" + meter + "', '" + loginUsername + "', '" + loginPassword + "', '" + userType + "', '" + name + "')";
            
            
            try {
                // Initialize Connection (Assuming DBConnection class exists and has 'stmt' Statement object)
                DBConnection c = new DBConnection(); 
                
                // Execute both queries using the Statement object
                c.stmt.executeUpdate(query1);
                c.stmt.executeUpdate(query2);
                
                JOptionPane.showMessageDialog(null, "Customer Details and Login Credentials Added Successfully");
                setVisible(false);
                
                // Navigate to the next frame (uncomment if MeterInfo class is available)
                // new MeterInfo(meter);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: Failed to add data. Check logs for details. (Possible error: Meter/Name too long for Login table VARCHAR(10))");
            }
        } else {
            // Cancel button action
            setVisible(false);
        }
    }
    
// ----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        new NewCustomer();
    }
}