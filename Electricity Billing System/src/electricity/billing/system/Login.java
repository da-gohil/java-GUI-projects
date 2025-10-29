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

    private static final Color PRIMARY_ACCENT_BLUE = new Color(0, 122, 255);
    private static final Font STANDARD_FONT = new Font("Tahoma", Font.PLAIN, 14);
    private static final int COMPONENT_HEIGHT = 25;
    private static final int BUTTON_HEIGHT = 35;
    private static final int PADDING_VERTICAL = 20; // Slightly increased for readability

    public Login() {
        super("Welcome to Electricity Billing Management: Login Page");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // --- Labels and Inputs ---
        addLabel("Username", 300, PADDING_VERTICAL, 100, 20);
        txtUsername = addTextField(400, PADDING_VERTICAL, 180, COMPONENT_HEIGHT);

        addLabel("Password", 300, PADDING_VERTICAL + COMPONENT_HEIGHT + 10, 100, 20);
        txtPassword = addPasswordField(400, PADDING_VERTICAL + COMPONENT_HEIGHT + 10, 180, COMPONENT_HEIGHT);

        addLabel("Login As", 300, PADDING_VERTICAL + (COMPONENT_HEIGHT * 2) + 20, 100, 20);
        choiceUserType = new Choice();
        choiceUserType.add("Admin");
        choiceUserType.add("Customer");
        choiceUserType.setBounds(400, PADDING_VERTICAL + (COMPONENT_HEIGHT * 2) + 20, 180, COMPONENT_HEIGHT);
        choiceUserType.setFont(STANDARD_FONT);
        add(choiceUserType);

        // --- Buttons ---
        int buttonY = PADDING_VERTICAL + (COMPONENT_HEIGHT * 3) + 40;
        btnLogin = createButton("Login", "icon/login.png", 300, buttonY, 130, BUTTON_HEIGHT, false);   // Primary
        btnSignup = createButton("Signup", "icon/signup.png", 450, buttonY, 130, BUTTON_HEIGHT, false); // Secondary
        btnCancel = createButton("Exit Application", "icon/cancel.jpg", 375, buttonY + BUTTON_HEIGHT + 10, 130, BUTTON_HEIGHT, false); // Secondary
        // --- Frame Logo ---
        
        try {
            ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/LoginPageLogo.jpg"));
            Image scaled = logoIcon.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT);
            JLabel lblLogo = new JLabel(new ImageIcon(scaled));
            lblLogo.setBounds(0, 0, 250, 300);
            add(lblLogo);
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }

        setSize(640, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

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
        add(txt);
        return txt;
    }

    private JPasswordField addPasswordField(int x, int y, int width, int height) {
        JPasswordField txt = new JPasswordField();
        txt.setBounds(x, y, width, height);
        txt.setFont(STANDARD_FONT);
        add(txt);
        return txt;
    }

    private JButton createButton(String text, String iconPath, int x, int y, int width, int height, boolean isPrimary) {
        JButton btn;
        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            btn = new JButton(text, new ImageIcon(scaled));
        } catch (Exception e) {
            System.err.println(iconPath + " not found. Using text button.");
            btn = new JButton(text);
        }

        btn.setBounds(x, y, width, height);
        btn.setFont(STANDARD_FONT);

        if (isPrimary) {
            btn.setBackground(PRIMARY_ACCENT_BLUE); // Blue background
            btn.setForeground(Color.WHITE);         // White text
        } else {
            btn.setBackground(Color.WHITE);         // White background
            btn.setForeground(PRIMARY_ACCENT_BLUE); // Blue text
            btn.setBorder(BorderFactory.createLineBorder(PRIMARY_ACCENT_BLUE, 2)); // Blue border
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
                        new MainHomePage(user, meter);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Username or Password for the selected user type.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        txtPassword.setText("");
                        txtUsername.setText("");
                    }
                }
            }
            Arrays.fill(passwordChars, ' ');
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error. Check connection.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
