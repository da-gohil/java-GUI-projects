package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GenerateBill extends JFrame implements ActionListener {

    String meter;
    JButton btnGenerate;
    Choice cMonth;
    JTextArea area;

    /**
     * Constructor for GenerateBill frame.
     * @param meter The meter number for which to generate the bill.
     */
    GenerateBill(String meter) {
        this.meter = meter;

        super("Generate Electricity Bill for Meter: " + meter);
        setSize(500, 800);
        setLocation(550, 30);

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 240, 250)); // Light Gray-Blue for a cleaner look
        
        // Use a FlowLayout for a simple header bar
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15)); 

        JLabel heading = new JLabel("Generate Bill");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        heading.setForeground(new Color(0, 100, 200));

        JLabel meterNumber = new JLabel("Meter No: " + meter);
        meterNumber.setFont(new Font("Tahoma", Font.BOLD, 14));

        cMonth = new Choice();

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
             cMonth.add(month);
        }
        
        cMonth.setFont(new Font("Tahoma", Font.PLAIN, 14));

        area = new JTextArea(50, 15);
        // Clearer initial instructions
        area.setText("\n\n\t--------------------------\n\tSelect a Month and click\n\t'Generate Bill' to see the\n\t detailed statement.");
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));
        area.setEditable(false);

        JScrollPane pane = new JScrollPane(area);

        // Apple-inspired primary button
        btnGenerate = new JButton("Generate Bill");
        btnGenerate.setBackground(new Color(0, 122, 255)); // Primary Accent Blue
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnGenerate.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGenerate.addActionListener(this);

        // Add components to the header panel
        panel.add(heading);
        panel.add(meterNumber);
        panel.add(cMonth);

        // Add components to the frame using BorderLayout
        add(panel, "North");
        add(pane, "Center");
        add(btnGenerate, "South");

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnGenerate) {
            try {
                DBConnection c = new DBConnection();

                String month = cMonth.getSelectedItem();

                // Start building the bill text
                area.setText("\n\t---------------------------------------------------\n");
                area.append("\t\t\tELECTRICITY BILL\n");
                area.append("\t\t\tFOR THE MONTH OF " + month.toUpperCase() + "\n");
                area.append("\t---------------------------------------------------\n\n");

                // 1. Fetch Customer Details
                ResultSet rs = c.stmt.executeQuery("SELECT * FROM customer WHERE meter_no = '"+meter+"'");

                if(rs.next()) {
                    area.append("\n  Customer Name: \t" + rs.getString("customer_name"));
                    area.append("\n  Meter Number: \t" + rs.getString("meter_no"));
                    area.append("\n  Address: \t\t" + rs.getString("address"));
                    area.append("\n  City: \t\t" + rs.getString("city"));
                    area.append("\n  State: \t\t" + rs.getString("state"));
                    area.append("\n  Email: \t\t" + rs.getString("email"));
                    area.append("\n  Phone: \t\t" + rs.getString("phone"));
                    area.append("\n---------------------------------------------------");
                    area.append("\n");
                }

                // 2. Fetch Meter Info
                rs = c.stmt.executeQuery("SELECT * FROM meter_info WHERE meter_no = '"+meter+"'");

                if(rs.next()) {
                    area.append("\n  Meter Location: \t" + rs.getString("meter_location"));
                    area.append("\n  Meter Type: \t\t" + rs.getString("meter_type"));
                    area.append("\n  Phase Code: \t\t" + rs.getString("phase_code"));
                    area.append("\n  Bill Type: \t\t" + rs.getString("bill_type"));
                    area.append("\n  Days: \t\t" + rs.getString("days"));
                    area.append("\n---------------------------------------------------");
                    area.append("\n");
                }

                // 3. Fetch Tariff/Tax Details
                // Using the 'tax' table name for consistency with CalculateBill.java
                rs = c.stmt.executeQuery("SELECT * FROM tax");

                if(rs.next()) {
                    area.append("\n  --- Tariff Charges Breakdown ---");
                    area.append("\n  Cost Per Unit: \t\t$" + rs.getString("cost_per_unit"));
                    area.append("\n  Meter Rent: \t\t$" + rs.getString("meter_rent"));
                    area.append("\n  Service Charge: \t$" + rs.getString("service_charge"));
                    area.append("\n  Service Tax: \t\t$" + rs.getString("service_tax"));
                    area.append("\n  Swacch Bharat Cess: \t$" + rs.getString("swacch_bharat_cess"));
                    area.append("\n  Fixed Tax: \t\t$" + rs.getString("fixed_tax"));
                    area.append("\n");
                }

                // 4. Fetch Calculated Bill Details for the month
                rs = c.stmt.executeQuery("SELECT * FROM bill WHERE meter_no = '"+meter+"' AND month='"+month+"'");

                if(rs.next()) {
                    area.append("\n  --- Monthly Consumption & Total ---");
                    area.append("\n  Units Consumed: \t\t" + rs.getString("units_consumed"));
                    area.append("\n  Bill Status: \t\t" + rs.getString("status"));
                    area.append("\n---------------------------------------------------");
                    area.append("\n  Total Payable: \t\t$" + rs.getString("total_bill"));
                    area.append("\n---------------------------------------------------\n");
                    area.append("\n\n\t\t\tAuthorized Signatory");
                    area.append("\n\t\t\t" + java.time.LocalDate.now()); // Add current date
                    area.append("\n");
                } else {
                    area.setText("\n\n\t!!! Bill for " + month + " not found !!!\n\tPlease ask the Admin to Calculate Bill first.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error generating bill: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Example usage: You would pass the meter number from the main login/home screen.
        new GenerateBill("100123");
    }
}
