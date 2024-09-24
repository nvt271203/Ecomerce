package com.example.fashionecommerce.Adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductChatAdapter extends RecyclerView.Adapter<ProductChatAdapter.MyViewHolder> {
    @NonNull
    private List<Product> productList = new ArrayList<>();
    private OnClickListener onClickListener;

    public ProductChatAdapter(@NonNull List<Product> productList, OnClickListener onClickListener) {
        this.productList = productList != null ? productList : Collections.emptyList();
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_chat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product =  productList.get(position);
        Picasso.get().load(product.getUrlsImages().get(0).getPathUrlSelected()).into(holder.imgProduct);
        holder.textNameProduct.setText(product.getName());
        holder.textPriceProduct.setText(String.valueOf(product.getSellingPrice()));
        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public interface OnClickListener{
        void OnClick(Product product);
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView textNameProduct, textPriceProduct;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textNameProduct = itemView.findViewById(R.id.textNameProduct);
            textPriceProduct = itemView.findViewById(R.id.textPriceProduct);
        }
    }
}
