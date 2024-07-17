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
public class LocalAddressHistory {
    
    private ArrayList <LocalAddress> localhistory;
    
    public LocalAddressHistory() {        
        this.localhistory = new ArrayList<LocalAddress>();
    }

    
    public void setLocalhistory(ArrayList<LocalAddress> localhistory) {
        this.localhistory = localhistory;
    }
    
    public ArrayList<LocalAddress> getLocalhistory() {
        return localhistory;
    }
    
   
    
    public LocalAddress addNewLocalAddress(){   
        LocalAddress newLocalAddress = new LocalAddress();
        localhistory.add(newLocalAddress);
        return newLocalAddress;
    }
    
     public void deleteLocalAddress(LocalAddress selectedLaddress) {
        localhistory.remove(selectedLaddress);        
    }
     
}
