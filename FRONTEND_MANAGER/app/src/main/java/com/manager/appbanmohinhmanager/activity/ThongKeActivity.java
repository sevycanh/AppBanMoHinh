package com.manager.appbanmohinhmanager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.retrofit.SalesApi;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongKeActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    PieChart pieChart;
    BarChart barChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SalesApi salesApi;
    TextView txtThongke;
    TextView txtThongkeBarChart;
    LinearLayout legendLayout;
    LinearLayout legendLayoutPieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        salesApi = RetrofitClient.getInstance(Utils.BASE_URL).create(SalesApi.class);
        initView();
        ActionToolBar();
        getDataChart();
        settingBarChart();
    }

    private void getDataChart() {
        legendLayout.setVisibility(View.GONE);
        legendLayoutPieChart.setVisibility(View.VISIBLE);
        txtThongke.setVisibility(View.VISIBLE);
        txtThongkeBarChart.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);
        List<PieEntry> listData = new ArrayList<>();
        List<String> productNames = new ArrayList<>();
        int maxNameLength = 20;

        compositeDisposable.add(salesApi.getDataChart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statisticalModel -> {
                            if (statisticalModel.isSuccess()) {
                                for (int i = 0; i < statisticalModel.getResult().size(); i++) {
                                    int sum = statisticalModel.getResult().get(i).getSum();
                                    int productId = statisticalModel.getResult().get(i).getProduct_id();
                                    String name = statisticalModel.getResult().get(i).getName();
                                    String id = "ID: " + productId;
                                    listData.add(new PieEntry(sum, id));
                                    productNames.add(name);
                                }

                                PieDataSet pieDataSet = new PieDataSet(listData,"");
                                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                pieDataSet.setValueLineColor(Color.BLACK);
                                pieDataSet.setValueTextSize(16f);
                                pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
                                PieData data = new PieData(pieDataSet);
                                pieChart.setData(data);
                                //pieChart.animateXY(2000, 2000);
                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setCenterText("Thống kê");

                                // Tạo chú thích dưới biểu đồ
                                Legend legend = barChart.getLegend();
                                legend.setForm(Legend.LegendForm.SQUARE);
                                legend.setFormSize(12f);
                                legend.setTextSize(14f);
                                legend.setTextColor(Color.BLACK);
                                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                                legend.setDrawInside(false);
                                legend.setXEntrySpace(10f);
                                legend.setYEntrySpace(10f);
                                legend.setYOffset(10f);

                                // Xóa các TextView hiện có trong legendLayout trước khi thêm mới
                                legendLayoutPieChart.removeAllViews();

                                for (int i = 0; i < productNames.size(); i++) {
                                    String productName = productNames.get(i);
                                    int color = pieDataSet.getColor(i);

                                    String text = "Top " +  (i + 1) + " : " + productName;

                                    TextView textView = new TextView(this);
                                    textView.setText(text);
                                    textView.setTextColor(color);

                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    layoutParams.setMargins(10, 10, 10, 10);
                                    textView.setLayoutParams(layoutParams);

                                    legendLayoutPieChart.addView(textView);
                                }

                                pieChart.invalidate();
                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
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
        txtThongke = findViewById(R.id.txtThongkePieChart);
        txtThongkeBarChart = findViewById(R.id.txtThongkeBarChart);
        legendLayout = findViewById(R.id.legendLayout);
        legendLayoutPieChart = findViewById(R.id.legendLayoutPieChart);
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
        List<String> productSumMoney = new ArrayList<>();
        legendLayout.setVisibility(View.VISIBLE);
        legendLayoutPieChart.setVisibility(View.GONE);
        txtThongke.setVisibility(View.GONE);
        txtThongkeBarChart.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
        compositeDisposable.add(salesApi.getStatisticalByMonth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        statisticalModel -> {
                            if (statisticalModel.isSuccess()) {
                                List<BarEntry> listData = new ArrayList<>();
                                for (int i = 0; i < statisticalModel.getResult().size(); i++) {
                                    int Sum = statisticalModel.getResult().get(i).getSumMonth();
                                    String month = statisticalModel.getResult().get(i).getMonth();
                                    listData.add(new BarEntry(Integer.parseInt(month), Sum));
                                    productSumMoney.add(Sum + "");
                                }

                                BarDataSet barDataSet = new BarDataSet(listData, "Thống kê");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                barDataSet.setValueTextSize(14f);
                                barDataSet.setValueTextColor(Color.RED);

                                BarData data = new BarData(barDataSet);

                                // barChart.animateXY(2000, 2000);
                                barChart.setData(data);

                                // Tạo chú thích dưới biểu đồ
                                Legend legend = barChart.getLegend();
                                legend.setForm(Legend.LegendForm.SQUARE);
                                legend.setFormSize(12f);
                                legend.setTextSize(14f);
                                legend.setTextColor(Color.BLACK);
                                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                                legend.setDrawInside(false);
                                legend.setXEntrySpace(10f);
                                legend.setYEntrySpace(10f);
                                legend.setYOffset(10f);

                                // Xóa các TextView hiện có trong legendLayout trước khi thêm mới
                                legendLayout.removeAllViews();

                                for (int i = 0; i < productSumMoney.size(); i++) {
                                    String productSum = productSumMoney.get(i);
                                    int color = barDataSet.getColor(i);
                                    String month = statisticalModel.getResult().get(i).getMonth();
                                    String covertSum = formatNumberWithDotSeparator(Integer.parseInt(productSum));
                                    String text = " Tổng doanh thu của tháng " +  month +  " là : " + covertSum + " VNĐ";

                                    TextView textView = new TextView(this);
                                    textView.setText(text);
                                    textView.setTextColor(color);

                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    layoutParams.setMargins(10, 10, 10, 10);
                                    textView.setLayoutParams(layoutParams);

                                    legendLayout.addView(textView);
                                }
                                barChart.invalidate();
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.d("thongke", throwable.getMessage());
                        }
                ));
    }

    public String formatNumberWithDotSeparator(int number) {
        String formattedNumber = String.format("%,d", number);
        return formattedNumber.replace(",", ".");
    }
}
