package com.example.fashionecommerce.ActivityUser;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityFingerprintBinding;

import java.util.concurrent.Executor;

public class FingerprintActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101010;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    private ActivityFingerprintBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFingerprintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
        configBiometric();
        configExecutor();
    }
    public void init() {
        binding.include6.textTitleToolbar.setText("Thiết lập vân tay");
    }
    private void initClicks() {
        binding.include6.back.setOnClickListener(v -> finish());
    }

    private void configExecutor() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.i("resultFingerprint", result.toString());
                finish();
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.

        binding.imgFingerprint.setOnClickListener(v -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }

    private void configBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "Cảm biến vân tay không tồn tại!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Cảm biến vân tay không hoạt động!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }
    }

}