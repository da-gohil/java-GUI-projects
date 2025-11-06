package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

/**
 * BillReport class displays all bills in the system.
 * Typically for Admin users to view/report all bills.
 */
public class BillReport extends JFrame {

    private JTable table;

    public BillReport() {
        super("Bill Report");

        // --- Frame Setup ---
        setSize(800, 650);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- Table Setup ---
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        // --- Scroll Pane ---
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // --- Load Data ---
        loadBillReportData();

        setVisible(true);
    }

    /**
     * Fetches all bill records from the database.
     */
    private void loadBillReportData() {
        try {
            DBConnection db = new DBConnection();
            String query = "SELECT meter_no, month, year, units, total_bill, status FROM bill ORDER BY year, month, meter_no";
            ResultSet rs = db.getStatement().executeQuery(query);

            table.setModel(DbUtils.resultSetToTableModel(rs));

            if (!rs.isBeforeFirst()) { // No rows returned
                JOptionPane.showMessageDialog(this, "No bills found in the system.",
                        "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill report: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BillReport();
    }
}
