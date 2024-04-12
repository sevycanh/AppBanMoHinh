package com.manager.appbanmohinhmanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.MaterialToolbar;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.OrderDetailManagerAdapter;
import com.manager.appbanmohinhmanager.model.ItemOrderDetailManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderDetailManagerActivity extends AppCompatActivity {
    MaterialToolbar toolBar_OrderDetail;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiManager apiManager;

    TextView tv_email, tv_order_id_detail, tv_address_detail, tv_phone_detail, tv_order_status_detail, tv_name_detail, tv_date_detail, tv_payment_detail, tv_total_detail;
    private List<ItemOrderDetailManager> itemOrderDetailList;
    private OrderDetailManagerAdapter orderDetailAdapter;
    private RecyclerView orderDetailRecylerView;
    private Button btn_changeStatus;
    private Spinner sp_order_detail;
    private int status;
    private int order_id_Detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_manager);
        AnhXa();
        actionToolBar_OrderDetail();
        setDetailInformation();
        btn_changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                int order_id_Detail = Integer.parseInt(intent.getStringExtra("order_id"));
                changeStatus(order_id_Detail, status);
            }
        });
    }

    private void AnhXa() {
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
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
        btn_changeStatus = findViewById(R.id.btn_CancelOrder);
        tv_email = findViewById(R.id.tv_email_detail);
        sp_order_detail = findViewById(R.id.sp_order_status_detail);
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
        order_id_Detail = Integer.parseInt(intent.getStringExtra("order_id"));
        getItemOrderDetail(order_id_Detail);
        String name = intent.getStringExtra("account_name");
        int order_status = Integer.parseInt(intent.getStringExtra("order_status"));
        String payment = intent.getStringExtra("paymentMethod");
        String address = intent.getStringExtra("address");
        String date = intent.getStringExtra("date");
        String phone = intent.getStringExtra("phone");
        String total = intent.getStringExtra("total");
        String email = intent.getStringExtra("email");
        tv_order_id_detail.setText(String.valueOf(order_id_Detail));
        tv_name_detail.setText(name);
        checkOrderStatus(order_status);
        tv_payment_detail.setText(payment);
        tv_address_detail.setText(address);
        tv_date_detail.setText(date);
        tv_phone_detail.setText(phone);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_total_detail.setText("Price: "+decimalFormat.format(Double.parseDouble(total))+"đ");
//        tv_total_detail.setText(total + " VND");
        tv_email.setText(email);
    }

    private void checkOrderStatus(int order_status) {
        if (order_status == 1) {
            tv_order_status_detail.setText("Chưa xác nhận");
            tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
            enableBtn();
        } else if (order_status == 2) {
            tv_order_status_detail.setText("Đã xác nhận");
            tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            enableBtn();
        } else if (order_status == 3) {
            tv_order_status_detail.setText("Đang giao");
            tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
            enableBtn();
        } else if (order_status == 4) {
            tv_order_status_detail.setText("Giao thành công");
            tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            enableBtn();
        } else if (order_status == 5) {
            tv_order_status_detail.setText("Đã hủy");
            tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            shutdownBtn();
        }
    }


    private void shutdownBtn() {
        btn_changeStatus.setText("Chuyển trạng thái");
        btn_changeStatus.setEnabled(false);
        btn_changeStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
    }

    private void enableBtn() {
        btn_changeStatus.setText("Chuyển trạng thái");
        btn_changeStatus.setEnabled(true);
        btn_changeStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }

    private void getItemOrderDetail(int orderId) {
        compositeDisposable.add(apiManager.getItemOrderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        itemOrderDetailModel -> {
                            if (itemOrderDetailModel.isSuccess()) {
                                itemOrderDetailList = itemOrderDetailModel.getResult();
                                orderDetailAdapter = new OrderDetailManagerAdapter(getApplicationContext(), itemOrderDetailList);
                                orderDetailRecylerView.setAdapter(orderDetailAdapter);
                                orderDetailRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void changeStatus(int order_id, int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailManagerActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.spinner_status, null);
        builder.setView(dialogView);
        builder.setTitle("Thay đổi trạng thái đơn hàng");
        builder.setMessage("Bạn có muốn thay đổi không?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Lấy trạng thái mới từ Spinner
                Spinner spinnerStatus = dialogView.findViewById(R.id.sp_order_status_detail);
                int newStatus = spinnerStatus.getSelectedItemPosition();
                newStatus = newStatus + 1;
//                Toast.makeText(OrderDetailManagerActivity.this, "orderid:" + order_id_Detail + "status: " + newStatus, Toast.LENGTH_SHORT).show();
                updateOrderStatusManager(order_id_Detail, newStatus);
                tv_order_status_detail.setText(spinnerStatus.getSelectedItem().toString());
                if (newStatus == 1) {
                    tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                } else if (newStatus == 2) {
                    tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                } else if (newStatus == 3) {
                    tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                } else if (newStatus == 4) {
                    tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    //add coin
                    Intent intent = getIntent();
                    int coin_receive = (Integer.parseInt(intent.getStringExtra("total")) * 100) / 100000;
                    compositeDisposable.add(apiManager.addCoin(order_id_Detail, coin_receive)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                    }, throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            ));
                } else if (newStatus == 5) {
                    tv_order_status_detail.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                }
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

        // Thiết lập dữ liệu cho Spinner
        Spinner spinnerStatus = dialogView.findViewById(R.id.sp_order_status_detail);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.changeOrderStatus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }


    private void updateOrderStatusManager(int orderId, int order_status) {
        compositeDisposable.add(apiManager.updateOrderStatusManager(orderId, order_status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        inforDetailModel -> {
                            // Xử lý kết quả ở đây nếu cần
                            if (inforDetailModel.isSuccess()) {
                                Toast.makeText(this, "Update status success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OrderDetailManagerActivity.this, "Hủy thất bại", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.d("Loi update status", throwable.getMessage());
                        }
                ));
    }
}