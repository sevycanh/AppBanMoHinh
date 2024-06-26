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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmohinh.Interface.ItemClickListener;
import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.ProductDetailActivity;
import com.example.shopmohinh.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.List;

public class SPMoiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public SPMoiAdapter(Context context, List<Product> array) {
        this.context = context;
        this.array = array;
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
            Product sanPhamMoi = array.get(position);
            myViewHolder.txtTen.setText(String.valueOf(sanPhamMoi.getName()));
            if (sanPhamMoi.getCoupon() > 0){
                myViewHolder.txtGiaChuaKM.setText(formatNumberWithDotSeparator(sanPhamMoi.getPrice()) + " VNĐ");
            }
            else {
                myViewHolder.txtGiaChuaKM.setVisibility(View.GONE);
            }
            int price = sanPhamMoi.getPrice();
            int discount = sanPhamMoi.getPrice() * sanPhamMoi.getCoupon() / 100;
            int finalPrice = price - discount;
            myViewHolder.txtGia.setText(formatNumberWithDotSeparator(finalPrice) + " VNĐ");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference()
                    .child("/images")
                    .child(sanPhamMoi.getMain_image());

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
        return array.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtGiaChuaKM,txtGia, txtTen;
        ImageView imgItem;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGiaChuaKM = itemView.findViewById(R.id.newsp_price_nocoupon);
            txtGiaChuaKM.setPaintFlags(txtGiaChuaKM.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
