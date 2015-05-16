package com.fidel.fidel.classes;

import java.io.Serializable;

/**
 * Created by jeremyduchesne on 3/05/15.
 */
public class Personne implements Serializable {

    private int id;
    private String nom;
    private String prenom;
    private String address;
    private String locality;
    private int postCode;
    private String country;
    private String numPhone;
    private String birthDate;
    private String passeportValidity;

    public Personne(int id, String nom, String prenom, String address, String locality, int postCode, String country, String numPhone, String birthDate, String passeportValidity) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.address = address;
        this.locality = locality;
        this.postCode = postCode;
        this.country = country;
        this.numPhone = numPhone;
        this.birthDate = birthDate;
        this.passeportValidity = passeportValidity;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
