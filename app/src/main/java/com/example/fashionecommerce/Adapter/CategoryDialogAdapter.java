package com.example.fashionecommerce.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.MyViewHolder > {
    @NonNull
    private List<String> idsCategoriesListSelected = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private OnClickListener onClickListener;

    public CategoryDialogAdapter(@NonNull List<String> idsCategoriesListSelected, List<Category> categoryList, OnClickListener onClickListener) {
        this.idsCategoriesListSelected = idsCategoriesListSelected;
        this.categoryList = categoryList;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_dialog, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textCategory.setText(category.getName());
        Picasso.get().load(category.getUrl()).into(holder.imgCategory);

        if (idsCategoriesListSelected.contains(category.getId())){
            holder.checkbox.setChecked(true);
        }

//        Tại mỗi lần nhấn vào itemView thì ta cần toggle trạng thái hiện có
//        Tức sẳn là true, Phủ định lại là false

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//       Cách 1 lỗi:
//                holder.checkbox.setChecked(!holder.checkbox.isChecked());
//                if (holder.checkbox.isChecked()){
//                    Log.i("checkbox", "true");
//                    idsCategoriesSelected.add(category.getId());
//                }else {
//                    Log.i("checkbox", "false");
//                    idsCategoriesSelected.remove(category.getId());
//                }
                holder.checkbox.setChecked(!holder.checkbox.isChecked()); // Thay đổi trạng thái của checkbox
                boolean isChecked = holder.checkbox.isChecked(); // Lưu trạng thái hiện tại của checkbox
                if (isChecked){
                    Log.i("checkbox", "true");
                    idsCategoriesListSelected.add(category.getId());
                    for (String str : idsCategoriesListSelected) {
                        Log.i("checkbox", str +"--");
                    }
                } else {
                    Log.i("checkbox", "false");
                    idsCategoriesListSelected.remove(category.getId());
                }



                onClickListener.OnClick(category, idsCategoriesListSelected);
//                holder.checkbox.setChecked(!holder.checkbox.isChecked());

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public interface OnClickListener {
        void OnClick(Category category, List<String> idsCategoriesListSeclected);

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgCategory;
        private TextView textCategory;
        private CheckBox checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            textCategory = itemView.findViewById(R.id.textCategory);
            checkbox = itemView.findViewById(R.id.checkBox);
        }
    }
}
