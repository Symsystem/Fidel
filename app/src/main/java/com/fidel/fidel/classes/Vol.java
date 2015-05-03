package com.fidel.fidel.classes;

import com.fidel.fidel.enums.TypeVol;

/**
 * Created by jeremyduchesne on 3/05/15.
 */
public class Vol {

    private int id;
    private String numVol;
    private TypeVol typeVol;
    private String depart;
    private String arrivee;

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
