package com.example.sebastian.smb;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CartItem {
    private boolean isPurchased;
    private String itemName;
    private int quantity;
    private double price;

    public CartItem(){
    }

    public CartItem(String name, int quantity, double price, boolean isPurchased){
        this.itemName = name;
        this.quantity = quantity;
        this.price = price;
        this.isPurchased = isPurchased;
    }

    public String getName(){
        return itemName;
    }
    public void setName(String name){
        this.itemName = name;
    }

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }

    public boolean getIsPurchased() {
        return isPurchased;
    }
    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String priceAsString(){
        return String.valueOf(price) + " zł";
    }

    @Override
    public boolean equals(Object obj) {
        return this.itemName.equals(((CartItem)obj).itemName);
    }

    @Override
    public int hashCode() {
        return this.itemName.hashCode();
    }
}
