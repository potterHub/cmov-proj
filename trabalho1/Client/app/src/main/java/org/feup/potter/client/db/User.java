package org.feup.potter.client.db;

import org.feup.potter.client.DataStructures.CreditCard;

import java.io.Serializable;

public class User implements Serializable{
    private String name;
    private String username;
    private String password;
    private CreditCard creditCard;

    private DataBaseHelper SQLDataBaseHelper;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
}