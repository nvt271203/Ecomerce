package com.example.fashionecommerce.FragmentAdmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fashionecommerce.ActivityAdmin.BannerAdminActivity;
import com.example.fashionecommerce.Activity.AuthencationActivity.LoginActivity;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.databinding.FragmentProfileAdminBinding;
import com.example.fashionecommerce.model.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileAdminFragment extends Fragment {
    private FragmentProfileAdminBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileAdminBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        checkPeson();
    }

    private void checkPeson() {
        if (FirebaseHelper.checkUserCurrent()){
            getNamePerson();
        }else {
            binding.textUser.setText("Khách");
        }
    }

    private void getNamePerson() {
        FirebaseHelper.databaseReference("admin")
                .child(FirebaseHelper.getUIDpersonCurrent())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String namePerson = snapshot.getValue(Admin.class).getName();
                        binding.textUser.setText(namePerson);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClicks();
    }

    private void initClicks() {
        binding.btnLogout.setOnClickListener(v -> {
            FirebaseHelper.firebaseAuth().signOut();
            Toast.makeText(requireContext(), "Đăng xuất thành công.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });
        binding.layoutSliderBanner.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), BannerAdminActivity.class)));
    }
}