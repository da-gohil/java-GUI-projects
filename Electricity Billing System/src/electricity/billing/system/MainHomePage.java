package electricity.billing.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * The MainHomePage class provides the main application dashboard, featuring a dynamic
 * menu bar whose items are conditionally displayed based on the user's role 
 * (Admin or Customer).
 */
public class MainHomePage extends JFrame implements ActionListener {
    
    // Define the primary blue accent color and dark text color for aesthetic consistency
    private static final Color PRIMARY_BLUE = new Color(0, 122, 255);
    private static final Color TEXT_DARK = new Color(51, 51, 51); // Dark Gray for professional look
    
    // Variables to store user context
    private String userType;
    private String meterNumber;
    
    // Constructor updated to accept the user context from Login.java
    MainHomePage(String userType, String meterNumber){
        
        super("Electricity Billing System - " + userType + " View"); 
        this.userType = userType;
        this.meterNumber = meterNumber;
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // 1. Load and Scale Image
        // NOTE: The image path is left as-is to comply with the mandate.
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/MainHomePage.jpg"));
        // Scaling to fit the maximized window
        Image i2 = i1.getImage().getScaledInstance(1550, 850, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        
        // 2. Add the component to the frame
        add(image); 
        
        // --- MENU BAR CREATION ---
        JMenuBar mb = new JMenuBar();
        
        // -------------------------
        // 3. MASTER MENU (Admin Only)
        // -------------------------
        if (this.userType.equals("Admin")) {
            JMenu master = new JMenu("Master"); 
            // Aesthetic fix: Use PRIMARY_BLUE for important Admin menus
            master.setForeground(PRIMARY_BLUE);
            
            // 3.1 New Customer
            JMenuItem newCustomer = new JMenuItem("New Customer"); 
            newCustomer.setFont(new Font("SanSerif", Font.PLAIN, 12));
            newCustomer.setBackground(Color.WHITE);
//            ImageIcon icon1 = new ImageIcon(ClassLoader.getSystemResource("icon/newCustomerMenuLogo.png"));
//            Image image1 = icon1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
//            newCustomer.setIcon(new ImageIcon(image1));
            newCustomer.setMnemonic('D');
            newCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
            newCustomer.addActionListener(this);
            master.add(newCustomer);
            
            // 3.2 Customer Details (Admin view of all customers)
            JMenuItem customerDetails = new JMenuItem("Customer Details"); 
            customerDetails.setFont(new Font("SanSerif", Font.PLAIN, 12));
            customerDetails.setBackground(Color.WHITE);
            ImageIcon icon2 = new ImageIcon(ClassLoader.getSystemResource("icon/icon2.png"));
            Image image2 = icon2.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
            customerDetails.setIcon(new ImageIcon(image2));
            customerDetails.setMnemonic('M');
            customerDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
            customerDetails.addActionListener(this);
            master.add(customerDetails);
            
            // 3.3 Deposit Details (Admin view of all collected deposits/bills)
            JMenuItem depositDetails = new JMenuItem("Deposit Details"); 
            depositDetails.setFont(new Font("SanSerif", Font.PLAIN, 12));
            depositDetails.setBackground(Color.WHITE);
            ImageIcon icon3 = new ImageIcon(ClassLoader.getSystemResource("icon/icon4.png"));
            Image image3 = icon3.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
            depositDetails.setIcon(new ImageIcon(image3));
            depositDetails.setMnemonic('N');
            depositDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
            depositDetails.addActionListener(this);
            master.add(depositDetails);
            
            // 3.4 Calculate Bill (Admin action: Entering meter readings)
            JMenuItem calculateBill = new JMenuItem("Calculate Bill"); 
            calculateBill.setFont(new Font("SanSerif", Font.PLAIN, 12));
            calculateBill.setBackground(Color.WHITE);
            ImageIcon icon4 = new ImageIcon(ClassLoader.getSystemResource("icon/icon5.png"));
            Image image4 = icon4.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
            calculateBill.setIcon(new ImageIcon(image4));
            calculateBill.setMnemonic('C');
            calculateBill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
            calculateBill.addActionListener(this);
            master.add(calculateBill);

            mb.add(master);
        }
        
        // -------------------------
        // 4. UTILITY MENU (For both Admin and Customer)
        // -------------------------
        JMenu utility = new JMenu("Utility"); 
        // Aesthetic fix: Use dark text for general utility menus
        utility.setForeground(TEXT_DARK); 
        
        // --- Customer/Common Items ---
        
        // 4.1 View Information (Common)
        JMenuItem viewInfo = new JMenuItem("View Information"); 
        viewInfo.setFont(new Font("SanSerif", Font.PLAIN, 12));
        viewInfo.setBackground(Color.WHITE);
        ImageIcon icon5 = new ImageIcon(ClassLoader.getSystemResource("icon/icon6.png")); 
        Image image5 = icon5.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        viewInfo.setIcon(new ImageIcon(image5));
        viewInfo.setMnemonic('V'); 
        viewInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        viewInfo.addActionListener(this);
        utility.add(viewInfo);
        
        // 4.2 Update Information (Common)
        JMenuItem updateInfo = new JMenuItem("Update Information"); 
        updateInfo.setFont(new Font("SanSerif", Font.PLAIN, 12));
        updateInfo.setBackground(Color.WHITE);
        ImageIcon icon_upd = new ImageIcon(ClassLoader.getSystemResource("icon/icon9.png")); 
        Image image_upd = icon_upd.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        updateInfo.setIcon(new ImageIcon(image_upd));
        updateInfo.setMnemonic('I');
        updateInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        updateInfo.addActionListener(this);
        utility.add(updateInfo);
        
        // 4.3 Pay Bill (Common)
        JMenuItem payBill = new JMenuItem("Pay Bill"); 
        payBill.setFont(new Font("SanSerif", Font.PLAIN, 12));
        payBill.setBackground(Color.WHITE);
        ImageIcon icon6 = new ImageIcon(ClassLoader.getSystemResource("icon/icon7.png")); 
        Image image6 = icon6.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        payBill.setIcon(new ImageIcon(image6));
        payBill.setMnemonic('P');
        payBill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        payBill.addActionListener(this);
        utility.add(payBill);

        // 4.4 Bill Details (Common)
        JMenuItem billDetails = new JMenuItem("Bill Details"); 
        billDetails.setFont(new Font("SanSerif", Font.PLAIN, 12));
        billDetails.setBackground(Color.WHITE);
        ImageIcon icon7 = new ImageIcon(ClassLoader.getSystemResource("icon/icon8.png")); 
        Image image7 = icon7.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        billDetails.setIcon(new ImageIcon(image7));
        billDetails.setMnemonic('L');
        billDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        billDetails.addActionListener(this);
        utility.add(billDetails);
        
        // 4.5 Generate Bill (Common - used for viewing the bill document)
        JMenuItem generateBill = new JMenuItem("Generate Bill");
        generateBill.setFont(new Font("SanSerif", Font.PLAIN, 12));
        generateBill.setBackground(Color.WHITE);
        ImageIcon icon_gen_bill = new ImageIcon(ClassLoader.getSystemResource("icon/icon10.png"));
        Image image_gen_bill = icon_gen_bill.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        generateBill.setIcon(new ImageIcon(image_gen_bill));
        generateBill.setMnemonic('G');
        generateBill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        generateBill.addActionListener(this);
        utility.add(generateBill);
        
        // 4.6 Payment History (New item for viewing past payments)
        JMenuItem paymentHistory = new JMenuItem("Payment History");
        paymentHistory.setFont(new Font("SanSerif", Font.PLAIN, 12));
        paymentHistory.setBackground(Color.WHITE);
        ImageIcon icon_pay_hist = new ImageIcon(ClassLoader.getSystemResource("icon/icon13.png")); 
        Image image_pay_hist = icon_pay_hist.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        paymentHistory.setIcon(new ImageIcon(image_pay_hist));
        paymentHistory.setMnemonic('H');
        paymentHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        paymentHistory.addActionListener(this);
        utility.add(paymentHistory);

        // 4.7 Change Password (New item for security updates)
        JMenuItem changePassword = new JMenuItem("Change Password");
        changePassword.setFont(new Font("SanSerif", Font.PLAIN, 12));
        changePassword.setBackground(Color.WHITE);
        ImageIcon icon_chg_pass = new ImageIcon(ClassLoader.getSystemResource("icon/icon14.png")); 
        Image image_chg_pass = icon_chg_pass.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        changePassword.setIcon(new ImageIcon(image_chg_pass));
        changePassword.setMnemonic('W');
        changePassword.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        changePassword.addActionListener(this);
        utility.add(changePassword);
        
        mb.add(utility);
        
        // -------------------------
        // 5. REPORT MENU (Admin Only)
        // -------------------------
        if (this.userType.equals("Admin")) {
            JMenu report = new JMenu("Report"); 
            // Aesthetic fix: Use a muted red/maroon for reporting/warning colors
            report.setForeground(new Color(150, 0, 0)); 
            
            JMenuItem billReport = new JMenuItem("Bill Report"); 
            billReport.setFont(new Font("SanSerif", Font.PLAIN, 12));
            billReport.setBackground(Color.WHITE);
            // Re-using an existing icon or you'll need to source icon
            billReport.setMnemonic('R');
            billReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
            billReport.addActionListener(this);
            report.add(billReport);
            
            mb.add(report);
        }
        
        // -------------------------
        // 6. TOOLS MENU (Common)
        // -------------------------
        JMenu tools = new JMenu("Tools"); 
        // Aesthetic fix: Use dark text
        tools.setForeground(TEXT_DARK); 
        
        // 6.1 Notepad
        JMenuItem notepad = new JMenuItem("Notepad"); 
        notepad.setFont(new Font("SanSerif", Font.PLAIN, 12));
        notepad.setBackground(Color.WHITE);
        ImageIcon icon_notepad = new ImageIcon(ClassLoader.getSystemResource("icon/icon11.png")); 
        Image image_notepad = icon_notepad.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        notepad.setIcon(new ImageIcon(image_notepad));
        notepad.setMnemonic('T');
        notepad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        notepad.addActionListener(this);
        tools.add(notepad);
        
        // 6.2 Calculator
        JMenuItem calculator = new JMenuItem("Calculator"); 
        calculator.setFont(new Font("SanSerif", Font.PLAIN, 12));
        calculator.setBackground(Color.WHITE);
        ImageIcon icon_calc = new ImageIcon(ClassLoader.getSystemResource("icon/icon12.png")); 
        Image image_calc = icon_calc.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        calculator.setIcon(new ImageIcon(image_calc));
        calculator.setMnemonic('A');
        calculator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        calculator.addActionListener(this);
        tools.add(calculator);

        mb.add(tools);

        // -------------------------
        // 7. EXIT MENU (Common)
        // -------------------------
        JMenu exit = new JMenu("Exit"); 
        exit.setForeground(TEXT_DARK);
        
        // New: Logout (Go back to Login screen)
        JMenuItem logout = new JMenuItem("Logout");
        logout.setFont(new Font("SanSerif", Font.PLAIN, 12));
        logout.setBackground(Color.WHITE);
        logout.setMnemonic('O');
        logout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        logout.addActionListener(this);
        exit.add(logout);
        
        // Terminate Application
        JMenuItem exitApp = new JMenuItem("Exit Application"); 
        exitApp.setFont(new Font("SanSerif", Font.PLAIN, 12));
        exitApp.setBackground(Color.WHITE);
        exitApp.setMnemonic('E');
        exitApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exitApp.addActionListener(this);
        exit.add(exitApp);
        
        mb.add(exit);
        
        // 8. Set the MenuBar for the Frame
        setJMenuBar(mb);
        
        // Final Frame Settings
        revalidate(); 
        repaint();
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();
        
        try {
            if (msg.equals("Exit Application")) {
                System.exit(0);
            } else if (msg.equals("Logout")) {
                setVisible(false);
                // Assumption: A Login class exists to be instantiated
                new Login().setVisible(true);
            } else if (msg.equals("Notepad")) {
                // Runtime execution can fail on non-Windows systems
                Runtime.getRuntime().exec("notepad.exe"); 
            } else if (msg.equals("Calculator")) {
                // Runtime execution can fail on non-Windows systems
                Runtime.getRuntime().exec("calc.exe"); 
            }
            // --- Admin/Master Actions ---
            else if (msg.equals("New Customer")) {
                new NewCustomer().setVisible(true);
            }
            else if (msg.equals("Customer Details")) {
                 new CustomerDetails().setVisible(true);
            }
            else if (msg.equals("Deposit Details")) {
                new DepositDetails().setVisible(true);
            }
            else if (msg.equals("Calculate Bill")) {
                 new CalculateBill().setVisible(true);
            }
            else if (msg.equals("Bill Report")) {
                // This will likely open a view to see bills for all customers
                 new BillReport().setVisible(true);
            }
            
            // --- Common/Customer Actions ---
            else if (msg.equals("Generate Bill")) {
                // Customer views their own generated bill
                 new GenerateBill(this.meterNumber).setVisible(true); 
            }
            else if (msg.equals("View Information")) {
                // View information for the current user (using meterNumber)
                new ViewInformation(this.meterNumber).setVisible(true);
            }
            else if (msg.equals("Update Information")) {
                // Update information for the current user (using meterNumber)
                 new UpdateInformation(this.meterNumber).setVisible(true); 
            }
            else if (msg.equals("Pay Bill")) {
                // Pay bill for the current user (using meterNumber)
                new PayBill(this.meterNumber).setVisible(true);
            }
            else if (msg.equals("Bill Details")) {
                // View bill details for the current user (using meterNumber)
                new BillDetails(this.meterNumber).setVisible(true);
            }
            else if (msg.equals("Payment History")) {
                // New: View history of payments made by the current user
                // The current Canvas PaymentHistory is called here, passing the meter number
                new PaymentHistory(this.meterNumber).setVisible(true);
            }
            else if (msg.equals("Change Password")) {
                // New: Change password for the current user
                // Assumption: ChangePassword class takes meterNumber
                new ChangePassword(this.meterNumber).setVisible(true);
            }
            
        } catch (IOException e) {
            // Generic fallback for external app failure
            JOptionPane.showMessageDialog(this, "Could not launch external application: " + msg, "Launch Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error launching module: " + msg + "\n" + e.getMessage(), "Application Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args){
        // Dummy call for testing only. Real app uses Login class to call with user context.
        new MainHomePage("Admin", "N/A");
    }
    
}
