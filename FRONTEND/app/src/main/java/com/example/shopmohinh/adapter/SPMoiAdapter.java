package com.example.shopmohinh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmohinh.Interface.ItemClickListener;
import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.ProductDetailActivity;
import com.example.shopmohinh.fragment.HomeFragment;
import com.example.shopmohinh.model.Product;
import com.example.shopmohinh.model.SanPhamMoi;

import java.io.Serializable;
import java.util.List;

public class SPMoiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    List<Product> products;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

//    public SPMoiAdapter(Context context, List<SanPhamMoi> array) {
//        this.context = context;
//        this.array = array;
//    }

    public SPMoiAdapter(Context context,List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanphammoi, parent, false);
            return new MyViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Product sanPhamMoi = products.get(position);
            myViewHolder.txtTen.setText(String.valueOf(sanPhamMoi.getProduct_id()));
            myViewHolder.txtGia.setText("Giá: " + String.valueOf(sanPhamMoi.getPrice()) + "đ");
            Glide.with(context).load(sanPhamMoi.getMain_image()).into(myViewHolder.imgItem);

            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick) {
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("productDetail", (Serializable) sanPhamMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return products.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtGia, txtTen;
        ImageView imgItem;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtGia = itemView.findViewById(R.id.newsp_price);
            txtTen = itemView.findViewById(R.id.newsp_name);
            imgItem = itemView.findViewById(R.id.newsp_image);
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
