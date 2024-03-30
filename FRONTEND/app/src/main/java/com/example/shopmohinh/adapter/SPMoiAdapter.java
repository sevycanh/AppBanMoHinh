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
import com.example.shopmohinh.model.SanPhamMoi;

import java.util.List;

public class SPMoiAdapter extends BaseAdapter {
    List<SanPhamMoi> array;
    Context context;

    public SPMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView tensp;
        ImageView imgsp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_sanphammoi, null);
            viewHolder.tensp = convertView.findViewById(R.id.item_tensp);
            viewHolder.imgsp = convertView.findViewById(R.id.item_imgsp);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tensp.setText(array.get(position).getTensp());
            Glide.with(context).load(array.get(position).getHinhanh()).into(viewHolder.imgsp);
        }
        return convertView;
    }
}
