package electricity.billing.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Assuming this class is a standard Swing JFrame for the bill generation UI
public class GenerateBill extends JFrame implements ActionListener {

    // Field to store the meter number
    private final String meter;
    
    private JTextArea area;
    private JButton generateButton;
    private Choice monthChoice;
    private JScrollPane scrollPane;

    /**
     * Standard Constructor: Takes the meter number and initializes the frame.
     * This fully-defined constructor prevents the "flexible constructors" error.
     * * @param meter The meter number for which the bill is being generated.
     */
    public GenerateBill(String meter) {
        // The problematic line was likely inside a constructor that the compiler
        // misinterpreted as a compact record constructor. We define it fully here.
        this.meter = meter; // This is the line 20 assignment

        setTitle("Generate Bill - Meter No: " + this.meter);
        setSize(500, 700);
        setLayout(new BorderLayout());
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window

        // --- Header Panel (Top) ---
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JLabel heading = new JLabel("Generate Bill for Meter:");
        heading.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(heading);

        // Month Selection
        monthChoice = new Choice();
        monthChoice.add("January");
        monthChoice.add("February");
        monthChoice.add("March");
        monthChoice.add("April");
        monthChoice.add("May");
        monthChoice.add("June");
        monthChoice.add("July");
        monthChoice.add("August");
        monthChoice.add("September");
        monthChoice.add("October");
        monthChoice.add("November");
        monthChoice.add("December");
        panel.add(monthChoice);
        
        // Generate Button
        generateButton = new JButton("Generate Bill");
        generateButton.setBackground(Color.BLUE);
        generateButton.setForeground(Color.WHITE);
        generateButton.addActionListener(this);
        panel.add(generateButton);

        add(panel, BorderLayout.NORTH);

        // --- Text Area (Center) ---
        area = new JTextArea(20, 50);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        add(scrollPane, BorderLayout.CENTER);

        // Center the frame on the screen
        setLocationRelativeTo(null);
    }
    
    // Placeholder for bill generation logic (Replace with actual database/logic)
    private void generateBillContent(String selectedMonth) {
        // --- Mock Bill Content Generation ---
        StringBuilder bill = new StringBuilder();
        bill.append("ELECTRICITY BILL - ").append(selectedMonth.toUpperCase()).append("\n");
        bill.append("==================================================\n");
        bill.append("Customer Name: John Doe (Mock Data)\n");
        bill.append("Meter Number: ").append(this.meter).append("\n");
        bill.append("Billing Cycle: ").append(selectedMonth).append(" 2024\n");
        bill.append("==================================================\n\n");
        
        // Mock Unit Consumption and Charges
        int unitsConsumed = 150; // Example
        double ratePerUnit = 7.50; // Example
        double fixedCharge = 50.00; // Example
        double totalCharge = (unitsConsumed * ratePerUnit) + fixedCharge;

        bill.append(String.format("%-25s: %d Units\n", "Units Consumed", unitsConsumed));
        bill.append(String.format("%-25s: Rs. %.2f\n", "Rate per Unit", ratePerUnit));
        bill.append(String.format("%-25s: Rs. %.2f\n", "Fixed Charges", fixedCharge));
        bill.append("\n==================================================\n");
        bill.append(String.format("%-25s: Rs. %.2f\n", "TOTAL AMOUNT DUE", totalCharge));
        bill.append("==================================================\n");
        bill.append("\nNote: Please pay the bill before the due date.");
        
        area.setText(bill.toString());
    }

    // Event handling for the button
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == generateButton) {
            String selectedMonth = monthChoice.getSelectedItem();
            generateBillContent(selectedMonth);
        }
    }

    public static void main(String[] args) {
        // Example usage: Pass a mock meter number
        new GenerateBill("10012345").setVisible(true);
    }
}
