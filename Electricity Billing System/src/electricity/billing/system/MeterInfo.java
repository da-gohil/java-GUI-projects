package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;

/**
 * The MeterInfo class handles the configuration details for a new meter,
 * storing location, type, phase code, and billing type in the database.
 * It is updated to navigate back to the MainHomePage upon cancel.
 * * NOTE: Frame size is fixed at 1024x768.
 */
public class MeterInfo extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font FIELD_FONT = new Font("Tahoma", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);
    
    // Layout constants
    private static final int CONTENT_START_X = 100; 
    private static final int FIELD_COLUMN_X = 300;
    private static final int INPUT_WIDTH = 300; 
    private static final int ELEMENT_HEIGHT = 40; 
    private static final int Y_SPACING = 60;

    // --- Component Declarations ---
    private String meterNumber; 
    private JComboBox<String> meterLocation, meterType, phaseCode, billType;
    private JLabel daysValue; 
    private JButton submitButton, cancelButton;
    
    // NEW: Reference to the MainHomePage for navigation
    private MainHomePage mainHome; 

    /**
     * Constructor accepts the meter number and a reference to the main home page.
     */
    MeterInfo(String meterNumber, MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        this.meterNumber = meterNumber; 
        this.mainHome = mainHome; // NEW: Store the reference
        
        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Meter Information Setup");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);

        // --- Heading ---
        JLabel heading = new JLabel("METER CONFIGURATION", SwingConstants.CENTER);
        heading.setBounds(0, 50, FRAME_WIDTH, 40);
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_BLUE);
        add(heading);
        
        // Panel for form content on the left
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(0, 120, 700, 550); 
        formPanel.setBackground(BACKGROUND_COLOR);
        add(formPanel);

        int y_pos = 0;
        int labelX = CONTENT_START_X;
        int fieldX = FIELD_COLUMN_X;

        // --- Form Fields (Implementation unchanged) ---
        y_pos = addLabelAndDisplay(formPanel, "Meter Number:", this.meterNumber, y_pos, labelX, fieldX, new Color(200, 0, 0));
        y_pos = addLabelAndComboBox(formPanel, "Meter Location:", new String[]{"Outside", "Inside", "Basement", "Attic"}, meterLocation, y_pos, labelX, fieldX);
        y_pos = addLabelAndComboBox(formPanel, "Meter Type:", new String[]{"Electronic Meter", "Digital Meter", "Smart Meter"}, meterType, y_pos, labelX, fieldX);
        y_pos = addLabelAndComboBox(formPanel, "Phase Code:", new String[]{"01 (Single Phase)", "02 (Two Phase)", "03 (Three Phase)"}, phaseCode, y_pos, labelX, fieldX);
        y_pos = addLabelAndComboBox(formPanel, "Bill Type:", new String[]{"Residential", "Commercial", "Industrial"}, billType, y_pos, labelX, fieldX);
        y_pos = addLabelAndDisplay(formPanel, "Days:", "30 Days", y_pos, labelX, fieldX, PRIMARY_BLUE);
        
        // --- Note ---
        JLabel lblNote = createLabel("Note:", labelX, y_pos);
        formPanel.add(lblNote);
        JLabel noteValue = new JLabel("Bill is calculated for 30 days only");
        noteValue.setBounds(fieldX, y_pos, INPUT_WIDTH, ELEMENT_HEIGHT);
        noteValue.setFont(FIELD_FONT);
        noteValue.setForeground(Color.RED);
        formPanel.add(noteValue);
        y_pos += Y_SPACING + 20;

        // --- Buttons ---
        int buttonXStart = fieldX - 50;
        int buttonWidth = 150;
        
        submitButton = createButton("Submit", buttonXStart, y_pos, buttonWidth, ELEMENT_HEIGHT);
        formPanel.add(submitButton);

        // Renaming "Cancel" to "Back" for clearer intent, but using original variable name
        cancelButton = createButton("Back", buttonXStart + buttonWidth + 20, y_pos, buttonWidth, ELEMENT_HEIGHT); 
        formPanel.add(cancelButton);
        
        // --- Right Side Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/meter.png"));
            int imgX = 650;
            int imgY = 150;
            int imgW = FRAME_WIDTH - imgX - 50; 
            int imgH = 450; 
            Image i2 = i1.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(imgX, imgY, imgW, imgH);
            add(image);
        } catch (Exception e) {
            System.err.println("Meter image not found: " + e.getMessage());
        }
        
        setVisible(true);
    }
    
    // --- Helper Methods (Unchanged) ---
    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, ELEMENT_HEIGHT);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(new Color(51, 51, 51));
        return lbl;
    }
    
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
    
    private int addLabelAndDisplay(JPanel panel, String labelText, String displayValue, int startY, int labelX, int fieldX, Color displayColor) {
        JLabel lbl = createLabel(labelText, labelX, startY);
        panel.add(lbl);

        JLabel display = new JLabel(displayValue);
        display.setBounds(fieldX, startY, INPUT_WIDTH, ELEMENT_HEIGHT);
        display.setFont(new Font("Tahoma", Font.BOLD, 18));
        display.setForeground(displayColor);
        panel.add(display);
        
        if (labelText.contains("Days")) {
            this.daysValue = display; 
        }

        return startY + Y_SPACING;
    }
    
    private int addLabelAndComboBox(JPanel panel, String labelText, String[] options, JComboBox<String> comboBoxVar, int startY, int labelX, int fieldX) {
        JLabel lbl = createLabel(labelText, labelX, startY);
        panel.add(lbl);

        JComboBox<String> combo = new JComboBox<>(options);
        combo.setBounds(fieldX, startY, INPUT_WIDTH, ELEMENT_HEIGHT);
        combo.setFont(FIELD_FONT);
        combo.setBackground(Color.LIGHT_GRAY);
        panel.add(combo);
        
        if (labelText.contains("Location")) this.meterLocation = combo;
        else if (labelText.contains("Type:")) this.meterType = combo;
        else if (labelText.contains("Phase")) this.phaseCode = combo;
        else if (labelText.contains("Bill")) this.billType = combo;

        return startY + Y_SPACING;
    }

    // ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            
            // ... (Data retrieval and SQL logic - UNCHANGED) ...
            String meter = this.meterNumber;
            String phase = ((String) phaseCode.getSelectedItem()).split(" ")[0]; 
            String location = (String) meterLocation.getSelectedItem();
            String type = (String) meterType.getSelectedItem();
            String typebill = (String) billType.getSelectedItem();
            String totalDays = daysValue.getText().split(" ")[0]; 
            String query = "INSERT INTO meter_info (meter_no, meter_location, meter_type, phase_code, bill_type, days) VALUES(?, ?, ?, ?, ?, ?)";
            Connection dbConn = null;

            try {
                dbConn = new DBConnection().getConnection(); 
                if (dbConn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (PreparedStatement pst = dbConn.prepareStatement(query)) {
                    pst.setString(1, meter);
                    pst.setString(2, location);
                    pst.setString(3, type);
                    pst.setString(4, phase);
                    pst.setString(5, typebill);
                    pst.setString(6, totalDays); 
                    pst.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this, "Meter Information Submitted Successfully! Setup Complete.");
                setVisible(false);
                dispose();
                
                if (mainHome != null) { // NEW: Return to MainHomePage after successful submission
                    mainHome.setVisible(true);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: Failed to submit meter info. " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                try {
                    if (dbConn != null) {
                        dbConn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } else if (ae.getSource() == cancelButton) {
            setVisible(false); 
            dispose();
            
            // NEW: Back-to-Home logic
            if (mainHome != null) {
                mainHome.setVisible(true);
            }
        }
    }
    
    public static void main(String[] args) {
        new MeterInfo("DUMMY123", null); 
    }
}