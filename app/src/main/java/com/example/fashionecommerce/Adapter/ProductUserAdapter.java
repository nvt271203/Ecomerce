package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductUserAdapter extends RecyclerView.Adapter<ProductUserAdapter.MyViewHolder> {
    private List<Product> productList = new ArrayList<>();
    private Context context;
    private int layout;
    private OnClickListener onClickListener;

    public ProductUserAdapter(List<Product> productList, int layout, Context context, OnClickListener onClickListener) {
        this.productList = productList;
        this.context = context;
        this.layout = layout;
        this.onClickListener = onClickListener;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textProduct.setText(product.getName());
        Picasso.get().load(product.getUrlsImages().get(0).getPathUrlSelected()).into(holder.imgProduct);
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(product));
//        holder.textSellingPrice.setText(String.valueOf((int) product.getSalePrice()));
        holder.textSellingPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(product.getSellingPrice())));
//        Check giá trị %
        if (product.getSalePrice() > 0){
            double surplus = product.getSellingPrice() - product.getSalePrice();
            int percent = (int) ((int) surplus/product.getSellingPrice() * 100);
            if (percent >= 10){
                holder.textSalePrice.setVisibility(View.VISIBLE);
                holder.textSalePrice.setText(context.getString(R.string.percent, percent, "%"));
            }else{
//                String ps = String.valueOf(percent).replace("0","");
//                holder.textSalePrice.setText(context.getString(R.string.percent, Integer.parseInt(ps),"%"));
                String ps = String.valueOf(percent).replace("0","");
                if (!ps.isEmpty()) {
                    holder.textSalePrice.setVisibility(View.VISIBLE);
                    holder.textSalePrice.setText(context.getString(R.string.percent, Integer.parseInt(ps),"%"));
                }
            }
        }else{

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public interface OnClickListener {
        public void onClick(Product product);
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private Button btnOptionsProduct;
        private TextView textProduct, textSellingPrice, textSalePrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProduct = itemView.findViewById(R.id.textTitle);
            textSellingPrice = itemView.findViewById(R.id.textSellingPrice);
            textSalePrice = itemView.findViewById(R.id.textSalePrice);
        }
    }
}
