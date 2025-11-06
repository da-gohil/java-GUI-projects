package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;

/**
 * ViewInformation frame displays all customer details in a read-only format.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class ViewInformation extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font VALUE_FONT = new Font("Tahoma", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);
    
    // --- Component Declarations ---
    private JButton backButton;
    private JLabel lblName, lblMeterNumber, lblAddress, lblCity, lblState, lblEmail, lblPhone;
    
    // NEW: Reference to the MainHomePage for navigation
    private MainHomePage mainHome;

    /**
     * Constructor for ViewInformation frame.
     * @param meter The meter number of the customer to view.
     * @param mainHome Reference to the calling MainHomePage instance.
     */
    public ViewInformation(String meter, MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        this.mainHome = mainHome;
        
        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);
        setTitle("View Customer Information");

        // --- Heading ---
        JLabel heading = new JLabel("VIEW CUSTOMER INFORMATION", SwingConstants.CENTER);
        heading.setBounds(0, 50, FRAME_WIDTH, 40);
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_BLUE);
        add(heading);

        // --- Main Panel (Form Container) ---
        JPanel panel = new JPanel(null);
        panel.setBounds(50, 120, FRAME_WIDTH - 100, 380);
        panel.setBackground(new Color(245, 245, 245)); // Very Light Gray for contrast
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(PRIMARY_BLUE, 2), "Customer Details", 
                TitledBorder.LEFT, TitledBorder.TOP, LABEL_FONT, PRIMARY_BLUE
        ));
        add(panel);

        // --- Labels and Values ---
        int leftX = 50, rightX = 480, yPos = 40, yIncrement = 50;
        
        // --- Helper function to simplify adding labels and values ---
        yPos = addDetailRow(panel, "Name:", leftX, yPos);
        yPos = addDetailRow(panel, "Meter Number:", leftX, yPos);
        yPos = addDetailRow(panel, "Address:", leftX, yPos);
        yPos = addDetailRow(panel, "City:", leftX, yPos);
        
        // Reset yPos for the right column and add remaining details
        yPos = 40;
        addDetailRow(panel, "State:", rightX, yPos);
        yPos += yIncrement;
        addDetailRow(panel, "Email:", rightX, yPos);
        yPos += yIncrement;
        addDetailRow(panel, "Phone:", rightX, yPos);

        // --- Back Button ---
        // Using a standard location near the bottom center of the panel
        int buttonX = (panel.getWidth() / 2) - 60;
        backButton = createButton("Back", buttonX, 300, 120, 35);
        panel.add(backButton);

        // --- Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/viewcustomer.jpg"));
            int imgW = 600;
            int imgH = 200;
            Image i2 = i1.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds((FRAME_WIDTH/2) - (imgW/2), 520, imgW, imgH);
            add(image);
        } catch (Exception e) {
             System.err.println("Image not found: " + e.getMessage());
        }

        // --- Fetch Data from Database ---
        fetchCustomerData(meter);

        setVisible(true);
    }
    
    // --- Helper Methods ---
    
    /** Helper to add the label and initialize the corresponding value JLabel. */
    private int addDetailRow(JPanel panel, String labelText, int x, int y) {
        JLabel lblLabel = new JLabel(labelText);
        lblLabel.setBounds(x, y, 150, 20);
        lblLabel.setFont(LABEL_FONT);
        lblLabel.setForeground(new Color(51, 51, 51));
        panel.add(lblLabel);

        JLabel lblValue = new JLabel("N/A"); // Placeholder
        lblValue.setBounds(x + 150, y, 300, 20);
        lblValue.setFont(VALUE_FONT);
        lblValue.setForeground(PRIMARY_BLUE);
        panel.add(lblValue);
        
        // Assign the created JLabel to the correct instance variable using the labelText
        if (labelText.contains("Name")) this.lblName = lblValue;
        else if (labelText.contains("Meter Number")) this.lblMeterNumber = lblValue;
        else if (labelText.contains("Address")) this.lblAddress = lblValue;
        else if (labelText.contains("City")) this.lblCity = lblValue;
        else if (labelText.contains("State")) this.lblState = lblValue;
        else if (labelText.contains("Email")) this.lblEmail = lblValue;
        else if (labelText.contains("Phone")) this.lblPhone = lblValue;

        return y + 50;
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

    private void fetchCustomerData(String meter) {
        // Use try-with-resources for reliable connection management
        try (Connection conn = new DBConnection().getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer WHERE meter_no = '" + meter + "'")) {

            if (rs.next()) {
                lblName.setText(rs.getString("customer_name"));
                lblMeterNumber.setText(rs.getString("meter_no"));
                lblAddress.setText(rs.getString("address"));
                lblCity.setText(rs.getString("city"));
                lblState.setText(rs.getString("state"));
                lblEmail.setText(rs.getString("email"));
                lblPhone.setText(rs.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(this, "No customer found for meter number: " + meter, "Data Not Found", JOptionPane.WARNING_MESSAGE);
                lblMeterNumber.setText(meter); // Still display the meter number
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
            
            // Back-to-Home logic
            if (mainHome != null) {
                mainHome.setVisible(true); // Show existing main homepage
            }
        }
    }

    public static void main(String[] args) {
        new ViewInformation("100123", null); // Example for testing
    }
}