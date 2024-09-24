package com.example.fashionecommerce.Adapter;

import static android.telephony.PhoneNumberUtils.WAIT;
import static android.view.PixelCopy.SUCCESS;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Oder;
import com.example.fashionecommerce.model.StatusOder;

import java.util.ArrayList;

public class StatusOderAdapterUser extends RecyclerView.Adapter<StatusOderAdapterUser.MyViewHolder> {
    @NonNull
    private ArrayList<Oder> oderList = new ArrayList<>();
    private Context context;
    private Drawable waitDrawable;
    private OnClickListener onClickListener;

    public StatusOderAdapterUser(@NonNull ArrayList<Oder> oderList, Context context, OnClickListener onClickListener) {
        this.oderList = oderList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_oder_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Oder oder = oderList.get(position);
        holder.textName.setText(oder.getAddressDelivery().getName());
        holder.textQuantity.setText(String.valueOf(oder.getItemOderList().size()));
        holder.textPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(oder.getTotal())));
        holder.textDayTime.setText(GetMaskHelper.getDate(oder.getDataOder(), 2));
        holder.textStatus.setText(StatusOder.getStatus(oder.getStatus()));

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.OnClick("CANCEL", oder);
            }
        });

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.OnClick("DETAIL", oder);

            }
        });

        switch (oder.getStatus()){

            case WAIT:
                holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.wait));

                holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getDrawable(R.drawable.wait), null);

                // Lấy drawable và thay đổi màu
                waitDrawable = ContextCompat.getDrawable(context, R.drawable.wait);
                if (waitDrawable != null) waitDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.wait), PorterDuff.Mode.SRC_IN));
                // Thiết lập drawable đã thay đổi màu vào TextView
                holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, waitDrawable, null);

                break;
            case SUCCESS:
                holder.btnCancel.setVisibility(View.GONE);
//                holder.itemView.setBackgroundColor(Color.parseColor("#1000080FF"));
//                holder.itemView.setBackgroundColor(Color.parseColor("#10000000"));
                holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.success));

                // Lấy drawable và thay đổi màu
                waitDrawable = ContextCompat.getDrawable(context, R.drawable.success);

                if (waitDrawable != null) waitDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.success), PorterDuff.Mode.SRC_IN));
                // Thiết lập drawable đã thay đổi màu vào TextView
                holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, waitDrawable, null);

                break;
            default:
                holder.btnCancel.setVisibility(View.GONE);


                holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.refuse));

                // Lấy drawable và thay đổi màu
                waitDrawable = ContextCompat.getDrawable(context, R.drawable.refuse);

                if (waitDrawable != null) waitDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.refuse), PorterDuff.Mode.SRC_IN));
                // Thiết lập drawable đã thay đổi màu vào TextView
                holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, waitDrawable, null);

                break;
        }
    }

    @Override
    public int getItemCount() {
        return oderList.size();
    }
    public interface OnClickListener{
        void OnClick(String action, Oder oder);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textName, textQuantity, textPrice,textDayTime, textStatus;
        private Button btnCancel, btnDetail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            textDayTime = itemView.findViewById(R.id.textDayTime);
            textStatus = itemView.findViewById(R.id.textStatus);

            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
