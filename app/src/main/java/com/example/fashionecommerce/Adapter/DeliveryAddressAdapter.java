package com.example.fashionecommerce.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.DeliveryAddress;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.MyViewHolder> {
    @NonNull
    private List<DeliveryAddress> deliveryList = new ArrayList<>();
    private OnClickListener onClickListener;

    public DeliveryAddressAdapter(@NonNull List<DeliveryAddress> deliveryList, OnClickListener onClickListener) {
        this.deliveryList = deliveryList;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_address, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DeliveryAddress deliveryAddress = deliveryList.get(position);
        holder.textName.setText(deliveryAddress.getName());
        holder.textPhone.setText(deliveryAddress.getPhone());
        holder.textAddress.setText(deliveryAddress.getLocation());
        holder.textStreet.setText(deliveryAddress.getStreet() + " - " + deliveryAddress.getLocality());
        holder.itemView.setOnClickListener(v -> {
            onClickListener.OnClick(deliveryAddress);
        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }
    public interface OnClickListener{
        void OnClick(DeliveryAddress deliveryAddress);
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textName, textPhone, textAddress, textStreet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPhone = itemView.findViewById(R.id.textPhone);
            textAddress = itemView.findViewById(R.id.textAddress);
            textStreet = itemView.findViewById(R.id.textStreet);
        }
    }
}
