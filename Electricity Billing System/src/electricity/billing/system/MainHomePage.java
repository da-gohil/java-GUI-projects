package electricity.billing.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class MainHomePage extends JFrame implements ActionListener {

    // --- UI Consistency Constants ---
    private static final int FRAME_WIDTH = 1024; // MANDATED FIXED SIZE
    private static final int FRAME_HEIGHT = 768; // MANDATED FIXED SIZE

    private String userType;
    private String meterNumber;
    private HashMap<String, JMenuItem> menuItems = new HashMap<>();

    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color TEXT_DARK = new Color(51, 51, 51);

    public MainHomePage(String userType, String meterNumber) {
        super("Electricity Billing System - " + userType + " View");

        this.userType = userType;
        this.meterNumber = meterNumber;

        // --- ENFORCING FIXED SIZE MANDATE ---
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false); // Disable resizing
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Load main background image (Border-to-Border) ---
        JLabel mainImage = loadImageLabel("icon/MainHomePage.jpg", FRAME_WIDTH, FRAME_HEIGHT, "Main Home Page");
        if (mainImage != null) {
            // Must use BorderLayout.CENTER to ensure the image covers the entire client area
            add(mainImage, BorderLayout.CENTER);
        } else {
             // Fallback if image fails, set a plain background color
             getContentPane().setBackground(new Color(230, 230, 230));
        }

        JMenuBar mb = new JMenuBar();
        mb.setBackground(Color.WHITE); 
        
        // --- Admin Master Menu dynamically from DB or fallback ---
        if ("Admin".equalsIgnoreCase(this.userType)) {
            JMenu masterMenu = new JMenu("Master");
            masterMenu.setForeground(PRIMARY_BLUE);
            masterMenu.setFont(new Font("SanSerif", Font.BOLD, 14));

            addMenuItem(masterMenu, "New Customer", "icon/newCustomerMenuLogo.png", 'D');
            addMenuItem(masterMenu, "Customer Details", "icon/icon2.png", 'M');
            addMenuItem(masterMenu, "Deposit Details", "icon/icon4.png", 'N');
            addMenuItem(masterMenu, "Calculate Bill", "icon/icon5.png", 'C');

            mb.add(masterMenu);
        }

        // --- Utility Menu for all users dynamically ---
        JMenu utilityMenu = new JMenu("Utility");
        utilityMenu.setForeground(TEXT_DARK);
        utilityMenu.setFont(new Font("SanSerif", Font.BOLD, 14));

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
            reportMenu.setFont(new Font("SanSerif", Font.BOLD, 14));
            addMenuItem(reportMenu, "Bill Report", null, 'R');
            mb.add(reportMenu);
        }

        // --- Tools Menu ---
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setForeground(TEXT_DARK);
        toolsMenu.setFont(new Font("SanSerif", Font.BOLD, 14));
        addMenuItem(toolsMenu, "Notepad", "icon/icon11.png", 'T');
        addMenuItem(toolsMenu, "Calculator", "icon/icon12.png", 'A');
        mb.add(toolsMenu);

        // --- Exit Menu ---
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setForeground(TEXT_DARK);
        exitMenu.setFont(new Font("SanSerif", Font.BOLD, 14));
        addMenuItem(exitMenu, "Logout", null, 'O');
        addMenuItem(exitMenu, "Exit Application", null, 'E');
        mb.add(exitMenu);

        setJMenuBar(mb);
        setVisible(true);
    }

    // Helper: add menu item with safe icon loading
    private void addMenuItem(JMenu menu, String name, String iconPath, char mnemonic) {
        JMenuItem item = new JMenuItem(name);
        item.setFont(new Font("SanSerif", Font.PLAIN, 14)); 
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
                // System.err.println("Image not found: " + path); // Debug output removed for cleaner final code
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
                    // NOTE: Assumes Login class exists and has a no-arg constructor
                    new Login().setVisible(true); 
                    dispose();
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

    // Handle menu actions dynamically (MODIFIED to pass 'this' for navigation)
    private void handleMenuAction(String action) {
        this.setVisible(false); // Hide the main window before opening a task window
        
        switch (action) {
            case "New Customer":
                // NOTE: Assumes NewCustomer takes a MainHomePage reference
                new NewCustomer(this).setVisible(true);
                break;

            case "Customer Details":
                // NOTE: Assumes CustomerDetails takes a MainHomePage reference
                new CustomerDetails(this).setVisible(true); 
                break;
            case "Deposit Details":
                // NOTE: Assumes DepositDetails takes a MainHomePage reference
                new DepositDetails(this).setVisible(true); 
                break;
            case "Calculate Bill":
                // MODIFIED: CalculateBill now requires 'this'
                new CalculateBill(this).setVisible(true);
                break;
            case "Bill Report":
                // MODIFIED: BillReport now requires 'this'
                new BillReport(this).setVisible(true);
                break;
            case "Generate Bill":
                // MODIFIED: GenerateBill now requires 'this'
                new GenerateBill(this.meterNumber, this).setVisible(true);
                break;
            case "View Information":
                // NOTE: Assumes ViewInformation takes 'this'
                new ViewInformation(this.meterNumber, this).setVisible(true);
                break;
            case "Update Information":
                // NOTE: Assumes UpdateInformation takes 'this'
                new UpdateInformation(this.meterNumber, this).setVisible(true);
                break;
            case "Pay Bill":
                // NOTE: Assumes PayBill takes 'this'
                new PayBill(this.meterNumber, this).setVisible(true);
                break;
            case "Bill Details":
                // MODIFIED: BillDetails now requires 'this'
                new BillDetails(this.meterNumber, this).setVisible(true);
                break;
            case "Payment History":
                // NOTE: Assumes PaymentHistory takes 'this'
                new PaymentHistory(this.meterNumber, this).setVisible(true);
                break;
            case "Change Password":
                // MODIFIED: ChangePassword now requires 'this'
                new ChangePassword(this.meterNumber, this).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Action not implemented yet: " + action);
                this.setVisible(true); // Show main frame again if action failed
        }
    }

    public static void main(String[] args) {
        // Test with a sample Admin login
        new MainHomePage("Admin", "N/A"); 
    }
}