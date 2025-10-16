/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.billing.system;

import java.awt.Color;
import java.awt.Choice;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

/**
 *
 * @author danny
 */
public class Login extends JFrame{
    
    // Declare components globally
    JTextField usernameTextField;
    JPasswordField passwordField;
    Choice loginAsChoice;
    JButton loginButton, cancelButton, signupButton;

    Login(){
        
        super("Login Page");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        // --- 1. Username Label and Text Field ---
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(300, 20, 100, 20);
        add(lblUsername);
        
        usernameTextField = new JTextField();
        usernameTextField.setBounds(400, 20, 150, 20);
        add(usernameTextField);
        
        // --- 2. Password Label and Password Field ---
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(300, 60, 100, 20);
        add(lblPassword);
        
        passwordField = new JPasswordField(); 
        passwordField.setBounds(400, 60, 150, 20);
        add(passwordField);
        
        // --- 3. Login As Label and Choice (Dropdown) ---
        JLabel lblLoginAs = new JLabel("Login As");
        lblLoginAs.setBounds(300, 100, 100, 20);
        add(lblLoginAs);
        
        loginAsChoice = new Choice();
        loginAsChoice.add("Admin");
        loginAsChoice.add("Customer");
        loginAsChoice.setBounds(400, 100, 150, 20);
        add(loginAsChoice);
        
        // --- 4. Login Button (Submit) ---
        // FIX: Scaled image (i2) used to create a *new* ImageIcon for the button.
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/login.png"));
            Image i2 = i1.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            ImageIcon i_final = new ImageIcon(i2);
            loginButton = new JButton("Login", i_final);
        } catch (Exception e) {
             loginButton = new JButton("Login"); // Fallback
        }
        loginButton.setBounds(300, 160, 100, 20);
        add(loginButton);
        
        // --- 5. Cancel Button ---
        // FIX: Scaled image (i4) used to create a *new* ImageIcon for the button.
        try {
            ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icon/cancel.jpg"));
            Image i4 = i3.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            ImageIcon i_final = new ImageIcon(i4);
            cancelButton = new JButton("Cancel", i_final);
        } catch (Exception e) {
            cancelButton = new JButton("Cancel"); // Fallback
        }
        cancelButton.setBounds(450, 160, 100, 20);
        add(cancelButton);
        
        // --- 6. Sign Up Button ---
        // FIX: Scaled image (i6) used to create a *new* ImageIcon for the button.
        try {
            ImageIcon i5 = new ImageIcon(ClassLoader.getSystemResource("icon/signup.png"));
            Image i6 = i5.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            ImageIcon i_final = new ImageIcon(i6);
            signupButton = new JButton("Signup", i_final);
        } catch (Exception e) {
             signupButton = new JButton("Signup"); // Fallback
        }
        signupButton.setBounds(375, 200, 100, 20);
        add(signupButton);
        
        // --- 7. Frame Logo Image ---
        try {
            ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icon/MainFrameLogo.png"));
            Image i8 = i7.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            ImageIcon i9 = new ImageIcon(i8);
            JLabel image = new JLabel(i9);
            image.setBounds(0, 0, 250, 250);
            add(image);
        } catch (Exception e) {
            // Placeholder for logo failure
        }
        
        // Frame Settings
        setSize(640, 350);
        setLocation(400, 200);
        setVisible(true);
    }

    public static void main(String[] args){
        new Login();
    }
}