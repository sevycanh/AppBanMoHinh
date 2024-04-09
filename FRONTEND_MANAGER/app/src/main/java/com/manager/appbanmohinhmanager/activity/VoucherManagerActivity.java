package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.ProductManagerAdapter;
import com.manager.appbanmohinhmanager.adapter.VoucherManagerAdapter;
import com.manager.appbanmohinhmanager.helper.SwipeHelper;
import com.manager.appbanmohinhmanager.model.Coupon;
import com.manager.appbanmohinhmanager.retrofit.ApiBanHang;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VoucherManagerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtKho, txtRieng;
    View viewKho, viewRieng;
    ImageView imgThemVoucher;
    RecyclerView recyclerView;
    VoucherManagerAdapter voucherManagerAdapter;
    List<Coupon> couponList;
    CompositeDisposable compositeDisposable;
    ApiBanHang apiBanHang;
    SwipeHelper swipeHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_manager);

        initView();
        actionToolBar();
        initControll();
        getCouponPublic();
        initHelper();
    }

    private void initHelper() {
        swipeHelper= new SwipeHelper(this, recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                AlertDialog.Builder builder  = new AlertDialog.Builder(viewHolder.itemView.getContext());
                                builder.setTitle("Thông báo");
                                builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?");
                                builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        delete(couponList.get(pos).getCoupon_id());
                                        couponList.remove(pos);
                                        voucherManagerAdapter.notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();

                            }
                        }
                ));
            }
        };
    }

    private void delete(int id){
        compositeDisposable.add(apiBanHang.deleteVoucher(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                            } else {
                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void getCouponPublic() {
        compositeDisposable.add(apiBanHang.getCoupon_Manager("isPublic",1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        couponModel -> {
                            if (couponModel.isSuccess()){
                                couponList = couponModel.getResult();
                                voucherManagerAdapter = new VoucherManagerAdapter(getApplicationContext(), couponList, 1);
                                recyclerView.setAdapter(voucherManagerAdapter);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), couponModel.getMessage(), Toast.LENGTH_LONG).show();
                                recyclerView.setVisibility(View.GONE);
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void getCouponPrivate() {
        compositeDisposable.add(apiBanHang.getCoupon_Manager("isPrivate",0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        couponModel -> {
                            if (couponModel.isSuccess()){
                                couponList = couponModel.getResult();
                                voucherManagerAdapter = new VoucherManagerAdapter(getApplicationContext(), couponList, 0);
                                recyclerView.setAdapter(voucherManagerAdapter);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), couponModel.getMessage(), Toast.LENGTH_LONG).show();
                                recyclerView.setVisibility(View.GONE);
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void initControll() {
        txtKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewKho.setVisibility(View.VISIBLE);
                viewRieng.setVisibility(View.GONE);
                txtKho.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                txtRieng.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                getCouponPublic();
            }
        });
        txtRieng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewKho.setVisibility(View.GONE);
                viewRieng.setVisibility(View.VISIBLE);
                txtRieng.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
                txtKho.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                getCouponPrivate();
            }
        });
        imgThemVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddVoucherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBarVoucher);
        txtKho = findViewById(R.id.txtKhoVoucher_Voucher);
        txtRieng = findViewById(R.id.txtDanhRieng_Voucher);
        viewKho = findViewById(R.id.viewKhoVoucher);
        viewRieng = findViewById(R.id.viewDanhRiengVoucher);
        imgThemVoucher = findViewById(R.id.imgThemVoucher);

        recyclerView = findViewById(R.id.rcvVoucher_Manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        compositeDisposable = new CompositeDisposable();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        couponList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewKho.setVisibility(View.VISIBLE);
        viewRieng.setVisibility(View.GONE);
        txtKho.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));
        txtRieng.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        getCouponPublic();

    }
}