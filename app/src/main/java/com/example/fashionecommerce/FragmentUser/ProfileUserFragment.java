package com.example.fashionecommerce.FragmentUser;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fashionecommerce.Activity.AddressActivity;
import com.example.fashionecommerce.ActivityUser.PurchasedOderActivity;
import com.example.fashionecommerce.ActivityUser.StatusOderActivity;
import com.example.fashionecommerce.ActivityUser.FingerprintActivity;
import com.example.fashionecommerce.Activity.AuthencationActivity.LoginActivity;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.DialogAuthencationBinding;
import com.example.fashionecommerce.databinding.FragmentProfileUserBinding;
import com.example.fashionecommerce.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileUserFragment extends Fragment {

    private FragmentProfileUserBinding binding;
    private DialogAuthencationBinding bindingAuth;

    private Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileUserBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClicks();
        getDataUser();
    }

    private void getDataUser() {
        if (FirebaseHelper.checkUserCurrent()){
            FirebaseHelper.databaseReference("user")
                    .child(FirebaseHelper.getUIDpersonCurrent())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                binding.textUser.setText(user.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }else {
            binding.textUser.setText("Khách");
        }
    }


    private void initClicks() {
        binding.constraintAddress.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddressActivity.class));
        });
        binding.layoutFigetprint.setOnClickListener(v -> {
            confirmActivity("actionFirgetprint");
        });
        binding.layoutStatusOder.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StatusOderActivity.class));
        });
        binding.constraintPurchasedOder.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), PurchasedOderActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> {
            FirebaseHelper.firebaseAuth().signOut();
            Toast.makeText(requireContext(), "Đăng xuất thành công.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });
    }

    private void confirmActivity(String action) {
        if (FirebaseHelper.checkUserCurrent()){
            switch (action){
                case "actionFirgetprint": {
                    startActivity(new Intent(requireContext(), FingerprintActivity.class));
                    break;
                }
            }
        }else {
            showDialogAuthencation();
        }
    }


//    private BiometricPrompt authFingerprints() {
//        Executor executor = ContextCompat.getMainExecutor(requireContext());
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//            BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
//                @Override
//                public void onAuthenticationError(int errorCode, CharSequence errString) {
//                    super.onAuthenticationError(errorCode, errString);
//                    notifyMessage(errString.toString());
//                }
//
//                @Override
//                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//                    super.onAuthenticationHelp(helpCode, helpString);
//
//                }
//
//                @Override
//                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
//                    super.onAuthenticationSucceeded(result);
//                    notifyMessage("Xác thực thành công.");
//    //              Intent intent =
//
//                }
//
//                @Override
//                public void onAuthenticationFailed() {
//                    super.onAuthenticationFailed();
//                    notifyMessage("Xác thực thất bại!");
//                }
//            };
//            BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(requireContext(), executor,callback);
//            return biometricPrompt;
//        }
//
//    }
    private void notifyMessage(String message){}

    private void showDialogAuthencation() {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
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
}