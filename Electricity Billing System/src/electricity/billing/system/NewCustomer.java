package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;

/**
 * NewCustomer class handles creating a new customer, inserting into Customer and Login tables,
 * and navigating to MeterInfo after successful registration.
 * 
 * Cancel button returns to the already-running MainHomePage instead of creating a new one.
 */
public class NewCustomer extends JFrame implements ActionListener {

    private MainHomePage mainHome; // Reference to main homepage

    // --- Form Fields ---
    private JTextField txtCustomerName, txtAddress, txtCity, txtState, txtEmail, txtPhone;
    private JLabel lblMeterNumber;
    private JButton btnNext, btnCancel;

    public NewCustomer(MainHomePage mainHome) {
        super("New Customer Registration");
        this.mainHome = mainHome;

        setSize(700, 500);
        setLocation(400, 150);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        add(panel, BorderLayout.CENTER);

        // --- Fonts ---
        Font headingFont = new Font("SanSerif", Font.BOLD, 22);
        Font labelFont = new Font("SanSerif", Font.PLAIN, 14);

        // --- Heading ---
        JLabel lblHeading = new JLabel("New Customer Registration");
        lblHeading.setBounds(180, 10, 400, 25);
        lblHeading.setFont(headingFont);
        lblHeading.setForeground(new Color(0, 122, 255));
        panel.add(lblHeading);

        // --- Customer Name ---
        JLabel lblCustomerName = new JLabel("Customer Name:");
        lblCustomerName.setBounds(100, 80, 120, 20);
        lblCustomerName.setFont(labelFont);
        panel.add(lblCustomerName);

        txtCustomerName = new JTextField();
        txtCustomerName.setBounds(240, 80, 200, 20);
        txtCustomerName.setFont(labelFont);
        panel.add(txtCustomerName);

        // --- Meter Number Label ---
        JLabel lblMeterNo = new JLabel("Meter Number:");
        lblMeterNo.setBounds(100, 120, 120, 20);
        lblMeterNo.setFont(labelFont);
        panel.add(lblMeterNo);

        lblMeterNumber = new JLabel(generateUniqueMeter());
        lblMeterNumber.setBounds(240, 120, 200, 20);
        lblMeterNumber.setFont(new Font("SanSerif", Font.BOLD, 14));
        lblMeterNumber.setForeground(Color.RED);
        panel.add(lblMeterNumber);

        // --- Address ---
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(100, 160, 100, 20);
        lblAddress.setFont(labelFont);
        panel.add(lblAddress);

        txtAddress = new JTextField();
        txtAddress.setBounds(240, 160, 200, 20);
        txtAddress.setFont(labelFont);
        panel.add(txtAddress);

        // --- City ---
        JLabel lblCity = new JLabel("City:");
        lblCity.setBounds(100, 200, 100, 20);
        lblCity.setFont(labelFont);
        panel.add(lblCity);

        txtCity = new JTextField();
        txtCity.setBounds(240, 200, 200, 20);
        txtCity.setFont(labelFont);
        panel.add(txtCity);

        // --- State ---
        JLabel lblState = new JLabel("State:");
        lblState.setBounds(100, 240, 100, 20);
        lblState.setFont(labelFont);
        panel.add(lblState);

        txtState = new JTextField();
        txtState.setBounds(240, 240, 200, 20);
        txtState.setFont(labelFont);
        panel.add(txtState);

        // --- Email ---
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(100, 280, 100, 20);
        lblEmail.setFont(labelFont);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(240, 280, 200, 20);
        txtEmail.setFont(labelFont);
        panel.add(txtEmail);

        // --- Phone ---
        JLabel lblPhone = new JLabel("Phone Number:");
        lblPhone.setBounds(100, 320, 120, 20);
        lblPhone.setFont(labelFont);
        panel.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(240, 320, 200, 20);
        txtPhone.setFont(labelFont);
        panel.add(txtPhone);

        // --- Buttons ---
        btnNext = new JButton("Next");
        btnNext.setBounds(120, 390, 100, 25);
        btnNext.setBackground(new Color(0, 122, 255));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnNext.addActionListener(this);
        panel.add(btnNext);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(250, 390, 100, 25);
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(0, 122, 255));
        btnCancel.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255)));
        btnCancel.addActionListener(this);
        panel.add(btnCancel);

        // --- Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
            Image i2 = i1.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
            JLabel image = new JLabel(new ImageIcon(i2));
            add(image, "West");
        } catch (Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }

        setVisible(true);
    }

    // --- Generate Unique Meter Number ---
    private String generateUniqueMeter() {
        Random rand = new Random();
        String meter = "";
        boolean unique = false;
        try {
            DBConnection c = new DBConnection();
            while (!unique) {
                long number = Math.abs(rand.nextLong() % 900000) + 100000;
                meter = String.valueOf(number);
                ResultSet rs = c.getStatement().executeQuery(
                        "SELECT meter_no FROM customer WHERE meter_no = '" + meter + "'");
                if (!rs.next()) {
                    unique = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            meter = "ERROR123"; // fallback
        }
        return meter;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnNext) {

            String name = txtCustomerName.getText().trim();
            String meter = lblMeterNumber.getText().trim();
            String address = txtAddress.getText().trim();
            String city = txtCity.getText().trim();
            String state = txtState.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();

            if (name.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all customer details.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                DBConnection c = new DBConnection();

                // --- Insert into Customer ---
                String custQuery = "INSERT INTO customer (customer_name, meter_no, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstCust = c.getConnection().prepareStatement(custQuery);
                pstCust.setString(1, name);
                pstCust.setString(2, meter);
                pstCust.setString(3, address);
                pstCust.setString(4, city);
                pstCust.setString(5, state);
                pstCust.setString(6, email);
                pstCust.setString(7, phone);
                pstCust.executeUpdate();

                // --- Insert into Login ---
                String loginQuery = "INSERT INTO login (meter_no, username, password, userType, name) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstLogin = c.getConnection().prepareStatement(loginQuery);
                pstLogin.setString(1, meter);
                pstLogin.setString(2, meter); // username = meter
                pstLogin.setString(3, name);  // default password = name
                pstLogin.setString(4, "Customer");
                pstLogin.setString(5, name);
                pstLogin.executeUpdate();

                JOptionPane.showMessageDialog(this, "Customer added successfully!\nMeter No: " + meter + "\nDefault Password: " + name);

                setVisible(false);
                new MeterInfo(meter); // proceed to next step

            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Meter number " + meter + " already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
            if (mainHome != null) {
                mainHome.setVisible(true); // Show existing main homepage
            }
        }
    }

    public static void main(String[] args) {
        // For testing standalone, we pass null since no MainHomePage exists yet
        new NewCustomer(null);
    }
}
