package com.fidel.fidel.classes;

/**
 * Created by sym on 5/05/15.
 */
public class Bagage {

    private int id;
    private float weight;
    private int idRes;

    public Bagage(float weight, int idRes) {
        this.weight = weight;
        this.idRes = idRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
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
