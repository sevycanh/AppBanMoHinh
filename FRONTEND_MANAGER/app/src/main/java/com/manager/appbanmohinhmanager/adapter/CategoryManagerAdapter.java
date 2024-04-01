package com.manager.appbanmohinhmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.model.CategoryManager;
import com.manager.appbanmohinhmanager.model.ProductManager;

import java.util.List;
import java.util.zip.Inflater;

public class CategoryManagerAdapter extends RecyclerView.Adapter<CategoryManagerAdapter.ViewHolder> {
    Context context;

    public CategoryManagerAdapter(Context context, List<CategoryManager> array) {
        this.context = context;
        this.array = array;
    }

    List<CategoryManager> array;

    @NonNull
    @Override
    public CategoryManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryManagerAdapter.ViewHolder holder, int position) {
        CategoryManager categoryManager = array.get(position);
        Glide.with(context).load(categoryManager.getImage()).into(holder.imageCategory);
        holder.txtName.setText(String.valueOf(categoryManager.getCategory_id())+"_"+categoryManager.getName());
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
