package com.example.shopmohinh.address;
public class District {
    private String district_id;
    private String district_name;
    private String district_type;
    private String lat;
    private String lng;
    private String province_id;

    // Getters and setters
    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getDistrict_type() {
        return district_type;
    }

    public void setDistrict_type(String district_type) {
        this.district_type = district_type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }
}
