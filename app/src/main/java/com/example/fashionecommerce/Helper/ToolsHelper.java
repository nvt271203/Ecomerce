//package com.example.fashionecommerce.Helper;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import java.util.HashMap;
//
//public class ToolsHelper {
//    public static void getToken() {
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.d("MyToken", "Lấy Token thất bại", task.getException());
//                            return;
//                        }
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        Log.i("MyToken", token);
//                        setTokenUser(token);
//                    }
//
//                    private void setTokenUser(String token) {
//                        DatabaseReference instanceUser = FirebaseDatabase.getInstance().getReference("user").child(FirebaseHelper.getUIDpersonCurrent());
//                        instanceUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("token", token);
//                                instanceUser.updateChildren(hashMap);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                });
//    }
//    public static void getStoreIDperson(){
//        if (FirebaseHelper.checkUserCurrent()) {
//            FirebaseHelper.databaseReference("user")
//                    .child(FirebaseHelper.getUIDpersonCurrent())
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                setTokenPerson(FirebaseHelper.getUIDpersonCurrent(), );
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//        }
//    }
//
//    private static void setTokenPerson(String uiDpersonCurrent, boolean checkAdmin) {
//
//    }
//
//    private static void setTokenUser(String token) {
//        DatabaseReference instanceUser = FirebaseDatabase.getInstance().getReference("user").child(FirebaseHelper.getUIDpersonCurrent());
//        instanceUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("token", token);
//                instanceUser.updateChildren(hashMap);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    public static interface FirebaseCallback {
//        void onCallback(String idPerson);
//    }
//}
