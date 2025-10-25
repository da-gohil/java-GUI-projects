package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class ViewInformation extends JFrame implements ActionListener{

    JButton cancel;
    
    /**
     * Constructor for ViewInformation frame.
     * @param meter The meter number of the customer to view.
     */
    ViewInformation(String meter) {
        setBounds(350, 150, 850, 650);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        // --- Heading ---
        JLabel heading = new JLabel("VIEW CUSTOMER INFORMATION");
        heading.setBounds(250, 0, 500, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20)); // Changed to Bold for emphasis
        add(heading);
        
        // --- 1. Name ---
        JLabel lblname = new JLabel("Name");
        lblname.setBounds(70, 80, 100, 20);
        add(lblname);
        
        JLabel name = new JLabel("");
        name.setBounds(250, 80, 200, 20); // Increased width
        add(name);
        
        // --- 2. Meter Number ---
        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(70, 140, 100, 20);
        add(lblmeternumber);
        
        JLabel meternumber = new JLabel("");
        meternumber.setBounds(250, 140, 200, 20); // Increased width
        add(meternumber);
        
        // --- 3. Address ---
        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(70, 200, 100, 20);
        add(lbladdress);
        
        JLabel address = new JLabel("");
        address.setBounds(250, 200, 200, 20); // Increased width
        add(address);
        
        // --- 4. City ---
        JLabel lblcity = new JLabel("City");
        lblcity.setBounds(70, 260, 100, 20);
        add(lblcity);
        
        JLabel city = new JLabel("");
        city.setBounds(250, 260, 200, 20); // Increased width
        add(city);
        
        // --- 5. State (Moved to right column) ---
        JLabel lblstate = new JLabel("State");
        lblstate.setBounds(500, 80, 100, 20);
        add(lblstate);
        
        JLabel state = new JLabel("");
        state.setBounds(650, 80, 200, 20); // Increased width
        add(state);
        
        // --- 6. Email ---
        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(500, 140, 100, 20);
        add(lblemail);
        
        JLabel email = new JLabel("");
        email.setBounds(650, 140, 200, 20); // Increased width
        add(email);
        
        // --- 7. Phone ---
        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(500, 200, 100, 20);
        add(lblphone);
        
        JLabel phone = new JLabel("");
        phone.setBounds(650, 200, 200, 20); // Increased width
        add(phone);
        
        // --- Fetch Data from Database ---
        try {
            DBConnection c = new DBConnection(); // FIX: Used DBConnection
            // FIX: Used c.stmt and customer_name column name
            ResultSet rs = c.stmt.executeQuery("SELECT * FROM customer WHERE meter_no = '"+meter+"'");
            
            if(rs.next()) {
                name.setText(rs.getString("customer_name")); // FIX: Used customer_name
                address.setText(rs.getString("address"));
                city.setText(rs.getString("city"));
                state.setText(rs.getString("state"));
                email.setText(rs.getString("email"));
                phone.setText(rs.getString("phone"));
                meternumber.setText(rs.getString("meter_no"));
            } else {
                JOptionPane.showMessageDialog(this, "No customer found for meter number: " + meter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer data: " + e.getMessage());
        }
        
        // --- Cancel Button ---
        cancel = new JButton("Cancel");
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setBounds(350, 340, 100, 25);
        add(cancel);
        cancel.addActionListener(this);
        
        // --- Image ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/viewcustomer.jpg"));
        Image i2 = i1.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(20, 350, 600, 300);
        add(image);
        
        // Add a panel for a container-like look
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173, 216, 230)); // Light Blue
        p.setBounds(10, 50, 830, 380);
        add(p);
        
        // Move components inside the panel
        p.add(lblname); p.add(name);
        p.add(lblmeternumber); p.add(meternumber);
        p.add(lbladdress); p.add(address);
        p.add(lblcity); p.add(city);
        p.add(lblstate); p.add(state);
        p.add(lblemail); p.add(email);
        p.add(lblphone); p.add(phone);
        p.add(cancel);
        
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
