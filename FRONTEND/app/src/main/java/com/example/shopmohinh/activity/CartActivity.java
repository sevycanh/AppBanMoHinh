package com.example.shopmohinh.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.adapter.CartAdapter;
import com.example.shopmohinh.helper.SwipeHelper;
import com.example.shopmohinh.model.EventBus.TinhTongEvent;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien, txtCouponSelect;
    Toolbar toolbar;
    RecyclerView recyclerViewGioHang;
    Button btnMuaHang, btnDeleteGioHang;
    CartAdapter gioHangAdapter;
    SwipeHelper swipeHelper;
    LinearLayout couponSelect;
    long tongtiensp;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initControl();
        initHelper();
    }

    private void initHelper() {
        swipeHelper= new SwipeHelper(this, recyclerViewGioHang) {
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
//                                        Utils.mangmuahang.remove(Utils.manggiohang.get(pos));
                                        for (int j=0; j<Utils.purchases.size(); j++){
                                            if(Utils.purchases.get(j).getIdProduct() == Utils.carts.get(pos).getIdProduct()){
                                                Utils.purchases.remove(j);
                                                EventBus.getDefault().postSticky(new TinhTongEvent());
                                            }
                                        }
                                        UpdateCartApi(Utils.carts.get(pos).getIdProduct(), 0);
                                        Utils.carts.remove(pos);
                                        gioHangAdapter.notifyDataSetChanged();
                                        EventBus.getDefault().postSticky(new TinhTongEvent());
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

    private void RemoveAllCartApi(int productId){
        compositeDisposable.add(apiBanHang.deleteShoppingCart(Utils.user_current.getAccount_id())
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


    private void TinhTongTien(){
        if(Utils.carts.size() == 0){
            giohangtrong.setVisibility(View.VISIBLE);
        }
        else{
            tongtiensp = 0;
            for(int i =0;i <Utils.purchases.size();i++){
                tongtiensp = tongtiensp + (Utils.purchases.get(i).getPrice() * Utils.purchases.get(i).getQuantity());
            }
            if(Utils.coupon != null) {
                long discount = tongtiensp * Utils.coupon.getDiscount() / 100;
                tongtiensp = tongtiensp - discount;
            }
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setGroupingSeparator('.'); // Dấu phân tách hàng nghìn
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###", symbols);
            String tongtienspFormat = decimalFormat.format(tongtiensp);
            tongtien.setText(tongtienspFormat);
            Log.d("tongtien", tongtienspFormat);
        }
    }


    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.coupon = null;
                finish();
            }
        });

        recyclerViewGioHang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);
        if(Utils.carts.size() == 0){
            giohangtrong.setVisibility(View.VISIBLE);
        }
        else{
            gioHangAdapter = new CartAdapter(getApplicationContext(), Utils.carts);
            recyclerViewGioHang.setAdapter(gioHangAdapter);
            TinhTongTien();
        }
//        giohangtrong.setVisibility(View.VISIBLE);
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.purchases.size() == 0){
                    Toast toast = Toast.makeText( view.getContext() , "Bạn chưa chọn sản phẩm để mua hàng!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Đóng thông báo sau 5 giây
                            Toast.makeText(view.getContext() , "", Toast.LENGTH_SHORT).cancel();
                        }
                    }, 5000); // 5000 milliseconds = 5 giây
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
                    intent.putExtra("tongtien", tongtiensp);
                    startActivity(intent);
                }

            }
        });

        btnDeleteGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn xóa tất cả khỏi giỏ hàng?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.carts.clear();
                        Utils.purchases.clear();
                        RemoveAllCartApi(Utils.user_current.getAccount_id());
                        EventBus.getDefault().postSticky(new TinhTongEvent());
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
        });

        couponSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CouponSelectActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        giohangtrong = findViewById(R.id.txtGioHangTrong);
        tongtien = findViewById(R.id.txtTongTien);
        toolbar = findViewById(R.id.toolbarGioHang);
        recyclerViewGioHang = findViewById(R.id.recyclerViewGioHang);
        btnMuaHang = findViewById(R.id.btnMuaHang);
        btnDeleteGioHang = findViewById(R.id.btnDeleteGioHang);
        couponSelect = findViewById(R.id.CouponSelect);
        txtCouponSelect = findViewById(R.id.txtCouponSelect);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        gioHangAdapter.notifyDataSetChanged();
        TinhTongTien();
        if(Utils.coupon != null && Utils.coupon.getDiscount() > 0){
            txtCouponSelect.setText(Utils.coupon.getDiscount() + "%");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event!=null){
            TinhTongTien();
        }
    }

}