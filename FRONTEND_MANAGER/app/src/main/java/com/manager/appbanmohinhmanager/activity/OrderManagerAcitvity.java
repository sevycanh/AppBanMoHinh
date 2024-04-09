package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.OrderManagerAdapter;
import com.manager.appbanmohinhmanager.model.OrderManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderManagerAcitvity extends AppCompatActivity {
    MaterialToolbar tb_orderManager;
    Spinner sp_sortOrder;
    RecyclerView rcv_Orders;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiManager apiManager;
    List<OrderManager> orderManagerList;
    OrderManagerAdapter orderManagerAdapter;
    LinearLayoutManager linearLayoutManager;
    SearchView sv_search;
    private TextView noOrder;
    private static final long SUBMIT_DELAY = 2500; // milliseconds
    private Handler submitHandler = new Handler();
    private Runnable submitRunnable;
    private int selection = 0;
    private String madonhang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager_acitvity);
        AnhXa();
        actionOrderToolbar();
        setSpinnerSortOrder();
        getOrderInOrderManager();
        setSeacrchView();

    }

    private void AnhXa() {
        tb_orderManager = findViewById(R.id.tb_orderManager);
        sp_sortOrder = findViewById(R.id.sp_sortOrder);
        rcv_Orders = findViewById(R.id.rcv_Orders);
        orderManagerList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        noOrder = findViewById(R.id.noOrder);
        sv_search = findViewById(R.id.sv_searchbyuserName);
        rcv_Orders.setLayoutManager(linearLayoutManager);
    }

    private void actionOrderToolbar() {
        setSupportActionBar(tb_orderManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_orderManager.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setSpinnerSortOrder() {
        ArrayAdapter<CharSequence> adapterSortOrder = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.sortOrderOptions, android.R.layout.simple_spinner_item);
        adapterSortOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sortOrder.setAdapter(adapterSortOrder);
        //setOnClickItem
        sp_sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selection = 0;
                    orderManagerList.clear();
                    submitQuery(madonhang);
                } else if (i == 1) {
                    selection = 1;
                    submitQuery(madonhang);
                } else if (i == 2) {
                    selection = 2;
                    submitQuery(madonhang);
                } else if (i == 3) {
                    selection = 3;
                    submitQuery(madonhang);
                } else if (i == 4) {
                    selection = 4;
                    submitQuery(madonhang);
                } else if (i == 5) {
                    selection = 5;
                    submitQuery(madonhang);
                }
                //Toast.makeText(OrderManagerAcitvity.this, selection + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(OrderManagerAcitvity.this, "Nothing", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getOrderInOrderManager() {
        compositeDisposable.add(apiManager.getOrderInOrderManager()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderManagerModel -> {
                            orderManagerList = orderManagerModel.getResult();
                            orderManagerAdapter = new OrderManagerAdapter(OrderManagerAcitvity.this, orderManagerList);
                            rcv_Orders.setAdapter(orderManagerAdapter);

                        }, throwable -> {
                            Toast.makeText(this, "Lỗi đỗ danh sách đơn hàng", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void setSeacrchView() {
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                madonhang = sv_search.getQuery().toString();
                submitHandler.removeCallbacks(submitRunnable);
                submitRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("run", selection + s + "");
                        submitQuery(s);
                    }
                };
                submitHandler.postDelayed(submitRunnable, SUBMIT_DELAY);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        if (selection != 0) {
            sp_sortOrder.setSelection(selection);
            submitQuery(madonhang);
        } else {
            getOrderInOrderManager();
        }
        super.onResume();
    }

    private void submitQuery(String query) {
        searchOrderbyID(selection, query);
    }


    private void searchOrderbyID(int order_status, String order_id) {
        compositeDisposable.add(apiManager.searchOrderbyID(order_status, order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderManagerModel -> {
                            if (orderManagerModel.isSuccess()) {
                                orderManagerList.clear();
                                orderManagerList.addAll(orderManagerModel.getResult());
                                orderManagerAdapter.notifyDataSetChanged();
                                noOrder.setVisibility(View.GONE);
                            } else {
//                                Toast.makeText(this, "khong co du lieu", Toast.LENGTH_SHORT).show();
                                orderManagerList.clear();
                                orderManagerAdapter.notifyDataSetChanged();
                                noOrder.setVisibility(View.VISIBLE);
                            }
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("Loi getSort", throwable.getMessage());
                        }
                ));
    }


}