package com.fidel.fidel.classes;

/**
 * Created by jeremyduchesne on 5/05/15.
 */
public class Achat {

    private String libelle;
    private int quantite;
    private double prix;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
