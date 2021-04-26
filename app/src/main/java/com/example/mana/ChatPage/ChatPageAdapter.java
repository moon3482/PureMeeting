package com.example.mana.ChatPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mana.R;
import com.example.mana.chating.Client;

import java.util.ArrayList;

public class ChatPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatListData> arrayList = null;
    private Context context;

    public ChatPageAdapter(ArrayList<ChatListData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chating_list_item, parent, false);
        return new ChattingListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatListData chatListData = arrayList.get(position);
        ChattingListHolder holder1 = (ChattingListHolder) holder;
        Glide.with(holder1.itemView.getContext()).load(chatListData.profileimg).into(holder1.profileimg);
        holder1.name.setText(chatListData.getName());
        if (chatListData.lastMessage.equals("no")) {
            holder1.lastmessage.setText("대화를 시작해보세요.");
        } else {
            holder1.lastmessage.setText(chatListData.lastMessage);
        }

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ClickRoom", chatListData.roomnum);
                editor.putString("youid", chatListData.id);
                editor.apply();
                Intent intent = new Intent(context, Client.class);
//                intent.putExtra("room", chatListData.getRoomnum());
//                intent.putExtra("name", name);
//                intent.putExtra("youid", chatListData.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ChattingListHolder extends RecyclerView.ViewHolder {
        String roomnum;
        TextView name, lastmessage;
        ImageView profileimg;

        public ChattingListHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chating_list_name);
            profileimg = itemView.findViewById(R.id.cvChatProfileImg);
            lastmessage = itemView.findViewById(R.id.tvChatPageLastMessage);
        }
    }
}
