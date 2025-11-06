package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * Simulates the external payment gateway window (e.g., Paytm website) using JEditorPane.
 * Now enforces the fixed 1024x768 size and consistent button styling.
 */
public class PayTM extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("SanSerif", Font.BOLD, 16);

    // --- Component Declarations ---
    private PayBill parentBillFrame; // Reference to the original PayBill frame
    JButton btnBack;

    /**
     * Constructor for the simulated payment gateway.
     * @param parent The calling PayBill frame instance.
     */
    public PayTM(PayBill parent) {
        this.parentBillFrame = parent;
        setTitle("External Payment Gateway (Simulated)");

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Use BorderLayout for a clear content/control separation
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // --- 1. JEditorPane (Web View) ---
        JEditorPane j = new JEditorPane();
        j.setEditable(false);

        try {
            // Attempt to load the external payment URL
            j.setPage("https://paytm.com/online-payments");
        } catch (Exception e) {
            // Fallback content if the external URL cannot be loaded
            j.setContentType("text/html");
            j.setText("<html><body style='font-family: Arial, sans-serif; padding: 50px; color: #333; background-color: #f9f9f9;'>"
                            + "<h2 style='color: " + String.format("#%06x", PRIMARY_BLUE.getRGB() & 0xFFFFFF) + ";'>Payment Gateway Access Error</h2>"
                            + "<p style='font-size: 16px;'>Could not load Payment Gateway URL. Displaying a secure simulation screen instead.</p>"
                            + "<div style='margin-top: 30px; padding: 20px; border: 1px solid #ccc; background-color: #fff;'>"
                            + "<strong>SECURE PAYMENT SIMULATION:</strong>"
                            + "<ul><li>Bill Amount: **$XXX.XX** (Passed from PayBill)</li>"
                            + "<li>Meter No: **" + (parent != null ? parent.getMeterNumber() : "N/A") + "**</li></ul>"
                            + "<p>Press 'Back to Bill' to simulate payment completion and update the status.</p></div></body></html>");
        }

        // Add a scroll pane around the editor pane
        JScrollPane scrollPane = new JScrollPane(j);
        add(scrollPane, BorderLayout.CENTER);

        // --- 2. Control Panel (Bottom) ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        controlPanel.setBackground(BACKGROUND_COLOR); // Match frame background
        
        // --- 3. Back Button (Consistent Style: White BG, Black Text, Blue Border) ---
        btnBack = new JButton("Back to Bill");
        btnBack.setBounds(0, 0, 180, 40); // Layout manager handles bounds, but set size for consistency
        btnBack.setFont(BUTTON_FONT);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Universal Style: White BG, Black Text, Blue Border
        btnBack.setBackground(BACKGROUND_COLOR);
        btnBack.setForeground(Color.BLACK);
        btnBack.setBorder(new LineBorder(PRIMARY_BLUE, 2));
        
        btnBack.addActionListener(this);

        controlPanel.add(btnBack);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    // NOTE: This method is needed because PayTM's main method uses it, but 
    // we cannot assume it exists until the user provides PayBill.
    // public static class PayBill extends JFrame { 
    //     private String meter; 
    //     public PayBill(String meter) { this.meter = meter; } 
    //     public String getMeterNumber() { return meter; }
    // }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBack) {
            // Close this frame
            setVisible(false);
            dispose();

            // Show the original PayBill frame again
            if (parentBillFrame != null) {
                parentBillFrame.simulatePaymentCompletion(); // Assume this method updates the status
                parentBillFrame.setVisible(true);
            }
        }
    }
    
    // Main method for testing (must be commented out or fixed once all classes are available)
    public static void main(String[] args) {
        // Since we don't have PayBill yet, we can't test this directly.
        // We will proceed with the next component request.
    }
}