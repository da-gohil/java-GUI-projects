package electricity.billing.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * GenerateBill allows viewing a detailed bill for a selected customer and month.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class GenerateBill extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 30);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);

    // --- Component Declarations ---
    private final String meter;
    private JTextArea area;
    private JButton btnGenerate, btnBack;
    private Choice monthChoice;
    private JScrollPane scrollPane;
    private MainHomePage mainHome;

    /**
     * Standard Constructor: Takes the meter number and initializes the frame.
     * @param meter The meter number for which the bill is being generated.
     * @param mainHome Reference to the calling MainHomePage instance (can be null for testing).
     */
    public GenerateBill(String meter, MainHomePage mainHome) {
        this.meter = meter;
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setTitle("Generate Bill - Meter No: " + this.meter);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // --- Header Panel (Top Control) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 20));
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel heading = new JLabel("Generate/View Bill for Meter: " + this.meter);
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_BLUE);
        headerPanel.add(heading);
        
        // Month Selection
        monthChoice = new Choice();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for(String m : months) monthChoice.add(m);
        monthChoice.setFont(new Font("Tahoma", Font.PLAIN, 16));
        monthChoice.setPreferredSize(new Dimension(150, 30));
        headerPanel.add(monthChoice);
        
        // Generate Button (Mandated Style Override for Primary Action)
        btnGenerate = createButton("Generate Bill", 0, 0, 180, 45); 
        btnGenerate.setBackground(PRIMARY_BLUE);
        btnGenerate.setForeground(Color.WHITE);
        headerPanel.add(btnGenerate);
        
        // Back Button (Mandated Style)
        btnBack = createButton("Back", 0, 0, 150, 45); 
        headerPanel.add(btnBack);
        
        add(headerPanel, BorderLayout.NORTH);

        // --- Text Area (Center - Bill Content) ---
        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));
        area.setBackground(new Color(240, 240, 240)); // Light gray background for the bill content
        
        scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(PRIMARY_BLUE, 2), 
            "Bill Document", 
            TitledBorder.CENTER, 
            TitledBorder.TOP, 
            new Font("Monospaced", Font.BOLD, 18), PRIMARY_BLUE));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial content on start
        generateBillContent(monthChoice.getSelectedItem()); 
        
        setVisible(true);
    }
    
    /** Creates a button with the universal white/black/blue style. */
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, height));
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

    /**
     * Placeholder for bill generation logic (Needs database integration).
     */
    private void generateBillContent(String selectedMonth) {
        // --- Database calls for real data (Customer Info, Bill Info, Tariffs) should go here ---
        
        // --- Mock Bill Content Generation (Kept for functionality) ---
        // Assume database calls populate these variables:
        String customerName = "John Doe (Mock Data)";
        int unitsConsumed = 150; 
        double ratePerUnit = 7.50; 
        double fixedCharge = 50.00; 
        double totalCharge = (unitsConsumed * ratePerUnit) + fixedCharge;
        String billingYear = "2024";

        StringBuilder bill = new StringBuilder();
        bill.append("\n\t\tELECTRICITY BILL - ").append(selectedMonth.toUpperCase()).append(" ").append(billingYear).append("\n");
        bill.append("\t\t======================================================\n\n");
        
        bill.append(String.format("%-40s: %s\n", "\tCUSTOMER NAME", customerName));
        bill.append(String.format("%-40s: %s\n", "\tMETER NUMBER", this.meter));
        bill.append(String.format("%-40s: %s %s\n", "\tBILLING MONTH", selectedMonth, billingYear));
        bill.append("\n\t\t------------------------------------------------------\n");

        bill.append(String.format("%-40s: %d Units\n", "\tUNITS CONSUMED", unitsConsumed));
        bill.append(String.format("%-40s: Rs. %.2f\n", "\tRATE PER UNIT", ratePerUnit));
        bill.append(String.format("%-40s: Rs. %.2f\n", "\tFIXED CHARGES", fixedCharge));

        bill.append("\n\t\t======================================================\n");
        bill.append(String.format("%-40s: Rs. %.2f\n", "\tTOTAL AMOUNT DUE", totalCharge));
        bill.append("\t\t======================================================\n");
        
        bill.append("\n\n\tNote: This is a detailed bill breakdown. Payment status can be checked\n");
        bill.append("\tthrough the Pay Bill section. Please pay before the due date.");
        
        area.setText(bill.toString());
    }

    // Event handling for the button
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnGenerate) {
            String selectedMonth = monthChoice.getSelectedItem();
            generateBillContent(selectedMonth);
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            if (mainHome != null) {
                mainHome.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        new GenerateBill("10012345", null).setVisible(true); // Example usage
    }
}