package org.feup.potter.client.DataStructures;


import java.io.Serializable;
import java.util.regex.Pattern;

public class CreditCard implements Serializable {

    public enum CardType implements Serializable {
        MASTER(2) {
            public String toString() {
                return "Master Card";
            } // 2
        },
        VISA(1) {
            public String toString() {
                return "Visa";
            } // 1
        };
        int idTypeInDB;

        CardType(int x) {
            this.idTypeInDB = x;
        }

        public int getIdType() {
            return idTypeInDB;
        }

    }

    private CardType type;
    private Integer imageId;

    private String cardNumber;
    private int monthExpiration;
    private int yearExpiration;

    // to check if the card is valid done in sets(still need to be done)
    private boolean validCard;

    public boolean isValid() {
        return this.validCard;
    }

    public CreditCard(CardType type, Integer imageId) {
        this.type = type;
        this.imageId = imageId;
        this.validCard = false;
        this.cardNumber = "";
    }

    public CreditCard(CardType type, Integer imageId, CreditCard card) {
        this.type = type;
        this.imageId = imageId;

        if (card != null) {
            this.validCard = card.validCard;
            this.cardNumber = card.cardNumber;
            this.monthExpiration = card.monthExpiration;
            this.yearExpiration = card.yearExpiration;
        } else {
            this.validCard = false;
            this.cardNumber = "";
        }
    }

    public Integer getImageId() {
        return this.imageId;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public int getMonthExpiration() {
        return this.monthExpiration;
    }

    public int getYearExpiration() {
        return this.yearExpiration;
    }

    public String getText() {
        return this.type.toString();
    }

    public CardType getType() {
        return this.type;
    }

    public int getTypeDbId() {
        return this.type.getIdType();
    }


    public void setCardType(CardType type) {
        this.type = type;
    }

    public void setCardNumber(String number) {
        this.cardNumber = number;
        this.validCard = Pattern.matches("^4[0-9]{12}(?:[0-9]{3})?$", number) || Pattern.matches("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$", number);
    }

    public void setMonthExpiration(int month) {
        this.monthExpiration = month;
    }

    public void setYearExpiration(int year) {
        this.yearExpiration = year;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof CreditCard)) return false;
        CreditCard otherMyClass = (CreditCard) other;

        return this.type == otherMyClass.type &&
                imageId.intValue() == otherMyClass.imageId.intValue() &&
                cardNumber.equals(otherMyClass.cardNumber) &&
                monthExpiration == otherMyClass.monthExpiration &&
                yearExpiration == otherMyClass.yearExpiration &&
                validCard == otherMyClass.validCard;
    }
}
