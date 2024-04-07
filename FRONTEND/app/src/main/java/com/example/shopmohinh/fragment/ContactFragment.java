package com.example.shopmohinh.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.MapsActivity;
import com.google.android.material.card.MaterialCardView;

import java.net.URL;
import java.util.Map;


public class ContactFragment extends Fragment {
    MaterialCardView cardZalo, cardFacebook, cardGmail, cardMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(view);
        initControll();
        return view;
    }

    private void initControll() {
        cardZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://chat.zalo.me/0357349402";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        cardFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/profile.php?id=100089556093069";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        cardGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"maicanhsvc@gmail.com"});
                startActivity(intent);

            }
        });
        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        cardZalo= view.findViewById(R.id.cardZalo_Contact);
        cardFacebook= view.findViewById(R.id.cardFacebook_Contact);
        cardGmail = view.findViewById(R.id.cardGmail_Contact);
        cardMap = view.findViewById(R.id.cardMap_Contact);
    }
}