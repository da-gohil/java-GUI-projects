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
 */
public class SignUp extends JFrame implements ActionListener {

    // UI components
    private JTextField txtMeter, txtUsername, txtName;
    private JPasswordField txtPassword;
    private Choice accountType;
    private JButton createButton, backButton;
    private JLabel lblMeter;

    // UI constants
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Tahoma", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 14);

    public SignUp() {
        super("Create Account");
        setBounds(400, 150, 700, 400);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 650, 300);
        panel.setBorder(new TitledBorder(new LineBorder(new Color(173, 216, 236), 2),
                "Create Account", TitledBorder.LEADING, TitledBorder.TOP, TITLE_FONT, PRIMARY_BLUE));
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        add(panel);

        int labelX = 100, fieldX = 260, elementHeight = 20, ySpacing = 40, currentY = 50;

        // Account Type
        JLabel heading = new JLabel("Create Account As");
        heading.setBounds(labelX, currentY, 140, elementHeight);
        heading.setForeground(Color.DARK_GRAY);
        heading.setFont(LABEL_FONT);
        panel.add(heading);

        accountType = new Choice();
        accountType.add("Admin");
        accountType.add("Customer");
        accountType.setBounds(fieldX, currentY, 150, elementHeight);
        accountType.setFont(FIELD_FONT);
        panel.add(accountType);

        currentY += ySpacing;

        // Meter Number (only for Customer)
        lblMeter = new JLabel("Meter Number");
        lblMeter.setBounds(labelX, currentY, 140, elementHeight);
        lblMeter.setForeground(Color.DARK_GRAY);
        lblMeter.setFont(LABEL_FONT);
        lblMeter.setVisible(false);
        panel.add(lblMeter);

        txtMeter = new JTextField();
        txtMeter.setBounds(fieldX, currentY, 150, elementHeight);
        txtMeter.setFont(FIELD_FONT);
        txtMeter.setVisible(false);
        panel.add(txtMeter);

        currentY += ySpacing;

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(labelX, currentY, 140, elementHeight);
        lblUsername.setForeground(Color.DARK_GRAY);
        lblUsername.setFont(LABEL_FONT);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(fieldX, currentY, 150, elementHeight);
        txtUsername.setFont(FIELD_FONT);
        panel.add(txtUsername);

        currentY += ySpacing;

        // Name
        JLabel lblName = new JLabel("Name");
        lblName.setBounds(labelX, currentY, 140, elementHeight);
        lblName.setForeground(Color.DARK_GRAY);
        lblName.setFont(LABEL_FONT);
        panel.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(fieldX, currentY, 150, elementHeight);
        txtName.setFont(FIELD_FONT);
        panel.add(txtName);

        currentY += ySpacing;

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(labelX, currentY, 140, elementHeight);
        lblPassword.setForeground(Color.DARK_GRAY);
        lblPassword.setFont(LABEL_FONT);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(fieldX, currentY, 150, elementHeight);
        txtPassword.setFont(FIELD_FONT);
        panel.add(txtPassword);

        // Buttons
        currentY += ySpacing + 10;

        createButton = new JButton("CREATE");
        createButton.setBackground(PRIMARY_BLUE);
        createButton.setForeground(Color.WHITE);
        createButton.setFont(BUTTON_FONT);
        createButton.setBounds(140, currentY, 120, 28);
        createButton.addActionListener(this);
        panel.add(createButton);

        backButton = new JButton("BACK");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(PRIMARY_BLUE);
        backButton.setFont(BUTTON_FONT);
        backButton.setBounds(280, currentY, 120, 28);
        backButton.addActionListener(this);
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

        // Signup image
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/signupImage.png"));
            Image i2 = i1.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(410, 30, 250, 250);
            panel.add(image);
        } catch (Exception e) {
            System.err.println("Signup image not found: " + e.getMessage());
        }

        setVisible(true);
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
            PreparedStatement pst;

            if (type.equals("Admin")) {
                // Auto-generate meter for Admin
                meter = "ADMIN_" + System.currentTimeMillis();
                pst = conn.prepareStatement("INSERT INTO login (meter_no, username, password, userType, name) VALUES (?, ?, ?, ?, ?)");
                pst.setString(1, meter);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.setString(4, type);
                pst.setString(5, name);
            } else {
                // Check if meter exists for Customer
                pst = conn.prepareStatement("SELECT * FROM login WHERE meter_no = ?");
                pst.setString(1, meter);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Meter number does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Update username, password, and name
                pst = conn.prepareStatement("UPDATE login SET username = ?, password = ?, name = ?, userType = ? WHERE meter_no = ?");
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, name);
                pst.setString(4, type);
                pst.setString(5, meter);
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            setVisible(false);
            dispose();
            new Login();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating account. Check DB connection or duplicate values.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
