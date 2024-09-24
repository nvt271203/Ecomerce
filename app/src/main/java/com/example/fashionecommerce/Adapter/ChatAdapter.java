package com.example.fashionecommerce.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    @NonNull
    private List<Chat> listChat = new ArrayList<>();


    public ChatAdapter(List<Chat> listChat) {
        this.listChat = listChat;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!listChat.isEmpty()) {
            Chat chat = listChat.get(position);

            String idSender = chat.getIdSender();
            if (idSender != null && idSender.equals(FirebaseHelper.getUIDpersonCurrent())){
                holder.textMessageRight.setVisibility(View.VISIBLE);
                holder.textMessageRight.setText(chat.getMessage());

                holder.imgReceiver.setVisibility(View.GONE);
                holder.textMessageLeft.setVisibility(View.GONE);

//                if (chat.getPathImage() != null){
//                    holder.imgPictureSend.setVisibility(View.VISIBLE);
//                    Picasso.get().load(chat.getPathImage()).into(holder.imgPictureSend);
//                }


            }else {
                holder.imgReceiver.setVisibility(View.VISIBLE);
                holder.textMessageLeft.setVisibility(View.VISIBLE);
                holder.textMessageLeft.setText(chat.getMessage());

                holder.textMessageRight.setVisibility(View.GONE);





            }

        }

    }


    @Override
    public int getItemCount() {
        return listChat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textMessageLeft, textMessageRight;
        public ImageView imgReceiver, imgPictureSend;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageLeft = itemView.findViewById(R.id.textMessageLeft);
            textMessageRight = itemView.findViewById(R.id.textMessageRight);
            imgReceiver = itemView.findViewById(R.id.imgReceiver);
//            imgPictureSend = itemView.findViewById(R.id.imgSend);

        }
    }
}
