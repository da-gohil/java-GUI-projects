package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// NOTE: This class assumes the MeterInfo is opened after NewCustomer, 
// and receives the meter number as a parameter.
public class MeterInfo extends JFrame implements ActionListener {

    String meterNumber; // Field to hold the meter number passed from NewCustomer
    JComboBox<String> meterLocation, meterType, phaseCode, billType, days;
    JButton submitButton, cancelButton;

    // Constructor that accepts the meter number
    MeterInfo(String meterNumber) {
        this.meterNumber = meterNumber; // Store the meter number
        
        setSize(700, 500);
        setLocation(400, 200);
        setTitle("Meter Information");

        // --- Panel Setup ---
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173, 216, 230));
        add(p);

        // --- Heading ---
        JLabel heading = new JLabel("Meter Information");
        heading.setBounds(180, 10, 250, 25);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 24));
        p.add(heading);
        
        // --- 1. Meter Number (Non-editable) ---
        JLabel lblMeterNo = new JLabel("Meter Number");
        lblMeterNo.setBounds(100, 80, 150, 20);
        p.add(lblMeterNo);
        
        JLabel meterNumberValue = new JLabel(meterNumber); // Display the parsed  meter number
        meterNumberValue.setBounds(280, 80, 200, 20);
        p.add(meterNumberValue);

        // --- 2. Meter Location (Dropdown) ---
        JLabel lblLocation = new JLabel("Meter Location");
        lblLocation.setBounds(100, 120, 150, 20);
        p.add(lblLocation);

        String[] locationOptions = {"Outside", "Inside"};
        meterLocation = new JComboBox<>(locationOptions);
        meterLocation.setBounds(280, 120, 200, 20);
        meterLocation.setBackground(Color.WHITE);
        p.add(meterLocation);

        // --- 3. Meter Type (Dropdown) ---
        JLabel lblType = new JLabel("Meter Type");
        lblType.setBounds(100, 160, 150, 20);
        p.add(lblType);

        String[] typeOptions = {"Electric Meter", "Solar Meter", "Smart Meter"};
        meterType = new JComboBox<>(typeOptions);
        meterType.setBounds(280, 160, 200, 20);
        meterType.setBackground(Color.WHITE);
        p.add(meterType);

        // --- 4. Phase Code (Dropdown) ---
        JLabel lblPhase = new JLabel("Phase Code");
        lblPhase.setBounds(100, 200, 150, 20);
        p.add(lblPhase);

        String[] phaseOptions = {"011", "022", "033", "044", "055", "066", "077", "088", "099"};
        phaseCode = new JComboBox<>(phaseOptions);
        phaseCode.setBounds(280, 200, 200, 20);
        phaseCode.setBackground(Color.WHITE);
        p.add(phaseCode);

        // --- 5. Bill Type (Dropdown) ---
        JLabel lblBillType = new JLabel("Bill Type");
        lblBillType.setBounds(100, 240, 150, 20);
        p.add(lblBillType);

        String[] billOptions = {"Normal", "Commercial", "Industrial"};
        billType = new JComboBox<>(billOptions);
        billType.setBounds(280, 240, 200, 20);
        billType.setBackground(Color.WHITE);
        p.add(billType);

        // --- 6. Days (Dropdown/Fixed) ---
        JLabel lblDays = new JLabel("Days");
        lblDays.setBounds(100, 280, 150, 20);
        p.add(lblDays);

        String[] dayOptions = {"30 Days"};
        days = new JComboBox<>(dayOptions); // Use JComboBox for consistency with the image
        days.setBounds(280, 280, 200, 20);
        days.setBackground(Color.WHITE);
        p.add(days);
         
        // --- 7. Note ---
        JLabel lblNote = new JLabel("Note");
        lblNote.setBounds(100, 320, 150, 20);
        p.add(lblNote);

        JLabel noteValue = new JLabel("By Default Bill is calculated for 30 days only");
        noteValue.setBounds(280, 320, 300, 20);
        p.add(noteValue);

        // --- Submit Button ---
        submitButton = new JButton("Submit");
        submitButton.setBounds(150, 390, 100, 25);
        submitButton.setBackground(Color.WHITE);
        submitButton.setForeground(Color.BLACK);
        submitButton.addActionListener(this);
        p.add(submitButton);

        // --- Cancel Button ---
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(300, 390, 100, 25);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);
        p.add(cancelButton);

        // --- Image (Optional, to match the visual style of your previous frame) ---
        // For this frame, I'll skip the image to keep the panel centered as shown in the image.
        
        setVisible(true);
    }

    // ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            
            // 1. Retrieve data
            String meter = meterNumber;
            String location = (String) meterLocation.getSelectedItem();
            String type = (String) meterType.getSelectedItem();
            String phase = (String) phaseCode.getSelectedItem();
            String typebill = (String) billType.getSelectedItem();
            String totalDays = (String) days.getSelectedItem(); // Should be "30 Days"
             
            // 2. Construct SQL Query (using plain Statement for consistency with previous code)
            // Assuming a table named 'meter_info' exists with columns:
            // meter_no, meter_location, meter_type, phase_code, bill_type, days
            String query = "INSERT INTO meter_info VALUES('" + meterNumber + "', '" + location + "', '" + type + "', '" + phase + "', '" + typebill + "', '" + totalDays + "')";
            
            try {
                // Initialize Connection (Assuming DBConnection class exists)
                // Use the same connection class as your NewCustomer frame.
                DBConnection c = new DBConnection(); 
                
                // Execute the query
                c.stmt.executeUpdate(query);
                
                JOptionPane.showMessageDialog(null, "Meter Information Submitted Successfully");
                setVisible(false);
                
                // Navigate to the main application dashboard or next logical screen
                // new Project().setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: Failed to submit meter info. " + e.getMessage());
            }

        } else if (ae.getSource() == cancelButton) {
            setVisible(false); // Close the current frame
        }
    }
    
    // Optional main method for standalone testing
    public static void main(String[] args) {
        new MeterInfo(""); 
    }
}