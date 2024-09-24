package com.example.fashionecommerce.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fashionecommerce.Adapter.StatusOderAdapterUser;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.databinding.ActivityStatusOderBinding;
import com.example.fashionecommerce.model.Oder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StatusOderActivity extends AppCompatActivity implements StatusOderAdapterUser.OnClickListener {
    private ActivityStatusOderBinding binding;
    private StatusOderAdapterUser oderAdapterUser;
    private ArrayList<Oder> oderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusOderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
        configStoreOder();
        configRVoder();
    }

    private void initClicks() {
        binding.include8.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainUserActivity.class));

        });
    }

    private void init(){
        binding.include8.textTitleToolbar.setText("Trạng thái đơn hàng");
    }
    private void configStoreOder() {
        FirebaseHelper.databaseReference("userOder")
                .child(FirebaseHelper.getUIDpersonCurrent())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        oderList.clear();
                        binding.progressBar.setVisibility(View.VISIBLE);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Oder oder = dataSnapshot.getValue(Oder.class);
                            oderList.add(oder);
                        }
                        Collections.reverse(oderList);
                        oderAdapterUser.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVoder() {
        binding.RVoder.setLayoutManager(new LinearLayoutManager(this));
        binding.RVoder.setHasFixedSize(true);
        oderAdapterUser = new StatusOderAdapterUser(oderList,this,this);
        binding.RVoder.setAdapter(oderAdapterUser);
    }


    @Override
    public void OnClick(String action, Oder oder) {
        switch (action){
            case "CANCEL" :{
//                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                break;
            }
            case "DETAIL" :{
                Intent intent = new Intent(this, ParametersOderActivity.class);
                intent.putExtra("getPurchasedOder", oder);
                startActivity(intent);
                break;
            }

        }
    }
}