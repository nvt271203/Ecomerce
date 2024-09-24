package com.example.fashionecommerce.ActivityAdmin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.fashionecommerce.Adapter.CategoryDialogAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityProductAdminBinding;
import com.example.fashionecommerce.databinding.DialogProductCategoriesBinding;
import com.example.fashionecommerce.model.Category;
import com.example.fashionecommerce.model.Product;
import com.example.fashionecommerce.model.UploadImage;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProductAdminActivity extends AppCompatActivity implements CategoryDialogAdapter.OnClickListener {
    private ActivityProductAdminBinding binding;
    private Product product;
    private AlertDialog dialog;
    private CategoryDialogAdapter categoryDialogAdapter;
//    Lưu trạng thái các id của doanh mục sản phẩm được chọn
    private List<String> idsCategoriesListSelected = new ArrayList<>();
//    Lưu các id danh mục sản phẩm sau khi đã chọn xọng.

    private List<String> dataIDsCategoriesListSelected = new ArrayList<>();
    private List<String> nameCategoriesListSelected = new ArrayList<>();
    private DialogProductCategoriesBinding bindingCategories;
    private boolean checkStatus = true;

    private List<UploadImage> uploadImageList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();
    private int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getDataStoreProduct();
        init();
        initClicks();
        configDataCategories();
    }
    private void init() {
        bindingCategories = DialogProductCategoriesBinding.inflate(LayoutInflater.from(this));
        binding.include.back.setOnClickListener(v -> finish());
        binding.include.textTitleToolbar.setText("Thêm sản phẩm");
    };

    private void initClicks() {
        binding.include.back.setOnClickListener(v -> finish());
        binding.include.textTitleToolbar.setText("Thêm sản phẩm");
        binding.imgProductFirst.setOnClickListener(v -> chooseImageProduct(0));
        binding.imgProductSecond.setOnClickListener(v -> chooseImageProduct(1));
        binding.imgProductThird.setOnClickListener(v -> chooseImageProduct(2));
        binding.btnAddCategories.setOnClickListener(v -> {
//            bindingCategories.progressBar.setVisibility(View.VISIBLE);
            showDialogCategories();
        });
        binding.btnSaveProduct.setOnClickListener(v -> validateData());
    }
    private void getDataStoreProduct() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = (Product) bundle.getSerializable("productSelected");
            configDataProductSelected();
        }
    }

    private void configDataProductSelected() {
        checkStatus = false;

        for (int i=0; i<product.getUrlsImages().size(); i++){
            Picasso.get().load(product.getUrlsImages().get(0).getPathUrlSelected()).into(binding.imgProductFirst);
            Picasso.get().load(product.getUrlsImages().get(1).getPathUrlSelected()).into(binding.imgProductSecond);
            Picasso.get().load(product.getUrlsImages().get(2).getPathUrlSelected()).into(binding.imgProductThird);
        }
//        for (UploadImage uploadImage: product.getUrlsImages()) {
//            uploadImageList.add(uploadImage);
//            Log.i("CheckImage:", uploadImage.getPathUrlSelected());
//        }
        binding.editTitle.setText(product.getName());
        binding.editDesc.setText(product.getDesc());
        binding.editCount.setText(String.valueOf(product.getCount()));
        binding.editWholesalePrice.setText(String.valueOf((int) product.getWholesalePrice()));
        binding.editSellingPrice.setText(String.valueOf((int) product.getSellingPrice()));
        binding.editSalePrice.setText(String.valueOf((int) product.getSalePrice()));
        configStoreCategories();

    }

    private void configStoreCategories() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("category")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            categoryList.clear();
                            for (DataSnapshot datasnaphot : snapshot.getChildren()){
                                categoryList.add(datasnaphot.getValue(Category.class));
                            }
                            configNameCategoriesList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configNameCategoriesList() {
        Log.i("checkCategories", String.valueOf(categoryList.size()));
        for (int i=0; i<categoryList.size(); i++){
            Log.i("checkCategories", categoryList.get(i).getName());
        }
        for (Category category : categoryList) {
            if (product.getIdsCategories().contains(category.getId())){
                dataIDsCategoriesListSelected.add(category.getId());
                nameCategoriesListSelected.add(category.getName());
            }
        }
        this.idsCategoriesListSelected.addAll(dataIDsCategoriesListSelected);
        for (String str: nameCategoriesListSelected) {
            Log.i("checkCategories","name: "+ str);
        }
        displayNameCategoriesListSelected();

    }

    private void configRVcategories() {
        bindingCategories.RVcategories.setLayoutManager(new LinearLayoutManager(this));
        bindingCategories.RVcategories.setHasFixedSize(true);
        categoryDialogAdapter = new CategoryDialogAdapter(idsCategoriesListSelected, categoryList, this);
        bindingCategories.RVcategories.setAdapter(categoryDialogAdapter);
    }
    @Override
    public void OnClick(Category category, List<String> idsGetCategoriesListSeclected) {
        if (idsGetCategoriesListSeclected.contains(category.getId())){
            nameCategoriesListSelected.add(category.getName());
        }else {
            nameCategoriesListSelected.remove(category.getName());
        }
        this.idsCategoriesListSelected = idsGetCategoriesListSeclected;
    }
    private void configDataCategories() {
        FirebaseHelper.databaseReference("category")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            categoryList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Category category = dataSnapshot.getValue(Category.class);
                                categoryList.add(category);
                            }
                        }

                        Log.i("Total", "Tổng danh mục sản phẩm: "+ categoryList.size());

                        configRVcategories();
                        categoryDialogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }



    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    private void showDialogCategories() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        bindingCategories = DialogProductCategoriesBinding.inflate(LayoutInflater.from(this));

        bindingCategories.btnAdd.setOnClickListener(v -> {
//            lưu trạng thái cuối cùng của toogle
            this.dataIDsCategoriesListSelected = idsCategoriesListSelected;
            displayNameCategoriesListSelected();
            Toast.makeText(this, "Danh mục đã được chọn.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        bindingCategories.btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
//        bindingCategories.progressBar.setVisibility(View.VISIBLE);
        configRVcategories();

//        bindingCategories.progressBar.setVisibility(View.GONE);

        builder.setView(bindingCategories.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private void displayNameCategoriesListSelected() {
        StringBuilder listTextCategories = new StringBuilder();
        for (String nameCategory: nameCategoriesListSelected) {
            listTextCategories.append(nameCategory).append(", ");
        }
        listTextCategories.delete(listTextCategories.length()-2, listTextCategories.length());
        binding.btnAddCategories.setText(listTextCategories);
        binding.btnAddCategories.setTextColor(getColor(R.color.item_menu_selected));
        binding.btnAddCategories.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.check), null);
    }

    private void validateData() {
        String textTitle = binding.editTitle.getText().toString().trim();
        String textDesc = binding.editDesc.getText().toString().trim();
        String textCount = binding.editCount.getText().toString().trim();
        double textWholesalePrice = binding.editWholesalePrice.getRawValue();
        double textSellingPrice = binding.editSellingPrice.getRawValue();
        double textSalePrice = binding.editSalePrice.getRawValue();
        if (!textTitle.isEmpty()){
            if (!textDesc.isEmpty()){
                if (!textCount.isEmpty()){
                    if (textWholesalePrice > 0){
                        if (textSellingPrice > 0){
                            if (textSalePrice > 0){
                                if (!dataIDsCategoriesListSelected.isEmpty()){
                                    if (product == null ) product = new Product();
                                    product.setName(textTitle);
                                    product.setDesc(textDesc);
                                    product.setWholesalePrice(textWholesalePrice);
                                    product.setSellingPrice(textSellingPrice);
                                    product.setSalePrice(textSalePrice);
                                    product.setCount(Integer.parseInt(textCount));
                                    product.setIdsCategories(dataIDsCategoriesListSelected);
//  1                              Kiểm tra xem ảnh sản phẩm có được thay đổi không (Nếu ảnh ko thay đổi)
                                    if (checkStatus){ //TH ảnh mới.
                                        if (uploadImageList.size() == 3){
//                                        Khi đã có đầy đủ ảnh, up ảnh lên FIREBASE
                                            for (int i=0; i<uploadImageList.size(); i++){
                                                storeDataImages(uploadImageList.get(i));
                                            }
                                        }else {
                                            hideKeyboard();
                                            Toast.makeText(this, "Bạn chưa chọn đủ 3 hình ảnh cho sản phẩm!", Toast.LENGTH_SHORT).show();
                                        }
                                    }else { // TH ảnh đc cập nhập lại
//                                    TH edit(Khoan hảy quan tâm đến)
//  1                              Kiểm tra xem ảnh sản phẩm có được thay đổi không(Nếu ảnh đã thay đổi, thì cập nhập lại ảnh)
                                        if (uploadImageList.size() > 0){
                                            for (int i=0; i<uploadImageList.size(); i++){
                                                storeDataImages(uploadImageList.get(i));
                                            }
                                        }else{
//                                            Trường hợp ng dùng k can thiệp vào ảnh, mà chỉ thực hiện sửa content
                                            product.checkProductNew(false);
                                            Toast.makeText(this, "Sản phẩm đã được sửa lưu.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                }else {
                                    hideKeyboard();
                                    Toast.makeText(this, "Bạn chưa chọn loại danh mục cho sản phẩm!", Toast.LENGTH_SHORT).show();
                                }
                            }else {

                                binding.editSalePrice.requestFocus();
                                binding.editSalePrice.setError("Giá sale sản phẩm trống!");
                            }
                        }else {
                            binding.editSellingPrice.requestFocus();
                            binding.editSellingPrice.setError("Giá bán sản phẩm trống!");
                        }
                    }else {
                        binding.editWholesalePrice.requestFocus();
                        binding.editWholesalePrice.setError("Giá sỉ sản phẩm trống!");
                    }
                }else {
                    binding.editCount.requestFocus();
                    binding.editCount.setError("Số lượng sản phẩm trống!");
                }
            }else {
                binding.editDesc.requestFocus();
                binding.editDesc.setError("Mô tả sản phẩm trống!");
            }
        }else {
            binding.editTitle.requestFocus();
            binding.editTitle.setError("Tên sản phẩm trống!");
        }
    }

    private void chooseImageProduct(int index) {
        requestCode = index;
        authPermissionPicture();
    }

        private void authPermissionPicture() {

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    allowAccessPicture();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(getApplicationContext(), "Quyền xác thực bị từ chối! \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("Permissions", deniedPermissions.toString());
                }
            };
            showDialogPermission(permissionlistener, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, "\"Cho phép truy cập vào thư viện ảnh của bạn.\"");
    }
    private void showDialogPermission(PermissionListener permissionlistener, String[] pemission, String msg) {
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Cấp quyền truy cập")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Từ chối")
                .setGotoSettingButtonText("Cho phép")
                .setPermissions(pemission)
                .check();
    }
    private void allowAccessPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if (result.getResultCode() == RESULT_OK){
                    Bitmap bitmapFirst;
                    Bitmap bitmapSecond;
                    Bitmap bitmapThird;

                    String pathURLImageSelcected;
                    Uri imageSelected = result.getData().getData();
                    try {
                        pathURLImageSelcected = imageSelected.toString();
                        switch (requestCode){
                            case 0:
                                if (Build.VERSION.SDK_INT < 28){
                                    bitmapFirst = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelected);
                                }else {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageSelected);
                                    bitmapFirst = ImageDecoder.decodeBitmap(source);
                                }
                                binding.imgProductFirst.setImageBitmap(bitmapFirst);
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT < 28){
                                    bitmapSecond = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelected);
                                }else {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageSelected);
                                    bitmapSecond = ImageDecoder.decodeBitmap(source);
                                }
                                binding.imgProductSecond.setImageBitmap(bitmapSecond);
                                break;
                            case 2:
                                if (Build.VERSION.SDK_INT < 28){
                                    bitmapThird = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelected);
                                }else {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageSelected);
                                    bitmapThird = ImageDecoder.decodeBitmap(source);
                                }
                                binding.imgProductThird.setImageBitmap(bitmapThird);
                                break;
                        }
                        configUploadImage(pathURLImageSelcected);
                    }catch (Exception e){
                        e.printStackTrace();;
                    }
                }
            }
    );
    public void configUploadImage(String pathURLselected){
        int request = 0;
        switch (requestCode){
            case 0 :
                request = 0;
                break;
            case 1 :
                request = 1;
                break;
            case 2 :
                request = 2;
                break;
        }
        UploadImage uploadImage = new UploadImage(request, pathURLselected);
        boolean checkContain = false;

        if (!uploadImageList.isEmpty()){
//            Trong đây cần kiểm tra thêm trường hợp edit thì ko add ảnh, bằng cách nào, nếu ảnh đó đã tồn tại thì ko add;
            for (int i=0 ; i<uploadImageList.size() ; i++){
                if (uploadImageList.get(i).getIndex() == request){
                    checkContain = true;
                }
            }
            if (checkContain){
                uploadImageList.set(request, uploadImage);
            }else {
                uploadImageList.add(uploadImage);
                for (UploadImage object : uploadImageList) {
                    Log.i("CheckImage", "Index: "+ object.getIndex()+"\nPath: "+object.getPathUrlSelected());
                }
            }
        }else {
            uploadImageList.add(uploadImage);
            for (UploadImage object : uploadImageList) {
                Log.i("CheckImage", "Index: "+ object.getIndex()+"\nPath: "+object.getPathUrlSelected());
            }
//            Log.i("CheckImage", "Index: "+ request +"\nRequest: "+ uploadImageList.get(request).getIndex()+"\nPath: "+uploadImageList.get(request).getPathUrlSelected());
        }
        Log.i("CheckImage", "Tổng số lượng ảnh được chọn: " + uploadImageList.size());
        for (int i=0; i<uploadImageList.size(); i++){
            Log.i("CheckImage", "Index: "+ i +"\nRequest: "+ uploadImageList.get(i).getIndex()+"\nPath: "+uploadImageList.get(i).getPathUrlSelected());
        }

    }
    public void storeDataImages(UploadImage uploadImage){
        int index = uploadImage.getIndex();
        String pathURLselected = uploadImage.getPathUrlSelected();
        StorageReference storage = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child("products")
                .child(product.getId())
                .child("image_" + index + ".jpeg");
        UploadTask uploadTask = storage.putFile(Uri.parse(pathURLselected));
        uploadTask.addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl().addOnCompleteListener(task -> {
            uploadImage.setPathUrlSelected(task.getResult().toString());

            if (checkStatus){
                product.getUrlsImages().add(uploadImage);
                Toast.makeText(this, "Sản phẩm đã được lưu.", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                product.getUrlsImages().set(index, uploadImage);

                finish();
            }

            if (uploadImageList.size() == index + 1){
                product.checkProductNew(checkStatus);
            }
        })).addOnFailureListener(e ->
                Toast.makeText(this, "Upload ảnh thất bại---"+e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    //Mặc định khi vào 1 ACTIVITY thì bàn phím ảo sẽ xuất hiện để nhập input, ta ẩn nó đi,
    //để người dùng chọn image đầu tiên thay vì nhập thông tin sản phẩm
    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.editTitle.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}

















