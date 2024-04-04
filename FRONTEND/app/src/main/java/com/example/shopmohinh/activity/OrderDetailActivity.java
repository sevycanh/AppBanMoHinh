package com.example.shopmohinh.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.adapter.OrderDetailAdapter;
import com.example.shopmohinh.model.ItemOrderDetail;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;


import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderDetailActivity extends AppCompatActivity {
    Toolbar toolBar_OrderDetail;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    TextView tv_order_id_detail, tv_address_detail, tv_phone_detail, tv_order_status_detail, tv_name_detail, tv_date_detail, tv_payment_detail, tv_total_detail;
    private List<ItemOrderDetail> itemOrderDetailList;
    private OrderDetailAdapter orderDetailAdapter;
    private RecyclerView orderDetailRecylerView;
    private Button btn_CancelOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        AnhXa();
        actionToolBar_OrderDetail();
        setDetailInformation();
        btn_CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                int order_id_Detail = Integer.parseInt(intent.getStringExtra("order_id"));
                cancelOrder(order_id_Detail);
            }
        });
    }

    private void AnhXa() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolBar_OrderDetail = findViewById(R.id.toolBar_OrderDetail);
        tv_order_id_detail = findViewById(R.id.tv_order_id_detail);
        tv_address_detail = findViewById(R.id.tv_address_detail);
        tv_phone_detail = findViewById(R.id.tv_phone_account_detail);
        tv_name_detail = findViewById(R.id.tv_accountName_detail);
        tv_order_status_detail = findViewById(R.id.tv_order_status_detail);
        tv_date_detail = findViewById(R.id.order_date_detail);
        tv_payment_detail = findViewById(R.id.tv_payment_method_detail);
        tv_total_detail = findViewById(R.id.tv_total_detail);
        orderDetailRecylerView = findViewById(R.id.recylerView_OrderDetail);
        btn_CancelOrder = findViewById(R.id.btn_CancelOrder);
    }

    private void actionToolBar_OrderDetail() {
        setSupportActionBar(toolBar_OrderDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar_OrderDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void setDetailInformation() {
        Intent intent = getIntent();
        int order_id_Detail = Integer.parseInt(intent.getStringExtra("order_id"));
        getItemOrderDetail(order_id_Detail);
        String name = intent.getStringExtra("account_name");
        int order_status = Integer.parseInt(intent.getStringExtra("order_status"));
        String payment = intent.getStringExtra("paymentMethod");
        String address = intent.getStringExtra("address");
        String date = intent.getStringExtra("date");
        String phone = intent.getStringExtra("phone");
        String total = intent.getStringExtra("total");
        tv_order_id_detail.setText(String.valueOf(order_id_Detail));
        tv_name_detail.setText(name);
        tv_order_status_detail.setText(checkOrderStatus(order_status));
        tv_payment_detail.setText(payment);
        tv_address_detail.setText(address);
        tv_date_detail.setText(date);
        tv_phone_detail.setText(phone);
        tv_total_detail.setText(total + " VND");
    }

    private String checkOrderStatus(int order_status) {
        String status = "";
        if (order_status == 1) {
            status = "Chưa xác nhận";
            tv_order_status_detail.setTextColor(ContextCompat.getColor(this, R.color.grey));
        } else if (order_status == 2) {
            status = "Đã xác nhận";
            shutdownBtnCancel();
        } else if (order_status == 3) {
            status = "Đang giao";
            tv_order_status_detail.setTextColor(ContextCompat.getColor(this, R.color.yellow));
            shutdownBtnCancel();

        } else if (order_status == 4) {
            status = "Giao thành công";
            tv_order_status_detail.setTextColor(ContextCompat.getColor(this, R.color.green));
            shutdownBtnCancel();

        } else {
            status = "Đã hủy";
            tv_order_status_detail.setTextColor(ContextCompat.getColor(this, R.color.red));
            shutdownBtnCancel();
        }
        return status;
    }

    private void shutdownBtnCancel() {
        btn_CancelOrder.setClickable(false);
        btn_CancelOrder.setFocusable(false);
        btn_CancelOrder.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
    }

    private void getItemOrderDetail(int orderId) {
        compositeDisposable.add(apiBanHang.getItemOrderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        itemOrderDetailModel -> {
                            if (itemOrderDetailModel.isSuccess()) {
                                itemOrderDetailList = itemOrderDetailModel.getResult();
                                orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(), itemOrderDetailList);
                                orderDetailRecylerView.setAdapter(orderDetailAdapter);
                                orderDetailRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void cancelOrder(int order_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setTitle("Xác nhận hủy đơn hàng");
        builder.setMessage("Bạn có chắc muốn hủy đơn hàng không ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(OrderDetailActivity.this, "Đồng ý hủy đơn hàng :" + order_id, Toast.LENGTH_SHORT).show();
                updateOrderStatus(order_id);
                finish();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateOrderStatus(int orderId) {
        compositeDisposable.add(apiBanHang.updateOrderStatus(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            // Xử lý kết quả ở đây nếu cần
                            if (orderModel.isSuccess()) {
//                                Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã hủy", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(OrderDetailActivity.this, "Hủy thất bại", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.d("Loi xoa san pham", throwable.getMessage());
                        }
                ));
    }

}