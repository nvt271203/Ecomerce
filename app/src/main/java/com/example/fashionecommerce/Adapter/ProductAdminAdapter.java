package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.provider.MediaStore;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.MyViewHolder> {
    private List<Product> productList = new ArrayList<>();
    private Context context;
    private OnClickListener onClickListener;

    public ProductAdminAdapter(List<Product> productList, Context context, OnClickListener onClickListener) {
        this.productList = productList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textProduct.setText(product.getName());
//        for (int i=0; i< product.getUrlsImages().size(); i++){
//            if (product.getUrlsImages().get(i) != null && product.getUrlsImages().get(i).getIndex() == 0){
//                Picasso.get().load(product.getUrlsImages().get(i).getPathUrlSelected()).into(holder.imgProduct);
//            }
//        }
        Picasso.get().load(product.getUrlsImages().get(0).getPathUrlSelected()).into(holder.imgProduct);
//        holder.textSellingPrice.setText(String.valueOf((int) product.getSalePrice()));
        holder.textSellingPrice.setText(context.getString(R.string.price));
        holder.textSellingPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(product.getSellingPrice())));

        holder.btnOptionsProduct.setOnClickListener(v -> onClickListener.onClick(product));

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
            btnOptionsProduct = itemView.findViewById(R.id.btnOptionsProduct);
        }
    }
}
