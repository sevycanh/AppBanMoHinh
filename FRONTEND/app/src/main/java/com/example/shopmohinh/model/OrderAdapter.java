package com.example.shopmohinh.model;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.OrderDetailActivity;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<Order> listOrder;
    private int statusColor;
    private int productInOrder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;
    private Context mContext;
    private InforDetailModel inforDetailModel = new InforDetailModel();

    public OrderAdapter(Context context, List<Order> listOrder) {
        this.listOrder = listOrder;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewOrder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(viewOrder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = listOrder.get(position);
        holder.tv_order_id.setText(String.valueOf("Mã đơn : " + order.getOrder_id()));
        holder.tv_total.setText(String.valueOf("Tổng tiền: " + order.getTotal()));
        holder.tv_detail.setText("Chi tiết ->");
        if (order.getOrder_status() == 0) {
            holder.tv_order_status.setText(String.valueOf("Chưa xác nhận"));
            statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.grey);
            holder.tv_order_status.setTextColor(statusColor);

        } else if (order.getOrder_status() == 1) {
            holder.tv_order_status.setText(String.valueOf("Đã xác nhận"));
        } else if (order.getOrder_status() == 2) {
            holder.tv_order_status.setText(String.valueOf("Đang giao"));
            statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow);
            holder.tv_order_status.setTextColor(statusColor);


        } else if (order.getOrder_status() == 3) {
            holder.tv_order_status.setText(String.valueOf("Giao thành công"));
            statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.green);
            holder.tv_order_status.setTextColor(statusColor);

        } else {
            holder.tv_order_status.setText(String.valueOf("Đã hủy"));
            statusColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
            holder.tv_order_status.setTextColor(statusColor);

        }
        holder.tv_price.setText(String.valueOf(order.getPerUnit()));
        holder.tv_product_name.setText(String.valueOf("x" + order.getQuantity()) + " " + order.getProduct_name());
        productInOrder = order.getProductsInOrder() - 1;
        holder.tv_productInOrder.setText(String.valueOf("+" + productInOrder));
        Glide.with(holder.itemView.getContext()).load(order.getMain_img()).into(holder.imv_Product);
        //onClick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Mã đơn hàng: " + order.getOrder_id() + " của userID:" + order.getAccount_id(), Toast.LENGTH_SHORT).show();
                apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
                getInforDetail(order.getOrder_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_order_id, tv_total, tv_detail, tv_order_status, tv_product_name, tv_price, tv_productInOrder;
        private CardView cardOrder;
        private ImageView imv_Product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardOrder = itemView.findViewById(R.id.cardOrder);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_total = itemView.findViewById((R.id.tv_total));
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_order_status = itemView.findViewById(R.id.order_status);
            tv_product_name = itemView.findViewById(R.id.tv_firstProduct);
            tv_price = itemView.findViewById(R.id.tv_pricePerUnit);
            tv_productInOrder = itemView.findViewById(R.id.tv_productInOrder);
            imv_Product = itemView.findViewById(R.id.imv_Product);
        }
    }

    private void getInforDetail(int orderId) {
        compositeDisposable.add(apiBanHang.getInforDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        inforDetailModel -> {
                            if (inforDetailModel.isSuccess()) {
                                List<InforDetail> results = inforDetailModel.getResult();
                                if (!results.isEmpty()) {
                                    InforDetail detail = results.get(0);
                                    String order_id = String.valueOf(detail.getOrder_id());
                                    String name = detail.getAccount_name();
                                    String order_status = String.valueOf(detail.getOrder_status());
                                    String paymentMethod = detail.getPayment_method();
                                    String address = detail.getAddress();
                                    String date = detail.getDate();
                                    String phone = detail.getPhone();
                                    String total = String.valueOf(detail.getTotal());
                                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                                    //set Push du lieu
                                    intent.putExtra("order_id", order_id);
                                    intent.putExtra("account_name", name);
                                    intent.putExtra("order_status", order_status);
                                    intent.putExtra("paymentMethod", paymentMethod);
                                    intent.putExtra("address", address);
                                    intent.putExtra("date", date);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("total", total);
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
                        }
                ));
    }

}
