package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Oder;
import com.example.fashionecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PurchaOderAdapter extends RecyclerView.Adapter<PurchaOderAdapter.MyViewHolder> implements ProductPurchaAdapter.OnClickListener {

    private List<Oder> oderList = new ArrayList<>();
    private Context context;

    public PurchaOderAdapter(@NonNull List<Oder> oderList, Context context) {
        this.oderList = oderList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchased_oder, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Oder oder = oderList.get(position);
        holder.textDayTime.setText(GetMaskHelper.getDate(oder.getDataOder(), 2));

        holder.textName.setText(oder.getAddressDelivery().getName());
        holder.textPhone.setText(oder.getAddressDelivery().getPhone());

        holder.textStreet.setText(oder.getAddressDelivery().getStreet());
        holder.textLocality.setText(oder.getAddressDelivery().getLocality());
        holder.textLocaltion.setText(oder.getAddressDelivery().getLocation());
        holder.textTotalAllPrice.setText(context.getString(R.string.total_all_price, GetMaskHelper.getValue(oder.getTotal())));


        holder.RVproductPurchased.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        holder.RVproductPurchased.setHasFixedSize(true);
        ProductPurchaAdapter purchaAdapter = new ProductPurchaAdapter(oder.getItemOderList(), this, context);
        holder.RVproductPurchased.setAdapter(purchaAdapter);
    }

    @Override
    public int getItemCount() {
        return oderList.size();
    }

    @Override
    public void OnClick(Product product) {
        // Implement the click handling logic if needed
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView RVproductPurchased;
        private TextView textDayTime, textName, textPhone, textStreet, textLocaltion,textLocality ,textTotalAllPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            RVproductPurchased = itemView.findViewById(R.id.RVproductPurchased);
            textDayTime = itemView.findViewById(R.id.textDayTime);
            textName = itemView.findViewById(R.id.textName);
            textPhone = itemView.findViewById(R.id.textPhone);
            textLocaltion = itemView.findViewById(R.id.textLocaltion);
            textLocality = itemView.findViewById(R.id.textLocality);
            textStreet = itemView.findViewById(R.id.textStreet);
            textTotalAllPrice = itemView.findViewById(R.id.textTotalAllPrice);
        }
    }
}
