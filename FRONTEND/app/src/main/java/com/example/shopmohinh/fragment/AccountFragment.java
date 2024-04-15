package com.example.shopmohinh.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopmohinh.R;
import com.example.shopmohinh.activity.AddressUserActivity;
import com.example.shopmohinh.activity.ForgotPassActivity;
import com.example.shopmohinh.activity.LogInActivity;
import com.example.shopmohinh.activity.SplashActivity;
import com.example.shopmohinh.model.User;
import com.example.shopmohinh.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;


public class AccountFragment extends Fragment {
    TextView txtUserNameProfile, txtUserPhoneProfile, txtUserEmailProfile,txtUserAddressProfile;

    Button btnChangeProfile, btnChangePasswordProfile,btnSignOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        Anhxa(rootView);
//        ActionViewFlipper();
//        return rootView;

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        initView(rootView);
        initControl();
        // Inflate the layout for this fragment
        return rootView;
    }
    private void initView(View rootView) {
        txtUserNameProfile = rootView.findViewById(R.id.txtUserNameProfile);
        txtUserPhoneProfile = rootView.findViewById(R.id.txtUserPhoneProfile);
        txtUserAddressProfile = rootView.findViewById(R.id.txtUserAddressProfile);
        txtUserEmailProfile = rootView.findViewById(R.id.txtUserEmailProfile);

        btnChangeProfile = rootView.findViewById(R.id.btnChangeProfile);
        btnChangePasswordProfile = rootView.findViewById(R.id.btnChangePasswordProfile);
        btnSignOut = rootView.findViewById(R.id.btnSignOut);
    }

    private void initControl() {
        txtUserNameProfile.setText(Utils.user_current.getUsername());
        if(!Utils.user_current.getAdministrative_address().isEmpty()){
            List<String> address_administrative = Arrays.asList(Utils.user_current.getAdministrative_address().split("\n"));
            txtUserAddressProfile.setText(Utils.user_current.getAddress() + "\n" + address_administrative.get(2) + ", " + address_administrative.get(1)+ ", " + address_administrative.get(0));
        }
        else{
            txtUserAddressProfile.setText(Utils.user_current.getAddress());
        }
        txtUserEmailProfile.setText(Utils.user_current.getEmail());
        txtUserPhoneProfile.setText(Utils.user_current.getPhone());
        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressUserActivity.class);
                startActivity(intent);
            }
        });

        btnChangePasswordProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ForgotPassActivity.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.user_current = new User();
                Paper.book().delete("user");
                Utils.purchases.clear();
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                        .signOut();
                Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if(!Utils.user_current.getAdministrative_address().isEmpty()){
            List<String> address_administrative = Arrays.asList(Utils.user_current.getAdministrative_address().split("\n"));
            txtUserAddressProfile.setText(Utils.user_current.getAddress() + "\n" + address_administrative.get(2) + ", " + address_administrative.get(1)+ ", " + address_administrative.get(0));
        }
        else{
            txtUserAddressProfile.setText(Utils.user_current.getAddress());
        }
        txtUserNameProfile.setText(Utils.user_current.getUsername());
        txtUserEmailProfile.setText(Utils.user_current.getEmail());
        txtUserPhoneProfile.setText(Utils.user_current.getPhone());

    }



}