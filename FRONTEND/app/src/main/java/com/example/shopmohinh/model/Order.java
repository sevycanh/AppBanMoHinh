package com.example.shopmohinh.model;

public class Order {
    private int order_id;
    private int account_id;
    private int total;
    private int order_status;
    private String product_name;
    private int product_id;
    private  int perUnit;
    private  int productsInOrder;
    private String main_img;
    private int quantity;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getPerUnit() {
        return perUnit;
    }

    public void setPerUnit(int perUnit) {
        this.perUnit = perUnit;
    }

    public int getProductsInOrder() {
        return productsInOrder;
    }

    public void setProductsInOrder(int productsInOrder) {
        this.productsInOrder = productsInOrder;
    }

    public String getMain_img() {
        return main_img;
    }

    public void setMain_img(String main_img) {
        this.main_img = main_img;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
