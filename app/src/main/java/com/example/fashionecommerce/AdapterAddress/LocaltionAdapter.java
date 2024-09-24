package com.example.fashionecommerce.AdapterAddress;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Localtion;

import java.util.List;

public class LocaltionAdapter extends RecyclerView.Adapter<LocaltionAdapter.MyViewHolder> {
    @NonNull
    private List<Localtion> localtionListList;
    private Context context;
    private OnClickListener onClickListener;
    public LocaltionAdapter(List<Localtion> localtionListList, Context context, OnClickListener onClickListener){
        this.localtionListList = localtionListList;
        this.context = context;
        this.onClickListener = onClickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_localtion, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Localtion localtion = localtionListList.get(position);
        holder.textProvinceCity.setText(localtion.getNameLocaltion());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Localtion.class);
//                intent.putExtra("dataProvince", localtion.getIdLocaltion());
//                Toast.makeText(context, localtion.getNameLocaltion(), Toast.LENGTH_SHORT).show();
//                context.startActivity(intent);
//            }
//        });
        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(localtion));
    }

    @Override
    public int getItemCount() {
        return localtionListList.size();
    }
    public interface OnClickListener{
        void OnClick(Localtion localtion);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textProvinceCity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProvinceCity = itemView.findViewById(R.id.textLocaltion);
        }
    }
}
