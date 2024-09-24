package com.example.fashionecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fashionecommerce.Adapter.OptionsAddressAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityOptionsChooseAddressBinding;
import com.example.fashionecommerce.model.Address;
import com.example.fashionecommerce.model.DeliveryAddress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptionsChooseAddressActivity extends AppCompatActivity implements OptionsAddressAdapter.OnClickListener {
    private ActivityOptionsChooseAddressBinding binding;
    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();
    private OptionsAddressAdapter optionsAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOptionsChooseAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
        configDataOptionsAddress();
        configRVoptionsAddress();
    }
    private void configRVoptionsAddress() {
        binding.RVaddress.setLayoutManager(new LinearLayoutManager(this));
        binding.RVaddress.setHasFixedSize(true);
        optionsAddressAdapter = new OptionsAddressAdapter(deliveryAddressList, this);
        binding.RVaddress.setAdapter(optionsAddressAdapter);
    }

    @Override
    public void OnClick(DeliveryAddress deliveryAddress) {
        Toast.makeText(this, "Địa chỉ đã được cập nhập", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("addressDeliverySelected", deliveryAddress);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void configDataOptionsAddress() {
        if (FirebaseHelper.checkUserCurrent()){
            FirebaseHelper.databaseReference("user")
                    .child(FirebaseHelper.getUIDpersonCurrent())
                    .child("deliveryAddress")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            deliveryAddressList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                DeliveryAddress deliveryAddress = dataSnapshot.getValue(DeliveryAddress.class);
                                deliveryAddressList.add(deliveryAddress);
                            }
                            Collections.reverse(deliveryAddressList);
                            optionsAddressAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }


    private void initClicks() {
        binding.include3.back.setOnClickListener(v -> finish());
        binding.btnAddAddress.setOnClickListener(v -> startActivity(new Intent(this, FormAddressActivity.class)));
    }

    private void init(){
        binding.include3.textTitleToolbar.setText("Chọn địa chỉ nhận hàng");
    }


}