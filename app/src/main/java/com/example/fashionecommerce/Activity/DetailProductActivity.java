package com.example.fashionecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.fashionecommerce.Adapter.ProductUserAdapter;
import com.example.fashionecommerce.Adapter.SliderProductAdapter;
import com.example.fashionecommerce.Activity.AuthencationActivity.LoginActivity;
import com.example.fashionecommerce.Database.ItemDB;
import com.example.fashionecommerce.Database.ItemOderDB;
import com.example.fashionecommerce.Database.ItemOderPurchasedDB;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityDetailProductBinding;
import com.example.fashionecommerce.databinding.DialogAuthencationBinding;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity implements ProductUserAdapter.OnClickListener {
    private ActivityDetailProductBinding binding;
    private DialogAuthencationBinding bindingAuth;
    private Dialog dialog;
    private Product productSelected;
    private List<Product> productList = new ArrayList<>();
    private ProductUserAdapter productUserAdapter;
    private ItemDB itemDB;
    private ItemOderDB itemOderDB;
    private ItemOderPurchasedDB itemOderParameterDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
        getExtra();
        configStoreProductRelated();
        configRVProductRelated();
        itemDB = new ItemDB(this);
        itemOderDB = new ItemOderDB(this);
        itemOderParameterDB = new ItemOderPurchasedDB(this);
    }

    private void configRVProductRelated() {
        binding.rvRelatedProducts.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false));
        binding.rvRelatedProducts.setHasFixedSize(true);
        productUserAdapter = new ProductUserAdapter(productList,R.layout.item_related_product_user, getApplicationContext(), this);
        binding.rvRelatedProducts.setAdapter(productUserAdapter);
    }
    @Override
    public void onClick(Product product) {

    }
    private void configStoreProductRelated() {
        FirebaseHelper.databaseReference("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    productList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        for (String category : productSelected.getIdsCategories()){
                            if (product.getIdsCategories().contains(category)){
                                if (!productList.contains(product) && !product.getId().equals(productSelected.getId())){
                                    productList.add(product);
                                }
                            }
                        }
                    }
                }
                Collections.reverse(productList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initClicks() {
        binding.include2.back.setOnClickListener(v -> finish());
        binding.imgAddCart.setOnClickListener(v -> {
                    if (!FirebaseHelper.checkUserCurrent()){
                        showDialogAuthencation();
                    }else {
                        addProductCart();
                    }
        });


//        binding.btnAddCart.setOnClickListener(v -> addProductCart());
    }
    private void showDialogAuthencation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogRetangle);
        bindingAuth = DialogAuthencationBinding.inflate(LayoutInflater.from(this));
        bindingAuth.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this,LoginActivity.class));
            dialog.dismiss();
        });
        bindingAuth.btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        builder.setView(bindingAuth.getRoot());
        dialog = builder.create();
        dialog.show();
    }
    private void addProductCart() {
        ItemOder itemOder = new ItemOder();
        itemOder.setIdProduct(productSelected.getId());
        itemOder.setQuantity(1);
        itemOder.setPrice(productSelected.getSellingPrice());
        itemOder.setIdUser(FirebaseHelper.getUIDpersonCurrent());
//        itemOder.setUrlImage(productSelected.getUrlsImages().get(0).getPathUrlSelected());
        itemOderDB.save(itemOder);
        itemDB.save(productSelected);
        Toast.makeText(this, "Đã thêm sản phẩm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
    }

    private void init(){
        binding.include2.textTitleToolbar.setText("");
    }
    private void getExtra() {
        productSelected = (Product) getIntent().getSerializableExtra("productSelected");
        congigImageProduct();
        configDataProduct();
    }

    private void congigImageProduct() {
        binding.sliderView.setSliderAdapter(new SliderProductAdapter(productSelected.getUrlsImages()));
        binding.sliderView.startAutoCycle();
        binding.sliderView.setScrollTimeInSec(3);
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
    }

    private void configDataProduct() {

        binding.textProduct.setText(productSelected.getName());
        binding.textDesc.setText(productSelected.getDesc());
    }


}