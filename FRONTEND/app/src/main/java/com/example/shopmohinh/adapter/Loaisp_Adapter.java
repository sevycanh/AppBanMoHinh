package com.example.shopmohinh.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.ProductActivity;
import com.example.shopmohinh.model.LoaiSP;


import java.util.ArrayList;
import java.util.List;

public class Loaisp_Adapter extends BaseAdapter {
    List<LoaiSP> loaiSPList = new ArrayList<>();
    Context context;

    public Loaisp_Adapter(Context context,List<LoaiSP> loaiSPList) {
        this.loaiSPList = loaiSPList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return loaiSPList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView tv_name;
        ImageView imageView;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
             viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item,null);
            viewHolder.tv_name = view.findViewById(R.id.text_view_loaisp);
            viewHolder.imageView = view.findViewById(R.id.image_view_loaisp);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.tv_name.setText(loaiSPList.get(i).getName());
        Glide.with(context).load(loaiSPList.get(i).getImage()).into(viewHolder.imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id_category", loaiSPList.get(i).getCategory_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
