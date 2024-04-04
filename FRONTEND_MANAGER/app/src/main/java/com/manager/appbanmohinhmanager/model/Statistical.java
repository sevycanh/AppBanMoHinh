package com.manager.appbanmohinhmanager.model;

public class Statistical {
    private int product_id;
    private String name;
    private int sum;
    private int SumMonth;

    private String Month;

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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSumMonth() {
        return SumMonth;
    }

    public void setSumMonth(int sumMonth) {
        SumMonth = sumMonth;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        this.Month = month;
    }
}
