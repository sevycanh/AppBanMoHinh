package com.example.shopmohinh.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class User {
    int account_id;
    String email;
    String username;
    String password;
    String address;
    String province;
    String district;
    String ward;
    String administrative_address;
    String phone;
    int coin;
    int check_in;
    int luckybox;
    String last_login;
    int role;
    int status;
    String token;

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

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAdministrative_address() {
        return administrative_address;
    }
    public void setAdministrative_address(String administrative_address) {
        this.administrative_address = administrative_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
