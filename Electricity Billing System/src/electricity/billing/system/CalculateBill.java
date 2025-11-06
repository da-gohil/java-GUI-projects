package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.sql.*;

/**
 * CalculateBill class allows Admin to calculate and save monthly electricity bills for customers.
 */
public class CalculateBill extends JFrame implements ActionListener {

    private Choice cMeterNumber, cMonth, cYear;
    private JTextField tfUnits;
    private JButton btnCalculate, btnCancel;
    private JLabel lblName, lblAddress;

    public CalculateBill() {
        super("Calculate Electricity Bill");
        setSize(700, 500);
        setLocation(400, 200);
        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173, 216, 230));
        add(p, "Center");

        JLabel heading = new JLabel("Calculate Electricity Bill");
        heading.setBounds(180, 10, 350, 25);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        p.add(heading);

        // --- Meter Number ---
        JLabel lblMeter = new JLabel("Meter Number");
        lblMeter.setBounds(100, 80, 120, 20);
        p.add(lblMeter);

        cMeterNumber = new Choice();
        cMeterNumber.setBounds(240, 80, 200, 20);
        populateMeterNumbers();
        cMeterNumber.addItemListener(e -> updateCustomerDetails());
        p.add(cMeterNumber);

        // --- Customer Name ---
        JLabel lblCustomerName = new JLabel("Customer Name");
        lblCustomerName.setBounds(100, 120, 120, 20);
        p.add(lblCustomerName);
        lblName = new JLabel();
        lblName.setBounds(240, 120, 200, 20);
        p.add(lblName);

        // --- Address ---
        JLabel lblCustomerAddress = new JLabel("Address");
        lblCustomerAddress.setBounds(100, 160, 100, 20);
        p.add(lblCustomerAddress);
        lblAddress = new JLabel();
        lblAddress.setBounds(240, 160, 200, 20);
        p.add(lblAddress);
        updateCustomerDetails();

        // --- Units Consumed ---
        JLabel lblUnits = new JLabel("Units Consumed");
        lblUnits.setBounds(100, 200, 120, 20);
        p.add(lblUnits);
        tfUnits = new JTextField();
        tfUnits.setBounds(240, 200, 200, 20);
        p.add(tfUnits);

        // --- Month ---
        JLabel lblMonth = new JLabel("Billing Month");
        lblMonth.setBounds(100, 240, 100, 20);
        p.add(lblMonth);
        cMonth = new Choice();
        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        for(String m: months) cMonth.add(m);
        cMonth.setBounds(240, 240, 200, 20);
        p.add(cMonth);

        // --- Year ---
        JLabel lblYear = new JLabel("Billing Year");
        lblYear.setBounds(100, 280, 100, 20);
        p.add(lblYear);
        cYear = new Choice();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int y = currentYear-5; y <= currentYear+5; y++) cYear.add(String.valueOf(y));
        cYear.setBounds(240, 280, 200, 20);
        p.add(cYear);

        // --- Buttons ---
        btnCalculate = new JButton("Calculate & Submit");
        btnCalculate.setBounds(120, 350, 160, 30);
        btnCalculate.setBackground(Color.BLACK);
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.addActionListener(this);
        p.add(btnCalculate);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(300, 350, 120, 30);
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(this);
        p.add(btnCancel);

        // --- Image ---
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
            Image i2 = i1.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
            JLabel image = new JLabel(new ImageIcon(i2));
            add(image, "West");
        } catch(Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    private void populateMeterNumbers() {
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.getStatement().executeQuery("SELECT meter_no FROM customer");
            while(rs.next()) cMeterNumber.add(rs.getString("meter_no"));
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading meter numbers.");
        }
    }

    private void updateCustomerDetails() {
        String meter = cMeterNumber.getSelectedItem();
        if(meter == null) return;
        try {
            DBConnection c = new DBConnection();
            PreparedStatement pst = c.getConnection().prepareStatement("SELECT customer_name,address FROM customer WHERE meter_no=?");
            pst.setString(1, meter);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                lblName.setText(rs.getString("customer_name"));
                lblAddress.setText(rs.getString("address"));
            } else {
                lblName.setText("N/A"); lblAddress.setText("N/A");
            }
        } catch(Exception e) {
            e.printStackTrace();
            lblName.setText("Error"); lblAddress.setText("Error");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==btnCalculate) {
            try {
                String meter = cMeterNumber.getSelectedItem();
                String month = cMonth.getSelectedItem();
                String yearStr = cYear.getSelectedItem();
                int year = Integer.parseInt(yearStr);
                int units = Integer.parseInt(tfUnits.getText());

                DBConnection c = new DBConnection();
                ResultSet taxRs = c.getStatement().executeQuery("SELECT * FROM tax");
                if(!taxRs.next()) {
                    JOptionPane.showMessageDialog(this, "No tax configuration found.");
                    return;
                }

                double unitRate = taxRs.getDouble("cost_per_unit");
                double meterRent = taxRs.getDouble("meter_rent");
                double serviceCharge = taxRs.getDouble("service_charge");
                double serviceTax = taxRs.getDouble("service_tax");
                double swacchCess = taxRs.getDouble("swacch_bharat_cess");
                double fixedTax = taxRs.getDouble("fixed_tax");

                double total = units*unitRate + meterRent + serviceCharge + serviceTax + swacchCess + fixedTax;
                total = Math.round(total*100.0)/100.0;

                // Insert into bill table with PreparedStatement
                String insert = "INSERT INTO bill(meter_no,month,year,units,total_bill,status) VALUES(?,?,?,?,?,?)";
                PreparedStatement pst = c.getConnection().prepareStatement(insert);
                pst.setString(1, meter);
                pst.setString(2, month);
                pst.setInt(3, year);
                pst.setInt(4, units);
                pst.setDouble(5, total);
                pst.setString(6, "Unpaid");
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Bill calculated and saved successfully.\nTotal: $" + total);
                setVisible(false);

            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Units must be numeric.");
            } catch(SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "Bill for this month/year already exists for this meter.");
            } catch(Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error calculating bill: "+e.getMessage());
            }
        } else if(ae.getSource()==btnCancel) {
            // Return to main homepage instead of just closing
            setVisible(false);
            new MainHomePage("Admin", "N/A");
        }
    }

    public static void main(String[] args) {
        new CalculateBill();
    }
}
