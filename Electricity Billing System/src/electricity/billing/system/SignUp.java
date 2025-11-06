package electricity.billing.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.*;

/**
 * SignUp class handles the user registration flow.
 * Admin accounts auto-generate a unique meter_no.
 * Customer accounts must provide an existing meter_no.
 * * NOTE: Frame size is fixed at 1024x768.
 */
public class SignUp extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final int PANEL_WIDTH = 700; // Width of the central content panel
    private static final int PANEL_HEIGHT = 500; // Height of the central content panel

    // UI components
    private JTextField txtMeter, txtUsername, txtName;
    private JPasswordField txtPassword;
    private Choice accountType;
    private JButton createButton, backButton;
    private JLabel lblMeter;

    // UI constants
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 18); // Slightly larger title
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 15);
    private static final Font FIELD_FONT = new Font("Tahoma", Font.PLAIN, 15);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 15);
    
    // Layout constants for panel positioning
    private static final int labelX = 50; 
    private static final int fieldX = 220;
    private static final int elementHeight = 30; // Slightly taller elements for better spacing
    private static final int ySpacing = 50; // Increased vertical spacing

    public SignUp() {
        super("Electricity Billing Management: Create Account");
        
        // --- Frame Settings (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // --- Central Panel ---
        JPanel panel = new JPanel();
        
        // Center the panel within the 1024x768 frame
        panel.setBounds(
            (FRAME_WIDTH - PANEL_WIDTH) / 2, 
            (FRAME_HEIGHT - PANEL_HEIGHT) / 2, 
            PANEL_WIDTH, 
            PANEL_HEIGHT
        );
        
        panel.setBorder(new TitledBorder(new LineBorder(new Color(173, 216, 236), 2),
                "Create Account", TitledBorder.LEADING, TitledBorder.TOP, TITLE_FONT, PRIMARY_BLUE));
        panel.setBackground(new Color(245, 245, 245)); // Light gray background for contrast
        panel.setLayout(null);
        add(panel);

        int currentY = 50;

        // 1. Account Type
        JLabel heading = createLabel("Create Account As", labelX, currentY);
        panel.add(heading);

        accountType = new Choice();
        accountType.add("Admin");
        accountType.add("Customer");
        accountType.setBounds(fieldX, currentY, 200, elementHeight);
        accountType.setFont(FIELD_FONT);
        panel.add(accountType);

        currentY += ySpacing;

        // 2. Meter Number (only for Customer)
        lblMeter = createLabel("Meter Number", labelX, currentY);
        lblMeter.setVisible(false);
        panel.add(lblMeter);

        txtMeter = createTextField(fieldX, currentY);
        txtMeter.setVisible(false);
        panel.add(txtMeter);

        currentY += ySpacing;

        // 3. Username
        JLabel lblUsername = createLabel("Username", labelX, currentY);
        panel.add(lblUsername);

        txtUsername = createTextField(fieldX, currentY);
        panel.add(txtUsername);

        currentY += ySpacing;

        // 4. Name
        JLabel lblName = createLabel("Name", labelX, currentY);
        panel.add(lblName);

        txtName = createTextField(fieldX, currentY);
        panel.add(txtName);

        currentY += ySpacing;

        // 5. Password
        JLabel lblPassword = createLabel("Password", labelX, currentY);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(fieldX, currentY, 200, elementHeight);
        txtPassword.setFont(FIELD_FONT);
        panel.add(txtPassword);

        // --- Buttons ---
        currentY += ySpacing + 20;
        int buttonWidth = 140;
        int buttonXStart = labelX + 50;

        createButton = createButton("CREATE", buttonXStart, currentY, buttonWidth, elementHeight + 5, true);
        panel.add(createButton);

        backButton = createButton("BACK", buttonXStart + buttonWidth + 40, currentY, buttonWidth, elementHeight + 5, false);
        panel.add(backButton);

        // Account type logic
        accountType.addItemListener(ae -> {
            if (accountType.getSelectedItem().equals("Customer")) {
                lblMeter.setVisible(true);
                txtMeter.setVisible(true);
            } else {
                lblMeter.setVisible(false);
                txtMeter.setVisible(false);
            }
        });

        // Signup image (Right Side of Panel)
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/signupImage.png"));
            // Scale the image to fit the panel without being too large
            Image i2 = i1.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(450, 100, 250, 250);
            panel.add(image);
        } catch (Exception e) {
            System.err.println("Signup image not found: " + e.getMessage());
        }

        setVisible(true);
    }
    
    // --- Helper Methods for UI Consistency ---
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 160, elementHeight);
        lbl.setForeground(Color.DARK_GRAY);
        lbl.setFont(LABEL_FONT);
        return lbl;
    }
    
    private JTextField createTextField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 200, elementHeight);
        txt.setFont(FIELD_FONT);
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return txt;
    }
    
    private JButton createButton(String text, int x, int y, int width, int height, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, height);
        btn.setFont(BUTTON_FONT);
        btn.setFocusPainted(false);

        if (isPrimary) {
            btn.setBackground(PRIMARY_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder());
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(PRIMARY_BLUE);
            btn.setBorder(new LineBorder(PRIMARY_BLUE, 1));
        }
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == createButton) {
            handleSignUp();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
            new Login();
        }
    }

    private void handleSignUp() {
        String type = accountType.getSelectedItem();
        String username = txtUsername.getText().trim();
        String name = txtName.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String meter = txtMeter.getText().trim();

        // Validation
        if (username.isEmpty() || name.isEmpty() || password.isEmpty() ||
                (type.equals("Customer") && meter.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = new DBConnection().getConnection()) {
            if (conn == null) return; // DB connection failed, error already shown by DBConnection
            
            PreparedStatement pst;

            if (type.equals("Admin")) {
                // Auto-generate meter for Admin
                meter = "ADMIN_" + System.currentTimeMillis();
                
                // Check for duplicate username
                if (isUsernameTaken(conn, username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                pst = conn.prepareStatement("INSERT INTO login (meter_no, username, password, userType, name) VALUES (?, ?, ?, ?, ?)");
                pst.setString(1, meter);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.setString(4, type);
                pst.setString(5, name);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Admin Account created successfully! Meter No: " + meter);

            } else { // Customer
                // 1. Check if the meter_no exists and is NOT yet assigned a username
                pst = conn.prepareStatement("SELECT username FROM login WHERE meter_no = ?");
                pst.setString(1, meter);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String existingUsername = rs.getString("username");
                        if (existingUsername != null && !existingUsername.isEmpty()) {
                             JOptionPane.showMessageDialog(this, "Meter Number is already registered!", "Error", JOptionPane.ERROR_MESSAGE);
                             return;
                        }
                    } else {
                        // Meter number does not exist in Login table (should be seeded from Admin entry)
                        JOptionPane.showMessageDialog(this, "Meter number does not exist! Contact Admin.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                // 2. Check for duplicate username
                if (isUsernameTaken(conn, username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 3. Update login entry for the meter
                pst = conn.prepareStatement("UPDATE login SET username = ?, password = ?, name = ?, userType = ? WHERE meter_no = ?");
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, name);
                pst.setString(4, type);
                pst.setString(5, meter);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Customer Login created successfully for Meter No: " + meter);
            }

            setVisible(false);
            dispose();
            new Login();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating account. Check DB connection or duplicate values.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper function to prevent duplicate usernames
    private boolean isUsernameTaken(Connection conn, String username) throws Exception {
        String checkQuery = "SELECT 1 FROM login WHERE username = ?";
        try (PreparedStatement checkPst = conn.prepareStatement(checkQuery)) {
            checkPst.setString(1, username);
            try (ResultSet rs = checkPst.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}