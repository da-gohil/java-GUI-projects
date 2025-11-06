package electricity.billing.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class MainHomePage extends JFrame implements ActionListener {

    private String userType;
    private String meterNumber;
    private HashMap<String, JMenuItem> menuItems = new HashMap<>();

    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color TEXT_DARK = new Color(51, 51, 51);

    public MainHomePage(String userType, String meterNumber) {
        super("Electricity Billing System - " + userType + " View");

        this.userType = userType;
        this.meterNumber = meterNumber;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load main background image safely
        JLabel mainImage = loadImageLabel("icon/MainHomePage.jpg", 1550, 850, "Main Home Page");
        add(mainImage);

        JMenuBar mb = new JMenuBar();

        // --- Admin Master Menu dynamically from DB or fallback ---
        if ("Admin".equalsIgnoreCase(this.userType)) {
            JMenu masterMenu = new JMenu("Master");
            masterMenu.setForeground(PRIMARY_BLUE);

            addMenuItem(masterMenu, "New Customer", "icon/newCustomerMenuLogo.png", 'D');
            addMenuItem(masterMenu, "Customer Details", "icon/icon2.png", 'M');
            addMenuItem(masterMenu, "Deposit Details", "icon/icon4.png", 'N');
            addMenuItem(masterMenu, "Calculate Bill", "icon/icon5.png", 'C');

            mb.add(masterMenu);
        }

        // --- Utility Menu for all users dynamically ---
        JMenu utilityMenu = new JMenu("Utility");
        utilityMenu.setForeground(TEXT_DARK);

        addMenuItem(utilityMenu, "View Information", "icon/icon6.png", 'V');
        addMenuItem(utilityMenu, "Update Information", "icon/icon9.png", 'I');
        addMenuItem(utilityMenu, "Pay Bill", "icon/icon7.png", 'P');
        addMenuItem(utilityMenu, "Bill Details", "icon/icon8.png", 'L');
        addMenuItem(utilityMenu, "Generate Bill", "icon/icon10.png", 'G');
        addMenuItem(utilityMenu, "Payment History", "icon/icon13.png", 'H');
        addMenuItem(utilityMenu, "Change Password", null, 'W');

        mb.add(utilityMenu);

        // --- Report Menu (Admin only) ---
        if ("Admin".equalsIgnoreCase(this.userType)) {
            JMenu reportMenu = new JMenu("Report");
            reportMenu.setForeground(new Color(150, 0, 0));
            addMenuItem(reportMenu, "Bill Report", null, 'R');
            mb.add(reportMenu);
        }

        // --- Tools Menu ---
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setForeground(TEXT_DARK);
        addMenuItem(toolsMenu, "Notepad", "icon/icon11.png", 'T');
        addMenuItem(toolsMenu, "Calculator", "icon/icon12.png", 'A');
        mb.add(toolsMenu);

        // --- Exit Menu ---
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setForeground(TEXT_DARK);
        addMenuItem(exitMenu, "Logout", null, 'O');
        addMenuItem(exitMenu, "Exit Application", null, 'E');
        mb.add(exitMenu);

        setJMenuBar(mb);
        setVisible(true);
    }

    // Helper: add menu item with safe icon loading
    private void addMenuItem(JMenu menu, String name, String iconPath, char mnemonic) {
        JMenuItem item = new JMenuItem(name);
        item.setFont(new Font("SanSerif", Font.PLAIN, 12));
        item.setBackground(Color.WHITE);
        item.setMnemonic(mnemonic);
        item.addActionListener(this);

        if (iconPath != null) {
            JLabel iconLabel = loadImageLabel(iconPath, 20, 20, name);
            if (iconLabel != null && iconLabel.getIcon() != null) {
                item.setIcon(iconLabel.getIcon());
            }
        }

        menu.add(item);
        menuItems.put(name, item);
    }

    // Safe image loader
    private JLabel loadImageLabel(String path, int width, int height, String fallbackText) {
        try {
            java.net.URL url = ClassLoader.getSystemResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaled));
            } else {
                System.err.println("Image not found: " + path);
                return new JLabel(fallbackText);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JLabel(fallbackText);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();

        try {
            switch (action) {
                case "Exit Application":
                    System.exit(0);
                    break;
                case "Logout":
                    setVisible(false);
                    new Login().setVisible(true);
                    break;
                case "Notepad":
                    Runtime.getRuntime().exec("notepad.exe");
                    break;
                case "Calculator":
                    Runtime.getRuntime().exec("calc.exe");
                    break;
                default:
                    handleMenuAction(action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error executing: " + action + "\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle menu actions dynamically
    private void handleMenuAction(String action) {
        // Dynamically determine classes or DB actions
        switch (action) {
            case "New Customer":
                new NewCustomer(this).setVisible(true);
                this.setVisible(false); // Optionally hide the main page until NewCustomer closes
                break;

            case "Customer Details":
                new CustomerDetails().setVisible(true);
                break;
            case "Deposit Details":
                new DepositDetails().setVisible(true);
                break;
            case "Calculate Bill":
                new CalculateBill().setVisible(true);
                break;
            case "Bill Report":
                new BillReport().setVisible(true);
                break;
            case "Generate Bill":
                new GenerateBill(this.meterNumber).setVisible(true);
                break;
            case "View Information":
                new ViewInformation(this.meterNumber).setVisible(true);
                break;
            case "Update Information":
                new UpdateInformation(this.meterNumber).setVisible(true);
                break;
            case "Pay Bill":
                new PayBill(this.meterNumber).setVisible(true);
                break;
            case "Bill Details":
                new BillDetails(this.meterNumber).setVisible(true);
                break;
            case "Payment History":
                new PaymentHistory(this.meterNumber).setVisible(true);
                break;
            case "Change Password":
                new ChangePassword(this.meterNumber).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Action not implemented yet: " + action);
        }
    }

    public static void main(String[] args) {
        new MainHomePage("Admin", "N/A"); // For testing; real login passes actual userType/meter
    }
}
