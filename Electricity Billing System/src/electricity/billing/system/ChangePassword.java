package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;

/**
 * ChangePassword allows a customer to change their login password.
 * Updated to use fixed 1024x768 size, consistent styling, and PreparedStatement for security.
 */
public class ChangePassword extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 30);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);

    // --- Component Declarations ---
    private JPasswordField pfOldPassword, pfNewPassword, pfConfirmPassword;
    private JButton btnUpdate, btnCancel;
    private final String meterNumber;
    private MainHomePage mainHome; // Reference to the calling MainHomePage instance

    /**
     * Constructor for ChangePassword frame.
     * @param meterNumber The customer's meter number (used as username).
     * @param mainHome Reference to the calling MainHomePage instance (can be null for testing).
     */
    public ChangePassword(String meterNumber, MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        this.meterNumber = meterNumber;
        this.mainHome = mainHome;
        
        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setTitle("Change Password for User: " + meterNumber);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(null);

        // --- Panel for Centering Content ---
        JPanel p = new JPanel(null);
        p.setBackground(BACKGROUND_COLOR);
        p.setBounds((FRAME_WIDTH - 600) / 2, (FRAME_HEIGHT - 450) / 2, 600, 450); // Centered 600x450 panel
        p.setBorder(new LineBorder(PRIMARY_BLUE, 2, true));
        add(p);
        
        // --- Heading ---
        JLabel lblHeading = new JLabel("CHANGE ACCOUNT PASSWORD");
        lblHeading.setFont(HEADING_FONT);
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(50, 30, 500, 40);
        lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblHeading);

        int y_pos = 110;
        int y_inc = 60;
        int labelX = 50, fieldX = 250, width = 280;

        // --- 1. Old Password ---
        p.add(createLabel("Old Password:", labelX, y_pos));
        pfOldPassword = createPasswordField(fieldX, y_pos, width);
        p.add(pfOldPassword);
        y_pos += y_inc;

        // --- 2. New Password ---
        p.add(createLabel("New Password:", labelX, y_pos));
        pfNewPassword = createPasswordField(fieldX, y_pos, width);
        p.add(pfNewPassword);
        y_pos += y_inc;

        // --- 3. Confirm Password ---
        p.add(createLabel("Confirm New Password:", labelX, y_pos));
        pfConfirmPassword = createPasswordField(fieldX, y_pos, width);
        p.add(pfConfirmPassword);
        y_pos += y_inc;

        // --- Buttons ---
        btnUpdate = createButton("Update", 120, 350, 150, 40);
        // Override color for primary action (Blue BG, White Text)
        btnUpdate.setBackground(PRIMARY_BLUE);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBorder(new LineBorder(PRIMARY_BLUE, 2)); 
        p.add(btnUpdate);

        btnCancel = createButton("Cancel", 330, 350, 150, 40);
        p.add(btnCancel); // Uses mandated style from helper function

        setVisible(true);
    }
    
    // --- UI Helper Methods ---
    
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 25);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(new Color(51, 51, 51));
        return lbl;
    }
    
    private JPasswordField createPasswordField(int x, int y, int width) {
        JPasswordField pf = new JPasswordField();
        pf.setBounds(x, y, width, 30);
        pf.setFont(new Font("Tahoma", Font.PLAIN, 16));
        pf.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        return pf;
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnUpdate) {
            handleUpdateAction();
        } else if (ae.getSource() == btnCancel) {
            handleCancelAction();
        }
    }
    
    private void handleUpdateAction() {
        String oldPass = new String(pfOldPassword.getPassword());
        String newPass = new String(pfNewPassword.getPassword());
        String confirmPass = new String(pfConfirmPassword.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New Password and Confirm Password do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // --- Database Logic (Using PreparedStatement for Security) ---
        String selectQuery = "SELECT password FROM users WHERE username = ?";
        String updateQuery = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = new DBConnection().getConnection()) {
            
            // 1. Verify Old Password
            String currentPass = null;
            try (PreparedStatement pstSelect = conn.prepareStatement(selectQuery)) {
                pstSelect.setString(1, meterNumber);
                try (ResultSet rs = pstSelect.executeQuery()) {
                    if (rs.next()) {
                        currentPass = rs.getString("password");
                    }
                }
            }

            if (currentPass == null) {
                JOptionPane.showMessageDialog(this, "User account not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!currentPass.equals(oldPass)) {
                JOptionPane.showMessageDialog(this, "Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 2. Update the password
            try (PreparedStatement pstUpdate = conn.prepareStatement(updateQuery)) {
                pstUpdate.setString(1, newPass);
                pstUpdate.setString(2, meterNumber);
                
                int rowsAffected = pstUpdate.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Password changed successfully.");
                    setVisible(false);
                    dispose();
                    if (mainHome != null) {
                        mainHome.setVisible(true);
                    }
                } else {
                     JOptionPane.showMessageDialog(this, "Failed to update password. User not found or no change occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCancelAction() {
        setVisible(false);
        dispose();
        if (mainHome != null) {
            mainHome.setVisible(true); // Navigate back to the MainHomePage
        }
    }

    public static void main(String[] args) {
        //new ChangePassword("100123", null); // Example test with null for standalone
    }
}