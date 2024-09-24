package com.example.fashionecommerce.AdapterAddress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Locality;
import com.example.fashionecommerce.model.Localtion;

import java.util.ArrayList;
import java.util.List;

public class LocalityAdapter extends RecyclerView.Adapter<LocalityAdapter.MyViewHolder>  {
    private List<Locality> localityList = new ArrayList<>();
    private final OnClickListener onClickListener;

    public LocalityAdapter(List<Locality> localityList, OnClickListener onClickListener){
        this.localityList = localityList;
        this.onClickListener = onClickListener;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_locality, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Locality locality = localityList.get(position);
        holder.textLocality.setText(locality.getNameLocality());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, district.getNameDistrict(), Toast.LENGTH_SHORT).show();
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.OnClick(locality);
            }
        });
    }
    public interface OnClickListener{
        void OnClick(Locality locality);
    }

    @Override
    public int getItemCount() {
        return localityList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textLocality;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textLocality = itemView.findViewById(R.id.textLocality);
        }
    }
}
