package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.model.CategoryManagerModel;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddCategoryActivity extends AppCompatActivity {
    EditText txtTenDanhMuc;
    Button btnChonAnh;
    Button btnSubmit;
    ImageView imgCategory;
    private static final int PICK_IMAGE = 1;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Uri imageUri = null;

    Toolbar toolbar;
    StorageReference storageReference;
    ProgressBar progressBar;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        initView();
        actionToolBar();
        handleClickedButtonAddImg();
        handleButtonSubmit();
    }

    private void handleClickedButtonAddImg() {
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.getClipData() != null) {
                Toast.makeText(this, "You must single Image", Toast.LENGTH_SHORT).show();
            } else {
                imageUri = data.getData();
                Glide.with(this)
                        .load(imageUri)
                        .into(imgCategory);
            }
        } else {
            Toast.makeText(this, "You havent't Pick any Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleButtonSubmit(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtTenDanhMuc.getText().toString().isEmpty()){
                    txtTenDanhMuc.requestFocus();
                    Toast.makeText(AddCategoryActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(AddCategoryActivity.this, "Vui lòng chọn hình đại diện", Toast.LENGTH_SHORT).show();
                } else {
                    final String randomName = UUID.randomUUID().toString();
                    uploadToFirebase(imageUri, randomName);
                }
            }
        });
    }

    private void uploadToFirebase(Uri imageuri, String randomName) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomName);
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String nameCategory = String.valueOf(txtTenDanhMuc.getText());
                        addNewCategory(nameCategory, randomName);
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCategoryActivity.this, "Images Uploading failed", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void addNewCategory(String nameCategory,String imgCategory){
            compositeDisposable.add(apiManager.addDataCategory(nameCategory, imgCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        CategoryManagerModel ->{
                            if (CategoryManagerModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Thêm Danh Mục Thành Công", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), CategoryManagerModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void initView(){
        txtTenDanhMuc = findViewById(R.id.txtTenDanhMuc);
        toolbar = findViewById(R.id.toolbarAddCategory);
        btnChonAnh = findViewById(R.id.btnAddImgCaterogy);
        btnSubmit = findViewById(R.id.btnSubmitAddCategory);
        imgCategory = findViewById(R.id.imgAddCategory);
        progressBar = findViewById(R.id.progressBarAddCategory);
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
    }
}