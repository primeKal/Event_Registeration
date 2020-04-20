package com.kalu.mainactivity;

import com.google.firebase.database.ServerValue;

public class ItemModel {

    private String name;
    private Object timestamp;
    private String price;
    private String quantity;
    private String uid;
    private String itemimage;
    private String username;
    private  String userimg;
    private  String itemKey;
    private String Token;

    public ItemModel() {

    }

    public ItemModel(String itemimage, String price, String quantity, String name, String token, String uid, String userimg, String username) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.uid = uid;
        this.itemimage = itemimage;
        this.username = username;
        this.userimg = userimg;
        this.Token=token;
        this.timestamp= ServerValue.TIMESTAMP;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItemimage() {
        return itemimage;
    }

    public void setItemimage(String itemimage) {
        this.itemimage = itemimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }
}
