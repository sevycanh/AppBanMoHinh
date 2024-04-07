package com.manager.appbanmohinhmanager.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.model.ItemOrderDetailManager;

import java.util.List;

public class OrderDetailManagerAdapter extends RecyclerView.Adapter<OrderDetailManagerAdapter.MyViewHolder> {
    private Context context;

    private List<ItemOrderDetailManager> itemOrderDetailList;


    public OrderDetailManagerAdapter(Context context, List<ItemOrderDetailManager> itemOrderDetailList) {
        this.itemOrderDetailList = itemOrderDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewDetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_manager, parent, false);
        return new MyViewHolder(viewDetail);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemOrderDetailManager itemOrderDetail = itemOrderDetailList.get(position);
        holder.tv_name.setText(itemOrderDetail.getName());
        holder.tv_productPrice.setText(String.valueOf(itemOrderDetail.getPrice() + " VND"));
        holder.tv_quantity.setText(String.valueOf(itemOrderDetail.getQuantity()));
        holder.tv_total.setText(String.valueOf(itemOrderDetail.getTotal()) + " VND");
//        Glide.with(holder.itemView.getContext()).load(itemOrderDetail.getMain_image()).into(holder.imgView_product);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(itemOrderDetail.getMain_image());

        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imgView_product);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });
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
