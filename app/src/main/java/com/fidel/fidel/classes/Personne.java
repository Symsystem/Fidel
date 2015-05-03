package com.fidel.fidel.classes;

/**
 * Created by jeremyduchesne on 3/05/15.
 */
public class Personne {

    private String nom;
    private String prenom;
    private String address;
    private int postCode;
    private String country;
    private String numPhone;
    private String birthDate;
    private String passeportValidity;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNumPhone() {
        return numPhone;
    }

    public void setNumPhone(String numPhone) {
        this.numPhone = numPhone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPasseportValidity() {
        return passeportValidity;
    }

    public void setPasseportValidity(String passeportValidity) {
        this.passeportValidity = passeportValidity;
    }
}
