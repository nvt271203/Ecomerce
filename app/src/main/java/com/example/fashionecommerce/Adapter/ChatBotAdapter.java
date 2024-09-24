package com.example.fashionecommerce.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionecommerce.Activity.DetailProductActivity;
import com.example.fashionecommerce.FragmentUser.MessageUserFragment;
import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.R;
import com.example.fashionecommerce.model.Chat;
import com.example.fashionecommerce.model.ItemSuggest;
import com.example.fashionecommerce.model.Product;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.MyViewHolder> implements DataSuggestAdapter.OnClickListener, ProductChatAdapter.OnClickListener {
    private List<Chat> chatList = new ArrayList<>();
//    private List<Product> productFilterList = new ArrayList<>();
    private OnClickListener onClickListener;
    private Context context;

    @SuppressLint("NotifyDataSetChanged")
    public ChatBotAdapter(List<Chat> chatList, Context context, OnClickListener onClickListener) {
        this.chatList = chatList;

//        this.productFilterList = productFilterList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        if (chat.getIdSender().equals(FirebaseHelper.getUIDpersonCurrent())){
            holder.textMessageRight.setVisibility(View.VISIBLE);
            holder.textMessageRight.setText(chat.getMessage());

            holder.imgChatBot.setVisibility(View.GONE);
            holder.textMessageLeft.setVisibility(View.GONE);

            if (chat.getPathImage() != null){
                holder.imgPictureSend.setVisibility(View.VISIBLE);
                Picasso.get().load(chat.getPathImage()).into(holder.imgPictureSend);
            }


        }else {
            holder.imgChatBot.setVisibility(View.VISIBLE);
            holder.textMessageLeft.setVisibility(View.VISIBLE);
            holder.textMessageLeft.setText(chat.getMessage());

            holder.textMessageRight.setVisibility(View.GONE);




            if (chat.getListProductFilter() != null){    //hiển thị danh sách sản phẩm được lọc
                holder.RVdataSuggest.setVisibility(View.VISIBLE);
                holder.RVdataSuggest.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
                holder.RVdataSuggest.setHasFixedSize(true);
                ProductChatAdapter productChatAdapter = new ProductChatAdapter(chat.getListProductFilter(), this);
                holder.RVdataSuggest.setAdapter(productChatAdapter);
            }
            if (chat.getListSuggest() != null){  // hiển thị danh sách gợi ý
                holder.RVdataSuggest.setVisibility(View.VISIBLE);

                holder.RVdataSuggest.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
                holder.RVdataSuggest.setHasFixedSize(true);
                DataSuggestAdapter dataSuggestAdapter = new DataSuggestAdapter(chat.getListSuggest(), this);
                holder.RVdataSuggest.setAdapter(dataSuggestAdapter);
                }
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void OnClick(ItemSuggest itemSuggest) {
//        Toast.makeText(context, textMessage.toString(), Toast.LENGTH_SHORT).show();
        Chat chat = new Chat();
        chat.setMessage(itemSuggest.getName());
        chat.setIdSender(FirebaseHelper.getUIDpersonCurrent());
        chat.setIdRecever("idBotChatAI");
        chatList.add(chat);
        notifyDataSetChanged();
//        chat.store();
//        context.startActivity(new Intent(context, MessageUserFragment.class));

//        Intent intent = new Intent(context, MessageUserFragment.class);
//        intent.putExtra("dataTextmessageSend", textMessage);
//        context.startActivity(intent);
        if (onClickListener != null) {
            onClickListener.returnMessageSend(itemSuggest);
        }

    }

    @Override
    public void OnClick(Product product) {
        Intent intent = new Intent(context, DetailProductActivity.class);
        intent.putExtra("productSelected", product);
        context.startActivity(intent);
    }

    public interface OnClickListener{
        void showRespond();
        void returnMessageSend(ItemSuggest itemSuggest);
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textMessageLeft, textMessageRight;
        public ImageView imgChatBot, imgPictureSend;
        public RecyclerView RVdataSuggest;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageLeft = itemView.findViewById(R.id.textMessageLeft);
            textMessageRight = itemView.findViewById(R.id.textMessageRight);
            imgChatBot = itemView.findViewById(R.id.imgChatBot);
            imgPictureSend = itemView.findViewById(R.id.imgSend);
            RVdataSuggest = itemView.findViewById(R.id.RVdataSuggest);

        }
    }
}
