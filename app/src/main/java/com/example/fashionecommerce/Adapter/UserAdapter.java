package com.example.fashionecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.Helper.GetMaskHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Chat;
import com.example.fashionecommerce.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    @NonNull
    private List<User> userList = new ArrayList<>();
    private OnClickListener onClickListener;
    private Context context;

    public UserAdapter(List<User> userList, Context context, OnClickListener onClickListener) {
        this.userList = userList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListUser = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
        return new MyViewHolder(itemListUser);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!userList.isEmpty()) {
            User user = userList.get(position);
            holder.textName.setText(user.getName());

//            if (chat.getMessage() != null) {
//                holder.textLastMessage.setVisibility(View.VISIBLE);
//                holder.textTime.setVisibility(View.VISIBLE);
//
//                holder.textLastMessage.setText(chat.getMessage());
//                holder.textTime.setText(GetMaskHelper.getDate(chat.getDataTime(), 2));
//            } else {
//                holder.textLastMessage.setVisibility(View.GONE);
//                holder.textTime.setVisibility(View.GONE);
//            }

            holder.itemView.setOnClickListener(view -> onClickListener.onClick(user.getId()));
        }

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }


    public interface OnClickListener{
        void onClick(String idReceiver);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser;
        private TextView textName;
        private TextView textLastMessage;
        private ImageView imgStatusOnline;
        private ImageView imgStatusOffline;
        private TextView textTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            textName = itemView.findViewById(R.id.textName);
            textLastMessage = itemView.findViewById(R.id.textLastMessage);
            imgStatusOnline = itemView.findViewById(R.id.imgStatusOnline);
            imgStatusOffline = itemView.findViewById(R.id.imgStatusOffline);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}
