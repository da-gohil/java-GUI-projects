package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulates the external payment gateway window (e.g., Paytm website) using JEditorPane.
 * Now returns to the same PayBill instance instead of creating a new one.
 */
public class PayTM extends JFrame implements ActionListener {

    private PayBill parentBillFrame; // Reference to the original PayBill frame
    JButton btnBack;

    // Define the primary blue accent color and light background mandated by the style guide
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color LIGHT_BG = new Color(230, 240, 250);

    public PayTM(PayBill parent) {
        this.parentBillFrame = parent;
        setTitle("External Payment Gateway (Simulated)");

        setSize(800, 600);
        setLocation(400, 150);

        // Use BorderLayout for a clear content/control separation
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

        // --- 1. JEditorPane (Web View) ---
        JEditorPane j = new JEditorPane();
        j.setEditable(false);

        try {
            // Attempt to load the external payment URL
            j.setPage("https://paytm.com/online-payments");
        } catch (Exception e) {
            // Fallback content if the external URL cannot be loaded
            j.setContentType("text/html");
            j.setText("<html><body style='font-family: SanSerif; padding: 20px; color: #333;'>"
                    + "<h2 style='color: " + String.format("#%06x", PRIMARY_BLUE.getRGB() & 0xFFFFFF) + ";'>Payment Gateway Access Error</h2>"
                    + "<p>Could not load Payment Gateway URL: https://paytm.com/online-payments</p>"
                    + "<p>This frame simulates the opening of an external payment site.</p></body></html>");
        }

        // Add a scroll pane around the editor pane
        JScrollPane scrollPane = new JScrollPane(j);
        add(scrollPane, BorderLayout.CENTER);

        // --- 2. Control Panel (Bottom) ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(LIGHT_BG);

        // --- 3. Back Button (Styled to be a secondary action) ---
        btnBack = new JButton("Back to Bill");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(PRIMARY_BLUE);
        btnBack.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnBack.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1));
        btnBack.setPreferredSize(new Dimension(150, 35));
        btnBack.addActionListener(this);

        controlPanel.add(btnBack);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBack) {
            // Close this frame
            setVisible(false);

            // Show the original PayBill frame again
            if (parentBillFrame != null) {
                parentBillFrame.setVisible(true);
            }
        }
    }

    // Main method for testing (creates a dummy PayBill frame)
    public static void main(String[] args) {
        PayBill dummyBill = new PayBill("100123");
        dummyBill.setVisible(true);
        new PayTM(dummyBill);
    }
}
