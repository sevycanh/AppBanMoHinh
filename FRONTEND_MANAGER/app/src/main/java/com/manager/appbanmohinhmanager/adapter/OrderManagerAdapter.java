package com.manager.appbanmohinhmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.activity.OrderDetailManagerActivity;
import com.manager.appbanmohinhmanager.model.InforDetailManager;
import com.manager.appbanmohinhmanager.model.InforDetailManagerModel;
import com.manager.appbanmohinhmanager.model.OrderManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderManagerAdapter extends RecyclerView.Adapter<OrderManagerAdapter.ViewHolder> {
    Context mContext;
    List<OrderManager> array;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiManager apiManager;

    public OrderManagerAdapter(Context mContext, List<OrderManager> array) {
        this.mContext = mContext;
        this.array = array;
    }

    @NonNull
    @Override
    public OrderManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderManager orderManager = array.get(position);
        holder.tv_order_id.setText("Mã đơn: " + String.valueOf(orderManager.getOrder_id()));
        if (orderManager.getUserName().equals("")) {
            holder.tv_userName.setText("None");
        } else {
            holder.tv_userName.setText(String.valueOf(orderManager.getUserName()));
        }
        holder.tv_firstProduct.setText("x" + orderManager.getQuantity() + " " + orderManager.getProduct_name());
        holder.tv_pricePerUnit.setText(String.valueOf(orderManager.getPerUnit()));
        int tempPIO = orderManager.getProductsInOrder() - 1;
        if (tempPIO == 0) {
            holder.tv_productInOrder.setText("");
        } else {
            holder.tv_productInOrder.setText("Xem thêm " + String.valueOf(tempPIO) + " sản phẩm");
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_total.setText("Giá: "+decimalFormat.format(Double.parseDouble(String.valueOf(orderManager.getTotal())))+"đ");
//        holder.tv_total.setText("Tổng tiền :" + String.valueOf(" " + orderManager.getTotal()) + " VND");
        if (orderManager.getOrder_status() == 1) {
            holder.tv_order_status.setText("Chưa xác nhận");
            holder.tv_order_status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey));
        } else if (orderManager.getOrder_status() == 2) {
            holder.tv_order_status.setText("Đã xác nhận");
            holder.tv_order_status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        } else if (orderManager.getOrder_status() == 3) {
            holder.tv_order_status.setText("Đang giao");
            holder.tv_order_status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow));
        } else if (orderManager.getOrder_status() == 4) {
            holder.tv_order_status.setText("Giao thành công");
            holder.tv_order_status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        } else if (orderManager.getOrder_status() == 5) {
            holder.tv_order_status.setText("Đã hủy");
            holder.tv_order_status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(orderManager.getMain_img());

        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mContext)
                                .load(uri)
                                .into(holder.imv_Product);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInforDetail(orderManager.getOrder_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,
                tv_userName,
                tv_order_status,
                tv_firstProduct,
                tv_pricePerUnit,
                tv_productInOrder,
                tv_total;
        ImageView imv_Product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //textView
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            tv_firstProduct = itemView.findViewById(R.id.tv_firstProduct);
            tv_pricePerUnit = itemView.findViewById(R.id.tv_pricePerUnit);
            tv_productInOrder = itemView.findViewById(R.id.tv_productInOrder);
            tv_total = itemView.findViewById(R.id.tv_total);
            //imageView
            imv_Product = itemView.findViewById(R.id.imv_Product);
            apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        }
    }

    private void getInforDetail(int orderId) {
        compositeDisposable.add(apiManager.getInforDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        inforDetailModel -> {
                            if (inforDetailModel.isSuccess()) {
                                List<InforDetailManager> results = inforDetailModel.getResult();
                                if (!results.isEmpty()) {
                                    InforDetailManager detail = results.get(0);
                                    String order_id = String.valueOf(detail.getOrder_id());
                                    String name = detail.getAccount_name();
                                    String order_status = String.valueOf(detail.getOrder_status());
                                    String paymentMethod = detail.getPayment_method();
                                    String address = detail.getAddress();
                                    String date = detail.getDate();
                                    String phone = detail.getPhone();
                                    String total = String.valueOf(detail.getTotal());
                                    String email = detail.getEmail();
                                    Intent intent = new Intent(mContext, OrderDetailManagerActivity.class);
                                    //set Push du lieu
                                    intent.putExtra("order_id", order_id);
                                    intent.putExtra("account_name", name);
                                    intent.putExtra("order_status", order_status);
                                    intent.putExtra("paymentMethod", paymentMethod);
                                    intent.putExtra("address", address);
                                    intent.putExtra("date", date);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("total", total);
                                    intent.putExtra("email", email);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "No order details found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Failed to get order details", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("Loi Intent :", throwable.getMessage());
                        }
                ));
    }
}
