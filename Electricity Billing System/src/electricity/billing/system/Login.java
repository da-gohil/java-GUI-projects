package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;

/**
 * Login screen for the Electricity Billing System. Provides secure
 * authentication and redirects to MainHomePage.
 */
public class Login extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Choice choiceUserType;
    private JButton btnLogin, btnCancel, btnSignup;

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    
    // Assumed original size of the logo image for non-resizing mandate
    private static final int LOGO_WIDTH = 350; 
    private static final int LOGO_HEIGHT = 500; 

    // Calculate X-start for the centered content block (to the right of the logo)
    private static final int CONTENT_PANEL_START_X = LOGO_WIDTH + 100; // 350 (logo) + 100 (gap) = 450
    private static final int LOGIN_FORM_WIDTH = 450; // Total width for the input group
    
    private static final Color PRIMARY_ACCENT_BLUE = new Color(0, 122, 255);
    private static final Font STANDARD_FONT = new Font("Tahoma", Font.BOLD, 14);
    private static final int COMPONENT_HEIGHT = 30;
    private static final int BUTTON_HEIGHT = 40;
    private static final int PADDING_VERTICAL = 150; // Start position from the top

    public Login() {
        super("Welcome to Electricity Billing Management: Login Page");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null); 

        // --- Frame Settings (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false); // Disable resizing to maintain 1024x768
        setLocationRelativeTo(null); // Center frame on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- Frame Logo (Fixed Size, Left Side) ---
        try {
            ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/LoginPageLogo.jpg"));
            // DO NOT RESIZE: Use the image at its original size (350x500 assumption)
            JLabel lblLogo = new JLabel(logoIcon);
            lblLogo.setBounds(0, (FRAME_HEIGHT - LOGO_HEIGHT) / 2, LOGO_WIDTH, LOGO_HEIGHT); // Center vertically
            add(lblLogo);
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }

        // --- Centered Labels and Inputs (Right Side) ---
        int yOffset = PADDING_VERTICAL;
        int labelX = CONTENT_PANEL_START_X;
        int fieldX = labelX + 110;
        int inputWidth = 250;

        addLabel("Username", labelX, yOffset, 100, 20);
        txtUsername = addTextField(fieldX, yOffset, inputWidth, COMPONENT_HEIGHT);
        yOffset += COMPONENT_HEIGHT + 20;

        addLabel("Password", labelX, yOffset, 100, 20);
        txtPassword = addPasswordField(fieldX, yOffset, inputWidth, COMPONENT_HEIGHT);
        yOffset += COMPONENT_HEIGHT + 20;

        addLabel("Login As", labelX, yOffset, 100, 20);
        choiceUserType = new Choice();
        choiceUserType.add("Admin");
        choiceUserType.add("Customer");
        choiceUserType.setBounds(fieldX, yOffset, inputWidth, COMPONENT_HEIGHT);
        choiceUserType.setFont(STANDARD_FONT);
        add(choiceUserType);
        yOffset += COMPONENT_HEIGHT + 50;

        // --- Buttons (Centered within the right panel) ---
        int buttonGroupStart = CONTENT_PANEL_START_X;
        int buttonWidth = 150;
        int spacing = 50;
        
        btnLogin = createButton("Login", "icon/login.png", buttonGroupStart, yOffset, buttonWidth, BUTTON_HEIGHT, true);   
        btnSignup = createButton("Signup", "icon/signup.png", buttonGroupStart + buttonWidth + spacing, yOffset, buttonWidth, BUTTON_HEIGHT, false); 
        yOffset += BUTTON_HEIGHT + 20;
        
        btnCancel = createButton("Exit Application", "icon/cancel.jpg", buttonGroupStart, yOffset, buttonWidth * 2 + spacing, BUTTON_HEIGHT, false);
        
        setVisible(true);
    }
    
    // Helper methods (addLabel, addTextField, createButton) remain the same...
    private JLabel addLabel(String text, int x, int y, int width, int height) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, width, height);
        lbl.setFont(STANDARD_FONT);
        add(lbl);
        return lbl;
    }

    private JTextField addTextField(int x, int y, int width, int height) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, width, height);
        txt.setFont(STANDARD_FONT);
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(txt);
        return txt;
    }

    private JPasswordField addPasswordField(int x, int y, int width, int height) {
        JPasswordField txt = new JPasswordField();
        txt.setBounds(x, y, width, height);
        txt.setFont(STANDARD_FONT);
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(txt);
        return txt;
    }

    private JButton createButton(String text, String iconPath, int x, int y, int width, int height, boolean isPrimary) {
        JButton btn;
        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btn = new JButton(text, new ImageIcon(scaled));
        } catch (Exception e) {
            btn = new JButton(text);
        }

        btn.setBounds(x, y, width, height);
        btn.setFont(STANDARD_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            btn.setBackground(PRIMARY_ACCENT_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder());
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(PRIMARY_ACCENT_BLUE);
            btn.setBorder(BorderFactory.createLineBorder(PRIMARY_ACCENT_BLUE, 2));
        }

        btn.addActionListener(this);
        add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnLogin) {
            handleLogin();
        } else if (ae.getSource() == btnCancel) {
            dispose();
            System.exit(0);
        } else if (ae.getSource() == btnSignup) {
            dispose();
            new SignUp(); 
        }
    }

    private void handleLogin() {
        String susername = txtUsername.getText();
        char[] passwordChars = txtPassword.getPassword();
        String spassword = new String(passwordChars);
        String user = choiceUserType.getSelectedItem();

        if (susername.isEmpty() || spassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Username and Password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection dbConn = new DBConnection().getConnection()) {
            if (dbConn == null) {
                 // DBConnection constructor already shows an error dialog.
                 return;
            }
            String query = "SELECT * FROM login WHERE username = ? AND password = ? AND userType = ?";
            try (PreparedStatement pst = dbConn.prepareStatement(query)) {
                pst.setString(1, susername);
                pst.setString(2, spassword);
                pst.setString(3, user);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String meter = rs.getString("meter_no");
                        dispose();
                        // SUCCESS: Transition to the main application page
                        new MainHomePage(user, meter); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Username or Password for the selected user type.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        txtPassword.setText("");
                        txtUsername.setText("");
                    }
                }
            }
            // Clear the password array for security
            Arrays.fill(passwordChars, ' ');
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An application error occurred during login.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}