package com.manager.appbanmohinhmanager.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.appbanmohinhmanager.R;

import java.util.ArrayList;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;

    public AddProductAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public AddProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_multiple_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageURI(uriArrayList.get(position));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriArrayList.remove(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
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