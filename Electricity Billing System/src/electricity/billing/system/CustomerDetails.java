package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;

/**
 * CustomerDetails class displays all customer records dynamically from the database.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class CustomerDetails extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font TABLE_HEADER_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font TABLE_CELL_FONT = new Font("Tahoma", Font.PLAIN, 14);

    // --- Component Declarations ---
    private JTable table;
    private JButton btnPrint, btnBack;
    private MainHomePage mainHome; // Reference to the calling MainHomePage instance

    /**
     * Constructor for the CustomerDetails frame.
     * @param mainHome Reference to the calling MainHomePage instance (can be null for testing).
     */
    public CustomerDetails(MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        super("All Customer Details");
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // --- Heading ---
        JLabel lblHeading = new JLabel("ALL CUSTOMER ACCOUNTS", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblHeading.setForeground(PRIMARY_BLUE);
        add(lblHeading, BorderLayout.NORTH);

        // --- Table Setup ---
        table = new JTable();
        table.setFont(TABLE_CELL_FONT);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        
        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(TABLE_HEADER_FONT);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around table
        add(sp, BorderLayout.CENTER);

        // --- Button Panel (South) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Centered flow layout

        // Print button
        btnPrint = createButton("Print Records", 0, 0, 150, 40); // Bounds set by FlowLayout
        buttonPanel.add(btnPrint);

        // Back button
        btnBack = createButton("Back to Home", 0, 0, 150, 40); // Bounds set by FlowLayout
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load customer data
        loadCustomerData();

        setVisible(true);
    }
    
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

    /**
     * Dynamically loads customer data from the database.
     */
    private void loadCustomerData() {
        // Query should select all relevant columns for customer details
        String query = "SELECT * FROM customer"; 
        
        // Use try-with-resources for better connection management
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // DbUtils.resultSetToTableModel requires the Rs2XML.jar library
            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error fetching customer details. Please ensure the 'customer' table exists.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnPrint) {
            try {
                // Set print mode to fit the width of the page
                table.print(JTable.PrintMode.FIT_WIDTH);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Could not print the table: " + e.getMessage(),
                        "Print Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            
            // Back-to-Home logic
            if (mainHome != null) {
                mainHome.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        // new CustomerDetails(null); // Example test with null for standalone
    }
}