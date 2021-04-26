package com.example.mana.ShopInfomation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mana.R;

import java.util.ArrayList;


public class ShopInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ShopInfoMenuData> arrayList = null;
    Context context;

    public ShopInfoAdapter(ArrayList<ShopInfoMenuData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_info_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShopInfoMenuData shopInfoMenuData = arrayList.get(position);
        ViewHolder holder1 = (ViewHolder) holder;
        Glide.with(context).load(shopInfoMenuData.getImagepath()).placeholder(R.drawable.basicprofile).into(holder1.ivshopinfo);
        holder1.menuName.setText(shopInfoMenuData.getMeunname());
        holder1.menuExplanation.setText(shopInfoMenuData.getExplanation());
        holder1.menuPrice.setText(shopInfoMenuData.getPrice()+"원");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivshopinfo;
        TextView menuName, menuExplanation, menuPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivshopinfo = itemView.findViewById(R.id.ivShopinfoMenuImage);
            menuName = itemView.findViewById(R.id.ManuName);
            menuExplanation = itemView.findViewById(R.id.tvShopMenu);
            menuPrice = itemView.findViewById(R.id.tvmenuPrice);
        }
    }
}
