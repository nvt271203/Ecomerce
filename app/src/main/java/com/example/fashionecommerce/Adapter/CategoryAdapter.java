package com.example.fashionecommerce.Adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder > {
    @NonNull
    private List<Category> categoryList = new ArrayList<>();
    private OnClickListener onClickListener;
//    update
    private int layout;
    private boolean background;
    private int itemSelected = 0;

    public CategoryAdapter(@NonNull List<Category> categoryList, OnClickListener onClickListener, int layout, boolean background) {
        this.categoryList = categoryList;
        this.onClickListener = onClickListener;
        this.layout = layout;
        this.background = background;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textCategory.setText(category.getName());
        Picasso.get().load(category.getUrl()).into(holder.imgCategory);


        if (background){
//            holder.itemView.setBackgroundResource(R.drawable.bg_item_menu_selected);
            holder.itemView.setOnClickListener(v -> {
                onClickListener.OnClick(category, "itemView");
                itemSelected = holder.getAdapterPosition();
                notifyDataSetChanged();
            });

            if (itemSelected == holder.getAdapterPosition()){
//                holder.itemView.setBackgroundResource(R.color.bg_primary);
                holder.itemView.setBackgroundResource(R.drawable.bg_radius20_selected_category);
                holder.imgCategory.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                holder.textCategory.setTextColor(Color.parseColor("#FFFFFF"));
            }else {
                holder.itemView.setBackgroundResource(R.drawable.bg_radius20_un_selected_category);
                holder.imgCategory.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
                holder.textCategory.setTextColor(Color.parseColor("#000000"));
            }
        }else {
            holder.itemView.setOnClickListener(v -> onClickListener.OnClick(category, "itemView"));
            holder.imgEdit.setOnClickListener(v -> onClickListener.OnClick(category, "edit"));
            holder.imgDelete.setOnClickListener(v -> onClickListener.OnClick(category, "delete"));

        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public interface OnClickListener {
        void OnClick(Category category, String type);

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgCategory, imgEdit, imgDelete;
        private TextView textCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
//            imgEdit = itemView.findViewById(R.id.imgEdit);
//            imgDelete = itemView.findViewById(R.id.imgDelete);
            textCategory = itemView.findViewById(R.id.textCategory);
        }
    }
}
