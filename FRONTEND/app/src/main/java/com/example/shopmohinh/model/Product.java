package com.example.shopmohinh.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("product_id")
    int product_id;
    String name;
    int price;
    int quantity;
    String description;
    String main_image;
    String sub_image;
    int coupon;
    @SerializedName("caregory_id")
    int caregory_id;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getSub_image() {
        return sub_image;
    }

    public void setSub_image(String sub_image) {
        this.sub_image = sub_image;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public int getCaregory_id() {
        return caregory_id;
    }

    public void setCaregory_id(int caregory_id) {
        this.caregory_id = caregory_id;
    }
}