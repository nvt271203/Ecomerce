package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Database.ItemOderDB;
import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ParameterAdapter extends RecyclerView.Adapter<ParameterAdapter.MyViewHolder> {
    @NonNull
    public List<ItemOder> itemOderList = new ArrayList<>();
    private Context context;

    public ParameterAdapter(@NonNull List<ItemOder> itemOderList, Context context) {
        this.itemOderList = itemOderList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_parameters, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemOder itemOder = itemOderList.get(position);

        ItemOderDB itemOderDB = new ItemOderDB(context);
        Product productOder = itemOderDB.getProductIdInt(itemOder.getId());


        //
        if (itemOder.getUrlImage() != null){
            Picasso.get().load(itemOder.getUrlImage()).into(holder.imgProduct);
        }else{
            Picasso.get().load(productOder.getUrlsImages().get(0).getPathUrlSelected()).into(holder.imgProduct);
        }


        //
        if (itemOder.getNameProduct() != null){
            holder.textProduct.setText(itemOder.getNameProduct());
        }else{
            holder.textProduct.setText(productOder.getName());
        }

        holder.textQuantity.setText(String.valueOf(itemOder.getQuantity()));
        holder.textPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(itemOder.getPrice() * itemOder.getQuantity())));


    }

    @Override
    public int getItemCount() {
        return itemOderList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textProduct, textQuantity, textPrice;
        private ImageView imgProduct;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProduct = itemView.findViewById(R.id.textProduct);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);

            imgProduct = itemView.findViewById(R.id.imgProduct);

        }
    }

}
