package com.example.fashionecommerce.Adapter;

import android.content.Context;
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

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Oder;
import com.example.fashionecommerce.model.StatusOder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatusOderAdapterAdmin extends RecyclerView.Adapter<StatusOderAdapterAdmin.MyViewHolder> {
    @NonNull
    private ArrayList<Oder> oderList = new ArrayList<>();
    private Context context;
    private OnClickListener onClickListener;

    public StatusOderAdapterAdmin(@NonNull ArrayList<Oder> oderList, Context context, OnClickListener onClickListener) {
        this.oderList = oderList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_oder_admin, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Oder oder = oderList.get(position);
//        configStoreOderUser(oder, holder);
        
        if (oder.getAddressDelivery() != null) {
            holder.textName.setText(oder.getAddressDelivery().getName());
        }
        holder.textQuantity.setText(String.valueOf(oder.getItemOderList().size()));
        holder.textPrice.setText(context.getString(R.string.price, GetMaskHelper.getValue(oder.getTotal())));
        holder.textDayTime.setText(GetMaskHelper.getDate(oder.getDataOder(), 2));

        StatusOder status = oder.getStatus();
        if (status != null) {
            switch (status) {
                case WAIT:

                    holder.btnStatus.setVisibility(View.VISIBLE);

                    holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.wait));

                    Drawable waitDrawable = ContextCompat.getDrawable(context, R.drawable.wait);
                    if (waitDrawable != null) {
                        waitDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.wait), PorterDuff.Mode.SRC_IN));
                        holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, waitDrawable, null);
                    }
                    break;

                case SUCCESS:
                    holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.success));

                    Drawable successDrawable = ContextCompat.getDrawable(context, R.drawable.success);
                    if (successDrawable != null) {
                        successDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.success), PorterDuff.Mode.SRC_IN));
                        holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, successDrawable, null);
                    }

//                    set default
                    holder.btnStatus.setVisibility(View.GONE);
                    break;

                default:

                    holder.btnStatus.setVisibility(View.GONE);
                    holder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.refuse));

                    Drawable refuseDrawable = ContextCompat.getDrawable(context, R.drawable.refuse);
                    if (refuseDrawable != null) {
                        refuseDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.refuse), PorterDuff.Mode.SRC_IN));
                        holder.textStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, refuseDrawable, null);
                    }
                    break;
            }
            holder.textStatus.setText(StatusOder.getStatus(status));
        }



        holder.btnStatus.setOnClickListener(v -> onClickListener.OnClick(oder, "STATUS"));
        holder.btnDetail.setOnClickListener(v -> onClickListener.OnClick(oder, "DETAIL"));
        
        
    }


    @Override
    public int getItemCount() {
        return oderList.size();
    }

    public interface OnClickListener {
        void OnClick(Oder oder, String action);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textName, textQuantity, textPrice, textDayTime, textStatus;
        private Button btnStatus, btnDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            textDayTime = itemView.findViewById(R.id.textDayTime);
            textStatus = itemView.findViewById(R.id.textStatus);
            btnStatus = itemView.findViewById(R.id.btnStatus);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
