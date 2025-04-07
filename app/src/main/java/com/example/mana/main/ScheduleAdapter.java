package com.example.mana.main;

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
import com.example.mana.mainPage.IdealProfileLoad;
import com.example.mana.R;
import com.example.mana.shopInfomation.ShopInfomation;

import java.util.ArrayList;


public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ScheduleData> arrayList = new ArrayList<>();

    public ScheduleAdapter(Context context, ArrayList<ScheduleData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleData scheduleData = arrayList.get(position);
        CustomViewHolder holder1 = (CustomViewHolder) holder;
        holder1.name.setText(scheduleData.name);
        if (Integer.parseInt(scheduleData.dday) == 0) {
            holder1.dday.setVisibility(View.GONE);
            holder1.ddday.setVisibility(View.VISIBLE);
        } else {
            holder1.ddday.setVisibility(View.GONE);
            holder1.dday.setVisibility(View.VISIBLE);
            holder1.dday.setText("D-day\n" + scheduleData.dday);
        }
        holder1.date.setText(scheduleData.reservetime);

        Glide.with(context).load(scheduleData.profilethumimg).into(holder1.circleView);
        holder1.profiledetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdealProfileLoad.class);
                intent.putExtra("id", scheduleData.id);
                intent.putExtra("shinchung", "id");
                context.startActivity(intent);
            }
        });
        holder1.shopdetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("loginName", "");
                Intent intent = new Intent(context, ShopInfomation.class);
                intent.putExtra("Type", "recommender");
                intent.putExtra("index", scheduleData.index);
                intent.putExtra("shopcode", scheduleData.shopcode);
                intent.putExtra("shopname", scheduleData.shopname);
                intent.putExtra("shoptype", scheduleData.shoptype);
                intent.putExtra("address", scheduleData.shopaddress);
                intent.putExtra("starttime", scheduleData.shopstarttime);
                intent.putExtra("endtime", scheduleData.endtime);
                intent.putExtra("username", username);
                intent.putExtra("roomnum", scheduleData.roomnum);
                intent.putExtra("imgurl", scheduleData.profilethumimg);
                intent.putExtra("reservetime", scheduleData.reservetime);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView circleView;
        TextView name;
        TextView profiledetailbtn, shopdetailbtn;
        TextView dday, date, ddday;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            circleView = itemView.findViewById(R.id.cvScheduleProfileimage);
            name = itemView.findViewById(R.id.tvScheduleName);
            profiledetailbtn = itemView.findViewById(R.id.tvScheduleProfileDetailbtn);
            shopdetailbtn = itemView.findViewById(R.id.tvScheduleShopDetailbtn);
            date = itemView.findViewById(R.id.tvScheduleNamedate);
            dday = itemView.findViewById(R.id.tvScheduleDday);
            ddday = itemView.findViewById(R.id.tvScheduleDDday);

        }
    }
}
