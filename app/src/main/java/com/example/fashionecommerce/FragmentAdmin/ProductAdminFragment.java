package com.example.fashionecommerce.FragmentAdmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fashionecommerce.ActivityAdmin.ProductAdminActivity;
import com.example.fashionecommerce.Adapter.ProductAdminAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.DialogFormCategoryBinding;
import com.example.fashionecommerce.databinding.DialogOptionsProductAdminBinding;
import com.example.fashionecommerce.databinding.FragmentProductAdminBinding;
import com.example.fashionecommerce.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductAdminFragment extends Fragment implements ProductAdminAdapter.OnClickListener {
    private FragmentProductAdminBinding binding;
    private ProductAdminAdapter productAdminAdapter;
    private List<Product> productList = new ArrayList<>();
    private AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductAdminBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClicks();
//        configRVproducts();
    }

    @Override
    public void onStart() {
        super.onStart();
        configDataProducts();
    }

    public void configDataProducts() {
        FirebaseHelper.databaseReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            productList.clear();
                            binding.progressBar.setVisibility(View.VISIBLE);
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                Product product = dataSnapshot.getValue(Product.class);
                                productList.add(product);
                            }
                        }
                        Collections.reverse(productList);
                        configRVproducts();
//                        productAdminAdapter.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void configRVproducts() {
        binding.recyclerViewProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewProduct.setHasFixedSize(true);
        productAdminAdapter = new ProductAdminAdapter(productList, getContext(), this);
        binding.recyclerViewProduct.setAdapter(productAdminAdapter);
    }
    @Override
    public void onClick(Product product) {
        showDialogOptions(product);
    }

    private void showDialogOptions(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogRetangle);
        DialogOptionsProductAdminBinding bindingOptions = DialogOptionsProductAdminBinding.inflate(LayoutInflater.from(requireContext()));
        for (int i =0; i<product.getUrlsImages().size(); i++){
            if (product.getUrlsImages().get(i).getIndex() == 0){
                Picasso.get().load(product.getUrlsImages().get(i).getPathUrlSelected()).into(bindingOptions.imgProduct);
            }
        }
        bindingOptions.textTitle.setText(product.getName());
        bindingOptions.checkBoxDraft.setChecked(false);
        bindingOptions.imgClose.setOnClickListener(v -> {
            dialog.dismiss();
        });
//        Kiểm tra xem trạng thái sản phẩm đã tồn tại dạng nháp chưa, nếu đã tồn tại thì check nó
        if (product.isStatusDraft()){
            bindingOptions.checkBoxDraft.setChecked(true);
        }
        bindingOptions.checkBoxDraft.setOnCheckedChangeListener((buttonCheckDraft, isChecked) -> {
            product.setStatusDraft(buttonCheckDraft.isChecked());
//            Giá trị false này ko có nghĩa lí gì, có thể nó sẽ giúp ích cho sau này :))
            product.storeData(false);
            if (product.isStatusDraft()){
                Toast.makeText(getContext(), "Sản phẩm đã đc lưu dưới dạng nháp.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "Sản phẩm đã đc phục hồi.", Toast.LENGTH_SHORT).show();

            }
            dialog.dismiss();
        });
        bindingOptions.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProductAdminActivity.class);
            intent.putExtra("productSelected", product);
            startActivity(intent);
            dialog.dismiss();
//            dialog.dismiss();
//            Toast.makeText(getContext(), "Sản phẩm đã đc chỉnh sửa.", Toast.LENGTH_SHORT).show();
        });
        bindingOptions.btnDeleteProduct.setOnClickListener(v ->{
            product.delete();
            dialog.dismiss();
            Toast.makeText(getContext(), "Sản phẩm đã đc xóa.", Toast.LENGTH_SHORT).show();
        });
        builder.setView(bindingOptions.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private void initClicks() {
        binding.btnAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProductAdminActivity.class));
        });
    }


}