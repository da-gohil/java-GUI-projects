package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/**
 * UpdateInformation frame allows a customer to update their personal details
 * such as address, city, state, email, and phone.
 */
public class UpdateInformation extends JFrame implements ActionListener {

    private JTextField tfAddress, tfState, tfCity, tfEmail, tfPhone;
    private JButton btnUpdate, btnCancel;
    private JLabel lblName;
    private final String meter;

    UpdateInformation(String meter) {
        this.meter = meter;

        setBounds(300, 150, 1050, 450);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // --- Heading ---
        JLabel heading = new JLabel("UPDATE CUSTOMER INFORMATION");
        heading.setBounds(110, 0, 400, 30);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(heading);

        // --- Name Label ---
        JLabel lblNameTitle = new JLabel("Name");
        lblNameTitle.setBounds(30, 70, 100, 20);
        add(lblNameTitle);

        lblName = new JLabel("");
        lblName.setBounds(230, 70, 200, 20);
        add(lblName);

        // --- Meter Number ---
        JLabel lblMeter = new JLabel("Meter Number");
        lblMeter.setBounds(30, 110, 100, 20);
        add(lblMeter);

        JLabel lblMeterValue = new JLabel(meter);
        lblMeterValue.setBounds(230, 110, 200, 20);
        add(lblMeterValue);

        // --- Address ---
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setBounds(30, 150, 100, 20);
        add(lblAddress);

        tfAddress = new JTextField();
        tfAddress.setBounds(230, 150, 200, 20);
        add(tfAddress);

        // --- City ---
        JLabel lblCity = new JLabel("City");
        lblCity.setBounds(30, 190, 100, 20);
        add(lblCity);

        tfCity = new JTextField();
        tfCity.setBounds(230, 190, 200, 20);
        add(tfCity);

        // --- State ---
        JLabel lblState = new JLabel("State");
        lblState.setBounds(30, 230, 100, 20);
        add(lblState);

        tfState = new JTextField();
        tfState.setBounds(230, 230, 200, 20);
        add(tfState);

        // --- Email ---
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(30, 270, 100, 20);
        add(lblEmail);

        tfEmail = new JTextField();
        tfEmail.setBounds(230, 270, 200, 20);
        add(tfEmail);

        // --- Phone ---
        JLabel lblPhone = new JLabel("Phone");
        lblPhone.setBounds(30, 310, 100, 20);
        add(lblPhone);

        tfPhone = new JTextField();
        tfPhone.setBounds(230, 310, 200, 20);
        add(tfPhone);

        // --- Fetch Existing Data ---
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.stmt.executeQuery("SELECT * FROM customer WHERE meter_no = '" + meter + "'");

            if (rs.next()) {
                lblName.setText(rs.getString("customer_name"));
                tfAddress.setText(rs.getString("address"));
                tfCity.setText(rs.getString("city"));
                tfState.setText(rs.getString("state"));
                tfEmail.setText(rs.getString("email"));
                tfPhone.setText(rs.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user data: " + e.getMessage());
        }

        // --- Buttons ---
        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(Color.BLACK);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBounds(70, 360, 100, 25);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBounds(230, 360, 100, 25);
        btnCancel.addActionListener(this);
        add(btnCancel);

        // --- Image ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/update.jpg"));
        Image i2 = i1.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(550, 50, 400, 300);
        add(image);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnUpdate) {
            String address = tfAddress.getText();
            String city = tfCity.getText();
            String state = tfState.getText();
            String email = tfEmail.getText();
            String phone = tfPhone.getText();

            try {
                DBConnection c = new DBConnection();
                String query = "UPDATE customer SET address = '" + address + "', city = '" + city + 
                               "', state = '" + state + "', email = '" + email + "', phone = '" + phone + 
                               "' WHERE meter_no = '" + meter + "'";

                c.stmt.executeUpdate(query);

                JOptionPane.showMessageDialog(this, "User Information Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating information: " + e.getMessage());
            }
        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateInformation("100123");
    }
}
