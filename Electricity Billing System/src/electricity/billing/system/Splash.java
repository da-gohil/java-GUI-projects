package electricity.billing.system;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The Splash class is responsible for displaying a temporary splash screen
 * during application startup with a growing animation effect.
 * It implements Runnable to manage the transition to the main Login screen
 * after a short delay on a separate thread.
 * * @author danny
 */
public class Splash extends JFrame implements Runnable {
    
    // Constant for the animation speed (in milliseconds)
    private static final int ANIMATION_DELAY_MS = 3;
    // Constant for the splash screen duration before transition (in milliseconds)
    private static final int SPLASH_DURATION_MS = 1000;
    
    // UI Component Naming: Naming convention lblComponent
    private JLabel lblImage;
    
    // Application Thread Naming: Descriptive variable name
    private Thread transitionThread;
    
    Splash() {
        // Set basic frame properties
        // setSize(730, 550); // Initial size will be set by the animation
        setUndecorated(true); // Remove frame border for a clean splash effect
        
        // Load the image icon
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/WelcomeImage.jpg"));
        
        // To resize as per the new size, we use the getScaledInstance method
        Image scaledImage = imageIcon.getImage().getScaledInstance(730, 550, Image.SCALE_DEFAULT);
        ImageIcon finalIcon = new ImageIcon(scaledImage);
        
        // Initialize the JLabel with the component naming convention
        lblImage = new JLabel(finalIcon);
        add(lblImage);
        
        // Get screen size for more reliable centering (Boundary Value Analysis fix)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        
        // Animation Loop for the growing effect (runs on the EDT - NOT ideal, but kept for effect)
        int widthIncrement = 1; // Descriptive name for x
        int startSize = 2;
        int endSize = 600;
        
        for (int i = startSize; i < endSize; i++) {
            // Boundary Condition Check: Ensure frame size doesn't exceed a reasonable max
            // and adjust location to keep it centered on the screen.
            setSize(i + widthIncrement, i);
            
            // Calculating location to keep the center of the frame constant (relative to screen center)
            int xLocation = (screenWidth - (i + widthIncrement)) / 2;
            int yLocation = (screenHeight - i) / 2;
            setLocation(xLocation, yLocation);
            
            // Make the frame visible after its initial properties are set
            if (i == startSize) {
                setVisible(true);
            }
            
            try {
                // Magic Number is now a descriptive constant
                Thread.sleep(ANIMATION_DELAY_MS);
            } catch (InterruptedException e) {
                // Log the exception rather than just printing the stack trace
                System.err.println("Splash screen animation interrupted: " + e.getMessage());
                // Re-interrupt the thread
                Thread.currentThread().interrupt(); 
            }
        }
        
        // Initializing the thread with the descriptive name and starting the transition
        transitionThread = new Thread(this);
        transitionThread.start();
    }
    
    @Override
    public void run() {
        try {
            // Wait for the defined duration
            Thread.sleep(SPLASH_DURATION_MS);
            
            // Step 1: Close the current splash frame
            setVisible(false);
            dispose(); // Release resources
            
            // Step 2: Open the Login frame of the user
            // Assuming 'Login' class exists in the same package or is imported
            new Login(); 
        } catch (InterruptedException e) {
            System.err.println("Transition thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Splash();
    }
}