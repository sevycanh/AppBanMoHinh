package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class UpdateCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtNameCategory;
    ImageView imgCategory;
    Button btnChonAnh;
    Button btnSave;
    ProgressBar progressBar;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    StorageReference storageReference;
    Uri imageUri;
    ApiManager apiManager;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.activity_update_category);
        initView();
        actionToolBar();
        showData(bundle);
        handleClickedButtonAddImg();
        handleClickedButtonSave(bundle);
    }

    private void showData(Bundle bundle){
        txtNameCategory.setText(bundle.getString("nameCategory"));
        Uri uri = bundle.getParcelable("UriImage");
        Glide.with(getApplicationContext())
                .load(uri)
                .into(imgCategory);
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

    private void handleClickedButtonSave(Bundle bundle){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String randomName = UUID.randomUUID().toString();
                uploadToFirebase(imageUri, randomName, bundle);
            }
        });
    }

    private void uploadToFirebase(Uri imageuri, String randomName, Bundle bundle) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomName);
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String nameCategory = String.valueOf(txtNameCategory.getText());
                        updateCategory(nameCategory, randomName, bundle);
                        DeleteToFirebase(bundle.getString("nameImg"));
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
                        Toast.makeText(UpdateCategoryActivity.this, "Images Uploading failed", Toast.LENGTH_SHORT).show();
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

    private void DeleteToFirebase(String nameimg){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(nameimg);
        storageReference.delete();
    }

    private void updateCategory(String nameCategory, String randomName,Bundle bundle){
        int idCategory = bundle.getInt("id");
//        Toast.makeText(this, idCategory +"_"+ nameCategory +"_"+ randomName, Toast.LENGTH_SHORT).show();
        compositeDisposable.add(apiManager.updateDataCategory(idCategory, nameCategory, randomName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        CategoryManagerModel ->{
                            if (CategoryManagerModel.isSuccess()){
                                Toast.makeText(this, "Cập Nhật Dữ Liệu Thành Công", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                )
        );
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbarUpdateCategory);
        txtNameCategory = findViewById(R.id.txtUpdateTenDanhMuc);
        imgCategory = findViewById(R.id.imgUpdateCategory);
        btnChonAnh = findViewById(R.id.btnUpdateImgCaterogy);
        btnSave = findViewById(R.id.btnSave);
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        progressBar = findViewById(R.id.progressBarUpdateCategory);
    }
}