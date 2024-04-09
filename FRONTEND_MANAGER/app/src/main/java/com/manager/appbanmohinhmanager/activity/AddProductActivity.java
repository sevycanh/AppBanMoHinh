package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.AddProductAdapter;
import com.manager.appbanmohinhmanager.model.CategoryManager;
import com.manager.appbanmohinhmanager.model.ProductManager;
import com.manager.appbanmohinhmanager.model.ProductManagerModel;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_multiple_img;
    Button btn_single_img;
    ImageView single_img;
    Button btn_submit;
    CircularProgressIndicator progressBar;

    ArrayList<Uri> uri;
    AddProductAdapter adapter;
    private static final int PICK_IMAGE = 1;
    String typechoose = "";
    private Uri imageUri;
    private Uri ImgMain = null;
    private List<Uri> listImgSub = new ArrayList<>();

    StorageReference storageReference;
    MaterialToolbar toolbar;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiManager apiManager;

    List<CategoryManager> arrayCategory;
    Spinner spinner;
    int idcategory;

    List<ProductManager> MangProduct;

    int quantity;
    ImageView btnMinus, btnPlus;

    TextInputEditText txtNameProduct, txtDescriptionProduct, txtPriceProduct, txtCouponProduct, txtQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Bundle bundle = getIntent().getExtras();
        quantity = 0;
        initView();
        handleClickedButtonSingle();
        handleClickedButtonMultiple();
        handleSubmitData(bundle);
        actionToolBar();
        getCategory();
        handleButtonQuantity();
    }

    private void handleButtonQuantity() {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (typechoose == "Multiple") {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                if (data.getClipData() != null) {
                    int countOfImages = data.getClipData().getItemCount();
                    for (int i = 0; i < countOfImages; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();
                        uri.add(imageUri);
                        listImgSub.add(imageUri);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    imageUri = data.getData();
                    uri.add(imageUri);
                    listImgSub.add(imageUri);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "You havent't Pick any Image", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                if (data.getClipData() != null) {
                    Toast.makeText(this, "You must single Image", Toast.LENGTH_SHORT).show();
                } else {
                    imageUri = data.getData();
                    ImgMain = imageUri;
                    Glide.with(this)
                            .load(imageUri)
                            .into(single_img);
                }
            } else {
                Toast.makeText(this, "You havent't Pick any Image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void handleClickedButtonSingle() {
        btn_single_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                typechoose = "Single";
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    private void handleClickedButtonMultiple() {
        btn_multiple_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                typechoose = "Multiple";
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });
    }

    private void uploadToFirebase(Uri imageuri, String randomName, boolean check) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomName);
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if(check == true){
                            finish();
                        }
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
                        Toast.makeText(AddProductActivity.this, "Images Uploading failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleSubmitData(Bundle bundle) {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if (txtNameProduct.getText().toString().isEmpty()) {
                    check = false;
                    txtNameProduct.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                } else if (txtDescriptionProduct.getText().toString().isEmpty()) {
                    check = false;
                    txtDescriptionProduct.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
                } else if (txtPriceProduct.getText().toString().isEmpty()) {
                    check = false;
                    txtPriceProduct.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập giá", Toast.LENGTH_SHORT).show();
                } else if (txtPriceProduct.getText().toString().matches(".*[a-zA-Z].*")) {
                    check = false;
                    txtPriceProduct.setText("");
                    txtPriceProduct.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (ImgMain == null) {
                    check = false;
                    Toast.makeText(AddProductActivity.this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                } else if (listImgSub.size() == 0) {
                    check = false;
                    Toast.makeText(AddProductActivity.this, "Vui lòng chọn ảnh mô tả", Toast.LENGTH_SHORT).show();
                } else if (txtCouponProduct.getText().toString().matches(".*[a-zA-Z].*")) {
                    check = false;
                    txtCouponProduct.setText("0");
                    txtCouponProduct.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Khuyến mãi không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                    if (check == true) {
                        String nameProduct = txtNameProduct.getText().toString();
                        String descriptionProduct = txtDescriptionProduct.getText().toString();
                        int priceProduct = Integer.parseInt(txtPriceProduct.getText().toString());
                        int couponProduct = Integer.parseInt(txtCouponProduct.getText().toString().equals("") ? "0" : txtCouponProduct.getText().toString());
                        int nextid = bundle.getInt("nextid") + 1;
                        List<String> ArraySubImg = new ArrayList<>();
                        for (int i = 0; i < listImgSub.size(); i++) {
                            ArraySubImg.add(String.valueOf(nextid) + "_" + UUID.randomUUID().toString());
                        }
                        final String MainImg = String.valueOf(nextid) + "_" + UUID.randomUUID().toString();
                        String subImg = "";
                        for (int i = 0; i < listImgSub.size(); i++) {
                            uploadToFirebase(listImgSub.get(i), ArraySubImg.get(i), false);
                            if (i == (listImgSub.size() - 1)) {
                                subImg += (ArraySubImg.get(i));
                            } else {
                                subImg += (ArraySubImg.get(i) + ",");
                            }
                        }
                        uploadToFirebase(ImgMain, MainImg, true);

                        uploadDataProduct(nameProduct, priceProduct, quantity, descriptionProduct, MainImg, subImg, couponProduct, idcategory, 1);

                    }
                }
            });
        }

        private void uploadDataProduct (String name,int price, int quantity, String
        description, String main_image, String subimage,int coupon, int idcategory, int status){
            Log.d("query", name + ", " + price + ", " + quantity + ", " + description + ", " + main_image + ", " + subimage + ", " + coupon + ", " + status);
            compositeDisposable.add(apiManager.addDataProduct(name, price, quantity, description, main_image, subimage, coupon, idcategory, status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            ProductManagerModel -> {
                                if (ProductManagerModel.isSuccess()) {
                                    Toast.makeText(this, "Thêm sản phẩm mới thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, ProductManagerModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }, throwable -> {
                                Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    )
            );
        }

        private void actionToolBar () {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        private void getCategory () {
            compositeDisposable.add(apiManager.getDataCategory()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            CategoryManagerModel -> {
                                if (CategoryManagerModel.isSuccess()) {
                                    arrayCategory = CategoryManagerModel.getResult();
                                    List<String> data = new ArrayList<>();
                                    for (int i = 0; i < arrayCategory.size(); i++) {
                                        int id = arrayCategory.get(i).getCategory_id();
                                        String s = arrayCategory.get(i).getCategory_id() + "_" + arrayCategory.get(i).getName();
                                        data.add(s);
                                    }
                                    ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
                                    spinner.setAdapter(adapterCategory);
                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String s[] = data.get(position).split("_");
                                            idcategory = Integer.parseInt(s[0]);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }

                            }, throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    )
            );
        }

        private void initView () {
            uri = new ArrayList<>();
            single_img = findViewById(R.id.imgAdd_Main);
            recyclerView = findViewById(R.id.recyclerView_MultipleImages);
            btn_single_img = findViewById(R.id.getMainPicture);
            btn_multiple_img = findViewById(R.id.getSubPicture);
            btn_submit = findViewById(R.id.submitDataAddProduct);
            progressBar = findViewById(R.id.progressBarAddProduct);
            adapter = new AddProductAdapter(uri);
            recyclerView.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 5));
            recyclerView.setAdapter(adapter);
            toolbar = findViewById(R.id.toolBarAddProduct);
            spinner = findViewById(R.id.spinnerAdd);
            apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
            arrayCategory = new ArrayList<>();
            txtQuantity = findViewById(R.id.txtQuantity);
            btnMinus = findViewById(R.id.btnMinus);
            btnPlus = findViewById(R.id.btnPlus);
            txtNameProduct = findViewById(R.id.txtAdd_NameProduct);
            txtDescriptionProduct = findViewById(R.id.txtAdd_Description);
            txtPriceProduct = findViewById(R.id.txtAdd_Price);
            txtCouponProduct = findViewById(R.id.txtAdd_Coupon);
            MangProduct = new ArrayList<>();
        }
    }