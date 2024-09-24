package com.example.fashionecommerce.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Address;
import com.example.fashionecommerce.model.DeliveryAddress;

import java.util.ArrayList;
import java.util.List;

public class OptionsAddressAdapter extends RecyclerView.Adapter<OptionsAddressAdapter.MyViewHolder> {
    @NonNull
    private OnClickListener onClickListener;
    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();

    public OptionsAddressAdapter(@NonNull List<DeliveryAddress> deliveryAddressList, OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.deliveryAddressList = deliveryAddressList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_options_choose_address, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == 0) {
            // Set the first radio button to checked
            holder.radioCheck.setChecked(true);
        } else {
            // Ensure other radio buttons are unchecked
            holder.radioCheck.setChecked(false);
        }

        DeliveryAddress deliveryAddress = deliveryAddressList.get(position);
        holder.textName.setText(deliveryAddress.getName());
        holder.textPhone.setText(deliveryAddress.getPhone());
        holder.textAddress.setText(deliveryAddress.getAddress());
        holder.textStreet.setText(deliveryAddress.getStreet());

        holder.itemView.setOnClickListener(v -> {
            onClickListener.OnClick(deliveryAddress);
        });
    }

    @Override
    public int getItemCount() {
        return deliveryAddressList.size();
    }
    public interface OnClickListener {
        void OnClick(DeliveryAddress deliveryAddress);
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        private RadioButton radioCheck;
        private TextView textName, textPhone, textAddress, textStreet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            radioCheck = itemView.findViewById(R.id.radioCheck);

            textName = itemView.findViewById(R.id.textName);
            textPhone = itemView.findViewById(R.id.textPhone);
            textAddress = itemView.findViewById(R.id.textAddress);
            textStreet = itemView.findViewById(R.id.textStreet);
        }
    }
}
