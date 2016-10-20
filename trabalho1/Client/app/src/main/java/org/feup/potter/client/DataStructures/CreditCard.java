package org.feup.potter.client.DataStructures;


public class CreditCard {

    public enum CardType{
        MASTER{
            public String toString(){
                return "Master Card";
            }
        },
        VISA{
            public String toString(){
                return "Visa";
            }
        }
    }
    private CardType type;
    private Integer imageId;

    private String cardNumber;
    private int monthExpiration;
    private int yearExpiration;

    public CreditCard(CardType type, Integer imageId){
        this.type = type;
        this.imageId = imageId;
    }

    public String getText(){
        return type.toString();
    }

    public Integer getImageId(){
        return imageId;
    }

    public String getCardNumber(){
        return this.cardNumber;
    }
    public int getMonthExpiration(){
        return this.monthExpiration;
    }

    public int getYearExpiration(){
        return this.yearExpiration;
    }


    public void setCardType(CardType type){
        this.type = type;
    }

    public void setCardNumber(String number){
        this.cardNumber = number;
    }
    public void setMonthExpiration(int month){
        this.monthExpiration = month;
    }

    public void setYearExpiration(int year){
        this.yearExpiration = year;
    }


}
