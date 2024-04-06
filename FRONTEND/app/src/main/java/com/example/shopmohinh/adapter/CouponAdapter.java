package com.example.shopmohinh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.CouponActivity;
import com.example.shopmohinh.model.Coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
    Context context;
    List<Coupon> couponList;

    public CouponAdapter(Context context, List<Coupon> couponList){
        this.context = context;
        this.couponList = couponList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        holder.txtCouponName_Item.setText(coupon.getName());
        holder.txtCouponPercent_Item.setText(coupon.getDiscount() + "%");

        String time = ChangeTime(coupon.getDateTo());
        holder.txtCouponExpireDate_Item.setText(time);
    }

    private String ChangeTime(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        try {
            java.util.Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return time;
        }
    }


    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCouponName_Item;
        TextView txtCouponPercent_Item;
        TextView txtCouponExpireDate_Item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCouponName_Item = itemView.findViewById(R.id.txtCouponName_Item);
            txtCouponPercent_Item = itemView.findViewById(R.id.txtCouponPercent_Item);
            txtCouponExpireDate_Item = itemView.findViewById(R.id.txtCouponExpireDate_Item);
        }
    }
}
