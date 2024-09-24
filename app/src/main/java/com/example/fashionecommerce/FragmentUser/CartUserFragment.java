package com.example.fashionecommerce.FragmentUser;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fashionecommerce.ActivityUser.ParametersOderActivity;
import com.example.fashionecommerce.Adapter.CartAdapter;
import com.example.fashionecommerce.Activity.AuthencationActivity.LoginActivity;
import com.example.fashionecommerce.Database.ItemDB;
import com.example.fashionecommerce.Database.ItemOderDB;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.DialogAuthencationBinding;
import com.example.fashionecommerce.databinding.DialogDeleteFormCategoryBinding;
import com.example.fashionecommerce.databinding.FragmentCartUserBinding;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.ItemOderPuchased;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartUserFragment extends Fragment implements CartAdapter.OnClickListener {
    private FragmentCartUserBinding binding;
    private DialogAuthencationBinding bindingAuth;
    private List<ItemOder> itemOderList = new ArrayList<>();
    private List<ItemOder> itemOderListSelected = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ItemDB itemDB;
    private AlertDialog dialog;
    private ItemOderDB itemOderDB;
    private ItemOderPuchased itemOderPuchased;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentCartUserBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemDB = new ItemDB(requireContext());
        itemOderDB = new ItemOderDB(requireContext());
//        itemOderList.addAll(itemOderDB.getList());
        checkUserCurrent();
//        Add all oders of user
        List<ItemOder> oderList = itemOderDB.getListOderUser();
        if (oderList != null) {
            itemOderList.addAll(oderList);
        }
        initClicks();

    }


    private void checkUserCurrent() {
        if (!FirebaseHelper.checkUserCurrent()){
            showDialogAuthencation();
        }else {
            configRVproductCart();
        }
    }
    private void showDialogAuthencation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogRetangle);
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
    private void initClicks() {
        binding.btnConfirmOder.setOnClickListener(v -> {
            if (itemOderDB.getTotalPriceProductsSelected(itemOderListSelected) != 0){
                Intent intent;
                if (FirebaseHelper.checkUserCurrent()){
                    intent = new Intent(requireContext(), ParametersOderActivity.class);
                    intent.putExtra("dataListOderSelected", (Serializable) itemOderListSelected);

                    resultLauncher.launch(intent);
//                intent = new Intent(requireContext(), ParametersOderActivity.class);
                }else {
                    intent = new Intent(requireContext(), LoginActivity.class);
                    startActivity(intent);

                }
            }else {
                Toast.makeText(requireContext(), "Bạn chưa chọn sản phẩm nào để mua!", Toast.LENGTH_SHORT).show();
            }

        });

    }

//    TRường hợp khi người dùng mua sản phẩm và chọn quay lại giỏ hàng. reset lại sản phẩm vừa mua
    @SuppressLint("NotifyDataSetChanged")
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    Intent data = result.getData();
                    if (data != null && data.hasExtra("getReturnItemOderList")) {
//                        Toast.makeText(requireContext(), "hi", Toast.LENGTH_SHORT).show();

                        itemOderList.clear();
                        itemOderList.addAll(itemOderDB.getListOderUser());
                        cartAdapter = new CartAdapter(itemOderList, itemOderDB, requireContext(), this);

                        itemOderListSelected = new ArrayList<>();
                        configTotalDataCart();
                        binding.btnConfirmOder.setText(getString(R.string.total_count, 0));

                        binding.rvProcutCart.setAdapter(cartAdapter);
//                       List<ItemOder> getitemOderList = (List<ItemOder>) data.getSerializableExtra("getReturnItemOderList");
//                        cartAdapter = new CartAdapter(getitemOderList, itemOderDB, requireContext(), this);
//                        binding.rvProcutCart.setAdapter(cartAdapter);
                    } else {
//                        Toast.makeText(this, "Không nhận được địa chỉ.", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(this, "Không thành công trong việc chọn địa chỉ.", Toast.LENGTH_SHORT).show();
                }
            }
    );
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void configRVproductCart() {
        Collections.reverse(itemOderList);
        binding.rvProcutCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProcutCart.setHasFixedSize(true);
        cartAdapter = new CartAdapter(itemOderList, itemOderDB, requireContext(), this);
        binding.rvProcutCart.setAdapter(cartAdapter);

        configTotalDataCart();
    }

    public void configTotalDataCart() {
//        binding.textTotalPriceProducts.setText(getString(R.string.total_price, GetMaskHelper.getValue(itemOderDB.getTotalPriceProducts())));
        binding.textTotalPriceProducts.setText(getString(R.string.total_price, GetMaskHelper.getValue(itemOderDB.getTotalPriceProductsSelected(itemOderListSelected))));
    }



    private void showDialogDeleteProduct(Product product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);

        DialogDeleteFormCategoryBinding dialogBinding = DialogDeleteFormCategoryBinding.inflate(LayoutInflater.from(requireContext()));
        Picasso.get().load(product.getUrlsImages().get(0).getPathUrlSelected()).into(dialogBinding.imgCategory);

        dialogBinding.textTitle.setText(product.getName());
        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.btnRemove.setOnClickListener(v -> {
            removeProductCart(position);
            Toast.makeText(requireContext(), "Sản phẩm đã được xóa.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        builder.setView(dialogBinding.getRoot());
        dialog = builder.create();
        dialog.show();

    }

    private void removeProductCart(int position) {
        ItemOder itemOder = itemOderList.get(position);
        itemOderList.remove(itemOder);
        itemOderDB.remove(itemOder);
        itemDB.remove(itemOder);
//      Cập nhập lại danh sách Giỏ hàng, khi có 1 sản phẩm đc xóa (tức reload lại danh sách k có sản phẩm vừa xóa)
        cartAdapter.notifyDataSetChanged();
//      Cập nhập lại tính tổng giá trị tự động của các phần tử trong giỏ hàng
        configTotalDataCart();

    }




    @Override
    public void OnClick(int position, String action) {

        int idProduct = itemOderList.get(position).getId();
        Product product = itemOderDB.getProductIdInt(idProduct);

        switch (action){
            case "increase" :
            case "decrease" :
                congigUpdateQuantity(position, action);
                break;
            case "delete" :
                showDialogDeleteProduct(product, position);
//                break;
//            case "parameterOder" :
//                showDialogDeleteProduct(product, position);
                break;
        }
    }

            private void congigUpdateQuantity(int position, String action) {
                ItemOder itemOder = itemOderList.get(position);
                if (action.equals("increase")){  // +
                    itemOder.setQuantity(itemOder.getQuantity() + 1);
                    itemOderDB.update(itemOder);
                    itemOderList.set(position, itemOder);
                }else {  // -
                    if (itemOder.getQuantity() > 1){
                        itemOder.setQuantity(itemOder.getQuantity() - 1);
                        itemOderDB.update(itemOder);
                        itemOderList.set(position, itemOder);
                    }
                }
                cartAdapter.notifyDataSetChanged();
                configTotalDataCart();
            }

//  Overide Function Checkbox  -> Lấy ra tổng giá trị đơn hàng
    @Override
    public void OnClick(List<ItemOder> itemOderListSelected) {
        this.itemOderListSelected = itemOderListSelected;
        // Đảm bảo rằng hàm GetMaskHelper.getValue trả về một số nguyên.
        int itemCount = Integer.parseInt(GetMaskHelper.getValue(itemOderDB.getCountlPriceProductsSeleted(itemOderListSelected)));
// Sử dụng giá trị này để đặt văn bản cho nút xác nhận đơn hàng.
        binding.btnConfirmOder.setText(getString(R.string.total_count, itemCount));
        configTotalDataCart();
    }
}