package com.example.fashionecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.fashionecommerce.ActivityAdmin.MainAdminActivity;
import com.example.fashionecommerce.ActivityUser.MainUserActivity;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(getMainLooper()).postDelayed(this::checkPeson, 1000);
    }

    private void checkPeson() {
        if (FirebaseHelper.checkUserCurrent()){
            getStorePerson();
        }
    }

    private void getStorePerson() {
        FirebaseHelper.databaseReference("admin")
                .child(FirebaseHelper.getUIDpersonCurrent())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            startActivity(new Intent(getBaseContext(), MainAdminActivity.class));
                        }else {
                            startActivity(new Intent(getBaseContext(), MainUserActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkAdmin(String idAdmin) {
        if (FirebaseHelper.checkUserCurrent()){
            if (FirebaseHelper.getUIDpersonCurrent().equals(idAdmin)){
                startActivity(new Intent(this, MainAdminActivity.class));
            }
        }
    }
}