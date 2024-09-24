package com.example.fashionecommerce.FragmentAdmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fashionecommerce.Activity.ChatActivity;
import com.example.fashionecommerce.Adapter.UserAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.databinding.FragmentMessageAdminBinding;
import com.example.fashionecommerce.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageAdminFragment extends Fragment implements UserAdapter.OnClickListener {
    public FragmentMessageAdminBinding binding;
    public List<User> userList = new ArrayList<>();
    public UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageAdminBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        configDataRVchat();
        configRVchat();

    }

    private void configDataRVchat() {
        FirebaseHelper.databaseReference("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        userList.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void configRVchat(){
        binding.RVchatUser.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.RVchatUser.setHasFixedSize(true);
        userAdapter = new UserAdapter(userList, requireContext(), this);
        binding.RVchatUser.setAdapter(userAdapter);

    }

    @Override
    public void onClick(String idReceiver) {
        Intent intent = new Intent(requireContext(), ChatActivity.class);
        intent.putExtra("idUserReceiver",idReceiver);
        startActivity(intent);
    }
}