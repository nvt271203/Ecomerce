package com.example.fashionecommerce.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityAddressBinding;
import com.example.fashionecommerce.databinding.ActivityFormAddressBinding;
import com.example.fashionecommerce.model.Address;
import com.example.fashionecommerce.model.DeliveryAddress;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormAddressActivity extends AppCompatActivity {
    private ActivityFormAddressBinding binding;
    private String getLocation;
    private String getLocality;
    private DeliveryAddress deliveryAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initClicks();
        init();
    }
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("getAddress")) {
//                        String getAddress = data.getStringExtra("getAddress");
                        getLocation = data.getStringExtra("getLocation");getLocality = data.getStringExtra("getLocality");
                        binding.btnAddAddress.setText(getLocation +" - "+getLocality);
                        binding.btnAddAddress.setTextColor(getColor(R.color.item_menu_selected));
                        binding.btnAddAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.check), null);
                    } else {
                        Toast.makeText(this, "Không nhận được địa chỉ.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Không thành công trong việc chọn địa chỉ.", Toast.LENGTH_SHORT).show();
                }
            }
    );
    private void initClicks() {
        binding.btnSave.setOnClickListener(v -> validate());
        binding.btnAddAddress.setOnClickListener(v ->{
            Intent intent = new Intent(this, ChooseAddressActivity.class);
            resultLauncher.launch(intent);
        });
        binding.include4.back.setOnClickListener(v -> finish());
    }

    private void validate() {
//        Tỉnh/Thành phố, Quận/Huyện, Phường/Xã
        String textName = binding.editName.getText().toString().trim();
        String textPhone = binding.editPhone.getText().toString().trim();
        String checkAddress = binding.btnAddAddress.getText().toString();
        String textStreet = binding.editStreet.getText().toString().trim();
        if (!textName.isEmpty()){
            if (!textPhone.isEmpty()){
                if (!checkAddress.equals("Tỉnh/Thành phố, Quận/Huyện, Phường/Xã")){
                    if (!textStreet.isEmpty()){
                        deliveryAddress = new DeliveryAddress();
                        deliveryAddress.setName(textName);
                        deliveryAddress.setPhone(textPhone);
                        deliveryAddress.setAddress(checkAddress);
                        deliveryAddress.setLocation(getLocation);
                        deliveryAddress.setLocality(getLocality);
                        deliveryAddress.setStreet(textStreet);
//                        deliveryAddress.storeData();

                        String idUsercurent = FirebaseHelper.getUIDpersonCurrent();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("user")
                                .child(idUsercurent)
                                .child("deliveryAddress")
                                .child(deliveryAddress.getId())
                                .setValue(deliveryAddress);
                        finish();
                        Toast.makeText(this, "Lưu thông tin người nhận thành công.", Toast.LENGTH_SHORT).show();
                    }else {
                        binding.editStreet.requestFocus();
                        binding.editStreet.setError("Số đường người nhận trống!");
                    }
                }else {
                    Toast.makeText(this, "Chưa chọn địa chỉ người nhận!", Toast.LENGTH_SHORT).show();
                }
            }else {
                binding.editPhone.requestFocus();
                binding.editPhone.setError("Số điện thoại người nhận trống!");
            }
        }else {
            binding.editName.requestFocus();
            binding.editName.setError("Tên người nhận trống!");
        }
    }

    private void init(){
        binding.include4.textTitleToolbar.setText("Thêm địa chỉ mới");
    }


}