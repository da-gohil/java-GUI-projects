package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import javax.swing.border.*;

/**
 * NewCustomer class handles creating a new customer, inserting into Customer and Login tables,
 * and navigating to MeterInfo after successful registration.
 * * NOTE: Frame size is fixed at 1024x768.
 */
public class NewCustomer extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("SanSerif", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("SanSerif", Font.BOLD, 16);
    private static final Font FIELD_FONT = new Font("SanSerif", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("SanSerif", Font.BOLD, 16);
    
    // Layout constants for positioning elements
    private static final int CONTENT_START_X = 50; 
    private static final int FIELD_COLUMN_X = 250;
    private static final int INPUT_WIDTH = 300; 
    private static final int ELEMENT_HEIGHT = 35; 
    private static final int Y_SPACING = 55;

    private MainHomePage mainHome; // Reference to main homepage

    // --- Form Fields (Variable names preserved) ---
    private JTextField txtCustomerName, txtAddress, txtCity, txtState, txtEmail, txtPhone;
    private JLabel lblMeterNumber;
    private JButton btnNext, btnCancel;

    public NewCustomer(MainHomePage mainHome) {
        super("New Customer Registration");
        this.mainHome = mainHome;

        // --- Frame Settings (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 

        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);

        // --- Heading ---
        JLabel lblHeading = new JLabel("NEW CUSTOMER REGISTRATION", SwingConstants.LEFT);
        lblHeading.setBounds(CONTENT_START_X, 50, 500, 40);
        lblHeading.setFont(HEADING_FONT);
        lblHeading.setForeground(PRIMARY_BLUE);
        add(lblHeading);

        // Panel to group form elements on the left
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(0, 100, 600, 550); 
        formPanel.setBackground(BACKGROUND_COLOR);
        add(formPanel);
        
        int currentY = 0;

        // --- Form Fields ---
        // NOTE: Reflection logic is complex and error-prone. Reverting to direct assignment
        // for clarity and robustness, assuming instance variables are initialized to null.
        
        // 1. Customer Name
        formPanel.add(createLabel("Customer Name:", CONTENT_START_X, currentY));
        txtCustomerName = createTextField(FIELD_COLUMN_X, currentY);
        formPanel.add(txtCustomerName);
        currentY += Y_SPACING;

        // 2. Meter Number (Generated)
        formPanel.add(createLabel("Meter Number:", CONTENT_START_X, currentY));
        lblMeterNumber = new JLabel(generateUniqueMeter());
        lblMeterNumber.setBounds(FIELD_COLUMN_X, currentY, INPUT_WIDTH, ELEMENT_HEIGHT);
        lblMeterNumber.setFont(new Font("SanSerif", Font.BOLD, 18));
        lblMeterNumber.setForeground(new Color(200, 0, 0)); 
        formPanel.add(lblMeterNumber);
        currentY += Y_SPACING;

        // 3. Address
        formPanel.add(createLabel("Address:", CONTENT_START_X, currentY));
        txtAddress = createTextField(FIELD_COLUMN_X, currentY);
        formPanel.add(txtAddress);
        currentY += Y_SPACING;

        // 4. City
        formPanel.add(createLabel("City:", CONTENT_START_X, currentY));
        txtCity = createTextField(FIELD_COLUMN_X, currentY);
        formPanel.add(txtCity);
        currentY += Y_SPACING;

        // 5. State
        formPanel.add(createLabel("State:", CONTENT_START_X, currentY));
        txtState = createTextField(FIELD_COLUMN_X, currentY);
        formPanel.add(txtState);
        currentY += Y_SPACING;

        // 6. Email
        formPanel.add(createLabel("Email:", CONTENT_START_X, currentY));
        txtEmail = createTextField(FIELD_COLUMN_X, currentY);
        formPanel.add(txtEmail);
        currentY += Y_SPACING;

        // 7. Phone
        formPanel.add(createLabel("Phone Number:", CONTENT_START_X, currentY));
        txtPhone = createTextField(FIELD_COLUMN_X, currentY);
        formPanel.add(txtPhone);
        currentY += Y_SPACING;
        
        // --- Buttons ---
        currentY += 15;

        // Applying Universal Button Style: White BG, Black Text, Blue Border
        btnNext = createButton("Next", FIELD_COLUMN_X, currentY, 120, ELEMENT_HEIGHT);
        formPanel.add(btnNext);

        btnCancel = createButton("Cancel", FIELD_COLUMN_X + 140, currentY, 120, ELEMENT_HEIGHT);
        formPanel.add(btnCancel);

        // --- Right Side Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
            int imgX = 600;
            int imgW = FRAME_WIDTH - imgX - 50; 
            int imgH = 500; 
            
            Image i2 = i1.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(imgX, 100, imgW, imgH);
            add(image);
        } catch (Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }

        setVisible(true);
    }
    
    // --- Helper Methods ---

    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, ELEMENT_HEIGHT);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(new Color(51, 51, 51));
        return lbl;
    }
    
    private JTextField createTextField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, INPUT_WIDTH, ELEMENT_HEIGHT);
        txt.setFont(FIELD_FONT);
        txt.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        return txt;
    }

    /** Creates a button with the universal white/black/blue style. */
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, height);
        btn.setFont(BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Universal Style: White BG, Black Text, Blue Border
        btn.setBackground(BACKGROUND_COLOR);
        btn.setForeground(Color.BLACK);
        btn.setBorder(new LineBorder(PRIMARY_BLUE, 2));
        
        btn.addActionListener(this);
        return btn;
    }

    // --- Generate Unique Meter Number (Logic preserved) ---
    private String generateUniqueMeter() {
        Random rand = new Random();
        String meter = "";
        boolean unique = false;
        // Simplified connection management for safety
        try {
            Connection conn = new DBConnection().getConnection();
            if(conn == null) return "DB_FAIL";
            
            while (!unique) {
                // Generates a 6-digit number
                long number = Math.abs(rand.nextLong() % 900000) + 100000;
                meter = String.valueOf(number);
                
                // Use PreparedStatement to check for existence
                PreparedStatement pst = conn.prepareStatement("SELECT meter_no FROM customer WHERE meter_no = ?");
                pst.setString(1, meter);
                
                try(ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        unique = true;
                    }
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            meter = "ERROR123"; // fallback
        }
        return meter;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnNext) {
            handleNextAction();
        } else if (ae.getSource() == btnCancel) {
            handleCancelAction();
        }
    }
    
    private void handleNextAction() {
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

        try (Connection conn = new DBConnection().getConnection()) {
            if (conn == null) return;
            
            // --- Insert into Customer ---
            String custQuery = "INSERT INTO customer (customer_name, meter_no, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstCust = conn.prepareStatement(custQuery);
            pstCust.setString(1, name);
            pstCust.setString(2, meter);
            pstCust.setString(3, address);
            pstCust.setString(4, city);
            pstCust.setString(5, state);
            pstCust.setString(6, email);
            pstCust.setString(7, phone);
            pstCust.executeUpdate();

            // --- Insert into Login (Username=Meter, Default Password=Name) ---
            String loginQuery = "INSERT INTO login (meter_no, username, password, userType, name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstLogin = conn.prepareStatement(loginQuery);
            pstLogin.setString(1, meter);
            pstLogin.setString(2, meter); 
            pstLogin.setString(3, name); 
            pstLogin.setString(4, "Customer");
            pstLogin.setString(5, name);
            pstLogin.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer added successfully!\nMeter No: " + meter + "\nDefault Password: " + name);

            setVisible(false);
            // CORRECTION: Pass the MainHomePage reference to the MeterInfo constructor
            new MeterInfo(meter, mainHome); 

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Meter number " + meter + " already exists. Please regenerate.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCancelAction() {
        setVisible(false);
        dispose();
        if (mainHome != null) {
            mainHome.setVisible(true); // Show existing main homepage
        }
    }

    public static void main(String[] args) {
        // For testing standalone, we pass null 
        new NewCustomer(null);
    }
}