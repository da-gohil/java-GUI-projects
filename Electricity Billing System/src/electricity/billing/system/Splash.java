package electricity.billing.system;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Splash screen displaying the project welcome message and animated logo.
 * 
 * <p>Conforms to the Electricity Billing System coding & aesthetic mandate:
 * <ul>
 *   <li>Apple-inspired minimalist aesthetic</li>
 *   <li>Consistent font, color, and component spacing</li>
 *   <li>Thread-safe Swing animation using javax.swing.Timer</li>
 *   <li>Strict naming conventions and documentation</li>
 * </ul>
 * </p>
 * 
 * @author danny
 */
public class Splash extends JFrame implements Runnable {

    // === Constants ===
    private static final int ANIMATION_DELAY_MS = 10;      // Frame delay (ms)
    private static final int SPLASH_DURATION_MS = 1200;    // Duration before transition (ms)
    private static final int START_WIDTH = 100;            // Initial frame width
    private static final int END_WIDTH = 700;              // Final frame width
    private static final int STEP = 8;                     // Increment per animation tick

    // === UI Components ===
    private JLabel lblImage;
    private JLabel lblTitle;

    // === Thread for delayed transition ===
    private Thread transitionThread;

    /**
     * Initializes and displays the animated splash screen.
     */
    public Splash() {
        setUndecorated(true);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Load splash image
        BufferedImage baseImage = loadImage("/icon/WelcomeImage.jpg");

        // === Title Label (Top) ===
        lblTitle = new JLabel("Welcome to Electricity Billing Management System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SanSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 122, 255)); // Apple blue
        lblTitle.setOpaque(false);
        add(lblTitle);

        // === Image Label (Below Title) ===
        lblImage = new JLabel();
        lblImage.setOpaque(false);
        add(lblImage);

        // Start animation after EDT initialization
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double aspect = baseImage != null ? (double) baseImage.getHeight() / baseImage.getWidth() : 0.75;

        EventQueue.invokeLater(() -> startAnimation(baseImage, aspect, screenSize));
    }

    /**
     * Loads the splash image from the classpath.
     * @param path The image resource path.
     * @return BufferedImage if found, otherwise null.
     */
    private BufferedImage loadImage(String path) {
        try {
            URL imageUrl = getClass().getResource(path);
            if (imageUrl == null) {
                System.err.println("Image not found: " + path);
                return null;
            }
            return ImageIO.read(imageUrl);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles the splash screen growth animation and transition.
     * @param baseImage The image to scale.
     * @param aspect The aspect ratio of the image.
     * @param screenSize The user’s screen size for centering.
     */
    private void startAnimation(BufferedImage baseImage, double aspect, Dimension screenSize) {
        final int[] currentWidth = { START_WIDTH };

        Timer timer = new Timer(ANIMATION_DELAY_MS, e -> {
            int width = currentWidth[0];
            int height = (int) (width * aspect);

            if (baseImage != null) {
                Image scaled = baseImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaled));
            }

            // Position title above image
            lblTitle.setBounds(0, 0, width, 40);
            lblImage.setBounds(0, 50, width, height);

            // Frame height = image + title + small padding
            setSize(width, height + 60);
            setLocation((screenSize.width - width) / 2, (screenSize.height - (height + 60)) / 2);

            if (!isVisible()) setVisible(true);

            if (width >= END_WIDTH) {
                ((Timer) e.getSource()).stop();
                transitionThread = new Thread(this, "SplashTransitionThread");
                transitionThread.start();
            } else {
                currentWidth[0] = width + STEP;
            }
        });

        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Runs the splash duration timer and transitions to the Login screen.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(SPLASH_DURATION_MS);
            SwingUtilities.invokeLater(() -> {
                setVisible(false);
                dispose();
                new Login();
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Entry point for the application. */
    public static void main(String[] args) {
        EventQueue.invokeLater(Splash::new);
    }
}
