package com.manager.appbanmohinhmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manager.appbanmohinhmanager.R;
import com.manager.appbanmohinhmanager.adapter.UpdateProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    Button btnSave;
    RecyclerView recyclerViewUpdate;

    ArrayList<Uri> uriArrayList;
    List<String> mangDataDownload = new ArrayList<>();
    List<Uri> uriData = new ArrayList<>();
    List<String> indexData = new ArrayList<>();

    UpdateProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        initView();
        downloadItemAtIndex(0);
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


    private void downloadToFirebase(String nameimg, DownloadCallback downloadCallback){
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

    private void initView(){
        uriArrayList = new ArrayList<>();
        btnSave = findViewById(R.id.btnSave);
        mangDataDownload.add("bb0427cf-987c-4328-a3c9-153f0a7260d9");
        mangDataDownload.add("b7c4c145-ea33-4f2d-86f9-dfaea05b8a89");
        mangDataDownload.add("776f2bd4-d279-4489-a21e-c2521b3e6f44");
        mangDataDownload.add("64a10317-1f18-47a5-b30a-65c6282cfc64");
        adapter = new UpdateProductAdapter(getApplicationContext(), uriArrayList, mangDataDownload);
        recyclerViewUpdate = findViewById(R.id.recyclerViewUpdate);
        recyclerViewUpdate.setLayoutManager(new GridLayoutManager(UpdateProductActivity.this, 5));
        recyclerViewUpdate.setAdapter(adapter);
    }
}
