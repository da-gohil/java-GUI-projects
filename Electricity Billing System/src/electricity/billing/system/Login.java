/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.billing.system;

import java.awt.Color;
import java.awt.Choice;
import java.awt.Image;
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
import java.net.URL;

/**
 *
 * @author danny
 */

public class Login extends JFrame implements ActionListener{
    
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
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/login.png"));
            Image i2 = i1.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            ImageIcon i_final1 = new ImageIcon(i2);
            loginButton = new JButton("Login", i_final1);
        } catch (Exception e) {
             loginButton = new JButton("Login"); // Fallback
        }
        loginButton.setBounds(300, 160, 100, 20);
        loginButton.addActionListener(this);
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(Color.BLACK);
        add(loginButton);
        
        // --- 5. Cancel Button ---
        try {
            ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icon/cancel.jpg"));
            Image i4 = i3.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            ImageIcon i_final2 = new ImageIcon(i4);
            cancelButton = new JButton("Cancel", i_final2);
        } catch (Exception e) {
            cancelButton = new JButton("Cancel"); 
        }
        cancelButton.setBounds(450, 160, 100, 20);
        cancelButton.addActionListener(this);
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.BLACK);
        add(cancelButton);
        
        // --- 6. Sign Up Button ---
        try {
            ImageIcon i5 = new ImageIcon(ClassLoader.getSystemResource("icon/signup.png"));
            Image i6 = i5.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            ImageIcon i_final3 = new ImageIcon(i6);
            signupButton = new JButton("Signup", i_final3);
        } catch (Exception e) {
             signupButton = new JButton("Signup"); // Fallback
        }
        signupButton.setBounds(375, 200, 100, 20);
        signupButton.addActionListener(this);
        signupButton.setBackground(new Color(60, 70, 70));
        signupButton.setForeground(Color.BLACK);
        add(signupButton);
        
        // --- 7. Frame Logo Image ---
        try {
            ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icon/MainFrameLogo.jpg"));
            Image i8 = i7.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            ImageIcon i9 = new ImageIcon(i8);
            JLabel image = new JLabel(i9);
            image.setBounds(0, 0, 250, 250);
            add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        
        // Frame Settings
        setSize(640, 350);
        setLocation(400, 200);
        setVisible(true);
    }
    
    // Action event will show when and where did the event get triggered from using getSource()
    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == loginButton){
            // Login Checker
            String susername = usernameTextField.getText();
            String spassword = new String(passwordField.getPassword()); 
            String user = loginAsChoice.getSelectedItem(); 
            
            try{
                DBConnection conn = new DBConnection();
                
                String query = "SELECT * FROM login WHERE username = '"+susername+"' AND password = '"+spassword+"' AND userType = '"+user+"'";               
               
                ResultSet rs = conn.stmt.executeQuery(query);
                
                if (rs.next()){
                    //Upon Successful Validation, navigate to MainHomePage
                    setVisible(false);
                    new MainHomePage(); 
                } else {
                    // Login Failed: notify user
                    JOptionPane.showMessageDialog(null, "Invalid Login Details");
                    // Clearing the password field on failure
                    passwordField.setText("");
                    usernameTextField.setText("");
                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }else if(ae.getSource() == cancelButton){
            setVisible(false);
            
        }else if(ae.getSource() == signupButton){
            setVisible(false);
            new SignUp(); 
        }
    }

    public static void main(String[] args){
        new Login();
    }
}
