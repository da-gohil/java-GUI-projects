package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

/**
 * BillDetails class fetches and displays all bills for a given meter number.
 * Typically used in both Admin and Customer views.
 */
public class BillDetails extends JFrame {

    private JTable table;
    private String meterNumber;

    public BillDetails(String meter) {
        super("Bill Details - Meter: " + meter);

        this.meterNumber = meter;

        // --- Frame Setup ---
        setSize(700, 650);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- Table Setup ---
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        // --- Fetch Data ---
        loadBillData();

        // --- Scroll Pane ---
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Fetches bill records for the given meter and sets the JTable model.
     */
    private void loadBillData() {
        try {
            DBConnection db = new DBConnection();
            String query = "SELECT month, year, units, total_bill, status FROM bill WHERE meter_no = ?";
            PreparedStatement pst = db.getConnection().prepareStatement(query);
            pst.setString(1, meterNumber);
            ResultSet rs = pst.executeQuery();

            table.setModel(DbUtils.resultSetToTableModel(rs));

            if (!rs.isBeforeFirst()) { // No rows returned
                JOptionPane.showMessageDialog(this, "No bills found for meter: " + meterNumber,
                        "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Example usage, replace with a real meter number
        new BillDetails("100123");
    }
}
