package com.example.fashionecommerce.Activity.AuthencationActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityMainBinding;
import com.example.fashionecommerce.databinding.ActivityRegisterBinding;
import com.example.fashionecommerce.model.Admin;
import com.example.fashionecommerce.model.User;
import com.google.android.gms.tasks.OnFailureListener;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private User user;
    private Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void validate(View view) {
        String textName = binding.editNameUser.getText().toString().trim();
        String textPhone = binding.editPhone.getText().toString().trim();
        String textEmail = binding.editEmail.getText().toString().trim();
        String textPass = binding.editPassword.getText().toString().trim();
        String textConfirmPass = binding.editConfirmPassword.getText().toString().trim();

        if (!textName.isEmpty()){
            if (!textPhone.isEmpty()){
                if (!textEmail.isEmpty()){
                    if (!textPass.isEmpty()){
                        if (!textConfirmPass.isEmpty()){
                            binding.progressBar.setVisibility(View.VISIBLE);
                            user = new User(textName, textPhone, textEmail, textPass);
                            register(user);

                        }else{
                            binding.editConfirmPassword.requestFocus();
                            binding.editConfirmPassword.setError("Mật khẩu không khớp!");
                            binding.editConfirmPassword.setText("");

                        }
                    }else{
                        binding.editPassword.requestFocus();
                        binding.editPassword.setError("Số điện thoại trống!");
                    }
                }else{
                    binding.editEmail.requestFocus();
                    binding.editEmail.setError("Số điện thoại trống!");
                }
            }else{
                binding.editPhone.requestFocus();
                binding.editPhone.setError("Số điện thoại trống!");
            }
        }else{
            binding.editNameUser.requestFocus();
            binding.editNameUser.setError("Tên trống!");
        }

    }

    private void register(User user) {
        FirebaseHelper.firebaseAuth().createUserWithEmailAndPassword(user.getEmail(), user.getPass())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String id = task.getResult().getUser().getUid();
                        user.setId(id);
                        user.storeData();
                        Intent intent = new Intent();
                        intent.putExtra("email", user.getEmail());
                        setResult(RESULT_OK, intent);
                        finish();
                        Toast.makeText(this, "Đăng kí thành công.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Đăng kí thất bại." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);

                });

    }
}