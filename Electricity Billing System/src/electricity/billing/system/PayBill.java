package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PayBill frame allows the customer to view their bill details for a selected month
 * and mark the bill as paid in the database.
 */
public class PayBill extends JFrame implements ActionListener {

    // Components
    private Choice cMonth;
    private JButton btnPay, btnBack;
    private String meter;

    private JLabel lblMeterNumber, lblCustomerName, lblUnitsConsumed, lblTotalBill, lblStatus;

    // Colors
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color LIGHT_BG = new Color(230, 240, 250);

    public PayBill(String meter) {
        this.meter = meter;

        setLayout(null);
        setBounds(300, 150, 900, 600);
        getContentPane().setBackground(LIGHT_BG);

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(Color.WHITE);
        p.setBounds(30, 30, 500, 500);
        add(p);

        Font headerFont = new Font("SanSerif", Font.BOLD, 24);
        Font labelFont = new Font("SanSerif", Font.PLAIN, 14);
        Font dataFont = new Font("SanSerif", Font.BOLD, 15);

        // Heading
        JLabel lblHeading = new JLabel("Electricity Bill Payment");
        lblHeading.setFont(headerFont);
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(100, 20, 400, 30);
        p.add(lblHeading);

        int y_pos = 90;
        int y_inc = 50;

        // Meter Number
        JLabel lblMeterLabel = new JLabel("Meter Number:");
        lblMeterLabel.setBounds(35, y_pos, 200, 20);
        lblMeterLabel.setFont(labelFont);
        p.add(lblMeterLabel);

        lblMeterNumber = new JLabel(meter);
        lblMeterNumber.setBounds(250, y_pos, 200, 20);
        lblMeterNumber.setFont(dataFont);
        p.add(lblMeterNumber);
        y_pos += y_inc;

        // Customer Name
        JLabel lblNameLabel = new JLabel("Customer Name:");
        lblNameLabel.setBounds(35, y_pos, 200, 20);
        lblNameLabel.setFont(labelFont);
        p.add(lblNameLabel);

        lblCustomerName = new JLabel("Loading...");
        lblCustomerName.setBounds(250, y_pos, 200, 20);
        lblCustomerName.setFont(dataFont);
        p.add(lblCustomerName);
        y_pos += y_inc;

        // Month Choice
        JLabel lblMonthLabel = new JLabel("Select Month:");
        lblMonthLabel.setBounds(35, y_pos, 200, 20);
        lblMonthLabel.setFont(labelFont);
        p.add(lblMonthLabel);

        cMonth = new Choice();
        cMonth.setBounds(250, y_pos, 200, 20);
        cMonth.setFont(labelFont);
        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        for(String month : months) cMonth.add(month);
        p.add(cMonth);
        y_pos += y_inc;

        // Units
        JLabel lblUnitsLabel = new JLabel("Units Consumed:");
        lblUnitsLabel.setBounds(35, y_pos, 200, 20);
        lblUnitsLabel.setFont(labelFont);
        p.add(lblUnitsLabel);

        lblUnitsConsumed = new JLabel("N/A");
        lblUnitsConsumed.setBounds(250, y_pos, 200, 20);
        lblUnitsConsumed.setFont(dataFont);
        p.add(lblUnitsConsumed);
        y_pos += y_inc;

        // Total Bill
        JLabel lblTotalLabel = new JLabel("Total Bill Amount (Rs.):");
        lblTotalLabel.setBounds(35, y_pos, 200, 20);
        lblTotalLabel.setFont(labelFont);
        p.add(lblTotalLabel);

        lblTotalBill = new JLabel("N/A");
        lblTotalBill.setBounds(250, y_pos, 200, 20);
        lblTotalBill.setFont(new Font("SanSerif", Font.BOLD, 18));
        lblTotalBill.setForeground(new Color(204, 51, 0));
        p.add(lblTotalBill);
        y_pos += y_inc;

        // Status
        JLabel lblStatusLabel = new JLabel("Payment Status:");
        lblStatusLabel.setBounds(35, y_pos, 200, 20);
        lblStatusLabel.setFont(labelFont);
        p.add(lblStatusLabel);

        lblStatus = new JLabel("Unknown");
        lblStatus.setBounds(250, y_pos, 200, 20);
        lblStatus.setFont(dataFont);
        lblStatus.setForeground(Color.RED);
        p.add(lblStatus);
        y_pos += y_inc;

        // --- Load Initial Data ---
        loadCustomerAndBill(cMonth.getSelectedItem());

        // Month listener
        cMonth.addItemListener(e -> loadCustomerAndBill(cMonth.getSelectedItem()));

        // Buttons
        btnPay = new JButton("Pay Bill Now");
        btnPay.setBackground(PRIMARY_BLUE);
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnPay.setBounds(100, 440, 140, 30);
        btnPay.addActionListener(this);
        p.add(btnPay);

        btnBack = new JButton("Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(PRIMARY_BLUE);
        btnBack.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnBack.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE));
        btnBack.setBounds(270, 440, 140, 30);
        btnBack.addActionListener(this);
        p.add(btnBack);

        // Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bill.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 450, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(550, 50, 300, 450);
        add(image);

        setVisible(true);
    }

    // Load customer name and bill info for the given month
    private void loadCustomerAndBill(String month) {
        try {
            DBConnection c = new DBConnection();

            // Customer Name
            String custQuery = "SELECT customer_name FROM customer WHERE meter_no = ?";
            PreparedStatement pstCust = c.getConnection().prepareStatement(custQuery);
            pstCust.setString(1, meter);
            ResultSet rsCust = pstCust.executeQuery();
            if(rsCust.next()) lblCustomerName.setText(rsCust.getString("customer_name"));

            // Bill info
            String billQuery = "SELECT * FROM bill WHERE meter_no = ? AND month = ?";
            PreparedStatement pstBill = c.getConnection().prepareStatement(billQuery);
            pstBill.setString(1, meter);
            pstBill.setString(2, month);
            ResultSet rsBill = pstBill.executeQuery();
            if(rsBill.next()) {
                lblUnitsConsumed.setText(rsBill.getString("units_consumed"));
                lblTotalBill.setText("Rs. " + rsBill.getString("total_bill"));
                lblStatus.setText(rsBill.getString("status"));
                if("Paid".equalsIgnoreCase(rsBill.getString("status"))) lblStatus.setForeground(new Color(34, 139, 34));
                else lblStatus.setForeground(Color.RED);
            } else {
                lblUnitsConsumed.setText("N/A");
                lblTotalBill.setText("N/A");
                lblStatus.setText("Bill Not Calculated");
                lblStatus.setForeground(Color.BLUE);
            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill info: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == btnPay) {
            String selectedMonth = cMonth.getSelectedItem();

            if("Bill Not Calculated".equals(lblStatus.getText()) || "N/A".equals(lblTotalBill.getText())) {
                JOptionPane.showMessageDialog(this, "Bill for " + selectedMonth + " not calculated.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if("Paid".equalsIgnoreCase(lblStatus.getText())) {
                JOptionPane.showMessageDialog(this, "Bill for " + selectedMonth + " is already paid.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                DBConnection c = new DBConnection();
                String updateQuery = "UPDATE bill SET status='Paid' WHERE meter_no=? AND month=?";
                PreparedStatement pstUpdate = c.getConnection().prepareStatement(updateQuery);
                pstUpdate.setString(1, meter);
                pstUpdate.setString(2, selectedMonth);
                pstUpdate.executeUpdate();

                lblStatus.setText("Paid");
                lblStatus.setForeground(new Color(34, 139, 34));
                JOptionPane.showMessageDialog(this, "Payment Successful!\nBill for " + selectedMonth + " marked as Paid.", "Payment Confirmed", JOptionPane.INFORMATION_MESSAGE);
            } catch(Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage());
            }
        } else if(ae.getSource() == btnBack) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new PayBill("100123"); // Example meter
    }
}
