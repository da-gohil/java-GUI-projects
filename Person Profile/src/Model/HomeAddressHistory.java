/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Danny
 */
public class HomeAddressHistory {
    
    private ArrayList <HomeAddress> history_h;
    
    public HomeAddressHistory(){
        this.history_h = new ArrayList<HomeAddress>();
        
    }

    public ArrayList<HomeAddress> getHistory_h() {
        return history_h;
    }

    public void setHistory_h(ArrayList<HomeAddress> history) {
        this.history_h = history_h;
    }

    public  HomeAddress addNewHomeAddress() {
    
        HomeAddress newHomeAddress = new HomeAddress();
        history_h.add(newHomeAddress);
        return newHomeAddress;
    }
    
     public void deleteHAddress(HomeAddress selectedHAddress) {
        history_h.remove(selectedHAddress);
        
    }
    
}
