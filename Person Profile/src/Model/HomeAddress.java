/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Danny
 */
public class HomeAddress {
    
    private String name;
    private String addLine1;
    private String addLine2;
    private String City;    
    private String Locality;
    private String Zip_code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String Locality) {
        this.Locality = Locality;
    }

    public String getZip_code() {
        return Zip_code;
    }

    public void setZip_code(String Zip_code) {
        this.Zip_code = Zip_code;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
