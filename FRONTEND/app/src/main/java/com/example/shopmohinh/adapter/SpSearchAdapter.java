package com.example.shopmohinh.adapter;

import static com.example.shopmohinh.utils.NumberWithDotSeparator.formatNumberWithDotSeparator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.example.shopmohinh.Interface.ItemClickListener;
import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.ProductDetailActivity;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.model.SanPhamSearch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.List;

public class SpSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> array;

    public SpSearchAdapter(Context context, List<Product> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Product sanPhamSearch = array.get(position);
        myViewHolder.txtTen.setText(String.valueOf(sanPhamSearch.getName()));
        if (sanPhamSearch.getCoupon() > 0){
            myViewHolder.txtGiaChuaKM.setText(formatNumberWithDotSeparator(sanPhamSearch.getPrice()) + " VNĐ");
        }
        else {
            myViewHolder.txtGiaChuaKM.setVisibility(View.GONE);
        }
        int price = sanPhamSearch.getPrice();
        int discount = sanPhamSearch.getPrice() * sanPhamSearch.getCoupon() / 100;
        int finalPrice = price - discount;
        myViewHolder.txtGia.setText(formatNumberWithDotSeparator(finalPrice) + " VNĐ");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(sanPhamSearch.getMain_image());

        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(myViewHolder.imgItem);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productDetail", (Serializable) sanPhamSearch);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtGiaChuaKM, txtGia, txtTen;
        ImageView imgItem;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGiaChuaKM = itemView.findViewById(R.id.search_price_nocoupon);
            txtGiaChuaKM.setPaintFlags(txtGiaChuaKM.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtGia = itemView.findViewById(R.id.sp_price_search);
            txtTen = itemView.findViewById(R.id.sp_name_search);
            imgItem = itemView.findViewById(R.id.sp_image_search);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
