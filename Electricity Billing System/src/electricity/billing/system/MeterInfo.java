package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*; // Added necessary SQL imports

/**
 * The MeterInfo class handles the configuration details for a new meter,
 * storing location, type, phase code, and billing type in the database.
 */
public class MeterInfo extends JFrame implements ActionListener {

    // --- Aesthetic Constants ---
    private static final Color PRIMARY_ACCENT_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font STANDARD_FONT = new Font("Tahoma", Font.PLAIN, 14); 
    private static final Font HEADING_FONT = new Font("Tahoma", Font.PLAIN, 24);

    // --- Component Declarations ---
    private String meterNumber; // Field to hold the meter number passed from NewCustomer
    private JComboBox<String> meterLocation, meterType, phaseCode, billType;
    private JLabel daysValue; // Changed from JComboBox to JLabel as it is a fixed value
    private JButton submitButton, cancelButton;

    // Constructor that accepts the meter number
    MeterInfo(String meterNumber) {
        this.meterNumber = meterNumber; // Store the meter number
        
        // --- Frame Setup ---
        setSize(700, 500);
        setLocation(400, 200);
        setTitle("Meter Information Setup");
        
        // --- Panel Setup (Use BoxLayout for better structure management) ---
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(BACKGROUND_COLOR);
        add(p);
        
        // --- Heading ---
        JLabel heading = new JLabel("Meter Information");
        heading.setBounds(200, 10, 300, 30);
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_ACCENT_BLUE);
        p.add(heading);
        
        int y_pos = 80;
        int label_width = 150;
        int comp_width = 250;
        int comp_height = 25;
        int spacing = 40;

        // --- 1. Meter Number (Non-editable) ---
        JLabel lblMeterNo = new JLabel("Meter Number:");
        lblMeterNo.setBounds(100, y_pos, label_width, comp_height);
        lblMeterNo.setFont(STANDARD_FONT);
        p.add(lblMeterNo);
        
        JLabel meterNumberLabel = new JLabel(this.meterNumber); // Display the parsed meter number
        meterNumberLabel.setBounds(280, y_pos, comp_width, comp_height);
        meterNumberLabel.setFont(STANDARD_FONT);
        p.add(meterNumberLabel);
        y_pos += spacing;

        // --- 2. Meter Location (Dropdown) ---
        JLabel lblLocation = new JLabel("Meter Location:");
        lblLocation.setBounds(100, y_pos, label_width, comp_height);
        lblLocation.setFont(STANDARD_FONT);
        p.add(lblLocation);

        String[] locationOptions = {"Outside", "Inside", "Basement", "Attic"};
        meterLocation = new JComboBox<>(locationOptions);
        meterLocation.setBounds(280, y_pos, comp_width, comp_height);
        meterLocation.setBackground(Color.LIGHT_GRAY);
        meterLocation.setFont(STANDARD_FONT);
        p.add(meterLocation);
        y_pos += spacing;

        // --- 3. Meter Type (Dropdown) ---
        JLabel lblType = new JLabel("Meter Type:");
        lblType.setBounds(100, y_pos, label_width, comp_height);
        lblType.setFont(STANDARD_FONT);
        p.add(lblType);

        String[] typeOptions = {"Electronic Meter", "Digital Meter", "Smart Meter"};
        meterType = new JComboBox<>(typeOptions);
        meterType.setBounds(280, y_pos, comp_width, comp_height);
        meterType.setBackground(Color.LIGHT_GRAY);
        meterType.setFont(STANDARD_FONT);
        p.add(meterType);
        y_pos += spacing;

        // --- 4. Phase Code (Dropdown) ---
        JLabel lblPhase = new JLabel("Phase Code:");
        lblPhase.setBounds(100, y_pos, label_width, comp_height);
        lblPhase.setFont(STANDARD_FONT);
        p.add(lblPhase);

        String[] phaseOptions = {"01 (Single Phase)", "02 (Two Phase)", "03 (Three Phase)"};
        phaseCode = new JComboBox<>(phaseOptions);
        phaseCode.setBounds(280, y_pos, comp_width, comp_height);
        phaseCode.setBackground(Color.LIGHT_GRAY);
        phaseCode.setFont(STANDARD_FONT);
        p.add(phaseCode);
        y_pos += spacing;

        // --- 5. Bill Type (Dropdown) ---
        JLabel lblBillType = new JLabel("Bill Type:");
        lblBillType.setBounds(100, y_pos, label_width, comp_height);
        lblBillType.setFont(STANDARD_FONT);
        p.add(lblBillType);

        String[] billOptions = {"Residential", "Commercial", "Industrial"};
        billType = new JComboBox<>(billOptions);
        billType.setBounds(280, y_pos, comp_width, comp_height);
        billType.setBackground(Color.LIGHT_GRAY);
        billType.setFont(STANDARD_FONT);
        p.add(billType);
        y_pos += spacing;

        // --- 6. Days (Fixed Value) ---
        JLabel lblDays = new JLabel("Days:");
        lblDays.setBounds(100, y_pos, label_width, comp_height);
        lblDays.setFont(STANDARD_FONT);
        p.add(lblDays);

        // This should be a non-editable JLabel, as the time period is fixed.
        daysValue = new JLabel("30 Days"); 
        daysValue.setBounds(280, y_pos, comp_width, comp_height);
        daysValue.setFont(new Font("Tahoma", Font.BOLD, 14));
        p.add(daysValue);
        y_pos += spacing;
        
        // --- 7. Note ---
        JLabel lblNote = new JLabel("Note:");
        lblNote.setBounds(100, y_pos, label_width, comp_height);
        lblNote.setFont(STANDARD_FONT);
        p.add(lblNote);

        JLabel noteValue = new JLabel("By Default Bill is calculated for 30 days only");
        noteValue.setBounds(280, y_pos, comp_width + 50, comp_height);
        noteValue.setForeground(Color.RED);
        p.add(noteValue);
        y_pos += spacing + 20;

        // --- Submit Button (Primary Action Style) ---
        submitButton = new JButton("Submit");
        submitButton.setBounds(150, y_pos, 150, comp_height + 5);
        submitButton.setBackground(PRIMARY_ACCENT_BLUE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(STANDARD_FONT);
        submitButton.addActionListener(this);
        p.add(submitButton);

        // --- Cancel Button (Secondary Action Style) ---
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(330, y_pos, 150, comp_height + 5);
        cancelButton.setBackground(Color.DARK_GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(STANDARD_FONT);
        cancelButton.addActionListener(this);
        p.add(cancelButton);
        
        // --- Optional Image Placeholder (to match the application style) ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/meter.png"));
        Image i2 = i1.getImage().getScaledInstance(200, 350, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(500, 60, 200, 350);
        // p.add(image);

        // Add padding to the panel by placing it in a separate frame area
        setLayout(new BorderLayout());
        add(p, "Center");
        
        setVisible(true);
    }

    // ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            
            // 1. Retrieve data
            String meter = this.meterNumber;
            // Retrieve only the first part of the phase code (e.g., "01" from "01 (Single Phase)")
            String phase = ((String) phaseCode.getSelectedItem()).split(" ")[0]; 
            
            // Cast and retrieve other values
            String location = (String) meterLocation.getSelectedItem();
            String type = (String) meterType.getSelectedItem();
            String typebill = (String) billType.getSelectedItem();
            String totalDays = daysValue.getText(); 
            
            // 2. Construct Secure SQL Query using PreparedStatement
            // Columns: meter_no, meter_location, meter_type, phase_code, bill_type, days
            String query = "INSERT INTO meter_info (meter_no, meter_location, meter_type, phase_code, bill_type, days) VALUES(?, ?, ?, ?, ?, ?)";
            
            Connection dbConn = null;

            try {
                // Initialize Connection (Assumes DBConnection class exists and provides Connection object)
                DBConnection c = new DBConnection();
                dbConn = c.getConnection(); // Assuming DBConnection has a getter for Connection

                // Check for null connection
                if (dbConn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Use PreparedStatement for security
                try (PreparedStatement pst = dbConn.prepareStatement(query)) {
                    pst.setString(1, meter);
                    pst.setString(2, location);
                    pst.setString(3, type);
                    pst.setString(4, phase);
                    pst.setString(5, typebill);
                    pst.setString(6, totalDays); 
                    
                    // Execute the insertion
                    pst.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this, "Meter Information Submitted Successfully!");
                setVisible(false);
                
            } catch (SQLException e) {
                // Handle SQL errors (e.g., integrity constraint violation if meter_no already exists)
                JOptionPane.showMessageDialog(this, "Database Error: Failed to submit meter info. " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                // Handle other unexpected errors
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                // Close the connection securely
                try {
                    if (dbConn != null) {
                        dbConn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } else if (ae.getSource() == cancelButton) {
            setVisible(false); // Close the current frame
        }
    }
    
    // Optional main method for standalone testing
    public static void main(String[] args) {
        // Test with a dummy meter number
        new MeterInfo("123456"); 
    }
}
