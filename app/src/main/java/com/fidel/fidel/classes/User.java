package com.fidel.fidel.classes;

import java.io.Serializable;

/**
 * Created by sym on 3/05/15.
 */
public class User implements Serializable{

    private int id;
    private String login;
    private String email;
    private int wallet;

    public User(int id, String login, String email, int wallet){
        this.id = id;
        this.login = login;
        this.email = email;
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
