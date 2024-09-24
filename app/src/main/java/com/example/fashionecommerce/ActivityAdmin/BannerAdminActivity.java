package com.example.fashionecommerce.ActivityAdmin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.ActivityBannerBinding;
import com.example.fashionecommerce.databinding.DialogProductCategoriesBinding;
import com.example.fashionecommerce.model.Product;
import com.example.fashionecommerce.model.UploadImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class BannerAdminActivity extends AppCompatActivity {
    public ActivityBannerBinding binding;
    private List<UploadImage> uploadImageList = new ArrayList<>();
    private boolean checkStatus = true;

    private int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initClicks();
    }
    private void init() {
//        bindingCategories = DialogProductCategoriesBinding.inflate(LayoutInflater.from(this));
        binding.include.back.setOnClickListener(v -> finish());
        binding.include.textTitleToolbar.setText("Thêm Slider");
    };

    private void initClicks() {
        binding.include.back.setOnClickListener(v -> finish());
        binding.include.textTitleToolbar.setText("Thêm sản phẩm");
        binding.imgSliderFirst.setOnClickListener(v -> chooseImageProduct(0));
        binding.imgSliderSecond.setOnClickListener(v -> chooseImageProduct(1));
        binding.imgSliderThird.setOnClickListener(v -> chooseImageProduct(2));

        binding.btnSaveSliders.setOnClickListener(v -> validateData());
    }
    private void validateData() {
//  1                              Kiểm tra xem ảnh sản phẩm có được thay đổi không (Nếu ảnh ko thay đổi)
//        if (checkStatus){ //TH ảnh mới.
            if (uploadImageList.size() == 3){
//                                        Khi đã có đầy đủ ảnh, up ảnh lên FIREBASE
                for (int i=0; i<uploadImageList.size(); i++){
                    storeDataImages(uploadImageList.get(i));
                }
            }else {
                Toast.makeText(this, "Bạn chưa chọn đủ 3 hình ảnh cho sản phẩm!", Toast.LENGTH_SHORT).show();
            }
//        }else { // TH ảnh đc cập nhập lại
//                                    TH edit(Khoan hảy quan tâm đến)
//  1                              Kiểm tra xem ảnh sản phẩm có được thay đổi không(Nếu ảnh đã thay đổi, thì cập nhập lại ảnh)
//            if (uploadImageList.size() > 0){
//                for (int i=0; i<uploadImageList.size(); i++){
//                    storeDataImages(uploadImageList.get(i));
//                }
//            }else{
//                                            Trường hợp ng dùng k can thiệp vào ảnh, mà chỉ thực hiện sửa content
//                product.checkProductNew(false);
                Toast.makeText(this, "Sản phẩm đã được sửa lưu.", Toast.LENGTH_SHORT).show();
                finish();
            }
//        }
//
//    }
    public void storeDataImages(UploadImage uploadImage){
        int index = uploadImage.getIndex();
        String pathURLselected = uploadImage.getPathUrlSelected();
        StorageReference storage = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child("banner")
                .child("slider_" + index + ".jpeg");
        UploadTask uploadTask = storage.putFile(Uri.parse(pathURLselected));
        uploadTask.addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl().addOnCompleteListener(task -> {
            uploadImage.setPathUrlSelected(task.getResult().toString());
            upStoreBanner(uploadImage);
            Toast.makeText(this, "Banner đã đc lưu", Toast.LENGTH_SHORT).show();
//            if (checkStatus){
//
//                product.getUrlsImages().add(uploadImage);
//                Toast.makeText(this, "Sản phẩm đã được lưu.", Toast.LENGTH_SHORT).show();
//                finish();
//            }else {
//                product.getUrlsImages().set(index, uploadImage);
//
//                finish();
//            }
//
//            if (uploadImageList.size() == index + 1){
//                product.checkProductNew(checkStatus);
//            }
        })).addOnFailureListener(e ->
                Toast.makeText(this, "Upload ảnh thất bại---"+e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void upStoreBanner(UploadImage uploadImage) {
        FirebaseHelper.databaseReference("admin")
                .child(FirebaseHelper.getUIDpersonCurrent())
                .child("bannerList")
                .setValue(uploadImageList);
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
                                binding.imgSliderFirst.setImageBitmap(bitmapFirst);
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT < 28){
                                    bitmapSecond = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelected);
                                }else {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageSelected);
                                    bitmapSecond = ImageDecoder.decodeBitmap(source);
                                }
                                binding.imgSliderSecond.setImageBitmap(bitmapSecond);
                                break;
                            case 2:
                                if (Build.VERSION.SDK_INT < 28){
                                    bitmapThird = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelected);
                                }else {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageSelected);
                                    bitmapThird = ImageDecoder.decodeBitmap(source);
                                }
                                binding.imgSliderThird.setImageBitmap(bitmapThird);
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
}