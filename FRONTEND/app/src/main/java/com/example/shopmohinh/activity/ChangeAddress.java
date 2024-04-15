package com.example.shopmohinh.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.adapter.CartAdapter;
import com.example.shopmohinh.adapter.ChangeAddressAdapter;
import com.example.shopmohinh.address.District;
import com.example.shopmohinh.address.GetAddress;
import com.example.shopmohinh.address.Province;
import com.example.shopmohinh.address.Ward;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangeAddress extends AppCompatActivity {
    MaterialToolbar toolbar;
    RadioButton radio_buttonProvince, radio_buttonDistrict, radio_buttonWard;

    TextView txtAddressChange;

    RecyclerView recyclerViewAddress;
    Province provinceUser;
    District districtUser;
    Ward wardUser;
    ChangeAddressAdapter adapter;
    List<Province> provinceList;

    List<District> districtList;

    List<Ward> wardList;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        initControl();
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        wardList = new ArrayList<>();

        if(!Utils.user_current.getAdministrative_address().isEmpty()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        GetAddress address = new GetAddress();
                        final JSONObject data = address.getProvince();
                        JSONArray resultsArray = data.getJSONArray("results");
                        Gson gson = new Gson();
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject provinceObject = resultsArray.getJSONObject(i);
                            Province province = gson.fromJson(provinceObject.toString(), Province.class);
                            provinceList.add(province);
                        }

                        final JSONObject data1 = address.getDistrict(Utils.user_current.getProvince());
                        resultsArray = data1.getJSONArray("results");
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject districtObject = resultsArray.getJSONObject(i);
                            District district = gson.fromJson(districtObject.toString(), District.class);
                            districtList.add(district);
                        }

                        final JSONObject data2 = address.getWard(Utils.user_current.getDistrict());
                        resultsArray = data2.getJSONArray("results");
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject wardObject = resultsArray.getJSONObject(i);
                            Ward ward = gson.fromJson(wardObject.toString(), Ward.class);
                            wardList.add(ward);
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < provinceList.size(); i++) {
                                    if(provinceList.get(i).getProvince_id().equals(Utils.user_current.getProvince())){
                                        provinceUser = provinceList.get(i);
                                        radio_buttonProvince.setText(provinceUser.getProvince_name());

                                    }
                                }
                                for (int i = 0; i < districtList.size(); i++) {
                                    if(districtList.get(i).getDistrict_id().equals(Utils.user_current.getDistrict())){
                                        districtUser = districtList.get(i);
                                        radio_buttonDistrict.setText(districtUser.getDistrict_name());
                                    }
                                }
                                for (int i = 0; i < wardList.size(); i++) {
                                    if(wardList.get(i).getWard_id().equals(Utils.user_current.getWard())){
                                        wardUser = wardList.get(i);
                                        radio_buttonWard.setText(wardUser.getWard_name());
                                    }
                                }
                                radio_buttonDistrict.setVisibility(View.VISIBLE);
                                radio_buttonWard.setVisibility(View.VISIBLE);
                                radio_buttonWard.toggle();
                            }
                        });
                    } catch (final Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Handle exception in UI
                                Log.e("PROVINCE", "Exception: " + e.getMessage());
                            }
                        });
                    }
                }
            }).start();
        }
        radio_buttonProvince.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    txtAddressChange.setText("Tỉnh/ Thành Phố");
                    provinceList.clear();
                    districtList.clear();
                    wardList.clear();
                    radio_buttonProvince.setBackgroundResource(R.drawable.custom_address);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GetAddress address = new GetAddress();
                                final JSONObject data = address.getProvince();
                                JSONArray resultsArray = data.getJSONArray("results");
                                Gson gson = new Gson();
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject provinceObject = resultsArray.getJSONObject(i);
                                    Province province = gson.fromJson(provinceObject.toString(), Province.class);
                                    provinceList.add(province);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new ChangeAddressAdapter(getApplicationContext(), "province");
                                        adapter.setProvinceList(provinceList);
                                        adapter.setOnItemClickListener(new ChangeAddressAdapter.OnItemClickListener(){
                                            @Override
                                            public void onClick(int position) {
                                                provinceUser = provinceList.get(position);
                                                radio_buttonProvince.setText(provinceUser.getProvince_name());
                                                radio_buttonDistrict.setText("Quận/ Huyện");
                                                radio_buttonDistrict.setVisibility(View.VISIBLE);
                                                radio_buttonWard.setVisibility(View.GONE);
                                                radio_buttonWard.setText("Phường/ Xã");
                                                radio_buttonDistrict.toggle();
                                            }
                                        });
                                        recyclerViewAddress.setAdapter(adapter);
                                        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        Log.d("PROVINCE", String.valueOf(data));
                                    }
                                });
                            } catch (final Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Handle exception in UI
                                        Log.e("PROVINCE", "Exception: " + e.getMessage());
                                    }
                                });
                            }
                        }
                    }).start();
                }
                else{
                    radio_buttonProvince.setBackground(null);
                }
            }
        });
        radio_buttonDistrict.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    txtAddressChange.setText("Quận/ Huyện");
                    districtList.clear();
                    wardList.clear();
                    radio_buttonDistrict.setBackgroundResource(R.drawable.custom_address);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GetAddress address = new GetAddress();
                                final JSONObject data = address.getDistrict(provinceUser.getProvince_id());
                                JSONArray resultsArray = data.getJSONArray("results");
                                Gson gson = new Gson();
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject districtObject = resultsArray.getJSONObject(i);
                                    District district = gson.fromJson(districtObject.toString(), District.class);
                                    districtList.add(district);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new ChangeAddressAdapter(getApplicationContext(), "district");
                                        adapter.setDistrictList(districtList);
                                        adapter.setOnItemClickListener(new ChangeAddressAdapter.OnItemClickListener(){
                                            @Override
                                            public void onClick(int position) {
                                                districtUser = districtList.get(position);
                                                radio_buttonDistrict.setText(districtList.get(position).getDistrict_name());
                                                radio_buttonWard.setText("Phường/ Xã");
                                                radio_buttonWard.setVisibility(View.VISIBLE);
                                                radio_buttonWard.toggle();
                                            }
                                        });
                                        recyclerViewAddress.setAdapter(adapter);
                                        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                        Log.d("DISTRICT", String.valueOf(districtUser.getDistrict_id()));
                                    }
                                });
                            } catch (final Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Handle exception in UI
                                        Log.e("DISTRICT", "Exception: " + e.getMessage());
                                    }
                                });
                            }
                        }
                    }).start();
                }
                else{
                    radio_buttonDistrict.setBackground(null);
                }
            }
        });
        radio_buttonWard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    txtAddressChange.setText("Phường/ Xã");
                    wardList.clear();
                    radio_buttonWard.setBackgroundResource(R.drawable.custom_address);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GetAddress address = new GetAddress();
                                final JSONObject data = address.getWard(districtUser.getDistrict_id());
                                JSONArray resultsArray = data.getJSONArray("results");
                                Gson gson = new Gson();
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject wardObject = resultsArray.getJSONObject(i);
                                    Ward ward = gson.fromJson(wardObject.toString(), Ward.class);
                                    wardList.add(ward);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new ChangeAddressAdapter(getApplicationContext(), "ward");
                                        adapter.setWardList(wardList);
                                        adapter.setOnItemClickListener(new ChangeAddressAdapter.OnItemClickListener(){
                                            @Override
                                            public void onClick(int position) {
                                                wardUser = wardList.get(position);
                                                radio_buttonWard.setText(wardList.get(position).getWard_name());
                                                Utils.user_current.setProvince(provinceUser.getProvince_id());
                                                Utils.user_current.setDistrict(districtUser.getDistrict_id());
                                                Utils.user_current.setWard(wardUser.getWard_id());
                                                Utils.user_current.setAdministrative_address(provinceUser.getProvince_name()+"\n" + districtUser.getDistrict_name() + "\n" + wardUser.getWard_name());
                                                SaveAdministrativeAddress();
                                            }
                                        });
                                        recyclerViewAddress.setAdapter(adapter);
                                        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        Log.d("Ward", String.valueOf(data));
                                    }
                                });
                            } catch (final Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Handle exception in UI
                                        Log.e("Ward", "Exception: " + e.getMessage());
                                    }
                                });
                            }
                        }
                    }).start();
                }
                else{
                    radio_buttonWard.setBackground(null);
                }
            }
        });
    }

    private void SaveAdministrativeAddress() {
        Paper.book().write("user", Utils.user_current);
        compositeDisposable.add(apiBanHang.updateAddressProfile(Utils.user_current.getAccount_id(), Utils.user_current.getProvince(), Utils.user_current.getDistrict(), Utils.user_current.getWard(), Utils.user_current.getAdministrative_address())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
//                                 Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        },
                        throwable -> {
//                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarChangeAddress);
        radio_buttonProvince = findViewById(R.id.radio_buttonProvince);
        radio_buttonDistrict = findViewById(R.id.radio_buttonDistrict);
        radio_buttonWard = findViewById(R.id.radio_buttonWard);
        recyclerViewAddress = findViewById(R.id.recyclerViewAddress);
        txtAddressChange= findViewById(R.id.txtAddressChange);
    }
}