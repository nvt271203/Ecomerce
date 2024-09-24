package com.example.fashionecommerce.ActivityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fashionecommerce.FragmentAdmin.HomeAdminFragment;
import com.example.fashionecommerce.FragmentAdmin.MessageAdminFragment;
import com.example.fashionecommerce.FragmentAdmin.OderAdminFragment;
import com.example.fashionecommerce.FragmentAdmin.ProductAdminFragment;
import com.example.fashionecommerce.FragmentAdmin.ProfileAdminFragment;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        configMenuBottomBar();
        initClicks();
//        ToolsHelper.getToken();
        getToken();
    }



    private void initClicks() {
//        findViewById(R.id.imgList).setOnClickListener(v ->
//                startActivity(new Intent(getBaseContext(), CategoryActivity.class)));
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!FirebaseHelper.checkUserCurrent()){
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//    }

    private void configMenuBottomBar() {
        final TextView textHome = findViewById(R.id.textHome);
        final TextView textProduct = findViewById(R.id.textProduct);
        final TextView textOder = findViewById(R.id.textOder);
        final TextView textProfile = findViewById(R.id.textProfile);
        final TextView textMessage = findViewById(R.id.textMessage);

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout productLayout = findViewById(R.id.productLayout);
        final LinearLayout oderLayout = findViewById(R.id.oderLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);
        final LinearLayout messageLayout = findViewById(R.id.messageLayout);

        //        config fragment tab
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view_tag, HomeAdminFragment.class, null)
                .commit();

        homeLayout.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, HomeAdminFragment.class, null)
                    .commit();
            textHome.setVisibility(View.VISIBLE);
            homeLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            productLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            oderLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            textProduct.setVisibility(View.GONE);
            textOder.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profileLayout.startAnimation(scaleAnimation);
            homeLayout.startAnimation(scaleAnimation);
            oderLayout.startAnimation(scaleAnimation);
            messageLayout.startAnimation(scaleAnimation);
            productLayout.startAnimation(scaleAnimation);
        });
        productLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, ProductAdminFragment.class, null)
                    .commit();

            textProduct.setVisibility(View.VISIBLE);
            productLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            oderLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            textHome.setVisibility(View.GONE);
            textOder.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profileLayout.startAnimation(scaleAnimation);
            homeLayout.startAnimation(scaleAnimation);
            messageLayout.startAnimation(scaleAnimation);
            oderLayout.startAnimation(scaleAnimation);
            productLayout.startAnimation(scaleAnimation);
        });
        oderLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, OderAdminFragment.class, null)
                    .commit();

            textOder.setVisibility(View.VISIBLE);
            oderLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            productLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            textProduct.setVisibility(View.GONE);
            textHome.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profileLayout.startAnimation(scaleAnimation);
            homeLayout.startAnimation(scaleAnimation);
            oderLayout.startAnimation(scaleAnimation);
            messageLayout.startAnimation(scaleAnimation);
            productLayout.startAnimation(scaleAnimation);
        });
        profileLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, ProfileAdminFragment.class, null)
                    .commit();

            textProfile.setVisibility(View.VISIBLE);
            profileLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            productLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            oderLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            textProduct.setVisibility(View.GONE);
            textHome.setVisibility(View.GONE);
            textOder.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profileLayout.startAnimation(scaleAnimation);
            homeLayout.startAnimation(scaleAnimation);
            messageLayout.startAnimation(scaleAnimation);
            oderLayout.startAnimation(scaleAnimation);
            productLayout.startAnimation(scaleAnimation);
        });

        messageLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, MessageAdminFragment.class, null)
                    .commit();

            textMessage.setVisibility(View.VISIBLE);
            messageLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            productLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            oderLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            textProduct.setVisibility(View.GONE);
            textOder.setVisibility(View.GONE);
            textHome.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profileLayout.startAnimation(scaleAnimation);
            messageLayout.startAnimation(scaleAnimation);
            homeLayout.startAnimation(scaleAnimation);
            oderLayout.startAnimation(scaleAnimation);
            productLayout.startAnimation(scaleAnimation);
        });


    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("MyToken", "Lấy Token thất bại", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.i("MyToken", token);
                        setTokenUser(token);
                    }

                    private void setTokenUser(String token) {
                        DatabaseReference instanceUser = FirebaseDatabase.getInstance().getReference("admin").child(FirebaseHelper.getUIDpersonCurrent());
                        instanceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("token", token);
                                instanceUser.updateChildren(hashMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
    }


}