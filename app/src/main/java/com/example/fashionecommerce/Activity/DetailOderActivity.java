package com.example.fashionecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.fashionecommerce.Adapter.ParameterAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityDetailOderBinding;
import com.example.fashionecommerce.model.DeliveryAddress;
import com.example.fashionecommerce.model.Oder;
import com.example.fashionecommerce.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class DetailOderActivity extends AppCompatActivity {
    private ActivityDetailOderBinding binding;
    private Oder oderSelected;
    private DeliveryAddress deliveryAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailOderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
        getExtra();
    }
    private void getExtra() {
        oderSelected = (Oder) getIntent().getSerializableExtra("OderSelected");
//        configStoreAddressDelivery();
//        configParametersOder();
        setStoreOder(oderSelected);
    }

    private void setStoreOder(Oder oderSelected) {
        binding.textName.setText(oderSelected.getAddressDelivery().getName());
        binding.textPhone.setText(oderSelected.getAddressDelivery().getPhone().toString());
        binding.textAddress.setText(oderSelected.getAddressDelivery().getLocation() + " - " +oderSelected.getAddressDelivery().getLocality());
        binding.textStreet.setText(oderSelected.getAddressDelivery().getStreet());

    }

//    private void configStoreAddressDelivery() {
//            FirebaseHelper.databaseReference("")
//                    .child(FirebaseHelper.getUIDpersonCurrent())
//                    .child("deliveryAddress")
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            deliveryAddressList.clear();
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                DeliveryAddress deliveryAddress = dataSnapshot.getValue(DeliveryAddress.class);
//                                deliveryAddressList.add(deliveryAddress);
//                            }
//                            Collections.reverse(deliveryAddressList);
//                            setStoreAddressDelivery();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//    }
//    private void configParametersOder() {
//        binding.RVparametersOder.setLayoutManager(new LinearLayoutManager(this));
//        binding.RVparametersOder.setHasFixedSize(true);
//        parameterAdapter = new ParameterAdapter(getItemOderListSelected, this);
//        binding.RVparametersOder.setAdapter(parameterAdapter);
//    }


    private void initClicks() {
        binding.include5.back.setOnClickListener(v -> finish());
    }

    private void init(){
        binding.include5.textTitleToolbar.setText("Chi tiết đơn hàng");
    }
}