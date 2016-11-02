package org.feup.potter.client.db;

import org.feup.potter.client.DataStructures.CreditCard;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String username;
    private String password;
    private CreditCard creditCard;
    private String pin;
    private String tokan;

    private DataBaseHelper SQLDataBaseHelper;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = "";
        this.tokan = "";
        this.id = "";
        this.pin = "";
    }

    public User(String id, String name, String username, String password,String pin, String tokan) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.tokan = tokan;
        this.id = id;
        this.pin = pin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreditCard(CreditCard card) {
        creditCard = card;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setTokan(String tokan) {
        this.tokan = tokan;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public String getPin() {
        return pin;
    }

    public String getTokan() {
        return tokan;
    }

}