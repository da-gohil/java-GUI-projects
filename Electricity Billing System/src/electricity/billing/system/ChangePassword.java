package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * ChangePassword allows a customer to change their login password.
 */
public class ChangePassword extends JFrame implements ActionListener {

    private JPasswordField pfOldPassword, pfNewPassword, pfConfirmPassword;
    private JButton btnUpdate, btnCancel;
    private String meterNumber;

    // Colors consistent with project style
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color LIGHT_BG = new Color(230, 240, 250);

    public ChangePassword(String meterNumber) {
        this.meterNumber = meterNumber;
        setTitle("Change Password");
        setBounds(400, 150, 500, 350);
        getContentPane().setBackground(LIGHT_BG);
        setLayout(null);

        // --- Heading ---
        JLabel lblHeading = new JLabel("Change Password");
        lblHeading.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(130, 20, 300, 30);
        add(lblHeading);

        // --- Old Password ---
        JLabel lblOld = new JLabel("Old Password:");
        lblOld.setBounds(50, 80, 150, 25);
        add(lblOld);

        pfOldPassword = new JPasswordField();
        pfOldPassword.setBounds(200, 80, 200, 25);
        add(pfOldPassword);

        // --- New Password ---
        JLabel lblNew = new JLabel("New Password:");
        lblNew.setBounds(50, 130, 150, 25);
        add(lblNew);

        pfNewPassword = new JPasswordField();
        pfNewPassword.setBounds(200, 130, 200, 25);
        add(pfNewPassword);

        // --- Confirm Password ---
        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setBounds(50, 180, 150, 25);
        add(lblConfirm);

        pfConfirmPassword = new JPasswordField();
        pfConfirmPassword.setBounds(200, 180, 200, 25);
        add(pfConfirmPassword);

        // --- Buttons ---
        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(100, 250, 120, 30);
        btnUpdate.setBackground(PRIMARY_BLUE);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(260, 250, 120, 30);
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(PRIMARY_BLUE);
        btnCancel.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE));
        btnCancel.addActionListener(this);
        add(btnCancel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnUpdate) {
            String oldPass = new String(pfOldPassword.getPassword());
            String newPass = new String(pfNewPassword.getPassword());
            String confirmPass = new String(pfConfirmPassword.getPassword());

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "New Password and Confirm Password do not match.");
                return;
            }

            try {
                DBConnection c = new DBConnection();
                ResultSet rs = c.stmt.executeQuery("SELECT password FROM users WHERE username = '"+meterNumber+"'");
                
                if (rs.next()) {
                    String currentPass = rs.getString("password");
                    if (!currentPass.equals(oldPass)) {
                        JOptionPane.showMessageDialog(this, "Old password is incorrect.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.");
                    return;
                }

                // Update the password
                String updateQuery = "UPDATE users SET password = '"+newPass+"' WHERE username = '"+meterNumber+"'";
                c.stmt.executeUpdate(updateQuery);

                JOptionPane.showMessageDialog(this, "Password changed successfully.");
                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error changing password: " + e.getMessage());
            }

        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new ChangePassword("100123"); // Example meter number
    }
}
