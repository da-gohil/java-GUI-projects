/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author danny
 */
public class MainHomePage extends JFrame implements ActionListener {
    
    MainHomePage(){
        
        super("Electricity Billing System"); // Added Frame Title
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // 1. Load and Scale Image
        // Reverting variable names as requested
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/MainHomePage.jpg"));
        // Scaling to a large, fixed size for a maximized window
        Image i2 = i1.getImage().getScaledInstance(1550, 850, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        
        // 2. Add the component to the frame
        add(image); 
        
        // --- MENU BAR CREATION ---
        
        // Reverting variable name to 'mb'
        JMenuBar mb = new JMenuBar();
        
        // -------------------------
        // 3. MASTER MENU
        // -------------------------
        JMenu master = new JMenu("Master"); // Reverting variable name to 'master'
        master.setForeground(Color.BLUE);
        
        // 3.1 New Customer
        JMenuItem newCustomer = new JMenuItem("New Customer"); // Reverting variable name
        newCustomer.setFont(new Font("monospaced", Font.PLAIN, 12));
        newCustomer.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon1 = new ImageIcon(ClassLoader.getSystemResource("icon/newCustomerMenuLogo.png"));
        Image image1 = icon1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        newCustomer.setIcon(new ImageIcon(image1));
        newCustomer.setMnemonic('D');
        newCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        newCustomer.addActionListener(this);
        master.add(newCustomer);
        
        // 3.2 Customer Details
        JMenuItem customerDetails = new JMenuItem("Customer Details"); // Reverting variable name
        customerDetails.setFont(new Font("monospaced", Font.PLAIN, 12));
        customerDetails.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon2 = new ImageIcon(ClassLoader.getSystemResource("icon/icon2.png"));
        Image image2 = icon2.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        customerDetails.setIcon(new ImageIcon(image2));
        customerDetails.setMnemonic('M');
        customerDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        customerDetails.addActionListener(this);
        master.add(customerDetails);
        
        // 3.3 Deposit Details
        JMenuItem depositDetails = new JMenuItem("Deposit Details"); // Reverting variable name
        depositDetails.setFont(new Font("monospaced", Font.PLAIN, 12));
        depositDetails.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon3 = new ImageIcon(ClassLoader.getSystemResource("icon/icon4.png"));
        Image image3 = icon3.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        depositDetails.setIcon(new ImageIcon(image3));
        depositDetails.setMnemonic('N');
        depositDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        depositDetails.addActionListener(this);
        master.add(depositDetails);

        
        // -------------------------
        // 4. USER UTILITY MENU
        // -------------------------
        JMenu utility = new JMenu("Utility"); // Reverting variable name to 'utility'
        utility.setForeground(new Color(200, 100, 50)); // Orange color
        
        // 4.1 Calculate Bill (For adding bill data)
        JMenuItem calculateBill = new JMenuItem("Calculate Bill"); // Reverting variable name
        calculateBill.setFont(new Font("monospaced", Font.PLAIN, 12));
        calculateBill.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon4 = new ImageIcon(ClassLoader.getSystemResource("icon/icon5.png"));
        Image image4 = icon4.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        calculateBill.setIcon(new ImageIcon(image4));
        calculateBill.setMnemonic('C');
        calculateBill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        calculateBill.addActionListener(this);
        utility.add(calculateBill);
        
        // 4.2 Generate Bill (New Item - For generating a printable document)
        JMenuItem generateBill = new JMenuItem("Generate Bill"); // Reverting variable name
        generateBill.setFont(new Font("monospaced", Font.PLAIN, 12));
        generateBill.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon_gen_bill = new ImageIcon(ClassLoader.getSystemResource("icon/icon10.png"));
        Image image_gen_bill = icon_gen_bill.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        generateBill.setIcon(new ImageIcon(image_gen_bill));
        generateBill.setMnemonic('G');
        generateBill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        generateBill.addActionListener(this);
        utility.add(generateBill);
        
        // 4.3 View Information (Already Present)
        JMenuItem viewInfo = new JMenuItem("View Information"); // Reverting variable name
        viewInfo.setFont(new Font("monospaced", Font.PLAIN, 12));
        viewInfo.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon5 = new ImageIcon(ClassLoader.getSystemResource("icon/icon6.png")); 
        Image image5 = icon5.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        viewInfo.setIcon(new ImageIcon(image5));
        viewInfo.setMnemonic('V'); 
        viewInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        viewInfo.addActionListener(this);
        utility.add(viewInfo);
        
        // 4.4 Update Information (New Item - For modifying existing customer details)
        JMenuItem updateInfo = new JMenuItem("Update Information"); // Reverting variable name
        updateInfo.setFont(new Font("monospaced", Font.PLAIN, 12));
        updateInfo.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon_upd = new ImageIcon(ClassLoader.getSystemResource("icon/icon9.png")); 
        Image image_upd = icon_upd.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        updateInfo.setIcon(new ImageIcon(image_upd));
        updateInfo.setMnemonic('I');
        updateInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        updateInfo.addActionListener(this);
        utility.add(updateInfo);
        
        // 4.5 Pay Bill (Already Present)
        JMenuItem payBill = new JMenuItem("Pay Bill"); // Reverting variable name
        payBill.setFont(new Font("monospaced", Font.PLAIN, 12));
        payBill.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon6 = new ImageIcon(ClassLoader.getSystemResource("icon/icon7.png")); 
        Image image6 = icon6.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        payBill.setIcon(new ImageIcon(image6));
        payBill.setMnemonic('P');
        payBill.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        payBill.addActionListener(this);
        utility.add(payBill);

        // 4.6 Bill Details (Already Present)
        JMenuItem billDetails = new JMenuItem("Bill Details"); // Reverting variable name
        billDetails.setFont(new Font("monospaced", Font.PLAIN, 12));
        billDetails.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon7 = new ImageIcon(ClassLoader.getSystemResource("icon/icon8.png")); 
        Image image7 = icon7.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        billDetails.setIcon(new ImageIcon(image7));
        billDetails.setMnemonic('L');
        billDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        billDetails.addActionListener(this);
        utility.add(billDetails);
        
        
        // -------------------------
        // 5. REPORT MENU
        // -------------------------
        JMenu report = new JMenu("Report"); // Reverting variable name to 'report'
        report.setForeground(Color.RED);
        JMenuItem r1 = new JMenuItem("Bill Report"); // Reverting variable name to 'r1'
        r1.setFont(new Font("monospaced", Font.PLAIN, 12));
        r1.setBackground(Color.WHITE);
        r1.setMnemonic('R');
        r1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        r1.addActionListener(this);
        report.add(r1);
        
        // -------------------------
        // 6. TOOLS MENU (New menu for external applications)
        // -------------------------
        JMenu tools = new JMenu("Tools"); // Reverting variable name to 'tools'
        tools.setForeground(Color.GREEN.darker().darker()); 
        
        // 6.1 Notepad (New Item)
        JMenuItem notepad = new JMenuItem("Notepad"); // Reverting variable name
        notepad.setFont(new Font("monospaced", Font.PLAIN, 12));
        notepad.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon_notepad = new ImageIcon(ClassLoader.getSystemResource("icon/icon11.png")); 
        Image image_notepad = icon_notepad.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        notepad.setIcon(new ImageIcon(image_notepad));
        notepad.setMnemonic('T');
        notepad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        notepad.addActionListener(this);
        tools.add(notepad);
        
        // 6.2 Calculator (New Item)
        JMenuItem calculator = new JMenuItem("Calculator"); // Reverting variable name
        calculator.setFont(new Font("monospaced", Font.PLAIN, 12));
        calculator.setBackground(Color.WHITE);
        // Reverting icon variable names
        ImageIcon icon_calc = new ImageIcon(ClassLoader.getSystemResource("icon/icon12.png")); 
        Image image_calc = icon_calc.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        calculator.setIcon(new ImageIcon(image_calc));
        calculator.setMnemonic('A');
        calculator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        calculator.addActionListener(this);
        tools.add(calculator);


        // -------------------------
        // 7. EXIT MENU
        // -------------------------
        JMenu exit = new JMenu("Exit"); // Reverting variable name to 'exit'
        exit.setForeground(Color.BLACK);
        JMenuItem e1 = new JMenuItem("Exit Application"); // Reverting variable name to 'e1'
        e1.setFont(new Font("monospaced", Font.PLAIN, 12));
        e1.setBackground(Color.WHITE);
        e1.setMnemonic('E');
        e1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        e1.addActionListener(this);
        exit.add(e1);
        
        // 8. Add Menus to MenuBar
        mb.add(master);
        mb.add(utility);
        mb.add(report);
        mb.add(tools); // Added Tools menu
        mb.add(exit);
        
        // 9. Set the MenuBar for the Frame
        setJMenuBar(mb);
        
        // Ensure frame updates after adding menu and image
        revalidate(); 
        repaint();
        
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();
        
        if (msg.equals("Exit Application")) {
            System.exit(0);
        } else if (msg.equals("Notepad")) {
            try {
                // Attempts to open Notepad on Windows
                Runtime.getRuntime().exec("notepad.exe"); 
            } catch (IOException e) {
                // If notepad.exe fails, try other common OS commands
                try {
                     Runtime.getRuntime().exec("textedit"); // For macOS
                } catch (IOException e2) {
                    System.err.println("Could not launch Notepad or TextEdit: " + e.getMessage());
                }
            }
        } else if (msg.equals("Calculator")) {
            try {
                // Attempts to open Calculator on Windows
                Runtime.getRuntime().exec("calc.exe"); 
            } catch (IOException e) {
                 // If calc.exe fails, try other common OS commands
                try {
                     Runtime.getRuntime().exec("gnome-calculator"); // For Linux (Gnome)
                } catch (IOException e2) {
                    System.err.println("Could not launch Calculator: " + e.getMessage());
                }
            }
        }
        
        // Implement logic for other menu items here (e.g., opening new windows)
        /*
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
        else if (msg.equals("Generate Bill")) {
             new GenerateBill().setVisible(true); 
        }
        else if (msg.equals("View Information")) {
             new ViewInformation().setVisible(true);
        }
        else if (msg.equals("Update Information")) {
             new UpdateInformation().setVisible(true); 
        }
        else if (msg.equals("Pay Bill")) {
             new PayBill().setVisible(true);
        }
        else if (msg.equals("Bill Details")) {
             new BillDetails().setVisible(true);
        }
        else if (msg.equals("Bill Report")) {
             new BillReport().setVisible(true);
        }
        */
    }
    
    public static void main(String[] args){
        new MainHomePage();
    }
    
}
