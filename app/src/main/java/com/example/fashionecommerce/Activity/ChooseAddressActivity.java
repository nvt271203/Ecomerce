package com.example.fashionecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fashionecommerce.AdapterAddress.LocalityAdapter;
import com.example.fashionecommerce.AdapterAddress.LocaltionAdapter;
import com.example.fashionecommerce.Helper.LocalityListHelper;
import com.example.fashionecommerce.Helper.LocaltionListHelper;
import com.example.fashionecommerce.databinding.ActivityChooseAddressBinding;
import com.example.fashionecommerce.model.Address;
import com.example.fashionecommerce.model.Locality;
import com.example.fashionecommerce.model.Localtion;

public class ChooseAddressActivity extends AppCompatActivity implements LocalityAdapter.OnClickListener, LocaltionAdapter.OnClickListener {
    private ActivityChooseAddressBinding binding;
    private Address address = new Address();
    private String idLocaltion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initClicks();
        configRVaddress();
    }

//    private void configRVaddress() {
//        binding.RVaddress.setLayoutManager(new LinearLayoutManager(this));
//        binding.RVaddress.setHasFixedSize(true);
//        adapter = new LocaltionAdapter(LocaltionListHelper.getLocaltionList(), this, this);
//        binding.RVaddress.setAdapter(adapter);
//    }
    private void configRVaddress() {
        if (address != null) {
            if (address.getLocation() == null){
                binding.RVaddress.setLayoutManager(new LinearLayoutManager(this));
                binding.RVaddress.setHasFixedSize(true);
                LocaltionAdapter adapter = new LocaltionAdapter(LocaltionListHelper.getLocaltionList(), this, this);
                binding.RVaddress.setAdapter(adapter);
            } else if (address.getLocation() != null && address.getLocality() == null) {
                binding.layoutAddressSelected.setVisibility(View.VISIBLE);
                binding.textLocation.setVisibility(View.VISIBLE);
                binding.textLocation.setText(address.getLocation());

                binding.RVaddress.setLayoutManager(new LinearLayoutManager(this));
                binding.RVaddress.setHasFixedSize(true);
                LocalityAdapter adapter = new LocalityAdapter(LocalityListHelper.districtList(idLocaltion), this);
                binding.RVaddress.setAdapter(adapter);
            }else if (address.getLocality() != null) {
                binding.layoutAddress.setVisibility(View.GONE);

                binding.textLocality.setVisibility(View.VISIBLE);
                binding.textLocality.setText(address.getLocality());
                binding.btnSave.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initClicks() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.textReset.setOnClickListener(v -> {
            address = new Address();
            binding.layoutAddress.setVisibility(View.VISIBLE);

            binding.textLocation.setVisibility(View.GONE);
            binding.textLocality.setVisibility(View.GONE);
            binding.btnSave.setVisibility(View.GONE);
            configRVaddress();
        });
        binding.btnSave.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("getAddress", address.getAddress());
            intent.putExtra("getLocation", address.getLocation());
            intent.putExtra("getLocality", address.getLocality());
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void OnClick(Localtion localtion) {
//        Toast.makeText(this, localtion.getNameLocaltion(), Toast.LENGTH_SHORT).show();
        idLocaltion = localtion.getIdLocaltion();
        address.setLocation(localtion.getNameLocaltion());


        configRVaddress();
    }

    @Override
    public void OnClick(Locality locality) {
//        Toast.makeText(this, locality.getNameLocality(), Toast.LENGTH_SHORT).show();
        address.setLocality(locality.getNameLocality());
        configRVaddress();
    }
}