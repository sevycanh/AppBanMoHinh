package com.manager.appbanmohinhmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.manager.appbanmohinhmanager.activity.UpdateCategoryActivity;
import com.manager.appbanmohinhmanager.activity.UpdateProductActivity;
import com.manager.appbanmohinhmanager.model.CategoryManager;
import com.manager.appbanmohinhmanager.model.ProductManager;

import java.util.List;
import java.util.zip.Inflater;

public class CategoryManagerAdapter extends RecyclerView.Adapter<CategoryManagerAdapter.ViewHolder> {
    Context context;
    List<CategoryManager> array;

    public CategoryManagerAdapter(Context context, List<CategoryManager> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public CategoryManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_manager, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryManagerAdapter.ViewHolder holder, int position) {
        Bundle bundle = new Bundle();
        CategoryManager categoryManager = array.get(position);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        bundle.putString("nameImg", categoryManager.getImage());
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(categoryManager.getImage());


        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        bundle.putParcelable("UriImage", uri);
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imageCategory);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        bundle.putInt("id", categoryManager.getCategory_id());
        bundle.putString("nameCategory", categoryManager.getName());
        holder.txtName.setText(String.valueOf(categoryManager.getCategory_id())+"_"+categoryManager.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, categoryManager.getImage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UpdateCategoryActivity.class);
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
        ImageView imageCategory;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.imgCategory);
            txtName = itemView.findViewById(R.id.nameCategory);
        }
    }
}
