package com.example.fashionecommerce.FragmentUser;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fashionecommerce.Activity.AuthencationActivity.LoginActivity;
import com.example.fashionecommerce.Adapter.ChatAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.PushNotification.FCMsend;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.DialogAuthencationBinding;
import com.example.fashionecommerce.databinding.FragmentChatUserBinding;
import com.example.fashionecommerce.model.Admin;
import com.example.fashionecommerce.model.Chat;
import com.example.fashionecommerce.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatUserFragment extends Fragment {
    private FragmentChatUserBinding binding;
    private Admin admin;
    private DialogAuthencationBinding bindingAuth;
    private Dialog dialog;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatUserBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initClicks();
        checkUserCurrent();
    }

    private void init() {

    }

    private void checkUserCurrent() {
        if (!FirebaseHelper.checkUserCurrent()){
            showDialogAuthencation();
        }else {
            configStoreAdmin();
        }
    }
    private void showDialogAuthencation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogRetangle);
        bindingAuth = DialogAuthencationBinding.inflate(LayoutInflater.from(requireContext()));
        bindingAuth.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            dialog.dismiss();
        });
        bindingAuth.btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        builder.setView(bindingAuth.getRoot());
        dialog = builder.create();
        dialog.show();
    }
    private void configStoreAdmin() {
        FirebaseHelper.databaseReference("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        admin = dataSnapshot.getValue(Admin.class);
                        binding.textName.setText(admin.getName());
                    }
                }
                configStoreRVchat();
                configRVchat();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void configRVchat() {
        binding.RVchat.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.RVchat.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(chatList);
        binding.RVchat.setAdapter(chatAdapter);
    }

    private void configStoreRVchat() {
        FirebaseHelper.databaseReference("chat")
                .child(admin.getId())
                .child(FirebaseHelper.getUIDpersonCurrent())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (!chatList.isEmpty()) chatList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        chatList.add(chat);
                    }
                    chatAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initClicks() {
        binding.imgSend.setOnClickListener(view -> validateData());
    }
    private void validateData() {
        String textMessage = binding.editMessage.getText().toString().trim();
        if (!textMessage.isEmpty()){
            Chat chat = new Chat();
            chat.setMessage(textMessage);
            chat.setIdSender(FirebaseHelper.getUIDpersonCurrent());
            chat.setIdRecever(admin.getId());
            chat.storeSendAdmin(true);
            chatAdapter.notifyDataSetChanged();
            binding.editMessage.setText("");

            FirebaseHelper.databaseReference("user").child(FirebaseHelper.getUIDpersonCurrent())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String getNameSender = snapshot.getValue(User.class).getName();
//push notification
                            FCMsend.pushNotification(
                                    requireContext()
                                    , admin.getToken()
                                    , getNameSender
                                    , textMessage);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



        }else {
            binding.editMessage.requestFocus();
            binding.editMessage.setError("Bạn chưa nhập tin nhắn !");
        }
    }



}