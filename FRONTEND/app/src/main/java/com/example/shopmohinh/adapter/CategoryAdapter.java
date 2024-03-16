package com.example.shopmohinh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.shopmohinh.R;
import com.example.shopmohinh.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    List<Category> listCategory;
    Context context;

    public CategoryAdapter(Context context, List<Category> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_category, null);
            viewHolder.textName = view.findViewById(com.example.shopmohinh.R.id.item_name);
            viewHolder.imageView = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textName.setText(listCategory.get(i).getName());
        Glide.with(context).load(listCategory.get(i).getImage()).into(viewHolder.imageView);
        return view;
    }

    public class ViewHolder {
        TextView textName;
        ImageView imageView;
    }
}
