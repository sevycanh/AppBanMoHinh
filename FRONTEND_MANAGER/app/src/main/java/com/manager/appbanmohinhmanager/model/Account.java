package com.manager.appbanmohinhmanager.model;

public class Account {
    private int account_id;
    private String email;
    private String username;
    private String address;
    private String phone;
    private int check_in;
    private int luckybox;
    private int role;
    private int status;
    private String last_login;
    private String token;
    private int coin;
    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCheck_in() {
        return check_in;
    }

    public void setCheck_in(int check_in) {
        this.check_in = check_in;
    }

    public int getLuckybox() {
        return luckybox;
    }

    public void setLuckybox(int luckybox) {
        this.luckybox = luckybox;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
