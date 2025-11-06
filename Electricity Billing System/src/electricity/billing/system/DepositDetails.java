package electricity.billing.system;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.util.Calendar;

/**
 * DepositDetails class displays and allows filtering of all bill records,
 * acting as an administrative view of payment/deposit history.
 * Updated to use fixed 1024x768 size, consistent styling, and back-to-home navigation.
 */
public class DepositDetails extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font HEADING_FONT = new Font("Tahoma", Font.BOLD, 30);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font TABLE_CELL_FONT = new Font("Tahoma", Font.PLAIN, 14);


    private Choice cMeterNumber, cMonth, cYear;
    private JTable table;
    private JButton btnSearch, btnPrint, btnBack; // Added btnBack
    private MainHomePage mainHome; // Reference to the calling MainHomePage instance

    /**
     * Constructor for the DepositDetails frame.
     * @param mainHome Reference to the calling MainHomePage instance (can be null for testing).
     */
    public DepositDetails(MainHomePage mainHome) { // MODIFIED: Added MainHomePage reference
        super("Deposit & Bill Details");
        this.mainHome = mainHome;

        // --- Frame Setup (ENFORCING UI MANDATE) ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // --- Heading Panel (NORTH) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 20));
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel heading = new JLabel("CUSTOMER BILLING AND DEPOSIT HISTORY");
        heading.setFont(HEADING_FONT);
        heading.setForeground(PRIMARY_BLUE);
        headerPanel.add(heading);
        add(headerPanel, BorderLayout.NORTH);

        // --- Filter Panel (Top center) ---
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        filterPanel.setBackground(BACKGROUND_COLOR);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 1. Meter Number Filter
        filterPanel.add(createLabel("Meter No:"));
        cMeterNumber = createChoice(150);
        filterPanel.add(cMeterNumber);
        
        // 2. Month Filter
        filterPanel.add(createLabel("Month:"));
        cMonth = createChoice(120);
        String[] months = { "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };
        for (String m : months) cMonth.add(m);
        filterPanel.add(cMonth);

        // 3. Year Filter
        filterPanel.add(createLabel("Year:"));
        cYear = createChoice(100);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = 2020; y <= currentYear + 1; y++) cYear.add(String.valueOf(y));
        filterPanel.add(cYear);
        
        // 4. Search Button (Primary Blue)
        btnSearch = createStyledButton("Search", 120, 30, PRIMARY_BLUE, Color.WHITE);
        filterPanel.add(btnSearch);
        
        add(filterPanel, BorderLayout.NORTH); // Re-added NORTH section after removing heading

        // --- JTable Setup (CENTER) ---
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

        // --- Button Panel (SOUTH) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); 

        // Print Button (White BG, Blue Border)
        btnPrint = createStyledButton("Print Table", 150, 45, BACKGROUND_COLOR, PRIMARY_BLUE);
        buttonPanel.add(btnPrint);
        
        // Back Button (White BG, Blue Border)
        btnBack = createStyledButton("Back", 120, 45, BACKGROUND_COLOR, PRIMARY_BLUE);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        // Initial Data Load
        populateMeterNumbers();
        loadTable(); 

        setVisible(true);
    }
    
    // --- Helper UI Methods ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(LABEL_FONT);
//        lbl.setForeground(TEXT_DARK);
        return lbl;
    }
    
    private Choice createChoice(int width) {
        Choice c = new Choice();
        c.setPreferredSize(new Dimension(width, 25));
        c.setFont(TABLE_CELL_FONT);
        return c;
    }

    private JButton createStyledButton(String text, int width, int height, Color bg, Color border) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, height));
        btn.setFont(BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBackground(bg);
        btn.setForeground(bg.equals(PRIMARY_BLUE) ? Color.WHITE : Color.BLACK);
        btn.setBorder(new LineBorder(border, 2));
        
        btn.addActionListener(this);
        return btn;
    }
    
    // --- Data Methods ---
    private void populateMeterNumbers() {
        cMeterNumber.add("All Meters"); // Option to view all
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT meter_no FROM customer ORDER BY meter_no")) {
            while (rs.next()) {
                cMeterNumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading meter numbers: " + e.getMessage());
        }
    }

    // --- Load bills (All or Filtered) ---
    private void loadTable() {
        String meter = cMeterNumber.getSelectedItem();
        String month = cMonth.getSelectedItem();
        String year = cYear.getSelectedItem();
        
        StringBuilder query = new StringBuilder("SELECT meter_no, month, year, units, total_bill, status FROM bill WHERE 1=1");
        
        // If "All Meters" is not selected, apply the filter
        if (meter != null && !meter.equals("All Meters")) {
            query.append(" AND meter_no = '").append(meter).append("'");
        }
        
        // Apply month and year filters unconditionally
        if (month != null) {
            query.append(" AND month = '").append(month).append("'");
        }
        if (year != null) {
            query.append(" AND year = '").append(year).append("'");
        }

        query.append(" ORDER BY year DESC, FIELD(month, 'January','February','March','April','May','June','July','August','September','October','November','December') DESC, meter_no");


        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {
            
            table.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill details: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSearch) {
            loadTable(); // loadTable now handles the search logic
        } else if (ae.getSource() == btnPrint) {
            try {
                table.print(JTable.PrintMode.FIT_WIDTH);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error printing table: " + e.getMessage());
            }
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            dispose();
            if (mainHome != null) {
                mainHome.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        // new DepositDetails(null); // Example test
    }
}