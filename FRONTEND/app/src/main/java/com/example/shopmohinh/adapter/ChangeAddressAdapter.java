package com.example.shopmohinh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmohinh.Interface.OnItemClickListener;
import com.example.shopmohinh.R;
import com.example.shopmohinh.address.AddressInfo;
import com.example.shopmohinh.address.District;
import com.example.shopmohinh.address.Province;
import com.example.shopmohinh.address.Ward;

import java.util.List;

public class ChangeAddressAdapter  extends RecyclerView.Adapter<ChangeAddressAdapter.MyViewHolder> {
    Context context;

    String address;
    List<Province> provinceList;

    List<District> districtList;

    List<Ward> wardList;

    private OnItemClickListener listener;

    public ChangeAddressAdapter(Context context, String address) {
        this.context = context;
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public void setWardList(List<Ward> wardList) {
        this.wardList = wardList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeAddressAdapter.MyViewHolder holder, int position) {
        if(address.equals("province")){
            Province province = provinceList.get(position);
            holder.txtAddress.setText(province.getProvince_name());
        }
        else if(address.equals("district")){
            District district = districtList.get(position);
            holder.txtAddress.setText(district.getDistrict_name());
        }
        else if(address.equals("ward")){
            Ward ward = wardList.get(position);
            holder.txtAddress.setText(ward.getWard_name());
        }


        holder.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            }
        });

    }

    public interface OnItemClickListener  {
        void onClick(int position);
    }
    @Override
    public int getItemCount() {
        if(address.equals("province")){
           return provinceList.size();
        }
        else if(address.equals("district")){
           return districtList.size();
        }
        return wardList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtAddress;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.textViewAddress);
        }
    }


}
