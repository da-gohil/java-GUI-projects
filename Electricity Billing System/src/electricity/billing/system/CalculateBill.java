package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;

/**
 * CalculateBill class allows Admin to calculate and save monthly electricity bills for customers.
 * Implements fixed 1024x768 size, standard button styling, and back-to-home navigation.
 */
public class CalculateBill extends JFrame implements ActionListener {

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
    private Choice cMeterNumber, cMonth, cYear;
    private JTextField tfUnits;
    private JButton btnCalculate, btnCancel;
    private JLabel lblName, lblAddress;
    
    // NEW: Reference to the MainHomePage for navigation
    private MainHomePage mainHome;

    /**
     * Constructor accepts a reference to the main home page for navigation.
     */
    public CalculateBill(MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        super("Calculate Electricity Bill");
        this.mainHome = mainHome;
        
        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 

        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null); // Use absolute layout for better control

        // Panel for form content on the center-left
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 700, FRAME_HEIGHT); 
        p.setBackground(BACKGROUND_COLOR); // Use White for consistency
        add(p);

        // --- Heading ---
        JLabel heading = new JLabel("CALCULATE ELECTRICITY BILL");
        heading.setBounds(50, 50, 600, 40);
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_BLUE);
        p.add(heading);
        
        int y_start = 150;
        int x_label = 100;
        int x_field = 300;
        int spacing = 50;
        int field_width = 300;

        // --- 1. Meter Number ---
        y_start = addLabelAndChoice(p, "Meter Number:", cMeterNumber, x_label, x_field, y_start, field_width);
        cMeterNumber.addItemListener(e -> updateCustomerDetails());
        populateMeterNumbers();

        // --- 2. Customer Name (Dynamic) ---
        y_start = addLabelAndDisplay(p, "Customer Name:", lblName, x_label, x_field, y_start, field_width);

        // --- 3. Address (Dynamic) ---
        y_start = addLabelAndDisplay(p, "Address:", lblAddress, x_label, x_field, y_start, field_width);
        // Initial details load
        updateCustomerDetails();

        // --- 4. Units Consumed ---
        y_start = addLabelAndField(p, "Units Consumed:", tfUnits, x_label, x_field, y_start, field_width);

        // --- 5. Billing Month ---
        y_start = addLabelAndChoice(p, "Billing Month:", cMonth, x_label, x_field, y_start, field_width);
        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        for(String m: months) cMonth.add(m);

        // --- 6. Billing Year ---
        y_start = addLabelAndChoice(p, "Billing Year:", cYear, x_label, x_field, y_start, field_width);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // Populating 5 years before and after current year for dynamic data
        for(int y = currentYear - 5; y <= currentYear + 5; y++) cYear.add(String.valueOf(y));

        // --- Buttons ---
        int button_y = y_start + spacing;
        int button_width = 180;
        int button_height = 40;
        
        // --- Calculate Button (Consistent Style: White BG, Black Text, Blue Border) ---
        btnCalculate = createButton("Calculate & Submit", 150, button_y, button_width, button_height);
        p.add(btnCalculate);

        // --- Cancel/Back Button (Consistent Style: White BG, Black Text, Blue Border) ---
        btnCancel = createButton("Back", 150 + button_width + 40, button_y, 120, button_height);
        p.add(btnCancel);

        // --- Right Side Image (Fills Border-to-Border Space) ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon2.jpg")); // Changed to hicon2 for variation
            int imgX = 700;
            int imgY = 100;
            int imgW = FRAME_WIDTH - imgX - 50; 
            int imgH = 500; 
            
            Image i2 = i1.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(imgX, imgY, imgW, imgH);
            add(image);
        } catch (Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }

        setVisible(true);
    }
    
    // --- Helper Methods ---

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
    
    // Helper to add a label and a Choice component
    private int addLabelAndChoice(JPanel panel, String labelText, Choice choiceVar, int labelX, int fieldX, int startY, int width) {
        JLabel lbl = new JLabel(labelText);
        lbl.setBounds(labelX, startY, 200, 20);
        lbl.setFont(LABEL_FONT);
        panel.add(lbl);

        choiceVar = new Choice();
        choiceVar.setBounds(fieldX, startY, width, 20);
        choiceVar.setFont(FIELD_FONT);
        panel.add(choiceVar);
        
        // Assign to instance variable using variable name logic (similar to NewCustomer, but for Choice)
        if (labelText.contains("Meter Number")) this.cMeterNumber = choiceVar;
        else if (labelText.contains("Month")) this.cMonth = choiceVar;
        else if (labelText.contains("Year")) this.cYear = choiceVar;

        return startY + 50;
    }
    
    // Helper to add a label and a display JLabel (dynamic text)
    private int addLabelAndDisplay(JPanel panel, String labelText, JLabel labelVar, int labelX, int fieldX, int startY, int width) {
        JLabel lbl = new JLabel(labelText);
        lbl.setBounds(labelX, startY, 200, 20);
        lbl.setFont(LABEL_FONT);
        panel.add(lbl);

        labelVar = new JLabel("Loading...");
        labelVar.setBounds(fieldX, startY, width, 20);
        labelVar.setFont(FIELD_FONT);
        labelVar.setForeground(PRIMARY_BLUE);
        panel.add(labelVar);
        
        // Assign to instance variable
        if (labelText.contains("Name")) this.lblName = labelVar;
        else if (labelText.contains("Address")) this.lblAddress = labelVar;

        return startY + 50;
    }
    
    // Helper to add a label and a JTextField
    private int addLabelAndField(JPanel panel, String labelText, JTextField fieldVar, int labelX, int fieldX, int startY, int width) {
        JLabel lbl = new JLabel(labelText);
        lbl.setBounds(labelX, startY, 200, 20);
        lbl.setFont(LABEL_FONT);
        panel.add(lbl);

        fieldVar = new JTextField();
        fieldVar.setBounds(fieldX, startY, width, 25);
        fieldVar.setFont(FIELD_FONT);
        fieldVar.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        panel.add(fieldVar);
        
        // Assign to instance variable
        if (labelText.contains("Units")) this.tfUnits = fieldVar;

        return startY + 50;
    }

    private void populateMeterNumbers() {
        if(cMeterNumber == null) return; // Guard clause if Choice is not initialized
        try (Connection conn = new DBConnection().getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT meter_no FROM customer")) {
            
            cMeterNumber.removeAll(); // Clear existing items
            while(rs.next()) {
                cMeterNumber.add(rs.getString("meter_no"));
            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading meter numbers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomerDetails() {
        String meter = cMeterNumber.getSelectedItem();
        if(meter == null || meter.isEmpty()) {
            lblName.setText("N/A"); lblAddress.setText("N/A");
            return;
        }
        
        // Use PreparedStatement for security and try-with-resources for reliable closing
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT customer_name,address FROM customer WHERE meter_no=?")) {
            
            pst.setString(1, meter);
            
            try(ResultSet rs = pst.executeQuery()) {
                if(rs.next()) {
                    lblName.setText(rs.getString("customer_name"));
                    lblAddress.setText(rs.getString("address"));
                } else {
                    lblName.setText("N/A"); lblAddress.setText("N/A");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            lblName.setText("Error"); lblAddress.setText("Error");
            JOptionPane.showMessageDialog(this, "Error fetching customer details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==btnCalculate) {
            handleCalculateAction();
        } else if(ae.getSource()==btnCancel) {
            handleCancelAction();
        }
    }
    
    private void handleCalculateAction() {
        // Input validation and parsing
        String meter = cMeterNumber.getSelectedItem();
        String month = cMonth.getSelectedItem();
        String yearStr = cYear.getSelectedItem();
        String unitsStr = tfUnits.getText();
        
        if (meter == null || meter.isEmpty() || month == null || month.isEmpty() || yearStr == null || yearStr.isEmpty() || unitsStr.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please ensure a meter, month, year, and units are selected/entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        int year, units;
        try {
            year = Integer.parseInt(yearStr);
            units = Integer.parseInt(unitsStr);
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Units must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = new DBConnection().getConnection()) {
            if (conn == null) return;
            
            // 1. Fetch Tax Configuration (Dynamic Data)
            double unitRate, meterRent, serviceCharge, serviceTax, swacchCess, fixedTax;
            try (Statement taxStmt = conn.createStatement();
                 ResultSet taxRs = taxStmt.executeQuery("SELECT * FROM tax")) {
                
                if(!taxRs.next()) {
                    JOptionPane.showMessageDialog(this, "No tax configuration found in the 'tax' table.", "Configuration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                unitRate = taxRs.getDouble("cost_per_unit");
                meterRent = taxRs.getDouble("meter_rent");
                serviceCharge = taxRs.getDouble("service_charge");
                serviceTax = taxRs.getDouble("service_tax");
                swacchCess = taxRs.getDouble("swacch_bharat_cess");
                fixedTax = taxRs.getDouble("fixed_tax");
            }

            // 2. Calculate Total Bill
            double total = units * unitRate + meterRent + serviceCharge + serviceTax + swacchCess + fixedTax;
            total = Math.round(total * 100.0) / 100.0; // Round to 2 decimal places

            // 3. Insert Bill Record
            String insert = "INSERT INTO bill(meter_no,month,year,units,total_bill,status) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement pst = conn.prepareStatement(insert)) {
                pst.setString(1, meter);
                pst.setString(2, month);
                pst.setInt(3, year);
                pst.setInt(4, units);
                pst.setDouble(5, total);
                pst.setString(6, "Unpaid");
                pst.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Bill calculated and saved successfully.\nTotal: $" + total);
            setVisible(false);
            dispose();
            
            // Return to main homepage after submission
            if (mainHome != null) {
                mainHome.setVisible(true);
            }

        } catch(SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Bill for " + month + "/" + year + " already exists for meter " + meter, "Duplicate Bill Error", JOptionPane.ERROR_MESSAGE);
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCancelAction() {
        setVisible(false);
        dispose();
        if (mainHome != null) {
            mainHome.setVisible(true); // Navigate back to the MainHomePage
        }
    }

    // The main method is left minimal for structural testing, adhering to the dynamic data rule.
    public static void main(String[] args) {
        // new CalculateBill(null); // Instantiated with null for standalone testing
    }
}