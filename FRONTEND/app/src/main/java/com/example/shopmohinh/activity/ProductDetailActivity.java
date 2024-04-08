package com.example.shopmohinh.activity;

import static com.example.shopmohinh.utils.NumberWithDotSeparator.formatNumberWithDotSeparator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.model.Cart;
import com.example.shopmohinh.model.ImageAdapter;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView imgCart;
    TextView txtName, txtPrice, txtSalePrice, tvStock, txtDescription,txtQuantity;
    Button btnDecrease, btnIncrease, btnAddToCart;
    Toolbar toolbar;
    ViewPager imageViewPager;
    private CircleIndicator circleIndicator;
    Product product;
    NotificationBadge badge;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.shopmohinh.R.layout.activity_product_detail);
        Mapping();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart();
            }
        });
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCart();
            }
        });
    }

    private void viewCart() {
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }

    private void AddToCart() {
        if (Utils.carts.size() > 0) {
            boolean flag = false;
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            for (int i=0; i<Utils.carts.size();i++){
                if (Utils.carts.get(i).getIdProduct() == product.getProduct_id()){
                    checkQuantityProduct(product.getProduct_id());
                    if(quantity + Utils.carts.get(i).getQuantity() <= Utils.product.getQuantity()){
                        Utils.carts.get(i).setQuantity(quantity + Utils.carts.get(i).getQuantity());
                        UpdateCartApi(Utils.carts.get(i).getQuantity());
                    }
                    else{
                        Utils.carts.get(i).setQuantity(Utils.product.getQuantity());
                        UpdateCartApi(Utils.carts.get(i).getQuantity());
                    }
                    flag = true;
                }
            }
            if (flag == false){
                int price = product.getPrice();
                int discount = product.getPrice() * product.getCoupon() / 100;
                int finalPrice = price - discount;

                Cart cart = new Cart();
                cart.setPrice(finalPrice);
                cart.setQuantity(quantity);
                cart.setIdProduct(product.getProduct_id());
                cart.setName(product.getName());
                cart.setImage(product.getMain_image());
                Utils.carts.add(cart);
                CartApi(quantity);
            }
        } else {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            int price = product.getPrice();
            int discount = product.getPrice() * product.getCoupon() / 100;
            int finalPrice = price - discount;

            Cart cart = new Cart();
            cart.setPrice(finalPrice);
            cart.setQuantity(quantity);
            cart.setIdProduct(product.getProduct_id());
            cart.setName(product.getName());
            cart.setImage(product.getMain_image());
            Utils.carts.add(cart);
            CartApi(quantity);
        }
        badge.setText(String.valueOf(Utils.carts.size()));
    }

    private void initData() {
        imgCart = findViewById(R.id.imgCart_CTSP);
        product = (Product) getIntent().getSerializableExtra("productDetail");
        txtName.setText(product.getName());

        //Calculator present product * price product
        int price = product.getPrice();
        int discount = product.getPrice() * product.getCoupon() / 100;
        int finalPrice = price - discount;
        txtSalePrice.setText(formatNumberWithDotSeparator(finalPrice) + " VNĐ");
        if (product.getCoupon() > 0) {
            txtPrice.setText(String.valueOf(formatNumberWithDotSeparator(product.getPrice())) + " VNĐ");
        } else {
            txtPrice.setText("");
        }
        tvStock.setText(String.valueOf(product.getQuantity()));
        txtDescription.setText(product.getDescription());

        String subImages = product.getSub_image();
        String[] subImageArray = subImages.split(",");
        List<String> subImageList = new ArrayList<>();

        for (String subImage : subImageArray) {
            subImageList.add(subImage.trim());
        }

        ImageAdapter imageAdapter = new ImageAdapter(subImageList,this);
        imageViewPager.setAdapter(imageAdapter);
        circleIndicator.setViewPager(imageViewPager);

        final int[] quantity = {1};
        int maxQuantity = product.getQuantity();
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] < maxQuantity) {
                    quantity[0]++;
                    txtQuantity.setText(String.valueOf(quantity[0]));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Số lượng sản phẩm vượt quá giới hạn!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Đóng Dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    txtQuantity.setText(String.valueOf(quantity[0]));
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Số lượng không được nhỏ hơn 1!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void Mapping() {
        txtName = findViewById(R.id.txtNameProduct);
        txtPrice = findViewById(R.id.txtPrice);
        txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtSalePrice = findViewById(R.id.tvSalePrice);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        tvStock = findViewById(R.id.tvStock);
        txtDescription = findViewById(R.id.tvDescription);
        txtQuantity = findViewById(R.id.txtQuantity);
        toolbar = findViewById(R.id.toolbarProductDetail);
        circleIndicator = findViewById(R.id.circleIndicator);
        imageViewPager = findViewById(R.id.imageViewPager);
        badge = findViewById(R.id.menu_quantity);
        if(Utils.carts!=null){
            badge.setText(String.valueOf(Utils.carts.size()));
        }
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        badge.setText(String.valueOf(Utils.carts.size()));
    }

    private void checkQuantityProduct(int productId) {
        compositeDisposable.add(apiBanHang.checkQuantityProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                Utils.product = productModel.getResult().get(0);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void CartApi(int quantity){
        Log.d("Cart", "Success");
        Log.d("Cart", String.valueOf(Utils.user_current.getAccount_id()));
        Log.d("Cart", String.valueOf(product.getProduct_id()));
        Log.d("Cart", String.valueOf(quantity));
        compositeDisposable.add(apiBanHang.shoppingCart(Utils.user_current.getAccount_id(), product.getProduct_id(), quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void UpdateCartApi(int quantity){
        compositeDisposable.add(apiBanHang.updateShoppingCart(Utils.user_current.getAccount_id(), product.getProduct_id(), quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }


}