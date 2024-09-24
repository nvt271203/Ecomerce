package com.example.fashionecommerce.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.UploadImage;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderProductAdapter extends SliderViewAdapter<SliderProductAdapter.MyViewHolder> {
    private List<UploadImage> imageList = new ArrayList<>();

    public SliderProductAdapter(List<UploadImage> imageList) {
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        UploadImage uploadImage = imageList.get(position);
        Picasso.get().load(uploadImage.getPathUrlSelected()).into(viewHolder.imgSlider);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    static class MyViewHolder extends SliderViewAdapter.ViewHolder{
        private ImageView imgSlider;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgSlider = itemView.findViewById(R.id.imgSlider);
        }
    }
}
