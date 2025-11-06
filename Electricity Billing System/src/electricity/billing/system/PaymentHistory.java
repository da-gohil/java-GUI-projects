package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.*;

/**
 * PaymentHistory frame displays a table of all generated bills and their payment status
 * for a specific customer identified by meter number.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class PaymentHistory extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("SanSerif", Font.BOLD, 32);
    private static final Font BUTTON_FONT = new Font("SanSerif", Font.BOLD, 16);
    private static final Font TABLE_HEADER_FONT = new Font("SanSerif", Font.BOLD, 16);
    private static final Font TABLE_CELL_FONT = new Font("SanSerif", Font.PLAIN, 14);

    // --- Component Declarations ---
    private JTable table;
    private JButton btnBack;
    private final String meter;
    private MainHomePage mainHome; // Reference to the calling MainHomePage instance

    /**
     * Constructor for the PaymentHistory frame.
     * @param meterNumber The meter number of the customer.
     * @param mainHome Reference to the calling MainHomePage instance.
     */
    public PaymentHistory(String meterNumber, MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        this.meter = meterNumber;
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setTitle("Payment History for Meter No. " + meterNumber);
        setLayout(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // --- Heading ---
        JLabel lblHeading = new JLabel("CUSTOMER BILLING HISTORY (Meter: " + meterNumber + ")");
        lblHeading.setFont(HEADING_FONT);
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBounds(50, 50, FRAME_WIDTH - 100, 40);
        lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblHeading);

        // --- Table Columns ---
        String[] columnNames = {"Month", "Year", "Units Consumed", "Total Bill (Rs)", "Payment Status"};

        // --- Fetch Data from DB ---
        DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Use DefaultTableModel for dynamic data
        fetchBillData(model);

        // --- Create JTable ---
        table = new JTable(model);
        table.setFont(TABLE_CELL_FONT);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        // --- Table Header Styling ---
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(TABLE_HEADER_FONT);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // --- Scroll Pane Positioning (Centered) ---
        JScrollPane scrollPane = new JScrollPane(table);
        int tableWidth = 800;
        int tableHeight = 500;
        scrollPane.setBounds((FRAME_WIDTH - tableWidth) / 2, 120, tableWidth, tableHeight);
        add(scrollPane);

        // --- Back Button ---
        btnBack = createButton("Back", (FRAME_WIDTH / 2) - 60, 650, 120, 40);
        add(btnBack);

        setVisible(true);
    }
    
    // --- Helper Methods ---
    
    /** Creates a button with the universal white/black/blue style. */
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, height);
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

    /** Fetches bill data and populates the DefaultTableModel. */
    private void fetchBillData(DefaultTableModel model) {
        String query = "SELECT month, year, units, total_bill, status FROM bill WHERE meter_no = ?";
        
        // Use PreparedStatement for safety and efficiency
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setString(1, meter);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("month"),
                        rs.getInt("year"),
                        rs.getInt("units"),
                        rs.getDouble("total_bill"),
                        rs.getString("status")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            
            // Back-to-Home logic
            if (mainHome != null) {
                mainHome.setVisible(true); // Show existing main homepage
            }
        }
    }

    public static void main(String[] args) {
        //new PaymentHistory("100123", null); // Example test with null for standalone
    }
}