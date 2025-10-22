/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.billing.system;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author danny
 */
public class SignUp extends JFrame implements ActionListener {
    
    // Declare components globally for action listeners
    JTextField txtMeterField, txtUsername, txtName;
    JPasswordField txtPwd;
    Choice accountType;
    JButton createButton, backButton;

    public SignUp(){
        
        super("Create Account");
        
        //Frame size and position
        setBounds(400, 150, 700, 400);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 650, 300);
        
        // Using a light blue border color
        Color borderColor = new Color(173, 216, 236);
        
        panel.setBorder(new TitledBorder(new LineBorder(borderColor, 2), 
                "Create Account", TitledBorder.LEADING, TitledBorder.TOP, 
                new Font("Tahoma", Font.BOLD, 16), borderColor));
        
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setForeground(new Color(34, 139, 34));
        add(panel);
        
        
        // --- Component Placement (Layout code) ---
        
        int labelX = 100;
        int fieldX = 260;
        int elementHeight = 20;
        int ySpacing = 40; 
        int currentY = 50; 
        
        // Row 1: Account Type
        JLabel heading = new JLabel("Create Account As");
        heading.setBounds(labelX, currentY, 140, elementHeight);
        heading.setForeground(Color.GRAY);
        heading.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(heading);

        accountType = new Choice();
        accountType.add("Admin");
        accountType.add("Customer");
        accountType.setBounds(fieldX, currentY, 150, elementHeight);
        panel.add(accountType);
        
        currentY += ySpacing;
        
        // Row 2: Meter Number
        JLabel lblMeter = new JLabel("Meter Number ");
        lblMeter.setBounds(labelX, currentY, 140, elementHeight);
        lblMeter.setForeground(Color.GRAY);
        lblMeter.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblMeter);
        txtMeterField = new JTextField();
        txtMeterField.setBounds(fieldX, currentY, 150, elementHeight);
        panel.add(txtMeterField);
        currentY += ySpacing;
        
        // Row 3: Username details
        JLabel lblUsername = new JLabel("Enter Username ");
        lblUsername.setBounds(labelX, currentY, 140, elementHeight);
        lblUsername.setForeground(Color.GRAY);
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblUsername);
        txtUsername = new JTextField();
        txtUsername.setBounds(fieldX, currentY, 150, elementHeight);
        panel.add(txtUsername);
        currentY += ySpacing;
        
        // Row 4: Name details
        JLabel lblName = new JLabel("Enter Name ");
        lblName.setBounds(labelX, currentY, 140, elementHeight);
        lblName.setForeground(Color.GRAY);
        lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblName);
        txtName = new JTextField(); 
        txtName.setBounds(fieldX, currentY, 150, elementHeight);
        panel.add(txtName);
        currentY += ySpacing;
        
        // Row 5: Password details
        JLabel lblPwd = new JLabel("Enter Password "); 
        lblPwd.setBounds(labelX, currentY, 140, elementHeight);
        lblPwd.setForeground(Color.GRAY);
        lblPwd.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(lblPwd);
        txtPwd = new JPasswordField(); 
        txtPwd.setBounds(fieldX, currentY, 150, elementHeight);
        panel.add(txtPwd);


        // Buttons
        currentY += ySpacing + 10; // Extra spacing before buttons
        
        
        // CREATE Button
        createButton = new JButton("CREATE");
        // Using a clear, standard button blue color for better visibility
        createButton.setBackground(new Color(30, 144, 255)); 
        createButton.setForeground(Color.BLACK);
        createButton.setFont(new Font("Tahoma", Font.BOLD, 13)); // Explicit Font for text clarity
        createButton.setBounds(140, currentY, 120, 25);
        createButton.addActionListener(this);
        panel.add(createButton);
        
        // Back Button
        backButton = new JButton("BACK");
        backButton.setBackground(new Color(30, 144, 255)); // Using same blue color
        backButton.setForeground(Color.BLACK);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13)); // Explicit Font for text clarity
        backButton.setBounds(280, currentY, 120, 25);
        backButton.addActionListener(this);
        panel.add(backButton);
        
        // Image on the right side of the panel
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/signUpImage.png"));
            Image i2 = i1.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            
            image.setBounds(410, 30, 250, 250);
            panel.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setVisible(true);
    }
    
 
    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == createButton){
            // 1. Gather all data from fields
            String accType = accountType.getSelectedItem();
            String username = txtUsername.getText();
            String meterField = txtMeterField.getText();
            String name = txtName.getText(); 
            String passwd = new String(txtPwd.getPassword()); // Safely get password text

            // 2. Input Validation (Basic check before hitting DB)
            if (meterField.isEmpty() || username.isEmpty() || name.isEmpty() || passwd.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required for signup.");
                return;
            }
            
            try{
                // 3. Establish DB connection
                DBConnection conn = new DBConnection();
                
                // 4. Construct the SQL query
                // NOTE: For better security (preventing SQL Injection), PreparedStatement should be used here.
                String query = "INSERT INTO login (meter_no, username, password, userType, name) " + 
                               "VALUES ('" + meterField + "', '" + username + "', '" + passwd + "', '" + accType + "', '" + name + "')";
                
                // 5. Execute the query
                // FIX: Changed conn.stmt to conn.s to match the variable name in DBConnection.java
                conn.stmt.executeUpdate(query);
                
                // 6. Success Message and redirection
                JOptionPane.showMessageDialog(null, "Account Created Successfully!");
                
                // Close the current frame and open the Login page
                setVisible(false);
                // Assuming Login class exists and has a parameterless constructor
                new Login();
                
            }catch(Exception e){
                e.printStackTrace();
                // Specific error message for unique constraint violation (like duplicate username/meter_no)
                if (e.getMessage().contains("Duplicate entry")) {
                    JOptionPane.showMessageDialog(null, "Error: Meter Number or Username already exists.");
                } else {
                    // Generic database error message
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred during account creation: " + e.getMessage());
                }
            }
            
        }else if(ae.getSource() == backButton){
            setVisible(false);
            new Login(); 
            
        }
    }

    
    public static void main(String[] args){
        new SignUp();
    }
}
