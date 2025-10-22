/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.billing.system;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;

/**
 *
 * @author danny
 */
public class Splash extends JFrame implements Runnable{
    
    // Changed t1 to a more descriptive variable name
    Thread thread;
    
    Splash(){
        
        // Changed i1 to imageIcon for clarity
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/WelcomeImage.jpg"));
        
        //To resize as per the new size, we use the getScaledInstance method
        // Changed i2 to scaledImage for clarity
        Image scaledImage = imageIcon.getImage().getScaledInstance(730, 550, Image.SCALE_DEFAULT);
        // Changed i3 to finalIcon for clarity
        ImageIcon finalIcon = new ImageIcon(scaledImage);
        
        JLabel image = new JLabel(finalIcon);
        add(image);
        
        setVisible(true); //By default the visibility of JFrame is hidden
        
        //Adding a transition for the image to grow incrementally
        int x = 1;
        for(int i = 2; i < 600; i++){
            setSize(i + x,i); //Set this as per your image size
            setLocation(700 - ((i + x)/2), 400 - (i/2) ); //To change the (X => Right, Y => from TOP)
//            setVisible(true); //By default the visibility of JFrame is hidden     
            try{
                Thread.sleep(3);
                
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        // Initializing the thread with the descriptive name
        thread = new Thread(this);
        thread.start(); //this method internally calls run methods of runnable (run)
    }
    
    @Override
    public void run(){
        try{
            //Step 1: Close the current frame of Electricity India
            Thread.sleep(1000);
            setVisible(false);
            
            //Step 2: Open the Login frame of the user
            new Login();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new Splash();
    }
}
