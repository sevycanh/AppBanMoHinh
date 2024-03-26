package com.example.shopmohinh.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmohinh.Interface.IImageClickListener;
import com.example.shopmohinh.R;
import com.example.shopmohinh.model.Cart;
import com.example.shopmohinh.model.EventBus.TinhTongEvent;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> gioHangList;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiBanHang apiBanHang;

    public CartAdapter(Context context, List<Cart> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang_swipe,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart gioHang = gioHangList.get(position);
        holder.txtTenSanPhamGioHang.setText(gioHang.getName());
        holder.txtSoLuongSanPhamGioHang.setText(gioHang.getQuantity() +"");
        Glide.with(context).load(gioHang.getImage()).into(holder.imageSanPhamGioHang);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dấu phân tách hàng nghìn
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###", symbols);
        String giaSanPhamFormat = decimalFormat.format(gioHang.getPrice());
        String TongGiaSanPhamFormat = decimalFormat.format(gioHang.getPrice() * gioHang.getQuantity());
        holder.txtGiaSanPhamGioHang.setText(giaSanPhamFormat);
        holder.txtTongGiaSanPhamGioHang.setText(TongGiaSanPhamFormat);


        holder.checkBoxGioHang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.carts.get(holder.getAdapterPosition()).setChecked(true);
                    if(!Utils.purchases.contains(gioHang)){
                        Utils.purchases.add(gioHang);
                    }
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }else{
                    Utils.carts.get(holder.getAdapterPosition()).setChecked(false);
                    for (int i=0; i<Utils.purchases.size(); i++){
                        if(Utils.purchases.get(i).getIdProduct() == gioHang.getIdProduct()){
                            Utils.purchases.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });

        holder.checkBoxGioHang.setChecked(gioHang.isChecked());
        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG ONCLICK", pos + "" );
                int soluong = gioHangList.get(holder.getAdapterPosition()).getQuantity();
                int soluongmoi = 1;
                long gia = gioHangList.get(holder.getAdapterPosition()).getPrice();
                if(giatri == 1){
                    if(soluong > 1){
                        soluongmoi = soluong - 1;
                        gioHangList.get(holder.getAdapterPosition()).setQuantity(soluongmoi);

                        holder.txtSoLuongSanPhamGioHang.setText(soluongmoi + "");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                        symbols.setGroupingSeparator('.'); // Dấu phân tách hàng nghìn
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###", symbols);
                        String formattedNumber = decimalFormat.format(gia * soluongmoi);
                        holder.txtTongGiaSanPhamGioHang.setText(formattedNumber + "");
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                        UpdateCartApi(Utils.carts.get(pos).getIdProduct(), soluongmoi);

                    }
                    else if(soluong == 1){
                        AlertDialog.Builder builder  = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thong bao");
                        builder.setMessage("Ban co muon xoa san pham nay khoi gio hang?");
                        builder.setPositiveButton("Dong Y", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UpdateCartApi(Utils.carts.get(pos).getIdProduct(), 0);
                                Utils.purchases.remove(gioHang);
                                Utils.carts.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Huy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                }
                else if (giatri == 2){
                    soluongmoi = soluong + 1;
                    gioHangList.get(pos).setQuantity(soluongmoi);
                    holder.txtSoLuongSanPhamGioHang.setText(soluongmoi + "");
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                    symbols.setGroupingSeparator('.'); // Dấu phân tách hàng nghìn
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###", symbols);
                    String formattedNumber = decimalFormat.format(gia * soluongmoi);
                    holder.txtTongGiaSanPhamGioHang.setText(formattedNumber + "");
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                    UpdateCartApi(Utils.carts.get(pos).getIdProduct(), soluongmoi);

                }

                else if (giatri == 3){
                    AlertDialog.Builder builder  = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Thong bao");
                    builder.setMessage("Ban co muon xoa san pham nay khoi gio hang?");
                    builder.setPositiveButton("Dong Y", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateCartApi(Utils.carts.remove(pos).getIdProduct(), 0);
                            Utils.purchases.remove(gioHang);
                            Utils.carts.remove(pos);
                            notifyDataSetChanged();
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    });
                    builder.setNegativeButton("Huy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }


            }
        });
    }

    private void UpdateCartApi(int productId ,int quantity){
        compositeDisposable.add(apiBanHang.updateShoppingCart(Utils.user_current.getAccount_id(), productId, quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
//                            Toast.makeText(this.context, "Thanh cong", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(this.context,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageSanPhamGioHang, imageCong, imageTru, imageXoa;
        TextView txtTenSanPhamGioHang,txtGiaSanPhamGioHang, txtSoLuongSanPhamGioHang, txtTongGiaSanPhamGioHang;

        IImageClickListener listener;
        CheckBox checkBoxGioHang;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            imageSanPhamGioHang = itemView.findViewById(R.id.imageSanPhamGioHang);
            txtTenSanPhamGioHang = itemView.findViewById(R.id.txtTenSanPhamGioHang);
            txtGiaSanPhamGioHang = itemView.findViewById(R.id.txtGiaSanPhamGioHang);
            txtSoLuongSanPhamGioHang = itemView.findViewById(R.id.txtSoLuongSanPhamGioHang);
            txtTongGiaSanPhamGioHang = itemView.findViewById(R.id.txtTongGiaSanPhamGioHang);
            checkBoxGioHang = itemView.findViewById(R.id.checkboxGioHang);
            imageCong = itemView.findViewById(R.id.itemGioHang_Cong);
            imageTru = itemView.findViewById(R.id.itemGioHang_Tru);
//            imageXoa = itemView.findViewById(R.id.itemGioHang_Xoa);
            imageCong.setOnClickListener(this);
            imageTru.setOnClickListener(this);

//            imageXoa.setOnClickListener(this);

        }
        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }
        @Override
        public void onClick(View view){
            //1 TRU, 2 CONG, 3 Xoa
            if(view == imageTru){
                listener.onImageClick(view, getAdapterPosition(), 1);
            }else if(view == imageCong){
                listener.onImageClick(view, getAdapterPosition(), 2);
            }
            else if(view == imageXoa){
                listener.onImageClick(view, getAdapterPosition(), 3);
            }
        }
    }
}
