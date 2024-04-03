package com.example.shopmohinh.adapter;

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
import com.example.shopmohinh.model.SanPhamSearch;

import java.util.List;

public class SpSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamSearch> array;

    public SpSearchAdapter(Context context, List<SanPhamSearch> array) {
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
        SanPhamSearch sanPhamSearch = array.get(position);
        myViewHolder.txtTen.setText(String.valueOf(sanPhamSearch.getName()));
        myViewHolder.txtGia.setText("Giá: " + String.valueOf(sanPhamSearch.getPrice()) + "đ");
        Glide.with(context).load(sanPhamSearch.getMain_image()).into(myViewHolder.imgItem);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtGia, txtTen;
        ImageView imgItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGia = itemView.findViewById(R.id.sp_price_search);
            txtTen = itemView.findViewById(R.id.sp_name_search);
            imgItem = itemView.findViewById(R.id.sp_image_search);
        }
    }
}
