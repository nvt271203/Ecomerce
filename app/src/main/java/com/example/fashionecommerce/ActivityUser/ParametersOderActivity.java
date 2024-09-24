package com.example.fashionecommerce.ActivityUser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.fashionecommerce.Activity.OptionsChooseAddressActivity;
import com.example.fashionecommerce.Adapter.ParameterAdapter;
import com.example.fashionecommerce.Database.ItemDB;
import com.example.fashionecommerce.Database.ItemOderDB;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityParametersOderBinding;
import com.example.fashionecommerce.databinding.DialogOderSuccessBinding;
import com.example.fashionecommerce.model.DeliveryAddress;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.ItemOderPuchased;
import com.example.fashionecommerce.model.Oder;
import com.example.fashionecommerce.model.StatusOder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParametersOderActivity extends AppCompatActivity {
    private ActivityParametersOderBinding binding;
    private DialogOderSuccessBinding bindingOder;
    private Dialog dialog;
    private Bundle bundle;
    private ItemDB itemDB;
    private ItemOderDB itemOderDB;
    private Oder getBunlde_Oder;
    private ItemOderPuchased itemOderPuchased;
    private List<ItemOder> itemOderList = new ArrayList<>();
    private List<ItemOder> getItemOderListSelected = new ArrayList<>();
    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();
    private ParameterAdapter parameterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParametersOderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bundle = getIntent().getExtras();

        itemDB = new ItemDB(this);
        itemOderDB = new ItemOderDB(this);
        getDataBundle();

        init();



//        Toast.makeText(this, "length: " + itemOderListSelected.size(), Toast.LENGTH_SHORT).show();
        initClicks();
        configStoreAddressDelivery();

//        configAddressDelivery();
        configParametersOder();
    }

//    private void configAddressDelivery() {
//
//    }

    private void getDataBundle() {
        if (bundle.getSerializable("dataListOderSelected")!= null){
            getItemOderListSelected = (List<ItemOder>) bundle.getSerializable("dataListOderSelected");
        }
        if (bundle.getSerializable("getPurchasedOder")!= null){
            getBunlde_Oder = (Oder) bundle.getSerializable("getPurchasedOder");
            setDataOder();
        }
    }

    private void setDataOder() {


        binding.btnAddCart.setVisibility(View.GONE);
        binding.btnSelectAddress.setVisibility(View.GONE);
        binding.btnMethodPay.setVisibility(View.GONE);

    }

    private void configStoreAddressDelivery() {
        if (getBunlde_Oder != null){    // oder ko null tức là trạng thái chi tiết của đơn hàng đã mua
            deliveryAddressList.add(getBunlde_Oder.getAddressDelivery());
            setStoreAddressDelivery();
        }else {                         // oder null tức là đang ở trạng thái chi tiết của giỏ hàng
            if (FirebaseHelper.checkUserCurrent()){
                FirebaseHelper.databaseReference("user")
                        .child(FirebaseHelper.getUIDpersonCurrent())
                        .child("deliveryAddress")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                deliveryAddressList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    DeliveryAddress deliveryAddress = dataSnapshot.getValue(DeliveryAddress.class);
                                    deliveryAddressList.add(deliveryAddress);
                                }
                                Collections.reverse(deliveryAddressList);
                                setStoreAddressDelivery();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

        }

    }

    private void setStoreAddressDelivery() {



        if (deliveryAddressList != null && !deliveryAddressList.isEmpty()) {
            DeliveryAddress deliveryAddress = deliveryAddressList.get(0);
            binding.textName.setText(deliveryAddress.getName());
            binding.textPhone.setText(deliveryAddress.getPhone());
            binding.textAddress.setText(deliveryAddress.getAddress());
            binding.textStreet.setText(deliveryAddress.getStreet());
        } else {
            // Xử lý trường hợp danh sách địa chỉ giao hàng rỗng
        }
    }


    private void configParametersOder() {
//        Get intent dataList
//        getDataBundle();
        binding.RVparametersOder.setLayoutManager(new LinearLayoutManager(this));
        binding.RVparametersOder.setHasFixedSize(true);
        if (!getItemOderListSelected.isEmpty()){   //Lấy dữ liệu từ giỏ hàng
            parameterAdapter = new ParameterAdapter(getItemOderListSelected, this);

        }else if (!getBunlde_Oder.getItemOderList().isEmpty()){      //Lấy dữ liệu từ hàng đã mua
            parameterAdapter = new ParameterAdapter(getBunlde_Oder.getItemOderList(), this);

        }

        binding.RVparametersOder.setAdapter(parameterAdapter);

    }
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("addressDeliverySelected")) {
                        DeliveryAddress deliveryAddress = (DeliveryAddress) data.getSerializableExtra("addressDeliverySelected");
                        deliveryAddressList.add(0, deliveryAddress);
                        setStoreAddressDelivery();
                    }
//                    else {
//                        Toast.makeText(this, "Không nhận được địa chỉ giao hàng.", Toast.LENGTH_SHORT).show();
//                    }
                }
//                else {
//                    Toast.makeText(this, "Chọn địa chỉ không thành công.", Toast.LENGTH_SHORT).show();
//                }
            }
    );



    private void initClicks() {
        binding.include5.back.setOnClickListener(v -> finish());
        
        binding.btnSelectAddress.setOnClickListener(v ->
//                startActivity(new Intent(this, OptionsChooseAddressActivity.class)));
                resultLauncher.launch(new Intent(this, OptionsChooseAddressActivity.class)));
        
        binding.btnAddCart.setOnClickListener(v -> validate());
    }
        
    public void init(){
        binding.include5.textTitleToolbar.setText("Đơn hàng đã mua");
        if (getBunlde_Oder == null){

            binding.textTotalProducts.setText(getApplicationContext().getString(R.string.price, GetMaskHelper.getValue(itemOderDB.getTotalPriceProductsSelected(getItemOderListSelected))));

            binding.textTotalAllTransportationPrice.setText(getApplicationContext().getString(R.string.price, GetMaskHelper.getValue(itemOderDB.getTotalPriceProductsSelected(getItemOderListSelected))));

            binding.textButtonTotalPrice.setText(getApplicationContext().getString(R.string.price, GetMaskHelper.getValue(itemOderDB.getTotalPriceProductsSelected(getItemOderListSelected))));
        }else {

            // Total price all products
            int sum = 0;
            for (ItemOder itemOder: getBunlde_Oder.getItemOderList()) {
                sum += (int) itemOder.getPrice();
            }

            binding.textTotalProducts.setText(getApplicationContext().getString(R.string.price, GetMaskHelper.getValue(sum)));
            binding.textTotalAllTransportationPrice.setText(getApplicationContext().getString(R.string.price, GetMaskHelper.getValue(sum)));


            binding.textButtonTotalPrice.setVisibility(View.GONE);
        }

    }
    private void validate(){
        if (deliveryAddressList != null && !deliveryAddressList.isEmpty()) {
//            Toast.makeText(this, "Bạn đã nhấn vào nút mua hàng", Toast.LENGTH_SHORT).show();
            Oder oder = new Oder();
            oder.setIdUser(FirebaseHelper.getUIDpersonCurrent());
            oder.setAddressDelivery(deliveryAddressList.get(0));
            oder.setTotal(itemOderDB.getTotalPriceProductsSelected(getItemOderListSelected));
            oder.setPay("Thanh toán khi nhận hàng");
            oder.setItemOderList(getItemOderListSelected);
            oder.setStatus(StatusOder.WAIT);

            oder.store(true);

//            ItemOder itemOder = itemOderList.get(position);
//            itemOderList.remove(itemOder);
            for (ItemOder itemOder: getItemOderListSelected) {
                itemOderDB.remove(itemOder);
                itemDB.remove(itemOder);
            }


            showDialogPurchasedOder();

        } else {
            // Xử lý trường hợp danh sách địa chỉ giao hàng rỗng
            Toast.makeText(this, "Danh sách địa chỉ giao hàng rỗng. Vui lòng thêm địa chỉ giao hàng.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDialogPurchasedOder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogRetangle);
        bindingOder = DialogOderSuccessBinding.inflate(LayoutInflater.from(this));
        bindingOder.btnPurchasedOder.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, StatusOderActivity.class));

        });
        bindingOder.btnCart.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("getReturnItemOderList", itemOderList.addAll(itemOderDB.getListOderUser()));
            setResult(RESULT_OK, intent);
            finish();
        });
        builder.setView(bindingOder.getRoot());
        dialog = builder.create();
        dialog.show();
    }

}