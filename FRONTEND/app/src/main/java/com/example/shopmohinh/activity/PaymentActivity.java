package com.example.shopmohinh.activity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.shopmohinh.R;
import com.example.shopmohinh.model.Cart;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.google.gson.Gson;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    //    RecyclerView recyclerViewDatHang;
    AppCompatButton btnDatHang;
    TextView txtTongTienDatHang, txtThongTinKH, txtDiaChiKH, txtPTThanhToan;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongtien;

    ApiBanHang apiBanHang;

    LinearLayout btnChangeAddress, btnChangePaymentMethod;
    int iddonhang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //zalo
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        ZaloPaySDK.init(2553, Environment.SANDBOX);

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

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dấu phân tách hàng nghìn
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###", symbols);

        tongtien = getIntent().getLongExtra("tongtien", 0);
        String tongTienFormat = decimalFormat.format(tongtien);
        txtTongTienDatHang.setText(tongTienFormat);
        txtThongTinKH.setText(Utils.user_current.getUsername() + " " + Utils.user_current.getPhone());
        txtDiaChiKH.setText(Utils.user_current.getAddress());
//        txtPTThanhToan.setText(Utils.PTThanhToan);

        btnChangePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressUserActivity.class);
                startActivity(intent);
            }
        });

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", new Gson().toJson(Utils.purchases));
                String name = Utils.user_current.getUsername();
                String sdt = String.valueOf(Utils.user_current.getPhone());
                String address = Utils.user_current.getAddress();

                Log.d("test", address);

                int id = Utils.user_current.getAccount_id();
                if(name.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Thông tin chưa đầy đủ !!!", Toast.LENGTH_LONG).show();
                }
                else if(sdt.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Thông tin chưa đầy đủ !!!", Toast.LENGTH_LONG).show();
                } else if (address.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Thông tin chưa đầy đủ !!!", Toast.LENGTH_LONG).show();
                }else {
                    compositeDisposable.add(apiBanHang.createOrder(name, sdt, String.valueOf(tongtien), id, address, new Gson().toJson(Utils.purchases))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                                        for (int i =0; i< Utils.purchases.size();i++){
                                            Cart gioHang = Utils.purchases.get(i);
                                            UpdateCartApi(Utils.purchases.get(i).getIdProduct(), 0);
                                            if (Utils.carts.contains(gioHang)){
                                                Utils.carts.remove(gioHang);
                                            }
                                        }
                                        //clear danh sach sp da chon
                                        Utils.purchases.clear();
                                        iddonhang = Integer.parseInt(messageModel.getIddonhang());
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
//                                    requestZalo();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });

    }

    private void UpdateCartApi(int productId ,int quantity){
        compositeDisposable.add(apiBanHang.updateShoppingCart(Utils.user_current.getAccount_id(), productId, quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
//                            Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

//    private void requestZalo() {
//        CreateOrder orderApi = new CreateOrder();
//
//        try {
//            JSONObject data = orderApi.createOrder(String.valueOf(tongtien));
//            Log.d("Amount", String.valueOf(tongtien));
//            String code = data.getString("return_code");
//            Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();
//
//            if (code.equals("1")) {
//                String token = data.getString("zp_trans_token");
//                Log.d("token", token);
//                Log.d("iddonhang", String.valueOf(iddonhang));
//
//                ZaloPaySDK.getInstance().payOrder(ThanhToanActivity.this, token, "demozpdk://app", new PayOrderListener() {
//                    @Override
//                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        compositeDisposable.add(apiBanHang.updateZalo(iddonhang,token)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(
//                                        messageModel -> {
//                                            Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        },
//                                        throwable -> {
//                                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                ));
//                    }
//
//                    @Override
//                    public void onPaymentCanceled(String s, String s1) {
//                        new AlertDialog.Builder(ThanhToanActivity.this)
//                                .setTitle("User Cancel Payment")
//                                .setMessage(String.format("zpTransToken: %s \n", token))
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                })
//                                .setNegativeButton("Cancel", null).show();
//                    }
//
//                    @Override
//                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//                        new AlertDialog.Builder(ThanhToanActivity.this)
//                                .setTitle("Payment Fail")
//                                .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), token))
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                })
//                                .setNegativeButton("Cancel", null).show();
//                    }
//                });
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private void initView() {
        toolbar = findViewById(R.id.toolBarPayment);
//        recyclerViewDatHang = findViewById(R.id.recyclerViewDatHang);
        btnDatHang = findViewById(R.id.btnDatHang);
        txtTongTienDatHang = findViewById(R.id.txtTongTienDatHang);
        txtThongTinKH = findViewById(R.id.txtThongTinKH);
        txtDiaChiKH = findViewById(R.id.txtDiaChiKH);
        txtPTThanhToan = findViewById(R.id.txtPTThanhToan);
        btnChangeAddress = findViewById(R.id.btnChangeAddress);
        btnChangePaymentMethod = findViewById(R.id.btnChangePaymentMethod);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initControl();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        ZaloPaySDK.getInstance().onResult(intent);
//    }
}