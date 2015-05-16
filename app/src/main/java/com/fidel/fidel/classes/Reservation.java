package com.fidel.fidel.classes;

import com.fidel.fidel.enums.TypeVoyageur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jeremyduchesne on 3/05/15.
 */
public class Reservation implements Serializable {

    private int id;
    private String numRes;
    private String date;
    private TypeVoyageur typeVoyageur;
    private Vol vol;
    private ArrayList<Personne> listPersonne;
    private User user;
    private HashMap<String, String> numSieges;

    public HashMap<String, String> getNumSieges() {
        return numSieges;
    }

    public void setNumSieges(HashMap<String, String> numSieges) {
        this.numSieges = numSieges;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumRes() {
        return numRes;
    }

    public void setNumRes(String numRes) {
        this.numRes = numRes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TypeVoyageur getTypeVoyageur() {
        return typeVoyageur;
    }

    public void setTypeVoyageur(TypeVoyageur typeVoyageur) {
        this.typeVoyageur = typeVoyageur;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public ArrayList<Personne> getListPersonne() {
        return listPersonne;
    }

    public void setListPersonne(ArrayList<Personne> listPersonne) {
        this.listPersonne = listPersonne;
    }

    public void addPers(Personne pers){
        listPersonne.add(pers);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
