package com.manager.appbanmohinhmanager.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.manager.appbanmohinhmanager.activity.UpdateProductActivity;
import com.manager.appbanmohinhmanager.model.ProductManager;

import java.text.DecimalFormat;
import java.util.List;

public class ProductManagerAdapter extends RecyclerView.Adapter<ProductManagerAdapter.ViewHolder>{
    Context context;
    List<ProductManager> array;

    public ProductManagerAdapter(Context context, List<ProductManager> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ProductManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_manager, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManagerAdapter.ViewHolder holder, int position) {
        Bundle bundle = new Bundle();
        ProductManager productManager = array.get(position);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(productManager.getMain_image());

        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imgMain);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        holder.txtName.setText(String.valueOf(productManager.getProduct_id())+"_"+productManager.getName());
        holder.txtQuantity.setText("Số Lượng: "+ String.valueOf(productManager.getQuantity()));

//        holder.txtPrice.setText("Giá: "+String.valueOf(productManager.getPrice()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText("Giá: "+decimalFormat.format(Double.parseDouble(String.valueOf(productManager.getPrice())))+"đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                bundle.putInt("idproduct", productManager.getProduct_id());
                bundle.putInt("id", productManager.getCategory_id());
                bundle.putString("name", productManager.getName());
                bundle.putInt("price", productManager.getPrice());
                bundle.putInt("quantity", productManager.getQuantity());
                bundle.putString("description", productManager.getDescription());
                bundle.putString("mainImg", productManager.getMain_image());
                bundle.putString("subImg", productManager.getSub_image());
                bundle.putInt("coupon", productManager.getCoupon());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgMain;
        TextView txtName, txtPrice, txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMain = itemView.findViewById(R.id.mainImageSP);
            txtName = itemView.findViewById(R.id.nameSP);
            txtPrice = itemView.findViewById(R.id.priceSP);
            txtQuantity = itemView.findViewById(R.id.quantitySP);

        }
    }
}
