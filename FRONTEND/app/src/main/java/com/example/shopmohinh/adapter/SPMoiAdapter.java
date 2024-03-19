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
import com.example.shopmohinh.fragment.HomeFragment;
import com.example.shopmohinh.model.SanPhamMoi;

import java.util.List;

public class SPMoiAdapter extends RecyclerView.Adapter<SPMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public SPMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanphammoi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtTen.setText(sanPhamMoi.getTensp());
        holder.txtGia.setText("Giá: " + String.valueOf(sanPhamMoi.getGiasp()) +"đ");
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtGia, txtTen;
        ImageView imgItem;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtGia = itemView.findViewById(R.id.newsp_price);
            txtTen = itemView.findViewById(R.id.newsp_name);
            imgItem = itemView.findViewById(R.id.newsp_image);
        }
    }
}
