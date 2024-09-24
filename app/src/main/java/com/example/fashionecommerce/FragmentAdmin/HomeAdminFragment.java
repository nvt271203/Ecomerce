package com.example.fashionecommerce.FragmentAdmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fashionecommerce.ActivityAdmin.CategoryAdminActivity;
import com.example.fashionecommerce.databinding.FragmentHomeAdminBinding;

public class HomeAdminFragment extends Fragment {
    private FragmentHomeAdminBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeAdminBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClicks();
    }
    private void initClicks() {
        binding.imgList.setOnClickListener(v ->{
            startActivity(new Intent(getContext(), CategoryAdminActivity.class));
        });
    }

}