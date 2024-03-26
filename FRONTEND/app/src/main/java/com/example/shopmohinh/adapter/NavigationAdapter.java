package com.example.shopmohinh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shopmohinh.R;
import com.example.shopmohinh.model.Item;

import java.util.ArrayList;


public class NavigationAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private ArrayList<Item> mItems;

    public NavigationAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        }

        Item currentItem = mItems.get(position);

        TextView textViewItem = listItem.findViewById(R.id.text_view_loaisp);
        ImageView imageView = listItem.findViewById(R.id.image_view_loaisp);
        textViewItem.setText(currentItem.getName());
        imageView.setImageResource(currentItem.getImageId());


        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị tên của mục bằng Toast
                Toast.makeText(mContext,"ban da chon " + currentItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return listItem;
    }
}
