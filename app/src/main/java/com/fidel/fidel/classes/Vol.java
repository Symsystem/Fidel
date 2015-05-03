package com.fidel.fidel.classes;

import com.fidel.fidel.enums.TypeVol;

import java.io.Serializable;

/**
 * Created by jeremyduchesne on 3/05/15.
 */
public class Vol implements Serializable {

    private int id;
    private String numVol;
    private TypeVol typeVol;
    private String depart;
    private String arrivee;
    private String heureDepart;
    private String heureArrivee;
    private String heureEmbarquement;
    private String gate;

    public String getHeureEmbarquement() {
        return heureEmbarquement;
    }

    public void setHeureEmbarquement(String heureEmbarquement) {
        this.heureEmbarquement = heureEmbarquement;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(String heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumVol() {
        return numVol;
    }

    public void setNumVol(String numVol) {
        this.numVol = numVol;
    }

    public TypeVol getTypeVol() {
        return typeVol;
    }

    public void setTypeVol(TypeVol typeVol) {
        this.typeVol = typeVol;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }
}
