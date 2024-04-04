package com.manager.appbanmohinhmanager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.retrofit.SalesApi;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StatisticalActivity extends AppCompatActivity {
    Toolbar toolbar;
    PieChart pieChart;
    BarChart barChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SalesApi salesApi;

    TextView txtThongke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical);
        salesApi = RetrofitClient.getInstance(Utils.BASE_URL).create(SalesApi.class);
        initView();
        ActionToolBar();
        getDataChart();
        settingBarChart();
    }

    private void settingBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setAxisMinimum(0);
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistical, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.revenue_statistics) {
            getRevenueStatistical();
            return true;
        } else if (item.getItemId() == R.id.item_statistics) {
            getDataChart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRevenueStatistical() {
        txtThongke.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
        compositeDisposable.add(salesApi.getStatisticalByMonth().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(statisticalModel -> {
                            if (statisticalModel.isSuccess()) {
                                List<BarEntry> listData = new ArrayList<>();
                                for (int i = 0; i < statisticalModel.getResult().size(); i++) {
                                    int Sum = statisticalModel.getResult().get(i).getSumMonth();
                                    String month = statisticalModel.getResult().get(i).getMonth();
                                    listData.add(new BarEntry(Integer.parseInt(month), Sum));
                                }
                                BarDataSet barDataSet = new BarDataSet(listData, "Thống kê");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                barDataSet.setValueTextSize(14f);
                                barDataSet.setValueTextColor(Color.RED);

                                BarData data = new BarData(barDataSet);
                                barChart.animateXY(2000, 2000);
                                barChart.setData(data);
                                barChart.invalidate();
                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
    }

    private void getDataChart() {
        List<PieEntry> listData = new ArrayList<>();
        int maxNameLength = 15;
        compositeDisposable.add(salesApi.getStatistical()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statisticalModel -> {
                            if (statisticalModel.isSuccess()) {
                                for (int i = 0; i < statisticalModel.getResult().size(); i++) {
                                    int productId = statisticalModel.getResult().get(i).getProduct_id();
                                    String name = statisticalModel.getResult().get(i).getName();

                                    // Giới hạn độ dài của tên sản phẩm
                                    if (name.length() > maxNameLength) {
                                        name = name.substring(0, maxNameLength) + "...";
                                    }

                                    // Tạo chuỗi hiển thị cho nhãn
                                    String label = "ID: " + productId + "\n" + name;

                                    // Thêm dữ liệu vào list
                                    listData.add(new PieEntry(1, label));
                                }

                                PieDataSet pieDataSet = getPieDataSet(listData);

                                // Tạo PieData và đặt DataSet cho nó
                                PieData data = new PieData(pieDataSet);
                                data.setDrawValues(true); // Cho phép hiển thị giá trị
                                data.setValueTextColor(Color.BLACK); // Đặt màu cho giá trị
                                data.setValueTextSize(10f); // Đặt kích thước cho giá trị
                                data.setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {
                                        return ""; // Không hiển thị giá trị số lượng
                                    }
                                });
                                pieChart.setData(data);

                                pieChart.animateXY(2000, 2000);
                                pieChart.getDescription().setEnabled(false);

                                // Hiển thị chú thích
                                Legend legend = pieChart.getLegend();
                                legend.setForm(Legend.LegendForm.CIRCLE);
                                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

                                pieChart.invalidate();

                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
    }

    @NonNull
    private static PieDataSet getPieDataSet(List<PieEntry> listData) {
        PieDataSet pieDataSet = new PieDataSet(listData, "");
        pieDataSet.setSliceSpace(3f); // Đặt khoảng cách giữa các phần
        pieDataSet.setSelectionShift(2f); // Đặt khoảng cách khi phần được chọn
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueLineWidth(1f);
        pieDataSet.setValueLinePart1Length(0.5f);
        pieDataSet.setValueLinePart2Length(0.5f);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Đặt màu cho các phần
        return pieDataSet;
    }


    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        barChart = findViewById(R.id.barchart);

        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);


        txtThongke = findViewById(R.id.txtThongke);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}