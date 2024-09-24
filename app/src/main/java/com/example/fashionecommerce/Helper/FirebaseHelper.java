package com.example.fashionecommerce.Helper;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {
    public static DatabaseReference databaseReference(String str){
        return FirebaseDatabase.getInstance().getReference(str);
    }
    public static FirebaseAuth firebaseAuth(){
        return FirebaseAuth.getInstance();
    }
//    public static String getUIDpersonCurrent(){
//        return firebaseAuth().getCurrentUser().getUid();
//    }
public static String getUIDpersonCurrent() {
    if (firebaseAuth().getCurrentUser() != null) {
        return firebaseAuth().getCurrentUser().getUid();
    } else {
        // Xử lý khi người dùng chưa đăng nhập, có thể trả về null hoặc thông báo lỗi
        return null;  // Hoặc bạn có thể xử lý theo cách khác, tùy thuộc vào logic ứng dụng của bạn
    }
}
    public static StorageReference storageReference(String str){
        return FirebaseStorage.getInstance().getReference(str);
    }
    public static Boolean checkUserCurrent(){
        return firebaseAuth().getCurrentUser() != null;
    }
//    public static String getIDadmin(){
//        FirebaseHelper.databaseReference("admin").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
