package com.example.shopmohinh.address;


import org.json.JSONObject;

public class GetAddress {
    public JSONObject getProvince(){
        JSONObject data = HttpProvider.sendGet(AddressInfo.URL_PROVINCE);
        return data;
    }

    public JSONObject getDistrict(String province_id){
        JSONObject data = HttpProvider.sendGet(AddressInfo.URL_DISTRICT + province_id);
        return data;
    }

    public JSONObject getWard(String district_id){
        JSONObject data = HttpProvider.sendGet(AddressInfo.URL_WARD + district_id);
        return data;
    }
}
