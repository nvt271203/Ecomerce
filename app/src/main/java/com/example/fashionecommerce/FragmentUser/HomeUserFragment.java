package com.example.fashionecommerce.FragmentUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fashionecommerce.Activity.DetailProductActivity;
import com.example.fashionecommerce.Adapter.CategoryAdapter;
import com.example.fashionecommerce.Adapter.ProductAdminAdapter;
import com.example.fashionecommerce.Adapter.ProductUserAdapter;
import com.example.fashionecommerce.Adapter.SliderProductAdapter;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.databinding.FragmentHomeUserBinding;
import com.example.fashionecommerce.model.Admin;
import com.example.fashionecommerce.model.Category;
import com.example.fashionecommerce.model.Product;
import com.example.fashionecommerce.model.UploadImage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeUserFragment extends Fragment implements CategoryAdapter.OnClickListener, ProductUserAdapter.OnClickListener {
    private FragmentHomeUserBinding binding;
    private CategoryAdapter categoryAdapter;
    private ProductUserAdapter productUserAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private List<UploadImage> bannerList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeUserBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initClicks();
    }

    private void configStoreBanner() {
        FirebaseHelper.databaseReference("admin")
                .child("cJLkDRhb5PdlofqIAAAhXWUrjby1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Admin admin = snapshot.getValue(Admin.class);
                        if (admin == null) {
                            Log.e("HomeUserFragment", "Admin object is null");
                            return;
                        }

                        List<UploadImage> banners = admin.getBannerList();

                        if (banners == null) {
                            Log.e("HomeUserFragment", "Banner list is null");
                            return;
                        }

                        bannerList.clear();
                        bannerList.addAll(banners);
                        configSliderBanner();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("HomeUserFragment", "DatabaseError: " + error.getMessage());
                    }
                });
    }


    public void configSliderBanner() {
        Log.i("getData", "length: "+bannerList.size());

        binding.sliderBanner.setSliderAdapter(new SliderProductAdapter(bannerList));
        binding.sliderBanner.startAutoCycle();
        binding.sliderBanner.setScrollTimeInSec(3);
        binding.sliderBanner.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.sliderBanner.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);

//        binding.sliderBanner.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
    }

//    private void configRVproducts() {
//        binding.recyclerViewProduct.setLayoutManager(new GridLayoutManager(requireContext(),2));
//        binding.recyclerViewProduct.setHasFixedSize(true);
//        productUserAdapter = new ProductUserAdapter(productList, R.layout.item_product_user, getContext(), this);
//        binding.recyclerViewProduct.setAdapter(productUserAdapter);
//    }
    private void configRVfilterProducts(List<Product> productFilterList) {
        binding.recyclerViewProduct.setLayoutManager(new GridLayoutManager(requireContext(),2));
        binding.recyclerViewProduct.setHasFixedSize(true);
        productUserAdapter = new ProductUserAdapter(productFilterList, R.layout.item_product_user, getContext(), this);
        binding.recyclerViewProduct.setAdapter(productUserAdapter);

    }
    @Override
    public void OnClick(Category category, String type) {
        List<Product> productFilterList = new ArrayList<>();
//        Toast.makeText(requireContext(), category.getId(), Toast.LENGTH_SHORT).show();
        for (Product product: productList) {
            for (int i =0; i<product.getIdsCategories().size(); i++){
                if(product.getIdsCategories().get(i).equals(category.getId())){
                    productFilterList.add(product);
                    Log.i("resultProduct", product.getName());
                }
            }

        }
        configRVfilterProducts(productFilterList);
    }
    private void configDataStoreProducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("products")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
//                            binding.progressBar.setVisibility(View.GONE);
                            productList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                productList.add(dataSnapshot.getValue(Product.class));
                            }
                            for (Product product: productList) {
                                Log.i("checkObject", product.getName());
                            }
                        }
                        Collections.reverse(productList);
//                        categoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configDataStoreCategories() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("category")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            categoryList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                categoryList.add(dataSnapshot.getValue(Category.class));
                            }
                            for (Category categories: categoryList) {
                                Log.i("checkObject", categories.getName());
                            }
                        }
                        Collections.reverse(categoryList);
                        categoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void configRVcategories(){
        binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.recyclerViewCategory.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(categoryList,this, R.layout.item_category_classify, true);
        binding.recyclerViewCategory.setAdapter(categoryAdapter);
    }
    private void initClicks() {
    }

    private void init() {
//        binding.progressBar.setVisibility(View.VISIBLE);
        configDataStoreProducts();
        configDataStoreCategories();
        configRVcategories();
//        configRVproducts();
        configStoreBanner();
    }


    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(requireContext(), DetailProductActivity.class);
        intent.putExtra("productSelected", product);
        startActivity(intent);
    }
}