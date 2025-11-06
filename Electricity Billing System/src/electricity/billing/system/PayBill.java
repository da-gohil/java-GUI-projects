package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;

/**
 * PayBill frame allows the customer to view their bill details for a selected month
 * and initiate payment via a simulated gateway (PayTM).
 * Updated to use fixed 1024x768 size, consistent styling, and correct navigation.
 */
public class PayBill extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 768;
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("SanSerif", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("SanSerif", Font.BOLD, 16);
    private static final Font DATA_FONT = new Font("SanSerif", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("SanSerif", Font.BOLD, 16);

    // --- Components ---
    private Choice cMonth;
    private JButton btnPay, btnBack;
    private final String meter;
    private JLabel lblMeterNumber, lblCustomerName, lblUnitsConsumed, lblTotalBill, lblStatus; // Now initialized below
    
    private MainHomePage mainHome;

    /**
     * Constructor for PayBill frame.
     */
    public PayBill(String meter, MainHomePage mainHome) {
        this.meter = meter;
        this.mainHome = mainHome;

        // --- Initialize all JLabel instance variables BEFORE calling helpers ---
        lblMeterNumber = new JLabel(meter); // Initialize with meter value
        lblCustomerName = new JLabel("Loading...");
        lblUnitsConsumed = new JLabel("N/A");
        lblTotalBill = new JLabel("N/A");
        lblStatus = new JLabel("Unknown");

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(BACKGROUND_COLOR);
        p.setBounds(80, 80, 500, 550);
        p.setBorder(new LineBorder(PRIMARY_BLUE, 2, true));
        add(p);

        // Heading
        JLabel lblHeading = new JLabel("Electricity Bill Payment");
        lblHeading.setFont(HEADER_FONT);
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(50, 30, 400, 40);
        lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblHeading);

        int y_pos = 110;
        int y_inc = 55;
        int labelX = 50, dataX = 250, width = 200;

        // --- 1. Meter Number ---
        // FIX: Now passing initialized lblMeterNumber. No text setting needed in helper for this one.
        configureAndAddDetailRow(p, "Meter Number:", lblMeterNumber, labelX, dataX, y_pos, LABEL_FONT, DATA_FONT);
        lblMeterNumber.setForeground(new Color(51, 51, 51));
        y_pos += y_inc;

        // --- 2. Customer Name ---
        configureAndAddDetailRow(p, "Customer Name:", lblCustomerName, labelX, dataX, y_pos, LABEL_FONT, DATA_FONT);
        y_pos += y_inc;

        // --- 3. Month Choice ---
        JLabel lblMonthLabel = new JLabel("Select Month:");
        lblMonthLabel.setBounds(labelX, y_pos, width, 25);
        lblMonthLabel.setFont(LABEL_FONT);
        p.add(lblMonthLabel);

        cMonth = new Choice();
        cMonth.setBounds(dataX, y_pos, width, 25);
        cMonth.setFont(DATA_FONT);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) cMonth.add(month);
        p.add(cMonth);
        y_pos += y_inc;

        // --- 4. Units Consumed ---
        configureAndAddDetailRow(p, "Units Consumed:", lblUnitsConsumed, labelX, dataX, y_pos, LABEL_FONT, DATA_FONT);
        y_pos += y_inc;

        // --- 5. Total Bill ---
        configureAndAddDetailRow(p, "Total Bill Amount (Rs.):", lblTotalBill, labelX, dataX, y_pos, LABEL_FONT, LABEL_FONT);
        lblTotalBill.setForeground(new Color(204, 51, 0));
        y_pos += y_inc;

        // --- 6. Status ---
        configureAndAddDetailRow(p, "Payment Status:", lblStatus, labelX, dataX, y_pos, LABEL_FONT, LABEL_FONT);
        lblStatus.setForeground(Color.RED);
        y_pos += y_inc;

        // --- Load Initial Data ---
        loadCustomerAndBill(cMonth.getSelectedItem());

        // Month listener
        cMonth.addItemListener(e -> loadCustomerAndBill(cMonth.getSelectedItem()));

        // --- Buttons ---
        int buttonY = 550;
        int buttonWidth = 150;

        btnPay = createButton("Pay Bill Now", 80, buttonY, buttonWidth, 40);
        btnPay.setBackground(PRIMARY_BLUE);
        btnPay.setForeground(Color.WHITE);
        btnPay.setBorder(new LineBorder(PRIMARY_BLUE, 2));
        p.add(btnPay);

        btnBack = createButton("Back", 270, buttonY, buttonWidth, 40);
        p.add(btnBack);

        // --- Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bill.png"));
            Image i2 = i1.getImage().getScaledInstance(350, 500, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i2));
            image.setBounds(600, 100, 350, 500);
            add(image);
        } catch (Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }

        setVisible(true);
    }
    
    /** * Helper to configure and add a label/value JLabel to the panel.
     * FIX: Now receives the already initialized JLabel instance variable.
     */
    private void configureAndAddDetailRow(JPanel panel, String labelText, JLabel valueLabel, int labelX, int dataX, int y, Font labelFont, Font dataFont) {
        JLabel lblLabel = new JLabel(labelText);
        lblLabel.setBounds(labelX, y, 200, 20);
        lblLabel.setFont(labelFont);
        panel.add(lblLabel);

        valueLabel.setBounds(dataX, y, 200, 20);
        valueLabel.setFont(dataFont);
        panel.add(valueLabel);
        
        // No setText here, as initial text is set in constructor or loadCustomerAndBill
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
    
    public String getMeterNumber() {
        return meter;
    }

    private void loadCustomerAndBill(String month) {
        try (Connection conn = new DBConnection().getConnection()) {
            
            // 1. Customer Name
            String custQuery = "SELECT customer_name FROM customer WHERE meter_no = ?";
            try (PreparedStatement pstCust = conn.prepareStatement(custQuery)) {
                pstCust.setString(1, meter);
                try (ResultSet rsCust = pstCust.executeQuery()) {
                    if (rsCust.next()) lblCustomerName.setText(rsCust.getString("customer_name"));
                    else lblCustomerName.setText("N/A");
                }
            }

            // 2. Bill info
            String billQuery = "SELECT units, total_bill, status FROM bill WHERE meter_no = ? AND month = ?";
            try (PreparedStatement pstBill = conn.prepareStatement(billQuery)) {
                pstBill.setString(1, meter);
                pstBill.setString(2, month);
                try (ResultSet rsBill = pstBill.executeQuery()) {
                    if (rsBill.next()) {
                        lblUnitsConsumed.setText(rsBill.getString("units"));
                        lblTotalBill.setText("Rs. " + rsBill.getString("total_bill"));
                        String status = rsBill.getString("status");
                        lblStatus.setText(status);
                        
                        if ("Paid".equalsIgnoreCase(status)) {
                            lblStatus.setForeground(new Color(34, 139, 34));
                            btnPay.setEnabled(false);
                            btnPay.setBackground(Color.GRAY);
                        } else {
                            lblStatus.setForeground(Color.RED);
                            btnPay.setEnabled(true);
                            btnPay.setBackground(PRIMARY_BLUE);
                        }
                    } else {
                        lblUnitsConsumed.setText("N/A");
                        lblTotalBill.setText("N/A");
                        lblStatus.setText("Bill Not Calculated");
                        lblStatus.setForeground(Color.BLUE);
                        btnPay.setEnabled(false);
                        btnPay.setBackground(Color.GRAY);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill info: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void simulatePaymentCompletion() {
        String selectedMonth = cMonth.getSelectedItem();
        
        String updateQuery = "UPDATE bill SET status='Paid' WHERE meter_no=? AND month=?";
        
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pstUpdate = conn.prepareStatement(updateQuery)) {
            
            pstUpdate.setString(1, meter);
            pstUpdate.setString(2, selectedMonth);
            pstUpdate.executeUpdate();

            // Update UI after successful update
            lblStatus.setText("Paid");
            lblStatus.setForeground(new Color(34, 139, 34));
            btnPay.setEnabled(false);
            btnPay.setBackground(Color.GRAY);
            JOptionPane.showMessageDialog(this, "Payment Successful!\nBill for " + selectedMonth + " marked as Paid.", "Payment Confirmed", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error finalizing payment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            loadCustomerAndBill(selectedMonth); 
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnPay) {
            String status = lblStatus.getText();
            String totalBillText = lblTotalBill.getText();
            String selectedMonth = cMonth.getSelectedItem();

            if ("Bill Not Calculated".equals(status) || "N/A".equals(totalBillText)) {
                JOptionPane.showMessageDialog(this, "Bill for " + selectedMonth + " is not calculated yet.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("Paid".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(this, "Bill for " + selectedMonth + " is already paid.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            setVisible(false);
            // This assumes a default constructor or that the user has fixed PayTM.java main()
            new PayTM(this); 
            
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            
            if (mainHome != null) {
                mainHome.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        new PayBill("100123", null); 
    }
}