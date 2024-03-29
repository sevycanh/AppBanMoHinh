package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.AddProductAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_multiple_img;
    Button btn_single_img;
    ImageView single_img;
    Button btn_submit;

    ArrayList<Uri> uri;
    AddProductAdapter adapter;
    private static final int PICK_IMAGE = 1;
    String typechoose = "";
    private Uri imageUri;
    private Uri ImgMain;
    private List<Uri> listImgSub = new ArrayList<>();

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        handleClickedButtonSingle();
        handleClickedButtonMultiple();
        handleSubmitData();
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

    private void uploadToFirebase(Uri imageuri) {
        final String randomName = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomName);
        storageReference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddProductActivity.this, "Images Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this, "Images Uploading failed", Toast.LENGTH_SHORT).show();
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

    private void handleSubmitData() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listImgSub.size(); i++) {
                    uploadToFirebase(listImgSub.get(i));
                }
                uploadToFirebase(ImgMain);
            }
        });
    }

    private void initView() {
        uri = new ArrayList<>();
        single_img = findViewById(R.id.single_image);
        recyclerView = findViewById(R.id.recyclerView_Multiple_Images);
        btn_single_img = findViewById(R.id.getMainPickture);
        btn_multiple_img = findViewById(R.id.getSubPickture);
        btn_submit = findViewById(R.id.submitDataAddProduct);
        adapter = new AddProductAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 5));
        recyclerView.setAdapter(adapter);
    }
}