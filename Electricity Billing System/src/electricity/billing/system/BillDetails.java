package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;

/**
 * BillDetails class fetches and displays all bills for a given meter number.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class BillDetails extends JFrame implements ActionListener {

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
    private final String meterNumber;
    private MainHomePage mainHome; // Reference to the calling MainHomePage instance

    /**
     * Constructor for the BillDetails frame.
     * @param meter The meter number of the customer.
     * @param mainHome Reference to the calling MainHomePage instance (can be null for testing).
     */
    public BillDetails(String meter, MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        super("Bill Details - Meter: " + meter);
        this.meterNumber = meter;
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // --- Heading ---
        JLabel lblHeading = new JLabel("BILLING DETAILS (Meter No: " + meterNumber + ")", SwingConstants.CENTER);
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

        // --- Fetch Data ---
        loadBillData();

        // --- Scroll Pane ---
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Padding around table
        add(sp, BorderLayout.CENTER);

        // --- Button Panel (South) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Centered flow layout

        // Back button (Mandated Style)
        btnBack = createButton("Back", 0, 0, 150, 45); 
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

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
     * Fetches bill records for the given meter and sets the JTable model.
     */
    private void loadBillData() {
        String query = "SELECT month, year, units, total_bill, status FROM bill WHERE meter_no = ? ORDER BY year DESC, FIELD(month, 'January','February','March','April','May','June','July','August','September','October','November','December')";
        
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setString(1, meterNumber);
            
            try (ResultSet rs = pst.executeQuery()) {
                table.setModel(DbUtils.resultSetToTableModel(rs));
                
                // Check if any data was loaded
                if (!rs.isBeforeFirst() && !rs.isAfterLast()) { 
                    JOptionPane.showMessageDialog(this, "No bills found for meter: " + meterNumber,
                            "No Data", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill data. Check 'bill' table and DB connection: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
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
        new BillDetails("100123", null); // Example usage
    }
}