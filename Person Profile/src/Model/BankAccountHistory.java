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
public class BankAccountHistory{
    
    private ArrayList <BankAccount> bankhistory;
    
    public BankAccountHistory(){
        this.bankhistory = new ArrayList<BankAccount>();
    }

    public ArrayList<BankAccount> getBankhistory() {
        return bankhistory;
    }

    public void setBankhistory(ArrayList<BankAccount> bankhistory) {
        this.bankhistory = bankhistory;
    }
    
    public BankAccount addNewAccount(){
    
        BankAccount newAccount = new BankAccount();
        bankhistory.add(newAccount);
        return newAccount;
            
    }

    public void deleteBankAccount(BankAccount selectedAccount) {
        bankhistory.remove(selectedAccount);
        
    }
}

