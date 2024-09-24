package com.example.fashionecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fashionecommerce.Adapter.DeliveryAddressAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityAddressBinding;
import com.example.fashionecommerce.model.DeliveryAddress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddressActivity extends AppCompatActivity implements DeliveryAddressAdapter.OnClickListener {
    private ActivityAddressBinding binding;
    private DeliveryAddressAdapter deliveryAddressAdapter;
    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
        configDataDeliveryAddress();
        configRVadderss();
    }

    private void configDataDeliveryAddress() {
        FirebaseHelper.databaseReference("user")
                .child(FirebaseHelper.getUIDpersonCurrent())
                .child("deliveryAddress")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            deliveryAddressList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                DeliveryAddress deliveryAddress = dataSnapshot.getValue(DeliveryAddress.class);
                                deliveryAddressList.add(deliveryAddress);
                            }
                        }
                        Collections.reverse(deliveryAddressList);
                        deliveryAddressAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVadderss() {
        binding.RVaddress.setLayoutManager(new LinearLayoutManager(this));
        binding.RVaddress.setHasFixedSize(true);
        deliveryAddressAdapter = new DeliveryAddressAdapter(deliveryAddressList, this);
        binding.RVaddress.setAdapter(deliveryAddressAdapter);
    }

    public void init(){
        binding.include3.textTitleToolbar.setText("Địa chỉ");
        binding.include3.back.setOnClickListener(v -> finish());
    }
    public void initClicks(){
        binding.btnAddAddress.setOnClickListener(v -> startActivity(new Intent(this, FormAddressActivity.class)));
    }

    @Override
    public void OnClick(DeliveryAddress deliveryAddress) {
        Toast.makeText(this, deliveryAddress.getName(), Toast.LENGTH_SHORT).show();
    }
}