package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.ItemSuggest;

import java.util.ArrayList;
import java.util.List;

public class DataSuggestAdapter extends RecyclerView.Adapter<DataSuggestAdapter.MyViewHolder> {
    @NonNull
    private List<ItemSuggest> suggestList = new ArrayList<>();
    private OnClickListener onClickListener;

    public DataSuggestAdapter(@NonNull List<ItemSuggest> suggestList, OnClickListener onClickListener) {
        this.suggestList = suggestList;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemSuggest itemSuggest = suggestList.get(position);
        holder.textSuggest.setText(itemSuggest.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.OnClick(itemSuggest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestList.size();
    }
    public interface OnClickListener{
        void OnClick(ItemSuggest suggest);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textSuggest;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textSuggest = itemView.findViewById(R.id.textSuggest);
        }
    }
}
