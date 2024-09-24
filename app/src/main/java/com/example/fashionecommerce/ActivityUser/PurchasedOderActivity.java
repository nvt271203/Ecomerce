package com.example.fashionecommerce.ActivityUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fashionecommerce.Adapter.PurchaOderAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.databinding.ActivityPurchasedOderBinding;
import com.example.fashionecommerce.model.Oder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PurchasedOderActivity extends AppCompatActivity {
    private ActivityPurchasedOderBinding binding;
    private List<Oder> oderList = new ArrayList<>();
    private PurchaOderAdapter purchaOderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchasedOderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClick();
        configDataPurchaseOder();


    }

    private void configDataPurchaseOder() {
        FirebaseHelper.databaseReference("userOder")
                .child(FirebaseHelper.getUIDpersonCurrent())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            oderList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                Oder oder = dataSnapshot.getValue(Oder.class);
                                oderList.add(oder);
                            }
                        }
                        Collections.reverse(oderList);
                        Log.i("kaasfsfd", "length "+ String.valueOf(oderList.size())) ;
                        Log.i("kaasfsfd", "length "+ oderList.get(0).getAddressDelivery().getName()) ;
                        binding.processBar.setVisibility(View.VISIBLE);
                        configRVpurchasedOder();
                        binding.processBar.setVisibility(View.GONE);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVpurchasedOder() {
        binding.RVpurchasedOder.setLayoutManager(new LinearLayoutManager(this));
        binding.RVpurchasedOder.setHasFixedSize(true);
        purchaOderAdapter = new PurchaOderAdapter(oderList, this);
        binding.RVpurchasedOder.setAdapter(purchaOderAdapter);

    }

    private void initClick() {
        binding.include7.back.setOnClickListener(v -> finish());
    }

    private void init(){
        binding.include7.textTitleToolbar.setText("Đơn hàng đã đặt");
    }
}