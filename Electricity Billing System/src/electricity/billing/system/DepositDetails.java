package electricity.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.util.Calendar;

/**
 * DepositDetails class displays and allows filtering of all bill records,
 * acting as an administrative view of payment/deposit history.
 */
public class DepositDetails extends JFrame implements ActionListener {

    private Choice cMeterNumber, cMonth, cYear;
    private JTable table;
    private JButton btnSearch, btnPrint;

    public DepositDetails() {
        super("Bill and Deposit Details");

        // --- Frame Setup ---
        setSize(750, 700);
        setLocation(350, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // --- Meter Number Filter ---
        JLabel lblMeterNumber = new JLabel("Search By Meter Number");
        lblMeterNumber.setBounds(20, 20, 180, 20);
        lblMeterNumber.setFont(new Font("SanSerif", Font.PLAIN, 14));
        add(lblMeterNumber);

        cMeterNumber = new Choice();
        cMeterNumber.setBounds(200, 20, 150, 20);
        add(cMeterNumber);

        // Populate Meter Numbers dynamically
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

        // --- Month Filter ---
        JLabel lblMonth = new JLabel("Search By Month");
        lblMonth.setBounds(20, 60, 120, 20);
        lblMonth.setFont(new Font("SanSerif", Font.PLAIN, 14));
        add(lblMonth);

        cMonth = new Choice();
        cMonth.setBounds(150, 60, 150, 20);
        String[] months = { "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };
        for (String m : months) cMonth.add(m);
        add(cMonth);

        // --- Year Filter ---
        JLabel lblYear = new JLabel("Search By Year");
        lblYear.setBounds(320, 60, 100, 20);
        lblYear.setFont(new Font("SanSerif", Font.PLAIN, 14));
        add(lblYear);

        cYear = new Choice();
        cYear.setBounds(420, 60, 100, 20);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = 2020; y <= currentYear; y++) cYear.add(String.valueOf(y));
        add(cYear);

        // --- JTable Setup ---
        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 100, 750, 550);
        add(sp);

        // --- Buttons ---
        btnSearch = new JButton("Search");
        btnSearch.setBounds(550, 20, 100, 25);
        btnSearch.setBackground(new Color(0, 122, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnSearch.addActionListener(this);
        add(btnSearch);

        btnPrint = new JButton("Print");
        btnPrint.setBounds(550, 60, 100, 25);
        btnPrint.setBackground(Color.WHITE);
        btnPrint.setForeground(new Color(0, 122, 255));
        btnPrint.setFont(new Font("SanSerif", Font.BOLD, 14));
        btnPrint.addActionListener(this);
        add(btnPrint);

        // Load initial table (all bills)
        loadTable();

        setVisible(true);
    }

    // --- Load all bills initially ---
    private void loadTable() {
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM bill")) {
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill details: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSearch) {
            searchBills();
        } else if (ae.getSource() == btnPrint) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error printing table: " + e.getMessage());
            }
        }
    }

    // --- Search bills by meter, month, and year ---
    private void searchBills() {
        String meter = cMeterNumber.getSelectedItem();
        String month = cMonth.getSelectedItem();
        String year = cYear.getSelectedItem();

        String query = "SELECT * FROM bill WHERE meter_no = ? AND month = ? AND year = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, meter);
            pst.setString(2, month);
            pst.setString(3, year);

            try (ResultSet rs = pst.executeQuery()) {
                table.setModel(DbUtils.resultSetToTableModel(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during search: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new DepositDetails();
    }
}
