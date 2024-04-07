package com.manager.appbanmohinhmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.activity.AccountDetailManagerActivity;
import com.manager.appbanmohinhmanager.model.Account;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import okhttp3.internal.Util;

public class AccountManagerAdapter extends RecyclerView.Adapter<AccountManagerAdapter.ViewHolder> {
    Context context;
    List<Account> accountList;
    ApiManager apiManager;
    CompositeDisposable compositeDisposable;
    String id, email, username, phone, date, coin, status;

    public AccountManagerAdapter(Context context, List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.tv_account_id_AccountManager.setText(String.valueOf(account.getAccount_id()));
        holder.tv_email_AccountManager.setText(account.getEmail());
        holder.tv_phone_AccountManager.setText(account.getPhone());
        if (account.getPhone().equals("")){
            holder.tv_phone_AccountManager.setText("Chưa cập nhật");
        }else {
            holder.tv_phone_AccountManager.setText(account.getPhone());
        }
        if (account.getStatus() == 1) {
            holder.tv_status_AccountManager.setText("Hoạt động");
            holder.tv_status_AccountManager.setTextColor(ContextCompat.getColor(context, R.color.green_Active_account));
        } else {
            holder.tv_status_AccountManager.setText("Khóa");
            holder.tv_status_AccountManager.setTextColor(ContextCompat.getColor(context, R.color.red_Deactive_account));
        }
        holder.tv_coin_AccountManager.setText(String.valueOf(account.getCoin()));
        holder.tv_coin_AccountManager.setTextColor(ContextCompat.getColor(context, R.color.main));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, account.getAccount_id() + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AccountDetailManagerActivity.class);
                id = String.valueOf(account.getAccount_id());
                email = account.getEmail();
                phone = account.getPhone();
                username = account.getUsername();
                date = account.getLast_login();
                coin = String.valueOf(account.getCoin());
                status = String.valueOf(account.getStatus());
                intent.putExtra("id", id);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("username", username);
                intent.putExtra("date", date);
                intent.putExtra("coin", coin);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imv_account_manager;
        TextView tv_account_id_AccountManager, tv_email_AccountManager, tv_phone_AccountManager, tv_status_AccountManager, tv_coin_AccountManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_account_manager = itemView.findViewById(R.id.imv_account_manager);
            tv_account_id_AccountManager = itemView.findViewById(R.id.tv_account_id_AccountManager);
            tv_email_AccountManager = itemView.findViewById(R.id.tv_email_AccountManager);
            tv_phone_AccountManager = itemView.findViewById(R.id.tv_phone_AccountManager);
            tv_status_AccountManager = itemView.findViewById(R.id.tv_status_AccountManager);
            tv_coin_AccountManager = itemView.findViewById(R.id.tv_coin_AccountManager);
            apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
            compositeDisposable = new CompositeDisposable();
        }
    }
}
