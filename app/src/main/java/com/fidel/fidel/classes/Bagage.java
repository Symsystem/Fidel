package com.fidel.fidel.classes;

/**
 * Created by sym on 5/05/15.
 */
public class Bagage {

    private int id;
    private int weight;
    private int idRes;

    public Bagage(int id, int weight, int idRes) {
        this.id = id;
        this.weight = weight;
        this.idRes = idRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }
}
