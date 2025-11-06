package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;

/**
 * BillReport class displays all bills in the system for Admin view.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class BillReport extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 30);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font TABLE_HEADER_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font TABLE_CELL_FONT = new Font("Tahoma", Font.PLAIN, 14);

    // --- Component Declarations ---
    private JTable table;
    private JButton btnBack;
    private MainHomePage mainHome; // Reference to the calling MainHomePage instance

    /**
     * Constructor for the BillReport frame.
     * @param mainHome Reference to the calling MainHomePage instance (can be null for testing).
     */
    public BillReport(MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        super("Bill Report");
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // --- Heading ---
        JLabel lblHeading = new JLabel("SYSTEM BILLING REPORT", SwingConstants.CENTER);
        lblHeading.setFont(HEADING_FONT);
        lblHeading.setForeground(PRIMARY_BLUE);
        lblHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeading, BorderLayout.NORTH);

        // --- Table Setup ---
        table = new JTable();
        table.setFont(TABLE_CELL_FONT);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        
        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(TABLE_HEADER_FONT);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Padding around table
        add(sp, BorderLayout.CENTER);

        // --- Button Panel (South) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Centered flow layout

        // Back button (Mandated Style)
        btnBack = createButton("Back to Home", 0, 0, 180, 45); 
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load Data
        loadBillReportData();

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
     * Fetches all bill records from the database.
     */
    private void loadBillReportData() {
        // Updated query to include 'year' in the bill table structure
        String query = "SELECT meter_no, month, year, units, total_bill, status FROM bill ORDER BY year DESC, FIELD(month, 'January','February','March','April','May','June','July','August','September','October','November','December'), meter_no";
        
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // DbUtils.resultSetToTableModel requires the Rs2XML.jar library
            table.setModel(DbUtils.resultSetToTableModel(rs));
            
            // Check if any data was loaded
            if (!rs.isBeforeFirst() && !rs.isAfterLast()) { 
                // Only show dialog if table is genuinely empty after attempt
                JOptionPane.showMessageDialog(this, "No bills found in the system.",
                        "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill report. Check 'bill' table and DB connection: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             e.printStackTrace();
            JOptionPane.showMessageDialog(this, "A connection error occurred: " + e.getMessage(),
                    "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            
            // Back-to-Home logic
            if (mainHome != null) {
                mainHome.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        //new BillReport(null); // Example test with null for standalone
    }
}