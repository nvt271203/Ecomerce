package com.example.fashionecommerce.FragmentUser;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.fashionecommerce.Adapter.ChatBotAdapter;
import com.example.fashionecommerce.Activity.AuthencationActivity.LoginActivity;
import com.example.fashionecommerce.Helper.DataSuggestHelper;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.DialogAuthencationBinding;
import com.example.fashionecommerce.databinding.FragmentMessageUserBinding;
import com.example.fashionecommerce.model.Category;
import com.example.fashionecommerce.model.Chat;
import com.example.fashionecommerce.model.DataSuggest;
import com.example.fashionecommerce.model.ItemSuggest;
import com.example.fashionecommerce.model.Product;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUserFragment extends Fragment implements ChatBotAdapter.OnClickListener {
    private FragmentMessageUserBinding binding;
    private DialogAuthencationBinding bindingAuth;
    private Dialog dialog;
    private Bitmap bitmap = null;
    private Chat chat;
    DataSuggest dataSuggest;
    public Bundle bundle;
    private String pathUriImageSelected = null;
    private String pathImageSelectedFirebase = null;
    private Boolean statusImageSelected = false;
    private ChatBotAdapter chatBotAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private List<String> nameCategoriestList = new ArrayList<>();
    private List<String> nameProductstList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    private List<Category> categoryList = new ArrayList<>();
    private List<ItemSuggest> dataSuggestsList = new ArrayList<>();



    private String suggestNAMEcategory ="no";
    private String getTextMessage = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageUserBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        return binding.getRoot();


    }

    private void getBundleData() {
        if (bundle != null){
            bundle = getActivity().getIntent().getExtras();
            getTextMessage = bundle.getString("dataProvince");
            Toast.makeText(requireContext(), "Fragment: " + getTextMessage.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getBundleData();
        init();
        checkUserCurrent();
//        configDataMessage();
//        configRVMessage();
        initClicks();
//        Lấy ra danh sách danh mục sản phẩm sẳn có để thực hiện chức năng gợi ý dữ liệu
        configStoreCategories();
//        Lấy ra danh sách tên sản phẩm sẳn có để thực hiện chức ănng gợi ý dữ liệu
        configStoreNameProduct();
        configStoreProducts();
        //        filterIDcategory("Ví");

    }

    private void checkListMessage() {
        if (chatList.size()==0) {
            chat = new Chat();
            chat.setMessage("Xin chào ! \nTôi có thể giúp gì được cho bạn ?");
            chat.setIdSender("idBotChatAI");
            chat.setIdRecever(FirebaseHelper.getUIDpersonCurrent());
            chatList.add(chat);
            chat.storeSendUser(false);

            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), null);
            binding.RVchat.setAdapter(chatBotAdapter);
        }
    }

    private void checkUserCurrent() {
        if (!FirebaseHelper.checkUserCurrent()){
            showDialogAuthencation();
        }else {
            configDataMessage();
            configRVMessage();
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

    private int filterPriceProduct(String textMessage) {
        Pattern pattern = Pattern.compile("\\d+"); // Tìm các chuỗi số có một hoặc nhiều chữ số liên tục
        Matcher matcher = pattern.matcher(textMessage);
        int number = 0;
        // Kiểm tra xem có phù hợp nào được tìm thấy không
        if (matcher.find()) {
            // Lấy phần tử phù hợp đầu tiên
            String numberString = matcher.group();
            // Chuyển chuỗi số thành số nguyên
            number = Integer.parseInt(numberString);
            // In ra số nguyên
            Log.i("ResultTypenameCategoriestList", String.valueOf(number));
            //  ------------ Thiết lập giá cho đối tượng gợi ý
//            dataSuggest.setPrice(number);
        }
        return number;
    }

    private String filterNameProduct(String textMessage) {
        configStoreNameProduct();
        textMessage = textMessage.toLowerCase();
        String[] strSubString = textMessage.split(" ");
        boolean foundSuggestion = false;

//        Lọc type là category
        String typeSuggest = "";
        for (String temp : strSubString) {
            for (String item : nameCategoriestList) {
                if (temp.contains(item.toLowerCase())){
                    typeSuggest = item;
                    Log.i("ResultTypenameCategoriestList","category - " + typeSuggest);

                    foundSuggestion = true;
                    break;
                }
            }
            if (foundSuggestion) break;
        }


        String nameSuggest = "";
        for (String temp : strSubString) {
            for (Product product : productList) {
//                Log.i("ResultTypenameCategoriestList","nameProduct - " + product.getName());
                if (product.getName().toLowerCase().contains(temp.toLowerCase()) ){
                    Log.i("ResultTypenameCategoriestList","name - " + nameSuggest);

                    if (!temp.toLowerCase().equals(typeSuggest.toLowerCase())){
                        nameSuggest = temp;
                    }
                }

//                if (foundSuggestion) break;
            }

        }

        return nameSuggest;
    }
    private String getSuggestNAMEcategory(String textMessage){
        String result = "HAHA";
        String getNameCategory = filterTypeProduct(textMessage);
        String[] strSubString = textMessage.split(" ");
        for (int i = 0; i<strSubString.length; i++){
            if (getNameCategory.toLowerCase().contains(strSubString[i].toLowerCase())){
                return result = strSubString[i+1];
            }
        }
        return result;
    }

    private String filterTypeProduct(String textMessage) {
        textMessage = textMessage.toLowerCase();
        String[] strSubString = textMessage.split(" ");

        boolean foundSuggestion = false;
        String typeSuggest = "";

        for (String temp : strSubString) {
            for (String item : nameCategoriestList) {
                if (temp.contains(item.toLowerCase())){
                    typeSuggest = item;
                    foundSuggestion = true;
                    break;
                }
            }
            if (foundSuggestion) break;
        }
        return typeSuggest;
    }

    private void configStoreProducts() {
        FirebaseHelper.databaseReference("products")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Product getProduct = dataSnapshot.getValue(Product.class);
                                productList.add(getProduct);
                            }
//                            Sau khi đã có thông tin dữ liệu từng loại sản phẩm tiến hành lọc dựa theo dữ liệu có sẳn
//                            filterDataProduct("Ví", "Ví", 200);

                        }
                        String str = "Tôi muốn mua một cái mũ";
                        filterNameProduct(str);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Chuyển đổi chuỗi thành chữ thường
        input = input.toLowerCase();

        // Viết hoa chữ cái đầu tiên của chuỗi
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
    public String lowerCaseString (String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.toLowerCase();
    }
    //    private List<String> filterDataProduct(String type, String name, int price) {
    private void filterDataProduct(String type, String name, int price, Boolean checkNoData) {
        List<Product> productFilterList = new ArrayList<>();


//        final List<String>[] data = new List[]{new ArrayList<>()};
//        String Ftype = capitalizeFirstLetter(type);
//        String Fname = capitalizeFirstLetter(name);

        String Ftype = lowerCaseString(type);
        String Fname = lowerCaseString(name);
        Log.i("returnDataFilter", "type: "+Ftype + "------name: " + Fname + "------price: " + price);

//
//
//                chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this, dataSuggets);
//                binding.RVchat.setAdapter(chatBotAdapter);
//
//                chat.setListSuggest(dataSuggets);
        FirebaseHelper.databaseReference("products")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String textRespond = "Danh sách sản phẩm";

                        for (Product product : productList) {


                            if(checkNoData) {
                                textRespond = "Xin lỗi, hiện tại shop chưa kinh doanh mặt hàng bạn cần! " +
                                        "\nDưới đây là 1 số mặt hàng có thể bạn sẽ thích.";
                                // Lọc loại sản phẩm
                                for (int i=0; i<categoryList.size();i++) {
                                    if (categoryList.get(i).getName().toLowerCase().contains(Ftype)){ // Nếu tồn tại loại danh mục này
//                                        // kiểm tra product hiện tại có chứa idCategory không
                                        if (product.getIdsCategories().contains(categoryList.get(i).getId())){
                                            Log.i("ResultClassifyFilter", "Lọc theo loại");
                                            productFilterList.add(product);
                                        }
                                    }
                                }
                            }
//  TH1: Lọc theo -----tên ----------sản phẩm
                            else if (product.getName().toLowerCase().contains(Fname) && (!Fname.equals(""))
                                    && Ftype.equals("") && price == 0){
                                Log.i("ResultClassifyFilter", "Lọc theo tên");

                                productFilterList.add(product);
                            }
//  TH2: Lọc theo------loại------------ sản phẩm
                            else if ((!Ftype.equals(""))
                                    && Fname.equals("") && price == 0){
                                // Lọc loại sản phẩm
                                for (int i=0; i<categoryList.size();i++) {
                                    if (categoryList.get(i).getName().toLowerCase().contains(Ftype)){ // Nếu tồn tại loại danh mục này
//                                        // kiểm tra product hiện tại có chứa idCategory không
                                        if (product.getIdsCategories().contains(categoryList.get(i).getId())){
                                            Log.i("ResultClassifyFilter", "Lọc theo loại");

                                            productFilterList.add(product);
                                        }
                                    }
                                }
                            }
//  TH4: Lọc theo-------- Loại và tên---------- sản phẩm
                            else if (product.getName().toLowerCase().contains(Fname) && (!Ftype.equals(""))
                                    && price == 0){

                                for (int i=0; i<categoryList.size();i++) {
                                    if (categoryList.get(i).getName().toLowerCase().contains(Ftype)){
                                        for (int j=0; j<product.getIdsCategories().size();j++) {
                                            if (product.getIdsCategories().get(j).contains(categoryList.get(i).getId())) {
                                                Log.i("ResultClassifyFilter", "Lọc theo Loại - Tên");

                                                productFilterList.add(product);
                                            }
                                        }
                                    }
                                }
                            }

//                            //  TH3: Lọc theo -----giá---------- sản phẩm
//
//                            else if (product.getSellingPrice() == Double.valueOf(price) && (price!=0)
//                                    && Fname.equals("") && Ftype.equals("")){
//                                if (product.getSellingPrice() == price){
//
//                                }
//
//                            }
////  TH5: Lọc theo-------- Loại và Giá---------- sản phẩm
//                            else if (product.getSellingPrice() == Double.valueOf(price) && (!Ftype.equals(""))
//                                    && Fname.equals("")){
//
//                                for (int i=0; i<categoryList.size();i++) {
//                                    if (categoryList.get(i).getName().contains(Ftype)){
//                                        for (int j=0; j<product.getIdsCategories().size();j++) {
//                                            if (product.getIdsCategories().get(j).contains(categoryList.get(i).getId())) {
////                                                Toast.makeText(requireContext(), product.getName(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }
//                                }
//                            }
////  TH6: Lọc theo-------- Tên và Giá---------- sản phẩm
//                            else if (product.getSellingPrice() == Double.valueOf(price) && product.getName().contains(Fname)
//                                    && Ftype.equals("")){
////                                Toast.makeText(requireContext(), product.getName(), Toast.LENGTH_SHORT).show();
//                            }
////  TH6: Lọc theo-3-------Loại và Tên và Giá---------- sản phẩm
//                            else if (price!=0 && product.getName().toLowerCase().contains(Fname)
//                                    && !Ftype.equals("")){
//                                // Lọc loại sản phẩm
//                                for (int i=0; i<categoryList.size();i++) {
//                                    if (categoryList.get(i).getName().toLowerCase().contains(Ftype)){ // Nếu tồn tại loại danh mục này
//                                        Toast.makeText(requireContext(), "filter Type-Name-Price", Toast.LENGTH_SHORT).show();
////                                        // kiểm tra product hiện tại có chứa idCategory không
//                                        if (product.getIdsCategories().contains(categoryList.get(i).getId())){
//                                            productFilterList.add(product);
//                                        }
//                                    }
//                                }
//                            }



                        }

                        chat = new Chat();
                        Log.i("ResultFilterproductFilterList","length:" + productFilterList.size());
                        chat.setListProductFilter(productFilterList);
                        chat.setMessage(textRespond);
                        chat.setIdSender("idBotChatAI");
                        chat.setIdRecever(FirebaseHelper.getUIDpersonCurrent());
                        chatList.add(chat);
//                        chat.store();
                        chatBotAdapter.notifyDataSetChanged();

//                        chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), null, dataSuggestsList);
//                        binding.RVchat.setAdapter(chatBotAdapter);
//                        chatBotAdapter.notifyDataSetChanged();
                        if (chat != null) {
//                            chat.setListSuggest(dataSuggestsList);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void configStoreCategories() {
        FirebaseHelper.databaseReference("category")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            nameCategoriestList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                Category category = dataSnapshot.getValue(Category.class);
//                                Lưu vào danh sách tên danh mục
                                nameCategoriestList.add(category.getName());
                                categoryList.add(category);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

















    private void configStoreNameProduct() {
        FirebaseHelper.databaseReference("products")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String nameProduct = dataSnapshot.getValue(Product.class).getName();
                            nameProductstList.add(nameProduct);
                        }
//                        for (int i=0; i<nameProductstList.size(); i++){
//                            if (i < nameProductstList.size()) {
//                                Log.i("resultNameProduct", nameProductstList.get(i));
//                            } else {
//                                Log.e("MessageUserFragment", "Index " + i + " is out of bounds for nameCategoriestList");
//                            }
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVMessage() {
        binding.RVchat.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.RVchat.setHasFixedSize(true);
//        chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this, dataSuggestsList);
        chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
        binding.RVchat.setAdapter(chatBotAdapter);
    }

    @Override
    public void showRespond() {
        Toast.makeText(requireContext(), "Hei", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void returnMessageSend(ItemSuggest itemSuggest) {
        Boolean checkFilterNoData = false;
//        Toast.makeText(requireContext(),"Fragment"+ textMessage, Toast.LENGTH_SHORT).show();
        if (itemSuggest.getType().equals("trainAPP")){
            dataSuggest = new DataSuggest();
            dataSuggest.setType(filterTypeProduct(itemSuggest.getName()));
            dataSuggest.setName(filterNameProduct(itemSuggest.getName()));
            Log.i("resultDataSuggestRETURN", "category: "+dataSuggest.getType()
                    +  "name: "+dataSuggest.getName()
                    +  "price: "+ dataSuggest.getPrice());
            filterDataProduct(dataSuggest.getType(), dataSuggest.getName(), 0, checkFilterNoData);
        }

        else if (itemSuggest.getType().equals("trainAI")) {
            requestAI(itemSuggest.getName());

        } else if (itemSuggest.getType().equals("trainNoData")) {
            checkFilterNoData = true;
            dataSuggest = new DataSuggest();
            dataSuggest.setType(filterTypeProduct(itemSuggest.getName()));
            filterDataProduct(dataSuggest.getType(), "", 0, checkFilterNoData);


        }


    }
    public void configDataMessage() {
        FirebaseHelper.databaseReference("messages")
//                .child(FirebaseHelper.getUIDpersonCurrent())
//                .child("idBotChatAI")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            chatList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Chat getChat = dataSnapshot.getValue(Chat.class);
                                chatList.add(getChat);
                            }


                            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), null);
                            binding.RVchat.setAdapter(chatBotAdapter);
                        }else {
                            Log.i("resultListMessage","data:  "+ chatList.size());
                            checkListMessage();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void configDataMessageNhap() {
        FirebaseHelper.databaseReference("messages")
//                .child(FirebaseHelper.getUIDpersonCurrent())
//                .child("idBotChatAI")
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Chat getChat = snapshot.getValue(Chat.class);
                        chatList.add(getChat);
//                        Log.i("result","data:  "+ getChat.getMessage());
                        chatBotAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void init(){
    }
    private void initClicks(){
        binding.imgSend.setOnClickListener(v ->{
                    if (!FirebaseHelper.checkUserCurrent()){
                        showDialogAuthencation();
                        binding.textBotChat.setText("");
                    }else {
                        validateSend();
                        hideKeyboard();
                    }

                }
//                validate()

        );
        binding.imgLibrary.setOnClickListener(v -> authPermissionPicture());
    }

    //        Toast.makeText(requireContext(), "Image", Toast.LENGTH_SHORT).show();
//        binding.layoutImageSelected.setVisibility(View.VISIBLE);
    private void authPermissionPicture() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                allowAccessPicture();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(requireContext(), "Quyền xác thực bị từ chối! \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Permissions", deniedPermissions.toString());
            }
        };
        showDialogPermission(permissionlistener, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, "\"Cho phép truy cập vào thư viện ảnh của bạn.\"");
    }
    private void allowAccessPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);


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
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if (result.getResultCode() == RESULT_OK){


//                    String pathURLImageSelcected;
                    Uri imageSelected = result.getData().getData();
                    try {
                        pathUriImageSelected = imageSelected.toString();
                        Log.i("ResultUploadImage", "bitmmap" + pathUriImageSelected);
                        if (Build.VERSION.SDK_INT < 28){
                            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageSelected);
                        }else {
                            ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), imageSelected);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }

                        binding.layoutImageSelected.setVisibility(View.VISIBLE);

                        binding.imgSelected.setImageBitmap(bitmap);

                        statusImageSelected = true;
                        Log.i("ResultUploadImage", "statusImageSelected: " + statusImageSelected);



                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);


//                    configUploadImage(pathURLImageSelcected);
                    }catch (Exception e){
                        e.printStackTrace();;
                    }
                }
            }
    );

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.editMessage.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void validateSend() {
        String textMessage = binding.editMessage.getText().toString().trim();
        if (!textMessage.isEmpty()){



            chat = new Chat();

            chat.setIdSender(FirebaseHelper.getUIDpersonCurrent());
            chat.setIdRecever("idBotChatAI");


// ++++++++++++++Check keyword+++++++++++++++
            if (DataSuggestHelper.dataGugget_KeyWord(textMessage)){

//                Nếu có chứa keyword liên quan đến cửa hàng -> datasuggest
                chat.setMessage(textMessage);
                chatList.add(chat);
                chatBotAdapter.notifyDataSetChanged();
//           chat.store();
                // Phản hồi tin nhắn
                handleMessage(textMessage);
                requestRespond(textMessage);
            }else {
//                Nếu ko chứa -> Train Gemini
                if (statusImageSelected){
//                    Train Gemini kèm hình ảnh
                    if (pathUriImageSelected != null) {
                        chat.setMessage(textMessage);
                        chat.setPathImage(pathUriImageSelected);

                        chatList.add(chat);

                        requestImage(textMessage);
                        storeDataImages();
                    }
                }else {
//                    Train Gemini không kèm hình ảnh
                    chat.setMessage(textMessage);
                    chatList.add(chat);
                    chatBotAdapter.notifyDataSetChanged();

                    requestAI(textMessage);
                }

//         -----------------
//            chat.store();
            }
//-----------------



            binding.RVchat.scrollToPosition(chatList.size() -  1);
            binding.editMessage.setText("");
        }else {
            Toast.makeText(requireContext(), "Bạn chưa nhập tin nhắn!", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestImage(String textMessage) {

//        chat.setPathImage(pathImageSelected);
        GenerativeModel generativeModel = new GenerativeModel("gemini-pro-vision",
                "AIzaSyCJJBDf3dbdQls67znGNmUtTuXiTGffCQ4");

        GenerativeModelFutures model = GenerativeModelFutures.from(generativeModel);
        Content content = new Content.Builder()
                .addText(textMessage)
                .addImage(bitmap)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
//                    textResult.setText(resultText);
//                    storeDataImages(resultText);
                    chat.setPathImage(pathImageSelectedFirebase);
                    chatBotAdapter.notifyDataSetChanged();
                    //                    chat.store();
//                    Sau khi gửi ảnh xong, phải cho về lại trạng thái false
                    statusImageSelected = false;
                    respondMessage(resultText);


                }
                @Override
                public void onFailure(Throwable t) {
//                    textResult.setText(t.toString());
                    respondMessage(t.toString());

                }
            }, requireContext().getMainExecutor());

            binding.layoutImageSelected.setVisibility(View.GONE);
        }
    }
    public void storeDataImages(){
        StorageReference storage = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child("messages")
                .child(chat.getId())
                .child("image_" + chat.getIdSender() + ".jpeg");
        UploadTask uploadTask = storage.putFile(Uri.parse(pathUriImageSelected));
        uploadTask.addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl().addOnCompleteListener(task -> {
//            uploadImage.setPathUrlSelected(task.getResult().toString());
            pathImageSelectedFirebase = task.getResult().toString();

            Log.i("ResultUploadImage", "pathSucces: " + pathImageSelectedFirebase);
//            chat.setPathImage(task.getResult().toString());
//            chat.store();

//            this.pathImageSelected = null;
//            respondMessage(getTextMessage);

        })).addOnFailureListener(e ->
                Toast.makeText(requireContext(), "Upload ảnh thất bại---"+e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    @SuppressLint("NotifyDataSetChanged")
    private void requestAI(String textMessage) {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-1.5-flash", "AIzaSyDfb5ZlkJRpWu04bWnkHmZgAQ7AV2-uZxc");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(textMessage)
                .build();



        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText().trim();
//                    chat.setMessage(textMessage);
//                    chat.store();

                    respondMessage(resultText);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, requireContext().getMainExecutor());
        }

//        chat.setMessage("Tôi là AI");

    }

    @SuppressLint("NotifyDataSetChanged")
    private void respondMessage(String resultText) {
        chat = new Chat();
        chat.setMessage(resultText);
        chat.setIdSender("idBotChatAI");
        chat.setIdRecever(FirebaseHelper.getUIDpersonCurrent());

        chatList.add(chat);
        chatBotAdapter.notifyDataSetChanged();

//        chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
//        binding.RVchat.setAdapter(chatBotAdapter);

        binding.RVchat.scrollToPosition(chatList.size() -  1);

//        chat.store();
    }







    private void handleMessage(String textMessage) {
        //        String str = "Tôi muốn mua những mặt hàng mũ cao bồi có giá 100 ở trong shop";
//        textMessage = textMessage.toLowerCase();
//        String[] strSubString = textMessage.split(" ");
//
//        boolean foundSuggestion = false;
//        String typeSuggest = "";
//
//        for (String temp : strSubString) {
//            for (String item : nameCategoriestList) {
//                if (temp.contains(item.toLowerCase())){
//                    typeSuggest = item;
//                    foundSuggestion = true;
//                    break;
//                }
//            }
//            if (foundSuggestion) break;
//        }
//        Log.i("ResultTypenameCategoriestList", typeSuggest);
        //  ------------ Thiết lập loại đối tượng gợi ý
        dataSuggest = new DataSuggest();
        dataSuggest.setType(filterTypeProduct(textMessage));

//  Nhận dạng tên sản phẩm
        dataSuggest.setName(filterNameProduct(textMessage));

        // Sử dụng biểu thức chính quy để tìm số trong chuỗi
        dataSuggest.setPrice(filterPriceProduct(textMessage));

        Log.i("resultDataSuggest", "category: "+dataSuggest.getType()
                +    "name: "+dataSuggest.getName()
                +   "price: "+ dataSuggest.getPrice());


    }




    @SuppressLint("NotifyDataSetChanged")
    private void requestRespond(String textMessage) {
        if (chat!=null){
            chat = new Chat();
        }else {
            chat = new Chat();
        }



        String textRespond = "";
        if (!textMessage.isEmpty()){
            if (dataSuggest == null) {
                dataSuggest = new DataSuggest();
            }

            textRespond = trainDataSuggest(dataSuggest.getType(), dataSuggest.getName(), 0, textMessage);



        }
        chat.setMessage(textRespond);
        chat.setIdSender("idBotChatAI");
        chat.setIdRecever(FirebaseHelper.getUIDpersonCurrent());
        chatList.add(chat);
        chatBotAdapter.notifyDataSetChanged();

//        chat.store();


    }
    @SuppressLint("NotifyDataSetChanged")
    private String trainDataSuggest(String type, String name, int price, String textMessage) {
        String textRespond = "Có phải nhu cầu mà bạn đang mong muốn là: ";

//  TH1: Lọc theo -----tên ----------sản phẩm
        //                textRespond = "Có phải vấn đề mà bạn đang gặp phải là: ";
        if ((!name.equals("")
                && type.equals("")) && price == 0){
            dataSuggestsList = DataSuggestHelper.suggest_NAME(name);
            chat.setListSuggest(dataSuggestsList);
            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
            binding.RVchat.setAdapter(chatBotAdapter);
//                                Toast.makeText(requireContext(), "Lọc theo tên sản phẩm", Toast.LENGTH_SHORT).show();
        }
//  TH2: Lọc theo------loại------------ sản phẩm
        else if ((!type.equals(""))
                && name.equals("") && price == 0){

            dataSuggestsList = DataSuggestHelper.suggest_CATEGORY(type, getSuggestNAMEcategory(textMessage));
            chat.setListSuggest(dataSuggestsList);

            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
            binding.RVchat.setAdapter(chatBotAdapter);
//                                Toast.makeText(requireContext(), "Lọc theo loại sản phẩm", Toast.LENGTH_SHORT).show();

        }


////  TH3: Lọc theo -----giá---------- sản phẩm
//        else if ( (price!=0)
//                && name.equals("") && type.equals("")){
//            dataSuggestsList = DataSuggestHelper.suggest_PRICE(price);
//            chat.setListSuggest(dataSuggestsList);
//
//            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
//            binding.RVchat.setAdapter(chatBotAdapter);
////                                Toast.makeText(requireContext(), "Lọc theo giá sản phẩm", Toast.LENGTH_SHORT).show();
//        }
//  TH4: Lọc theo-------- Loại và tên---------- sản phẩm
        else if ((!type.equals("")) && (!name.equals(""))
                && price == 0){
            dataSuggestsList = DataSuggestHelper.suggest_CATEGORY_NAME(type, name);
            chat.setListSuggest(dataSuggestsList);

            chatBotAdapter.notifyDataSetChanged();
            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
            binding.RVchat.setAdapter(chatBotAdapter);
//                                Toast.makeText(requireContext(), "Lọc theo loại và tên sản phẩm", Toast.LENGTH_SHORT).show();

        }
////  TH5: Lọc theo-------- Loại và Giá---------- sản phẩm
//        else if ((!type.equals("")) && (price!=0) &&
//                (name.equals(""))){
//            dataSuggestsList = DataSuggestHelper.suggest_CATEGORY_PRICE(type, price);
//            chat.setListSuggest(dataSuggestsList);
//
//            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
//            binding.RVchat.setAdapter(chatBotAdapter);
//
////                                Toast.makeText(requireContext(), "Lọc theo loại và giá sản phẩm", Toast.LENGTH_SHORT).show();
//
//        }
////  TH6: Lọc theo-------- Tên và Giá---------- sản phẩm
//        else if ((!name.equals("")) && (price!=0)
//                && type.equals("")){
//            dataSuggestsList = DataSuggestHelper.suggest_NAME_PRICE(name, price);
//            chat.setListSuggest(dataSuggestsList);
//
//            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
//            binding.RVchat.setAdapter(chatBotAdapter);
////                                Toast.makeText(requireContext(), "Lọc theo tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
//        }
////  TH6: Lọc theo--------Loại và Tên và Giá---------- sản phẩm
//        else if ((!name.equals("")) && (price!=0)
//                && (!type.equals(""))){
//            dataSuggestsList = DataSuggestHelper.suggest_CATEGORY_NAME_PRICE(type, name, price);
//            chat.setListSuggest(dataSuggestsList);
//
//            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
//            binding.RVchat.setAdapter(chatBotAdapter);
//
//            Toast.makeText(requireContext(), "Lọc theo loại, tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
//
//        }
//
//        else {
////            textRespond = "Xin lỗi, tôi vẫn chưa hiểu vấn để bạn đang gặp phải ! \nBạn có thể mô tả cụ thể hơn không ? ";
//            textRespond = "TH data suggest";
////            chat.setListSuggest(null);
//
//            chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), this);
//            binding.RVchat.setAdapter(chatBotAdapter);
//
//
//        }




        return textRespond;



    }
//                        chatBotAdapter = new ChatBotAdapter(chatList, requireContext(), null, dataSuggestsList);
//                        binding.RVchat.setAdapter(chatBotAdapter);
//                        chatBotAdapter.notifyDataSetChanged();



}
