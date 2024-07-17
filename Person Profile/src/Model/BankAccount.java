/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Danny
 */
public class BankAccount {
    
    private String Nameb;
    private String AccountType;
    private String AccountNumber;
    private String Balance;
    private String CreditLimit;
    private String OpeningDate;

    public String getName() {
        return Nameb;
    }

    public void setName(String name) {
        this.Nameb = name;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String AccountType) {
        this.AccountType = AccountType;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String AccountNumber) {
        this.AccountNumber = AccountNumber;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String Balance) {
        this.Balance = Balance;
    }

    public String getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(String CreditLimit) {
        this.CreditLimit = CreditLimit;
    }

    public String getOpeningDate() {
        return OpeningDate;
    }

    public void setOpeningDate(String OpeningDate) {
        this.OpeningDate = OpeningDate;
    }
    
    @Override
    public String toString(){
        return Nameb;
    }
    
}
