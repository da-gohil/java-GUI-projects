package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * ViewInformation frame displays all customer details in a read-only format.
 */
public class ViewInformation extends JFrame implements ActionListener {

    JButton cancel;
    JLabel lblName, lblMeterNumber, lblAddress, lblCity, lblState, lblEmail, lblPhone;

    /**
     * Constructor for ViewInformation frame.
     * @param meter The meter number of the customer to view.
     */
    public ViewInformation(String meter) {
        setBounds(350, 150, 850, 650);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setTitle("View Customer Information");

        // --- Heading ---
        JLabel heading = new JLabel("VIEW CUSTOMER INFORMATION");
        heading.setBounds(250, 10, 500, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading);

        // --- Main Panel ---
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(173, 216, 230)); // Light Blue
        panel.setBounds(10, 60, 830, 380);
        add(panel);

        // --- Labels ---
        int leftX = 50, rightX = 500, yPos = 50, yIncrement = 50;

        JLabel lblNameLabel = new JLabel("Name:");
        lblNameLabel.setBounds(leftX, yPos, 120, 20);
        panel.add(lblNameLabel);
        lblName = new JLabel();
        lblName.setBounds(leftX + 180, yPos, 200, 20);
        panel.add(lblName);

        JLabel lblMeterLabel = new JLabel("Meter Number:");
        lblMeterLabel.setBounds(leftX, yPos + yIncrement, 120, 20);
        panel.add(lblMeterLabel);
        lblMeterNumber = new JLabel();
        lblMeterNumber.setBounds(leftX + 180, yPos + yIncrement, 200, 20);
        panel.add(lblMeterNumber);

        JLabel lblAddressLabel = new JLabel("Address:");
        lblAddressLabel.setBounds(leftX, yPos + 2 * yIncrement, 120, 20);
        panel.add(lblAddressLabel);
        lblAddress = new JLabel();
        lblAddress.setBounds(leftX + 180, yPos + 2 * yIncrement, 200, 20);
        panel.add(lblAddress);

        JLabel lblCityLabel = new JLabel("City:");
        lblCityLabel.setBounds(leftX, yPos + 3 * yIncrement, 120, 20);
        panel.add(lblCityLabel);
        lblCity = new JLabel();
        lblCity.setBounds(leftX + 180, yPos + 3 * yIncrement, 200, 20);
        panel.add(lblCity);

        JLabel lblStateLabel = new JLabel("State:");
        lblStateLabel.setBounds(rightX, yPos, 120, 20);
        panel.add(lblStateLabel);
        lblState = new JLabel();
        lblState.setBounds(rightX + 150, yPos, 200, 20);
        panel.add(lblState);

        JLabel lblEmailLabel = new JLabel("Email:");
        lblEmailLabel.setBounds(rightX, yPos + yIncrement, 120, 20);
        panel.add(lblEmailLabel);
        lblEmail = new JLabel();
        lblEmail.setBounds(rightX + 150, yPos + yIncrement, 200, 20);
        panel.add(lblEmail);

        JLabel lblPhoneLabel = new JLabel("Phone:");
        lblPhoneLabel.setBounds(rightX, yPos + 2 * yIncrement, 120, 20);
        panel.add(lblPhoneLabel);
        lblPhone = new JLabel();
        lblPhone.setBounds(rightX + 150, yPos + 2 * yIncrement, 200, 20);
        panel.add(lblPhone);

        // --- Cancel Button ---
        cancel = new JButton("Cancel");
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setBounds(350, 300, 120, 30);
        cancel.addActionListener(this);
        panel.add(cancel);

        // --- Image ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/viewcustomer.jpg"));
        Image i2 = i1.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(120, 460, 600, 300);
        add(image);

        // --- Fetch Data from Database ---
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.stmt.executeQuery("SELECT * FROM customer WHERE meter_no = '" + meter + "'");

            if (rs.next()) {
                lblName.setText(rs.getString("customer_name"));
                lblMeterNumber.setText(rs.getString("meter_no"));
                lblAddress.setText(rs.getString("address"));
                lblCity.setText(rs.getString("city"));
                lblState.setText(rs.getString("state"));
                lblEmail.setText(rs.getString("email"));
                lblPhone.setText(rs.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(this, "No customer found for meter number: " + meter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer data: " + e.getMessage());
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new ViewInformation("100123"); // Example meter number
    }
}
