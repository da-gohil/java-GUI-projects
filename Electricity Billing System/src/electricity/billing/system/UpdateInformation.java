package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;

/**
 * UpdateInformation frame allows a customer to update their personal details
 * such as address, city, state, email, and phone.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class UpdateInformation extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font FIELD_FONT = new Font("Tahoma", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);
    
    // --- Component Declarations ---
    private JTextField tfAddress, tfState, tfCity, tfEmail, tfPhone;
    private JButton btnUpdate, btnCancel;
    private JLabel lblName;
    private final String meter;
    
    // NEW: Reference to the MainHomePage for navigation
    private MainHomePage mainHome;

    /**
     * Constructor for UpdateInformation frame.
     * @param meter The meter number of the customer to update.
     * @param mainHome Reference to the calling MainHomePage instance.
     */
    UpdateInformation(String meter, MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        this.meter = meter;
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);
        setTitle("Update Customer Information");

        // --- Heading ---
        JLabel heading = new JLabel("UPDATE CUSTOMER INFORMATION", SwingConstants.CENTER);
        heading.setBounds(0, 50, FRAME_WIDTH, 40);
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_BLUE);
        add(heading);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(50, 120, 500, 500); 
        formPanel.setBackground(new Color(240, 240, 240)); 
        formPanel.setBorder(new LineBorder(PRIMARY_BLUE, 1));
        add(formPanel);
        
        int yPos = 50;
        int yIncrement = 40;
        int fieldX = 220;
        int fieldWidth = 240;

        // --- 1. Name Label (Read-only) ---
        yPos = addLabelAndValue(formPanel, "Name:", lblName, 30, fieldX, yPos, fieldWidth);

        // --- 2. Meter Number (Read-only) ---
        JLabel lblMeterTitle = createLabel("Meter Number:", 30, yPos);
        formPanel.add(lblMeterTitle);
        JLabel lblMeterValue = createValueLabel(meter, fieldX, yPos, fieldWidth);
        formPanel.add(lblMeterValue);
        yPos += yIncrement;

        // --- 3. Address ---
        yPos = addLabelAndTextField(formPanel, "Address:", tfAddress, 30, fieldX, yPos, fieldWidth);

        // --- 4. City ---
        yPos = addLabelAndTextField(formPanel, "City:", tfCity, 30, fieldX, yPos, fieldWidth);

        // --- 5. State ---
        yPos = addLabelAndTextField(formPanel, "State:", tfState, 30, fieldX, yPos, fieldWidth);

        // --- 6. Email ---
        yPos = addLabelAndTextField(formPanel, "Email:", tfEmail, 30, fieldX, yPos, fieldWidth);

        // --- 7. Phone ---
        yPos = addLabelAndTextField(formPanel, "Phone:", tfPhone, 30, fieldX, yPos, fieldWidth);
        
        // --- Fetch Existing Data ---
        fetchExistingData();

        // --- Buttons ---
        int buttonY = yPos + yIncrement + 20;
        int buttonWidth = 120;

        btnUpdate = createButton("Update", 100, buttonY, buttonWidth, 35);
        formPanel.add(btnUpdate);

        btnCancel = createButton("Cancel", 270, buttonY, buttonWidth, 35);
        formPanel.add(btnCancel);

        // --- Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/update.jpg"));
            int imgW = 400;
            int imgH = 300;
            Image i2 = i1.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(580, 170, imgW, imgH);
            add(image);
        } catch (Exception e) {
             System.err.println("Image not found: " + e.getMessage());
        }

        setVisible(true);
    }
    
    // --- UI Helper Methods ---
    
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 150, 20);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(new Color(51, 51, 51));
        return lbl;
    }
    
    private JLabel createValueLabel(String text, int x, int y, int width) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, width, 20);
        lbl.setFont(FIELD_FONT);
        lbl.setForeground(PRIMARY_BLUE);
        return lbl;
    }

    private int addLabelAndValue(JPanel panel, String labelText, JLabel labelVar, int labelX, int fieldX, int startY, int width) {
        panel.add(createLabel(labelText, labelX, startY));
        labelVar = createValueLabel("Loading...", fieldX, startY, width);
        panel.add(labelVar);
        
        // Assign to instance variable
        if (labelText.contains("Name")) this.lblName = labelVar;

        return startY + 40;
    }
    
    private int addLabelAndTextField(JPanel panel, String labelText, JTextField fieldVar, int labelX, int fieldX, int startY, int width) {
        panel.add(createLabel(labelText, labelX, startY));

        JTextField newField = new JTextField();
        newField.setBounds(fieldX, startY, width, 25);
        newField.setFont(FIELD_FONT);
        newField.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        panel.add(newField);
        
        // Assign to instance variable
        if (labelText.contains("Address")) this.tfAddress = newField;
        else if (labelText.contains("City")) this.tfCity = newField;
        else if (labelText.contains("State")) this.tfState = newField;
        else if (labelText.contains("Email")) this.tfEmail = newField;
        else if (labelText.contains("Phone")) this.tfPhone = newField;

        return startY + 40;
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

    private void fetchExistingData() {
        // Use PreparedStatement for safer database interaction
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM customer WHERE meter_no = ?")) {
            
            pst.setString(1, meter);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    lblName.setText(rs.getString("customer_name"));
                    tfAddress.setText(rs.getString("address"));
                    tfCity.setText(rs.getString("city"));
                    tfState.setText(rs.getString("state"));
                    tfEmail.setText(rs.getString("email"));
                    tfPhone.setText(rs.getString("phone"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
        String address = tfAddress.getText().trim();
        String city = tfCity.getText().trim();
        String state = tfState.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        
        if (address.isEmpty() || city.isEmpty() || state.isEmpty() || email.isEmpty() || phone.isEmpty()) {
             JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        // Use PreparedStatement for security (replaces concatenated query)
        String query = "UPDATE customer SET address = ?, city = ?, state = ?, email = ?, phone = ? WHERE meter_no = ?";
        
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setString(1, address);
            pst.setString(2, city);
            pst.setString(3, state);
            pst.setString(4, email);
            pst.setString(5, phone);
            pst.setString(6, meter);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User Information Updated Successfully");
            setVisible(false);
            dispose();
            
            if (mainHome != null) {
                mainHome.setVisible(true); // Return to customer homepage
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating information: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
        //new UpdateInformation("100123", null); // Example for testing
    }
}