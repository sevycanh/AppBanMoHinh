package com.example.shopmohinh.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.OrderDetailActivity;
import com.example.shopmohinh.adapter.OrderAdapter;
import com.example.shopmohinh.model.Order;
import com.example.shopmohinh.retrofit.ApiBanHang;
import com.example.shopmohinh.retrofit.RetrofitClient;
import com.example.shopmohinh.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderFragment extends Fragment {
    private RecyclerView orderRecylerView;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;
    private ApiBanHang apiBanHang;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Spinner orderStatusSpinner;
    private int selection = 0;
    private TextView noOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        AnhXa(rootView);
        //push accountid de tim dung order
        getOrder(101);
        orderStatusSpinner.setAdapter(setOrderStatusSpinner());
        orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ThongBao", i + ""); // Log vị trí của mục được chọn
                if (i == 0) {
                    orderList.clear();
                    getOrder(101);
                    selection = 0;
                } else if (i == 1) {
                    getOrderByOrderStatus(101, 1);
                    selection = 1;
                } else if (i == 2) {
                    getOrderByOrderStatus(101, 2);
                    selection = 2;
                } else if (i == 3) {
                    getOrderByOrderStatus(101, 3);
                    selection = 3;
                } else if (i == 4) {
                    getOrderByOrderStatus(101, 4);
                    selection = 4;
                } else if (i == 5) {
                    getOrderByOrderStatus(101, 5);
                    selection = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }

    private void AnhXa(View rootView) {
        orderRecylerView = rootView.findViewById(R.id.recylerViewOrder);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        orderStatusSpinner = rootView.findViewById(R.id.sp_OrderStatus);
        noOrder = rootView.findViewById(R.id.noOrder);
    }

    private void getOrder(int accountId) {
        orderStatusSpinner.setSelection(0);
        compositeDisposable.add(apiBanHang.getOrder(accountId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            if (orderModel.isSuccess()) {
                                orderList = orderModel.getResult();
                                orderAdapter = new OrderAdapter(getContext(), orderList);
                                orderRecylerView.setAdapter(orderAdapter);
                                orderRecylerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                            }
                        }, throwable -> {
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getOrderByOrderStatus(int accountId, int order_status) {
        compositeDisposable.add(apiBanHang.getOrderByOrderStatus(accountId, order_status).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            if (orderModel.isSuccess()) {
                                orderList.clear();
                                orderList.addAll(orderModel.getResult());
                                orderAdapter.notifyDataSetChanged();
                                noOrder.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(getContext(), "khong co du lieu", Toast.LENGTH_SHORT).show();
                                orderList.clear();
                                orderAdapter.notifyDataSetChanged();
                                noOrder.setVisibility(View.VISIBLE);
                            }
                        }, throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    public SpinnerAdapter setOrderStatusSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.order_status_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void onResume() {
        orderStatusSpinner.setSelection(selection);
        super.onResume();
    }

}