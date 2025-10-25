package electricity.billing.system;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * The SignUp class provides the graphical user interface for new user registration.
 * This version uses standard system fonts and secure database interaction
 * via PreparedStatement using the updated DBConnection.getConnection() method.
 *
 * @author danny
 */
public class SignUp extends JFrame implements ActionListener {
    
    // --- Aesthetic Constants ---
    private static final Color PRIMARY_ACCENT_BLUE = new Color(0, 122, 255);
    private static final int BUTTON_HEIGHT = 28;

    // Simplified Font Definitions using reliable system font (Tahoma)
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Tahoma", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 14);
    
    // Declare components globally for action listeners
    JTextField txtMeterField, txtUsername, txtName;
    JPasswordField txtPwd;
    Choice accountType;
    JButton createButton, backButton;

    public SignUp(){
        
        super("Create Account");
        
        // Frame size and position
        setBounds(400, 150, 700, 400);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 650, 300);
        
        // Using a light blue border color consistent with accent
        Color borderColor = new Color(173, 216, 236);
        
        panel.setBorder(new TitledBorder(new LineBorder(borderColor, 2), 
                "Create Account", TitledBorder.LEADING, TitledBorder.TOP, 
                TITLE_FONT, PRIMARY_ACCENT_BLUE));
        
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setForeground(PRIMARY_ACCENT_BLUE);
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
        
        // Row 2: Meter Number
        JLabel lblMeter = new JLabel("Meter Number ");
        lblMeter.setBounds(labelX, currentY, 140, elementHeight);
        lblMeter.setForeground(Color.DARK_GRAY);
        lblMeter.setFont(LABEL_FONT);
        panel.add(lblMeter);
        txtMeterField = new JTextField();
        txtMeterField.setBounds(fieldX, currentY, 150, elementHeight);
        txtMeterField.setFont(FIELD_FONT);
        panel.add(txtMeterField);
        currentY += ySpacing;
        
        // Row 3: Username details
        JLabel lblUsername = new JLabel("Enter Username ");
        lblUsername.setBounds(labelX, currentY, 140, elementHeight);
        lblUsername.setForeground(Color.DARK_GRAY);
        lblUsername.setFont(LABEL_FONT);
        panel.add(lblUsername);
        txtUsername = new JTextField();
        txtUsername.setBounds(fieldX, currentY, 150, elementHeight);
        txtUsername.setFont(FIELD_FONT);
        panel.add(txtUsername);
        currentY += ySpacing;
        
        // Row 4: Name details
        JLabel lblName = new JLabel("Enter Name ");
        lblName.setBounds(labelX, currentY, 140, elementHeight);
        lblName.setForeground(Color.DARK_GRAY);
        lblName.setFont(LABEL_FONT);
        panel.add(lblName);
        txtName = new JTextField(); 
        txtName.setBounds(fieldX, currentY, 150, elementHeight);
        txtName.setFont(FIELD_FONT);
        panel.add(txtName);
        currentY += ySpacing;
        
        // Row 5: Password details
        JLabel lblPwd = new JLabel("Enter Password "); 
        lblPwd.setBounds(labelX, currentY, 140, elementHeight);
        lblPwd.setForeground(Color.DARK_GRAY);
        lblPwd.setFont(LABEL_FONT);
        panel.add(lblPwd);
        txtPwd = new JPasswordField(); 
        txtPwd.setBounds(fieldX, currentY, 150, elementHeight);
        txtPwd.setFont(FIELD_FONT);
        panel.add(txtPwd);


        // Buttons
        currentY += ySpacing + 10; // Extra spacing before buttons
        
        
        // CREATE Button (Primary Action Style)
        createButton = new JButton("CREATE");
        createButton.setBackground(PRIMARY_ACCENT_BLUE); 
        createButton.setForeground(Color.WHITE);
        createButton.setFont(BUTTON_FONT); 
        createButton.setBounds(140, currentY, 120, BUTTON_HEIGHT);
        createButton.addActionListener(this);
        panel.add(createButton);
        
        // Back Button (Secondary Action Style)
        backButton = new JButton("BACK");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(PRIMARY_ACCENT_BLUE); 
        backButton.setFont(BUTTON_FONT);
        backButton.setBounds(280, currentY, 120, BUTTON_HEIGHT);
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
            System.err.println("Signup image not found: " + e.getMessage());
        }
        
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == createButton){
            handleSignUp();
        } else if(ae.getSource() == backButton){
            setVisible(false);
            dispose();
            new Login(); 
        }
    }

    /**
     * Handles the secure account creation process using PreparedStatement.
     */
    private void handleSignUp() {
        // 1. Gather all data from fields
        String accType = accountType.getSelectedItem();
        String username = txtUsername.getText();
        String meterField = txtMeterField.getText();
        String name = txtName.getText(); 
        String passwd = new String(txtPwd.getPassword());

        // 2. Input Validation (Basic check before hitting DB)
        if (meterField.isEmpty() || username.isEmpty() || name.isEmpty() || passwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required for signup.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Connection dbConn = null;
        PreparedStatement pst = null;

        try {
            // 3. Establish DB connection - UPDATED TO USE getConnection()
            DBConnection conn = new DBConnection();
            dbConn = conn.getConnection(); 
            
            // Handle null connection (DB failure)
            if (dbConn == null) {
                 return; // Error message already shown in DBConnection constructor
            }
            
            // 4. Construct the SQL query using placeholders (?)
            String query = "INSERT INTO login (meter_no, username, password, userType, name) VALUES (?, ?, ?, ?, ?)";
            
            // 5. Use PreparedStatement for security (SQL Injection prevention)
            pst = dbConn.prepareStatement(query);
            pst.setString(1, meterField);
            pst.setString(2, username);
            pst.setString(3, passwd);
            pst.setString(4, accType);
            pst.setString(5, name);

            // 6. Execute the query
            pst.executeUpdate();
            
            // 7. Success Message and redirection
            JOptionPane.showMessageDialog(this, "Account Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Close the current frame and open the Login page
            setVisible(false);
            dispose();
            new Login();
            
        } catch(Exception e){
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Error: Meter Number or Username already exists. Please choose a different one.", "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred during account creation. See console for details.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            // 8. Manual resource cleanup (closing PreparedStatement and Connection)
            try {
                if (pst != null) pst.close();
                if (dbConn != null) dbConn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    
    public static void main(String[] args){
        new SignUp();
    }
}
