package com.example.fashionecommerce.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fashionecommerce.ActivityAdmin.MainAdminActivity;
import com.example.fashionecommerce.FragmentUser.CartUserFragment;
import com.example.fashionecommerce.FragmentUser.ChatUserFragment;
import com.example.fashionecommerce.FragmentUser.HomeUserFragment;
import com.example.fashionecommerce.FragmentUser.MessageUserFragment;
import com.example.fashionecommerce.FragmentUser.ProfileUserFragment;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainUserActivity extends AppCompatActivity {
    private final int selectedItemMenu = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configMenuBottomBar();

        String currentUID = FirebaseHelper.getUIDpersonCurrent();

        if (currentUID != null && currentUID.equals("cJLkDRhb5PdlofqIAAAhXWUrjby1")) {
            startActivity(new Intent(this, MainAdminActivity.class));
        }

        if (FirebaseHelper.checkUserCurrent() && currentUID != null && !currentUID.equals("cJLkDRhb5PdlofqIAAAhXWUrjby1")) {
            getToken();
        }


    }



    private void configMenuBottomBar() {
        final TextView textHome = findViewById(R.id.textHome);
        final TextView textCart = findViewById(R.id.textCart);
        final TextView textMessage = findViewById(R.id.textMessage);
        final TextView textProfile = findViewById(R.id.textProfile);

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);
        final LinearLayout messageLayout = findViewById(R.id.messageLayout);
        final LinearLayout cartLayout = findViewById(R.id.cartLayout);

        //        config fragment tab
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view_tag, HomeUserFragment.class, null)
                .commit();

        homeLayout.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container_view_tag, HomeUserFragment.class, null)
                                            .commit();
            textHome.setVisibility(View.VISIBLE);
            homeLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            cartLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));;

            textCart.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            homeLayout.startAnimation(scaleAnimation);
        });
        cartLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, CartUserFragment.class, null)
                            .commit();

            textCart.setVisibility(View.VISIBLE);
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            cartLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            textHome.setVisibility(View.GONE);
            textMessage.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            cartLayout.startAnimation(scaleAnimation);
        });
        messageLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, ChatUserFragment.class, null)
                    .commit();

            textMessage.setVisibility(View.VISIBLE);
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            cartLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            textCart.setVisibility(View.GONE);
            textHome.setVisibility(View.GONE);
            textProfile.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            messageLayout.startAnimation(scaleAnimation);
        });
        profileLayout.setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_tag, ProfileUserFragment.class, null)
                    .commit();

            textProfile.setVisibility(View.VISIBLE);
            homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            profileLayout.setBackgroundResource(R.drawable.bg_item_menu_selected);
            cartLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            messageLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            textMessage.setVisibility(View.GONE);
            textCart.setVisibility(View.GONE);
            textHome.setVisibility(View.GONE);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            profileLayout.startAnimation(scaleAnimation);
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
                        DatabaseReference instanceUser = FirebaseDatabase.getInstance().getReference("user").child(FirebaseHelper.getUIDpersonCurrent());
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