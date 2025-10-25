package electricity.billing.system;

import java.awt.Color;
import java.awt.Choice;
import java.awt.Image;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Arrays; // Needed for clearing password array

/**
 * The Login class provides the graphical user interface for user authentication.
 * It enforces strict UI/UX standards and utilizes secure
 * database interaction practices (PreparedStatement) to prevent SQL Injection.
 *
 * @author danny
 */
public class Login extends JFrame implements ActionListener {

    // --- UI Component Declarations (Strict Naming Convention) ---
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Choice choiceUserType;
    private JButton btnLogin, btnCancel, btnSignup;

    // --- Aesthetic Constants ---
    private static final Color PRIMARY_ACCENT_BLUE = new Color(0, 122, 255);
    // Changed Font to Tahoma for consistency with SignUp.java
    private static final Font STANDARD_FONT = new Font("Tahoma", Font.PLAIN, 14);
    private static final int COMPONENT_HEIGHT = 25;
    private static final int BUTTON_HEIGHT = 35;
    private static final int PADDING_VERTICAL = 15;

    Login() {
        super("Login Page");
        // Apply mandated background and layout
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // --- 1. Username Label and Text Field ---
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(300, PADDING_VERTICAL, 100, 20);
        lblUsername.setFont(STANDARD_FONT);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(400, PADDING_VERTICAL, 180, COMPONENT_HEIGHT);
        txtUsername.setFont(STANDARD_FONT);
        add(txtUsername);

        // --- 2. Password Label and Password Field ---
        JLabel lblPassword = new JLabel("Password");
        // Vertical spacing adjustment for clean layout
        lblPassword.setBounds(300, PADDING_VERTICAL + COMPONENT_HEIGHT + 10, 100, 20);
        lblPassword.setFont(STANDARD_FONT);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(400, PADDING_VERTICAL + COMPONENT_HEIGHT + 10, 180, COMPONENT_HEIGHT);
        txtPassword.setFont(STANDARD_FONT);
        add(txtPassword);

        // --- 3. User Type Label and Choice (Dropdown) ---
        JLabel lblUserType = new JLabel("Login As");
        // Vertical spacing adjustment for clean layout
        lblUserType.setBounds(300, PADDING_VERTICAL + (COMPONENT_HEIGHT * 2) + 20, 100, 20);
        lblUserType.setFont(STANDARD_FONT);
        add(lblUserType);

        choiceUserType = new Choice();
        choiceUserType.add("Admin");
        choiceUserType.add("Customer");
        choiceUserType.setBounds(400, PADDING_VERTICAL + (COMPONENT_HEIGHT * 2) + 20, 180, COMPONENT_HEIGHT);
        choiceUserType.setFont(STANDARD_FONT);
        add(choiceUserType);

        // Calculate the Y position for the first row of buttons
        int buttonY = PADDING_VERTICAL + (COMPONENT_HEIGHT * 3) + 40;

        // --- 4. Login Button (Primary Action) ---
        ImageIcon loginIcon;
        try {
            // Renamed local image variables to follow CamelCasing
            ImageIcon loginImageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/login.png"));
            Image scaledLoginImage = loginImageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            loginIcon = new ImageIcon(scaledLoginImage);
        } catch (Exception e) {
            System.err.println("Login icon not found. Falling back to text button.");
            loginIcon = null;
        }

        btnLogin = new JButton("Login", loginIcon);
        btnLogin.setBounds(300, buttonY, 130, BUTTON_HEIGHT);
        btnLogin.addActionListener(this);

        // Apply PRIMARY_ACCENT_BLUE style for high visibility and primary action
        btnLogin.setBackground(PRIMARY_ACCENT_BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(STANDARD_FONT);
        add(btnLogin);

        // --- 5. Cancel Button (Secondary Action) ---
        ImageIcon cancelIcon;
        try {
            ImageIcon cancelImageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/cancel.jpg"));
            Image scaledCancelImage = cancelImageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            cancelIcon = new ImageIcon(scaledCancelImage);
        } catch (Exception e) {
            System.err.println("Cancel icon not found. Falling back to text button.");
            cancelIcon = null;
        }

        btnCancel = new JButton("Cancel", cancelIcon);
        btnCancel.setBounds(450, buttonY, 130, BUTTON_HEIGHT);
        btnCancel.addActionListener(this);

        // Apply secondary style: White BG, Blue FG, high contrast
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(PRIMARY_ACCENT_BLUE);
        btnCancel.setFont(STANDARD_FONT);
        // Note: For Swing, border must be managed manually for the full effect.
        add(btnCancel);

        // --- 6. Sign Up Button (Secondary Action) ---
        ImageIcon signupIcon;
        try {
            ImageIcon signupImageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/signup.png"));
            Image scaledSignupImage = signupImageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            signupIcon = new ImageIcon(scaledSignupImage);
        } catch (Exception e) {
            System.err.println("Signup icon not found. Falling back to text button.");
            signupIcon = null;
        }

        // Positioned centrally below the login/cancel buttons
        btnSignup = new JButton("Signup", signupIcon);
        btnSignup.setBounds(375, buttonY + BUTTON_HEIGHT + 10, 130, BUTTON_HEIGHT);
        btnSignup.addActionListener(this);

        // Apply secondary style
        btnSignup.setBackground(Color.WHITE);
        btnSignup.setForeground(PRIMARY_ACCENT_BLUE);
        btnSignup.setFont(STANDARD_FONT);
        add(btnSignup);

        // --- 7. Frame Logo Image ---
        try {
            // Renamed local image variables
            ImageIcon logoImageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/MainFrameLogo.jpg"));
            Image scaledLogoImage = logoImageIcon.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT);
            ImageIcon finalLogoIcon = new ImageIcon(scaledLogoImage);

            // Renamed JLabel to follow convention
            JLabel lblLogoImage = new JLabel(finalLogoIcon);
            lblLogoImage.setBounds(0, 0, 250, 300);
            add(lblLogoImage);
        } catch (Exception e) {
            System.err.println("MainFrameLogo image not found: " + e.getMessage());
        }

        // Frame Settings
        setSize(640, 380); // Adjusted size slightly to accommodate larger buttons
        setLocationRelativeTo(null); // Center frame dynamically
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Handles action events from the buttons (Login, Cancel, Signup).
     * @param ae The action event triggered by a component.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnLogin) {
            handleLogin();
        } else if (ae.getSource() == btnCancel) {
            // Step 1: Hide the current frame
            setVisible(false);
            dispose(); // Release resources
            // Step 2: Exit the application
            System.exit(0);
        } else if (ae.getSource() == btnSignup) {
            // Navigate to the SignUp frame
            setVisible(false);
            dispose(); // Release resources
            new SignUp();
        }
    }

    /**
     * Executes the login logic, including input validation and secure database query.
     * Uses PreparedStatement to prevent SQL Injection (BVA/Security Fix).
     */
    private void handleLogin() {
        // --- Input Fetching and Naming ---
        String inputUsername = txtUsername.getText();
        char[] passwordChars = txtPassword.getPassword();
        String inputPassword = new String(passwordChars);
        String selectedUserType = choiceUserType.getSelectedItem();

        // BVA: Check for empty fields before connecting to the database
        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Username and Password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection dbConn = null;

        try {
            // 1. Establish DB connection
            DBConnection conn = new DBConnection();
            // CORRECTED: Using the public getter method 'getConnection()' from DBConnection.
            dbConn = conn.getConnection();

            // Handle null connection (DB failure)
            if (dbConn == null) {
                return; // Error message already shown in DBConnection constructor
            }

            // --- CRITICAL SECURITY FIX: Use PreparedStatement ---
            String query = "SELECT * FROM login WHERE username = ? AND password = ? AND userType = ?";

            // Use PreparedStatement for secure query execution
            // try-with-resources ensures pst is closed, even on exception
            try (PreparedStatement pst = dbConn.prepareStatement(query)) {

                // Bind user input securely to the placeholders
                pst.setString(1, inputUsername);
                pst.setString(2, inputPassword);
                pst.setString(3, selectedUserType);

                // Execute the query
                // try-with-resources ensures rs is closed, even on exception
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // Login Successful: Navigate to MainHomePage
                        String meterNumber = rs.getString("meter_no"); // Assuming a meter number column is present
                        setVisible(false);
                        dispose();
                        // FIX: Pass the userType and meterNumber to the MainHomePage constructor.
                        new MainHomePage(selectedUserType, meterNumber);
                    } else {
                        // Login Failed: notify user and clear fields
                        JOptionPane.showMessageDialog(this, "Invalid Username or Password for the selected user type.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        txtPassword.setText("");
                        txtUsername.setText("");
                    }
                }
            }

            // SECURITY BEST PRACTICE: Zero out the char array containing the password
            // Note: This is a robust security measure, though not strictly required for functionality.
            Arrays.fill(passwordChars, ' ');

        } catch (Exception e) {
            // Catch connection or SQL errors
            JOptionPane.showMessageDialog(this, "A connection error occurred. Please try again later.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Connection cleanup
            try {
                if (dbConn != null) {
                    dbConn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
