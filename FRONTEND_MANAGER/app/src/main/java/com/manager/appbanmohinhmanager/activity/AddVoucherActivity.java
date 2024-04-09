package com.manager.appbanmohinhmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.VoucherManagerAdapter;
import com.manager.appbanmohinhmanager.retrofit.ApiBanHang;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddVoucherActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnNgayBD, btnNgayKT, btnGioBD, btnGioKT, btnThem;
    TextView txtNgayBD, txtNgayKT, txtGioBD, txtGioKT;
    RadioGroup radioGroup;
    RadioButton rdCongKhai, rdDanhRieng;
    TextInputLayout tlid;
    TextInputEditText edtTen, edtSoLuong, edtGiam, edtId;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);

        initView();
        actionToolBar();
        initControl();
    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initControl() {
        btnNgayBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Chọn ngày bắt đầu")
                        .build();
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        txtNgayBD.setText(sdf.format(new Date(selection)));
                    }
                });
                picker.show(getSupportFragmentManager(), "date_picker");
            }
        });
        btnNgayKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Chọn ngày kết thúc")
                        .build();
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        txtNgayKT.setText(sdf.format(new Date(selection)));
                    }
                });
                picker.show(getSupportFragmentManager(), "date_picker");
            }
        });
        btnGioBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(0)
                        .setMinute(0)
                        .setTitleText("Chọn thời gian")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        txtGioBD.setText(timeString+":00");
                    }
                });
                timePicker.show(getSupportFragmentManager(), "TIME_PICKER_TAG");
            }
        });
        btnGioKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(0)
                        .setMinute(0)
                        .setTitleText("Chọn thời gian")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        txtGioKT.setText(timeString+":00");
                    }
                });
                timePicker.show(getSupportFragmentManager(), "TIME_PICKER_TAG");
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioCongKhai_AddVoucher){
                    tlid.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioDanhRieng_AddVoucher){
                    tlid.setVisibility(View.VISIBLE);
                }
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString().trim();
                String soluong = edtSoLuong.getText().toString().trim();
                String giam = edtGiam.getText().toString().trim();
                if (TextUtils.isEmpty(ten) || TextUtils.isEmpty(soluong) | TextUtils.isEmpty(giam)){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (rdDanhRieng.isChecked()){
                        String id = edtId.getText().toString().trim();
                        if (TextUtils.isEmpty(id)){
                            Toast.makeText(getApplicationContext(), "Vui lòng nhập id người nhận", Toast.LENGTH_SHORT).show();
                        } else {
                            compositeDisposable.add(apiBanHang.addVoucher(edtTen.getText().toString(), Integer.parseInt(edtSoLuong.getText().toString()), Integer.parseInt(edtGiam.getText().toString()), txtNgayBD.getText() + " "+ txtGioBD.getText(),
                                            txtNgayKT.getText() + " " + txtGioKT.getText(), Integer.parseInt(edtId.getText().toString()), 0)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {
                                                if (messageModel.isSuccess()){
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }, throwable -> {
                                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                    )
                            );
                        }
                    } else {
                        compositeDisposable.add(apiBanHang.addVoucher(edtTen.getText().toString(), Integer.parseInt(edtSoLuong.getText().toString()), Integer.parseInt(edtGiam.getText().toString()), txtNgayBD.getText() + " "+ txtGioBD.getText(),
                                        txtNgayKT.getText() + " " + txtGioKT.getText(), 0, 1)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            if (messageModel.isSuccess()){
                                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }, throwable -> {
                                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                )
                        );
                    }
                }

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBarAddVoucher);
        txtNgayBD = findViewById(R.id.txtNgayBD_AddVoucher);
        txtNgayKT = findViewById(R.id.txtNgayKT_AddVoucher);
        txtGioBD = findViewById(R.id.txtGioBD_AddVoucher);
        txtGioKT = findViewById(R.id.txtGioKT_AddVoucher);
        btnNgayBD = findViewById(R.id.btnNgayBD_AddVoucher);
        btnNgayKT = findViewById(R.id.btnNgayKT_AddVoucher);
        btnGioBD = findViewById(R.id.btnGioBD_AddVoucher);
        btnGioKT = findViewById(R.id.btnGioKT_AddVoucher);
        radioGroup = findViewById(R.id.radioGroup_AddVoucher);
        rdCongKhai = findViewById(R.id.radioCongKhai_AddVoucher);
        rdDanhRieng = findViewById(R.id.radioDanhRieng_AddVoucher);
        tlid = findViewById(R.id.tl_id_AddVoucher);
        btnThem = findViewById(R.id.btnThem_Voucher);
        edtTen = findViewById(R.id.edtTenVoucher_AddVoucher);
        edtSoLuong = findViewById(R.id.edtSoLuong_AddVoucher);
        edtGiam = findViewById(R.id.edtPhanTramGiam_AddVoucher);
        edtId = findViewById(R.id.edtID_AddVoucher);
        compositeDisposable = new CompositeDisposable();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}