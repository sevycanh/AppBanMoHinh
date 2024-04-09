package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.manager.appbanmohinhmanager.adapter.UpdateProductAdapter;
import com.manager.appbanmohinhmanager.model.CategoryManager;
import com.manager.appbanmohinhmanager.model.ProductManager;
import com.manager.appbanmohinhmanager.retrofit.ApiManager;
import com.manager.appbanmohinhmanager.retrofit.RetrofitClient;
import com.manager.appbanmohinhmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpdateProductActivity extends AppCompatActivity {
    Button btnSave, btnDelele;
    RecyclerView recyclerViewUpdate;
    MaterialToolbar toolbar;
    TextInputEditText txtNameProduct, txtDescriptionProduct, txtPriceProduct, txtCouponProduct, txtQuantity;
    ImageView mainImg;
    Button btn_multiple_img;
    Button btn_single_img;


    private Uri imageUri;
    private List<Uri> listImgSub = new ArrayList<>();
    private Uri ImgMain_Old = null;
    private Uri ImgMain_New = null;
    String ImgMain = "";

    List<CategoryManager> arrayCategory;
    Spinner spinner;
    int idcategory;

    private static final int PICK_IMAGE = 1;
    String typechoose = "";

    int quantity;
    ImageView btnMinus, btnPlus;

    ArrayList<Uri> uriArrayList;
    List<String> mangDataDownload = new ArrayList<>();
    List<String> mangDataDownloadDeleted = new ArrayList<>();

    UpdateProductAdapter adapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    List<ProductManager> MangProduct;
    ApiManager apiManager;

    List<String> dataSpinner;
    Uri imageuri;
    StorageReference storageReference;
    CircularProgressIndicator progressBar;
    List<String> ArraySubImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        Bundle bundle = getIntent().getExtras();
        initView();
        getCategory(bundle);
        actionToolBar();
        handleButtonQuantity();
        setDataProduct(bundle);
        handleClickedButtonSingle();
        handleClickedButtonMultiple();
        handleButtonSave(bundle);
        handleButtonDelete(bundle);
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
//                        uri.add(imageUri);
                        listImgSub.add(imageUri);
                        uriArrayList.add(imageUri);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    imageUri = data.getData();
                    uriArrayList.add(imageUri);
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
                    ImgMain_New = imageUri;
                    Glide.with(this)
                            .load(imageUri)
                            .into(mainImg);
                }
            } else {
                Toast.makeText(this, "You havent't Pick any Image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void handleButtonDelete(Bundle bundle) {
        btnDelele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm không?");

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDataProduct(bundle.getInt("idproduct"));
                    }
                });

                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
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

    private void handleButtonSave(Bundle bundle) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if (txtNameProduct.getText().toString().isEmpty()) {
                    check = false;
                    txtNameProduct.requestFocus();
                    Toast.makeText(UpdateProductActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                } else if (txtDescriptionProduct.getText().toString().isEmpty()) {
                    check = false;
                    txtDescriptionProduct.requestFocus();
                    Toast.makeText(UpdateProductActivity.this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
                } else if (txtPriceProduct.getText().toString().isEmpty()) {
                    check = false;
                    txtPriceProduct.requestFocus();
                    Toast.makeText(UpdateProductActivity.this, "Vui lòng nhập giá", Toast.LENGTH_SHORT).show();
                } else if (txtPriceProduct.getText().toString().matches(".*[a-zA-Z].*")) {
                    check = false;
                    txtPriceProduct.setText("");
                    txtPriceProduct.requestFocus();
                    Toast.makeText(UpdateProductActivity.this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (uriArrayList.size() == 0) {
                    check = false;
                    Toast.makeText(UpdateProductActivity.this, "Vui lòng chọn ảnh mô tả", Toast.LENGTH_SHORT).show();
                } else if (txtCouponProduct.getText().toString().matches(".*[a-zA-Z].*")) {
                    check = false;
                    txtCouponProduct.setText("0");
                    txtCouponProduct.requestFocus();
                    Toast.makeText(UpdateProductActivity.this, "Khuyến mãi không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                if (check == true) {
                    String nameProduct = txtNameProduct.getText().toString();
                    String descriptionProduct = txtDescriptionProduct.getText().toString();
                    int priceProduct = Integer.parseInt(txtPriceProduct.getText().toString());
                    int couponProduct = Integer.parseInt(txtCouponProduct.getText().toString());
                    int id = bundle.getInt("idproduct");
                    boolean checkFinish = false;

                    String subImg = "";
                    for (int i = 0; i < mangDataDownload.size(); i++) {
                        if (i == (mangDataDownload.size() - 1)) {
                            subImg += (mangDataDownload.get(i));
                        } else {
                            subImg += (mangDataDownload.get(i) + ",");
                        }
                    }
                    Log.d("NewList", String.valueOf(listImgSub.size()));
                    for (int i = 0; i < listImgSub.size(); i++) {
                        ArraySubImg.add(String.valueOf(id) + "_" + UUID.randomUUID().toString());
                    }
                    if (mangDataDownload.size() > 0 && listImgSub.size() > 0) {
                        subImg += ",";
                    }
                    for (int i = 0; i < listImgSub.size(); i++) {
                        if ((i == listImgSub.size() - 1) && ImgMain_New == null) {
                            checkFinish = true;
                        }
                        uploadToFirebase(listImgSub.get(i), ArraySubImg.get(i), checkFinish);
                        if (i == (listImgSub.size() - 1)) {
                            subImg += (ArraySubImg.get(i));
                        } else {
                            subImg += (ArraySubImg.get(i) + ",");
                        }
                    }
                    Log.d("subImg", subImg);
                    ImgMain = bundle.getString("mainImg");
                    if (ImgMain_New != null) {
                        final String MainImg = String.valueOf(id) + "_" + UUID.randomUUID().toString();
                        ImgMain = MainImg;
                        uploadToFirebase(ImgMain_New, MainImg, true);
                        DeleteToFirebase(bundle.getString("mainImg"));
                    }
                    for (int i = 0; i < mangDataDownloadDeleted.size(); i++) {
                        DeleteToFirebase(mangDataDownloadDeleted.get(i));
                    }
                    Log.d("mainImg", ImgMain);
                    updateDataProduct(id, nameProduct, priceProduct, quantity, descriptionProduct, ImgMain, subImg, couponProduct, idcategory, 1);
                    if (listImgSub.size() == 0 && ImgMain_New == null){
                        finish();
                    }
                }
            }
        });
    }

    private void DeleteToFirebase(String nameimg) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("/images")
                .child(nameimg);
        storageReference.delete();
    }

    private void uploadToFirebase(Uri imageuri, String randomName, boolean check) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomName);
        Log.d("uri", String.valueOf(imageuri));
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (check == true) {
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
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(UpdateProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("errorcheck", e.getMessage());
                    }
                });
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

    private void setDataProduct(Bundle bundle) {
        int id = bundle.getInt("id");
        txtNameProduct.setText(bundle.getString("name"));
        txtDescriptionProduct.setText(bundle.getString("description"));
        txtPriceProduct.setText(String.valueOf(bundle.getInt("price")));
        quantity = bundle.getInt("quantity");
        txtQuantity.setText(String.valueOf(quantity));
        txtCouponProduct.setText(String.valueOf(bundle.getInt("coupon")));
        String mang[] = bundle.getString("subImg").split(",");
        for (int i = 0; i < mang.length; i++) {
            mangDataDownload.add(mang[i]);
        }
        downloadItemAtIndex(0);
        downloadMainImg(bundle.getString("mainImg"));
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

    private void downloadMainImg(String nameimg) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference1 = storage.getReference()
                .child("/images")
                .child(nameimg);

        storageReference1.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImgMain_Old = uri;
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(mainImg);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProductActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void downloadItemAtIndex(int index) {
        if (index < mangDataDownload.size()) {
            downloadToFirebase(mangDataDownload.get(index), new DownloadCallback() {
                @Override
                public void onDownloadComplete() {
                    // Hàm downloadToFirebase đã hoàn thành, tiến hành tải xuống phần tử tiếp theo
                    downloadItemAtIndex(index + 1);
                }
            });
        }
    }

    private interface DownloadCallback {
        void onDownloadComplete();
    }


    private void downloadToFirebase(String nameimg, DownloadCallback downloadCallback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference1 = storage.getReference()
                .child("/images")
                .child(nameimg);

        storageReference1.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriArrayList.add(uri);
                        adapter.notifyDataSetChanged();
                        downloadCallback.onDownloadComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProductActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateDataProduct(int id, String name, int price, int quantity, String
            description, String main_image, String subimage, int coupon, int idcategory, int status) {
//        Log.d("query", name + ", " + price + ", " + quantity + ", " + description + ", " + main_image + ", " + subimage + ", " + coupon + ", " + status);
        compositeDisposable.add(apiManager.updateDataProduct(id, name, price, quantity, description, main_image, subimage, coupon, idcategory, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ProductManagerModel -> {
                            if (ProductManagerModel.isSuccess()) {
                                Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, ProductManagerModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void deleteDataProduct(int id) {
        compositeDisposable.add(apiManager.deleteProduct(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ProductManagerModel -> {
                            if (ProductManagerModel.isSuccess()) {
                                Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, ProductManagerModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void getCategory(Bundle bundle) {
        compositeDisposable.add(apiManager.getDataCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        CategoryManagerModel -> {
                            if (CategoryManagerModel.isSuccess()) {
                                arrayCategory = CategoryManagerModel.getResult();
                                dataSpinner = new ArrayList<>();
                                int selection = 0;
                                int idCategory = bundle.getInt("id");
                                for (int i = 0; i < arrayCategory.size(); i++) {
                                    int id = arrayCategory.get(i).getCategory_id();
                                    if (id == idCategory) {
                                        selection = i;
                                    }
                                    String s = arrayCategory.get(i).getCategory_id() + "_" + arrayCategory.get(i).getName();
                                    dataSpinner.add(s);
                                }
                                ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataSpinner);
                                spinner.setAdapter(adapterCategory);
                                spinner.setSelection(selection);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String s[] = dataSpinner.get(position).split("_");
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

    private void initView() {
        uriArrayList = new ArrayList<>();
        btnSave = findViewById(R.id.btnSaveProduct);
        btnDelele = findViewById(R.id.btnDeleteProduct);
        btn_single_img = findViewById(R.id.getMainUpdatePicture);
        btn_multiple_img = findViewById(R.id.getSubUpdatePicture);
        adapter = new UpdateProductAdapter(getApplicationContext(), uriArrayList, mangDataDownload, mangDataDownloadDeleted);
        recyclerViewUpdate = findViewById(R.id.recyclerViewMultipleImageUpdate);
        recyclerViewUpdate.setLayoutManager(new GridLayoutManager(UpdateProductActivity.this, 5));
        recyclerViewUpdate.setAdapter(adapter);
        toolbar = findViewById(R.id.toolBarTTSP);
        spinner = findViewById(R.id.spinnerUpdateProduct);
        apiManager = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiManager.class);
        arrayCategory = new ArrayList<>();
        txtQuantity = findViewById(R.id.txtUpdateQuantity);
        btnMinus = findViewById(R.id.btnUpdateMinus);
        btnPlus = findViewById(R.id.btnUpdatePlus);
        txtNameProduct = findViewById(R.id.txtUpdate_NameProduct);
        txtDescriptionProduct = findViewById(R.id.txtUpdate_Description);
        txtPriceProduct = findViewById(R.id.txtUpdate_Price);
        txtCouponProduct = findViewById(R.id.txtUpdate_Coupon);
        MangProduct = new ArrayList<>();
        mainImg = findViewById(R.id.single_image_update);
        progressBar = findViewById(R.id.progressBarUpdateProduct);
        ArraySubImg = new ArrayList<>();
    }
}
