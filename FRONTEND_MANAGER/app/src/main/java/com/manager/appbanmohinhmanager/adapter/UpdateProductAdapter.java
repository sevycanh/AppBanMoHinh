package com.manager.appbanmohinhmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class UpdateProductAdapter extends RecyclerView.Adapter<UpdateProductAdapter.ViewHolder> {

    Context context;
    private ArrayList<Uri> uriArray;
    private List<String> nameArray;

    public UpdateProductAdapter(Context context, ArrayList<Uri> uriArray, List<String> nameArray) {
        this.context = context;
        this.uriArray = uriArray;
        this.nameArray = nameArray;
    }

    @NonNull
    @Override
    public UpdateProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_multiple_image, parent, false);
        return new UpdateProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateProductAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(uriArray.get(position))
                .into(holder.imageView);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, nameArray.get(position), Toast.LENGTH_SHORT).show();
                DeleteToFirebase(nameArray.get(position));
                uriArray.remove(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });
    }

    private void DeleteToFirebase(String nameimg){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(nameimg);
        storageReference.delete();
    }

    @Override
    public int getItemCount() {
        return uriArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multiple_image);
            remove = itemView.findViewById(R.id.remove_item);
        }
    }
}
