package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.sql.*; // REQUIRED for DBConnection and SQL operations

public class NewCustomer extends JFrame implements ActionListener {

    // Declare all components globally following the txt, lbl, btn naming conventions
    JTextField txtCustomerName, txtAddress, txtState, txtCity, txtEmail, txtPhone;
    JButton btnNext, btnCancel;
    JLabel lblMeterNumber;

    NewCustomer() {
        super("New Customer Registration");
        
        // --- Frame Setup ---
        setSize(700, 500);
        setLocation(400, 150);

        // Define fonts based on mandate
        Font headingFont = new Font("SanSerif", Font.BOLD, 22);
        Font labelFont = new Font("SanSerif", Font.PLAIN, 14);

        // --- Panel Setup ---
        JPanel p = new JPanel();
        p.setLayout(null);
        // Use mandated white background
        p.setBackground(Color.WHITE); 
        
        // Use BorderLayout for the frame, adding the panel to the center and image to the left
        setLayout(new BorderLayout());
        add(p, "Center");

        // --- Heading ---
        JLabel lblHeading = new JLabel("New Customer Registration");
        lblHeading.setBounds(180, 10, 300, 25);
        lblHeading.setFont(headingFont);
        // Use mandated primary accent blue color
        lblHeading.setForeground(new Color(0, 122, 255));
        p.add(lblHeading);

        // --- 1. Customer Name ---
        JLabel lblCustomerName = new JLabel("Customer Name:");
        lblCustomerName.setBounds(100, 80, 120, 20);
        lblCustomerName.setFont(labelFont);
        p.add(lblCustomerName);

        txtCustomerName = new JTextField();
        txtCustomerName.setBounds(240, 80, 200, 20);
        txtCustomerName.setFont(labelFont);
        p.add(txtCustomerName);

        // --- 2. Meter Number Label ---
        JLabel lblMeterNo = new JLabel("Meter Number:");
        lblMeterNo.setBounds(100, 120, 120, 20);
        lblMeterNo.setFont(labelFont);
        p.add(lblMeterNo);

        // Label to display the generated meter number
        lblMeterNumber = new JLabel();
        lblMeterNumber.setBounds(240, 120, 200, 20);
        lblMeterNumber.setFont(new Font("SanSerif", Font.BOLD, 14));
        lblMeterNumber.setForeground(Color.RED); 
        p.add(lblMeterNumber);

        // --- Random Meter Number Generation ---
        Random ran = new Random();
        // Generates a random number and formats it to ensure a 6-digit display
        long number = Math.abs(ran.nextLong() % 900000) + 100000; // Ensures 6 digits
        lblMeterNumber.setText(String.valueOf(number));

        // --- 3. Address ---
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(100, 160, 100, 20);
        lblAddress.setFont(labelFont);
        p.add(lblAddress);

        txtAddress = new JTextField();
        txtAddress.setBounds(240, 160, 200, 20);
        txtAddress.setFont(labelFont);
        p.add(txtAddress);

        // --- 4. City ---
        JLabel lblCity = new JLabel("City:");
        lblCity.setBounds(100, 200, 100, 20);
        lblCity.setFont(labelFont);
        p.add(lblCity);

        txtCity = new JTextField();
        txtCity.setBounds(240, 200, 200, 20);
        txtCity.setFont(labelFont);
        p.add(txtCity);

        // --- 5. State ---
        JLabel lblState = new JLabel("State:");
        lblState.setBounds(100, 240, 100, 20);
        lblState.setFont(labelFont);
        p.add(lblState);

        txtState = new JTextField();
        txtState.setBounds(240, 240, 200, 20);
        txtState.setFont(labelFont);
        p.add(txtState);

        // --- 6. Email ---
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(100, 280, 100, 20);
        lblEmail.setFont(labelFont);
        p.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(240, 280, 200, 20);
        txtEmail.setFont(labelFont);
        p.add(txtEmail);

        // --- 7. Phone Number ---
        JLabel lblPhone = new JLabel("Phone Number:");
        lblPhone.setBounds(100, 320, 120, 20);
        lblPhone.setFont(labelFont);
        p.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(240, 320, 200, 20);
        txtPhone.setFont(labelFont);
        p.add(txtPhone);

        // --- 8. Buttons (Apple-inspired look) ---
        
        // Primary Action Button (Blue Background, White Text)
        btnNext = new JButton("Next");
        btnNext.setBounds(120, 390, 100, 25);
        btnNext.setBackground(new Color(0, 122, 255)); // Primary Accent Blue
        btnNext.setForeground(Color.WHITE);
        btnNext.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnNext.addActionListener(this);
        p.add(btnNext);

        // Secondary Action Button (White Background, Dark Blue Text)
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(250, 390, 100, 25);
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(0, 122, 255));
        btnCancel.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255)));
        btnCancel.addActionListener(this);
        p.add(btnCancel);

        // --- Image ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
        Image i2 = i1.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image, "West"); // Add image to the West side of the BorderLayout

        // Set the content pane background to the mandated light color
        getContentPane().setBackground(new Color(230, 240, 250));

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnNext) {
            
            // 1. Retrieve all form data using the new names
            String name = txtCustomerName.getText();
            String meter = lblMeterNumber.getText();
            String address = txtAddress.getText();
            String city = txtCity.getText();
            String state = txtState.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();

            // Basic Validation
            if (name.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all customer details.");
                return;
            }
            
            // 2. Define derived/fixed values for the LOGIN table
            String loginUsername = meter;
            String loginPassword = name; 
            String userType = "Customer";

            // 3. Construct SQL Queries
            
            // Query for 'customer' table (7 columns)
            String query1 = "INSERT INTO customer VALUES('" + name + "', '" + meter + "', '" + address + "', '" + city + "', '" + state + "', '" + email + "', '" + phone + "')";

            // Query for 'Login' table (5 columns - meter_no, username, password, userType, name)
            String query2 = "INSERT INTO login VALUES('" + meter + "', '" + loginUsername + "', '" + loginPassword + "', '" + userType + "', '" + name + "')";

            try {
                DBConnection c = new DBConnection();

                // Execute both queries using the Statement object
                c.stmt.executeUpdate(query1);
                c.stmt.executeUpdate(query2);

                JOptionPane.showMessageDialog(null, "Customer Details and Login Credentials Added Successfully.\n\nMeter No: " + meter + "\nDefault Password: " + name);
                
                setVisible(false);
                
                // Navigate to the next frame to collect Meter Information (as per original logic)
                new MeterInfo(meter);
                
            } catch (SQLException e) {
                 if (e.getSQLState().equals("23000")) { // Handle duplicate entry error (e.g., if meter number already exists)
                     JOptionPane.showMessageDialog(this, "Error: Meter Number " + meter + " already exists. Please try again.");
                 } else {
                     e.printStackTrace();
                     JOptionPane.showMessageDialog(null, "Database Error: Failed to add data. Check logs for details: " + e.getMessage());
                 }
            } catch (Exception e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
            }

        } else if (ae.getSource() == btnCancel) {
            // Cancel button action
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new NewCustomer();
    }
}
