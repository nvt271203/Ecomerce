package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Database.ItemOderDB;
import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.ItemOderPuchased;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private final List<ItemOder> itemOderList;
    private List<ItemOder> itemOderListSelected = new ArrayList<>();
    private final ItemOderDB itemOderDB;
    private final Context context;
    private final OnClickListener onClickListener;

    public CartAdapter(List<ItemOder> itemOderList, ItemOderDB itemOderDB, Context context, OnClickListener onClickListener) {
        this.itemOderList = itemOderList;
        this.itemOderDB = itemOderDB;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemOder itemOder = itemOderList.get(position);
        ItemOderPuchased itemOderPuchased = new ItemOderPuchased();
        itemOderPuchased.setIdProduct(itemOder.getIdProduct());
        itemOderPuchased.setName(itemOder.getNameProduct());
//        itemOderPuchased.setUrlImage(itemOder.);
        itemOderPuchased.setPrice(itemOder.getPrice());

        itemOderPuchased.setQuantity(itemOder.getQuantity());

        Product product = itemOderDB.getProductIdInt(itemOder.getId());

        if (product != null) {
            holder.textProduct.setText(product.getName());
            holder.textQuantity.setText(String.valueOf(itemOder.getQuantity()));
            holder.textPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(itemOder.getPrice() * itemOder.getQuantity())));
            Picasso.get().load(product.getUrlsImages().get(0).getPathUrlSelected()).into(holder.imgProduct);
        }




        // Function ICON
        ////
        holder.imgDelete.setOnClickListener(v -> onClickListener.OnClick(position, "delete"));
        holder.imgIncrease.setOnClickListener(v -> onClickListener.OnClick(position, "increase"));
        holder.imgDecrease.setOnClickListener(v -> onClickListener.OnClick(position, "decrease"));

        // Function Checkbox
        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()){

                itemOder.setUrlImage(itemOderDB.getItemDB(itemOder.getId()).getUrlImage());

                itemOderListSelected.add(itemOder);
                onClickListener.OnClick(itemOderListSelected);
//                Toast.makeText(context, "true - length: " + itemOderDB.getCountlPriceProductsSeleted(itemOderListSelected), Toast.LENGTH_SHORT).show();
            }else {
                itemOderListSelected.remove(itemOder);
                onClickListener.OnClick(itemOderListSelected);
//                Toast.makeText(context, "false - length: " + itemOderDB.getCountlPriceProductsSeleted(itemOderListSelected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemOderList.size();
    }
    public interface OnClickListener{
        public void OnClick(int position, String action);

        public void OnClick(List<ItemOder> itemOderListSelected);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgIncrease, imgDecrease, imgDelete;
        TextView textProduct, textQuantity, textPrice;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgIncrease = itemView.findViewById(R.id.imgIncrease);
            imgDecrease = itemView.findViewById(R.id.imgDescrease);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            textProduct = itemView.findViewById(R.id.textProduct);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);

            checkBox = itemView.findViewById(R.id.checkBoxProduct);
        }
    }

}
