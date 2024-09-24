package com.example.fashionecommerce.Activity.AuthencationActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.fashionecommerce.ActivityAdmin.MainAdminActivity;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.ActivityUser.MainUserActivity;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityLoginBinding;
import com.example.fashionecommerce.databinding.DialogFingerprintBinding;
import com.example.fashionecommerce.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DialogFingerprintBinding bindingFingerprint;
    private AlertDialog dialog;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
    }
    private void initClicks() {
//        binding.imgFingerprintlogin.setOnClickListener(v ->
//             showDialogConfirm());

        binding.include.back.setOnClickListener(v -> finish());
        binding.textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            resultLauncher.launch(intent);
        });

    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        bindingFingerprint = DialogFingerprintBinding.inflate(LayoutInflater.from(this));
        bindingFingerprint.btnLogin.setOnClickListener(v -> {
            dialog.dismiss();
        });
        bindingFingerprint.btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        builder.setView(bindingFingerprint.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    String email = result.getData().getStringExtra("email");
                    binding.editEmail.setText(email);
                }
            }
    );
    public void init(){
        binding.include.textTitleToolbar.setText("Đăng nhập");
    }

    public void validate(View view){
        String textEmail = binding.editEmail.getText().toString().trim();
        String textPass = binding.editPassword.getText().toString().trim();
        if (!textEmail.isEmpty()){
            if (!textPass.isEmpty()){
               binding.progressBar.setVisibility(View.VISIBLE);
               user = new User();
               user.setEmail(textEmail);
               user.setPass(textPass);
               login(user);
            }else{
                binding.editPassword.requestFocus();
                binding.editPassword.setError("Mật khẩu trống!");
            }
        }else{
            binding.editEmail.requestFocus();
            binding.editEmail.setError("Email trống!");
        }

    }

    private void login(User user) {
        FirebaseHelper.firebaseAuth().signInWithEmailAndPassword(user.getEmail(), user.getPass())
                .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idPerson = task.getResult().getUser().getUid().toString();
                        checkPerson(idPerson);
                        Toast.makeText(this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Đăng nhập thất bại."+ task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);
                });
    }

    private void checkPerson(String idPerson) {
        FirebaseHelper.databaseReference("user")
                .child(idPerson)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){ // User
                            startActivity(new Intent(getBaseContext(), MainUserActivity.class));
                        }else {  // Admin
                            startActivity(new Intent(getBaseContext(), MainAdminActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

    }
}

