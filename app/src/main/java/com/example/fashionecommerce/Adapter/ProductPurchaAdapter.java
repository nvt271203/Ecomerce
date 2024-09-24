package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductPurchaAdapter extends RecyclerView.Adapter<ProductPurchaAdapter.MyViewHolder> {
    @NonNull
    private List<ItemOder> itemOderList = new ArrayList<>();
    private OnClickListener onClickListener;
    private Context context;
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_purchased, parent, false);
        return new MyViewHolder(itemView);
    }

    public ProductPurchaAdapter(@NonNull List<ItemOder> itemOderList, OnClickListener onClickListener, Context context) {
        this.itemOderList = itemOderList;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemOder itemOder = itemOderList.get(position);
//        Picasso.get().load(itemOder.getUrlsImages().get(0).getPathUrlSelected()).into(holder.imgProduct);
        holder.textQuantity.setText(String.valueOf(itemOder.getQuantity()));
        holder.textPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(itemOder.getPrice() * itemOder.getQuantity())));

        Picasso.get().load(itemOder.getUrlImage()).into(holder.imgProduct);


        holder.textProduct.setText(itemOder.getNameProduct());
    }

    @Override
    public int getItemCount() {
        return itemOderList.size();
    }
    public interface OnClickListener{
        void OnClick(Product product);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView textProduct,textQuantity, textPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProduct = itemView.findViewById(R.id.textProduct);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
        }
    }
}
