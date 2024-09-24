package com.example.fashionecommerce.Activity;

import android.os.Bundle;

import com.example.fashionecommerce.Adapter.ChatAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.PushNotification.FCMsend;
import com.example.fashionecommerce.model.Chat;
import com.example.fashionecommerce.model.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fashionecommerce.databinding.ActivityChatBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private Bundle bundle;
    private String idDataReceiver = null;
    private User userReceiver;
    private ChatAdapter chatUserAdapter;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bundle = getIntent().getExtras();
        idDataReceiver = bundle.getString("idUserReceiver");
        init();
        initClicks();
    }
    private void init(){
        getDataBundel();
        configRVmessage();
        configStoreMessage();
    }

    private void configStoreMessage() {
        FirebaseHelper.databaseReference("chat")
                .child("cJLkDRhb5PdlofqIAAAhXWUrjby1")
                .child(idDataReceiver)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (!chatList.isEmpty()) chatList.clear();
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                Chat chat = dataSnapshot.getValue(Chat.class);
                                chatList.add(chat);
                            }
                        }
                        chatUserAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVmessage() {
        binding.RVchat.setLayoutManager(new LinearLayoutManager(this));
        binding.RVchat.setHasFixedSize(true);
        chatUserAdapter = new ChatAdapter(chatList);
        binding.RVchat.setAdapter(chatUserAdapter);
    }

    private void getDataBundel() {
        if (idDataReceiver != null){
            configStoreUser();
            configRVmessage();
        }
    }

    private void configStoreUser() {
        FirebaseHelper.databaseReference("user").child(idDataReceiver).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userReceiver = snapshot.getValue(User.class);
                    binding.textName.setText(userReceiver.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initClicks(){
        binding.imgBack.setOnClickListener(view -> finish());
        binding.imgSend.setOnClickListener(v -> validateData());
    }

    private void validateData() {
        String textMessage = binding.editMessage.getText().toString().trim();
        if (!textMessage.isEmpty()){
            Chat chat = new Chat();
            chat.setMessage(textMessage);
            chat.setIdRecever(idDataReceiver);
            chat.setIdSender(FirebaseHelper.getUIDpersonCurrent());
            chat.storeSendUser(true);
            chatUserAdapter.notifyDataSetChanged();

            FirebaseHelper.databaseReference("user").child(FirebaseHelper.getUIDpersonCurrent())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String getNameSender = snapshot.getValue(User.class).getName();
//push notification
                            FCMsend.pushNotification(
                                    getApplicationContext()
                                    , userReceiver.getToken()
                                    , getNameSender
                                    , textMessage);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            binding.editMessage.setText("");
        }else {
            binding.editMessage.requestFocus();
            binding.editMessage.setError("Bạn chưa nhập tin nhắn !");
        }
    }


}