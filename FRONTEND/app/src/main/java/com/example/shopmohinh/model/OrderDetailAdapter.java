package com.example.shopmohinh.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmohinh.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    private Context context;

    private List<ItemOrderDetail> itemOrderDetailList;


    public OrderDetailAdapter(Context context, List<ItemOrderDetail> itemOrderDetailList) {
        this.itemOrderDetailList = itemOrderDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewDetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new MyViewHolder(viewDetail);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemOrderDetail itemOrderDetail = itemOrderDetailList.get(position);
        holder.tv_name.setText(itemOrderDetail.getName());
        holder.tv_productPrice.setText(String.valueOf(itemOrderDetail.getPrice()+ " VND"));
        holder.tv_quantity.setText(String.valueOf(itemOrderDetail.getQuantity()));
        holder.tv_total.setText(String.valueOf(itemOrderDetail.getTotal()) + " VND");
        Glide.with(holder.itemView.getContext()).load(itemOrderDetail.getMain_image()).into(holder.imgView_product);
    }

    @Override
    public int getItemCount() {
        return itemOrderDetailList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_quantity, tv_productPrice, tv_total;
        private ImageView imgView_product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_productname_orderdetail);
            tv_productPrice = itemView.findViewById(R.id.item_productPrice_orderdetail);
            tv_quantity = itemView.findViewById(R.id.item_quantity_detail);
            imgView_product = itemView.findViewById(R.id.item_imgV_order_detail);
            tv_total = itemView.findViewById(R.id.tv_total_orderdetail);
        }
    }
}
