package com.manager.appbanmohinhmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.model.Coupon;

import java.util.List;

public class VoucherManagerAdapter extends RecyclerView.Adapter<VoucherManagerAdapter.ViewHolder> {
    Context context;
    List<Coupon> couponList;
    int isPublic;

    public VoucherManagerAdapter(Context context, List<Coupon> couponList, int isPublic) {
        this.context = context;
        this.couponList = couponList;
        this.isPublic = isPublic;
    }

    @NonNull
    @Override
    public VoucherManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherManagerAdapter.ViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        holder.txtName.setText(coupon.getName());
        holder.txtPercent.setText("% Giảm: " + coupon.getDiscount());
        holder.txtCount.setText("Số lượng còn: " + coupon.getCount());
        holder.txtTime.setText("HSD: " + coupon.getDateTo());
        if (isPublic == 0){
            holder.txtId.setText("ID sở hữu: " + coupon.getUser_id());
            holder.txtId.setVisibility(View.VISIBLE);
        } else {
            holder.txtId.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtPercent, txtCount, txtTime, txtId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtnameVoucher);
            txtCount = itemView.findViewById(R.id.txtcountVoucher);
            txtPercent = itemView.findViewById(R.id.txtpercentVoucher);
            txtTime = itemView.findViewById(R.id.txttimeVoucher);
            txtId = itemView.findViewById(R.id.txtIdVoucher);
        }
    }
}
