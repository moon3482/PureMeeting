package com.example.mana.chating;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.SocketKeepalive;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import com.example.mana.MainActivity;
import com.example.mana.R;
import com.example.mana.SendFaceTalk;
import com.example.mana.ShopInfomation.ShopInfomation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chatingadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<chatdata> arrayList = null;


    chatdata chatdata;
    private Context context;


    public Chatingadapter(ArrayList<chatdata> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("뷰타입", String.valueOf(viewType));
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem_my, parent, false);
                return new CustomViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitemimage, parent, false);
                return new CustomViewHolder2(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem_you, parent, false);
                return new CustomViewHolder3(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem_you_image, parent, false);
                return new CustomViewHolder4(view);
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem_zone, parent, false);
                return new CustomViewHolder5(view);
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem_zone_you, parent, false);
                return new CustomViewHolder6(view);
            case 6:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finalzone, parent, false);
                return new CustomViewHolder7(view);
            case 7:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choicemy, parent, false);
                return new CustomViewHolder8(view);
            case 8:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choiceyou, parent, false);
                return new CustomViewHolder9(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem_my, parent, false);
        return new CustomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", context.MODE_PRIVATE);
        String id = sharedPreferences.getString("loginId", "");
        String username = sharedPreferences.getString("loginName", "");
        String imgurl = sharedPreferences.getString("imgurl", "");
        String myname = sharedPreferences.getString("loginName", "");
        chatdata chatdata = arrayList.get(position);
        Log.e("타입", chatdata.type);
        Log.e("아이디", id);
        if (chatdata.id.equals(id) && chatdata.type.equals("msg")) {
            CustomViewHolder holder1 = (CustomViewHolder) holder;
            holder1.msgText.setText(chatdata.msg);
        } else if (chatdata.id.equals(id) && chatdata.type.equals("img")) {
            CustomViewHolder2 holder2 = (CustomViewHolder2) holder;
            GradientDrawable drawable =
                    (GradientDrawable) context.getDrawable(R.drawable.serverchat);
            holder2.imageViewMy.setBackground(drawable);
            holder2.imageViewMy.setClipToOutline(true);
            Glide.with(holder.itemView.getContext()).load(chatdata.msg).into(holder2.imageViewMy);
        } else if (chatdata.type.equals("msg") && !chatdata.id.equals(id)) {
            CustomViewHolder3 holder3 = (CustomViewHolder3) holder;
            Glide.with(holder3.itemView.getContext()).load(chatdata.img).into(holder3.imageViewYourProfile);
            holder3.textViewYourName.setText(chatdata.name);
            holder3.textViewYourMsg.setText(chatdata.msg);
        } else if (chatdata.type.equals("img") && !chatdata.id.equals(id)) {
            CustomViewHolder4 holder4 = (CustomViewHolder4) holder;
            GradientDrawable drawable =
                    (GradientDrawable) context.getDrawable(R.drawable.serverchat);
            holder4.imageViewYourImg.setBackground(drawable);
            holder4.imageViewYourImg.setClipToOutline(true);


            Glide.with(holder4.itemView.getContext()).load(chatdata.img).into(holder4.imageViewYourProfile);
            Glide.with(holder4.itemView.getContext()).load(chatdata.msg).into(holder4.imageViewYourImg);
            holder4.textViewYourName.setText(chatdata.name);
        } else if (chatdata.id.equals(id) && chatdata.type.equals("zone")) {
            CustomViewHolder5 holder5 = (CustomViewHolder5) holder;
            try {
                JSONObject jsonObject = new JSONObject(chatdata.msg);
                if (jsonObject.getString("status").equals("0")) {
                    holder5.MyItemName.setText(chatdata.name + "님이 장소 추천을 하셧습니다.");
                } else if (jsonObject.getString("status").equals("1")) {
                    holder5.MyItemName.setText(chatdata.name + "님이 장소 추천을 수락 하셧습니다.");
                    holder5.ShopDetail.setVisibility(View.GONE);
                    holder5.Facetalk.setVisibility(View.VISIBLE);
                } else {
                    holder5.MyItemName.setText(chatdata.name + "님이 장소 추천을 거절 하셧습니다.");
                    holder5.btncancel.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder5.Facetalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(chatdata.msg);
//                        Intent intent = new Intent(context, SendFaceTalk.class);
//                        intent.putExtra("room", chatdata.room);
//                        intent.putExtra("youid", chatdata.youid);
//                        intent.putExtra("name", myname);
//                        intent.putExtra("index", jsonObject.getString("index"));
//                        context.startActivity(intent);
//                        ((Activity) context).finish();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        JSONObject jsonObject1 = new JSONObject(chatdata.msg);
                        String url = "http://3.36.21.126/Android/mediahit.php";
                        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        if (Integer.parseInt(jsonObject.getString("hit")) == 99) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("선택완료");
                                            builder.setMessage("이미 선택이 완료되었습니다.");
                                            builder.setPositiveButton("확인", null);
                                            builder.show();

                                        } else if (Integer.parseInt(jsonObject.getString("hit")) >= 6) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("횟수초과");
                                            builder.setMessage("해당 추천의 허용가능한 페이스톡 횟수를 초과하였습니다.");
                                            builder.setPositiveButton("확인", null);
                                            builder.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("가능횟수");
                                            String hit = jsonObject.getString("hit");
                                            builder.setMessage("페이스톡 가능횟수" + String.valueOf((Integer.parseInt(hit) / 2)) + "/3\n\n페이스톡을 진행하시겠습니까?");
                                            builder.setPositiveButton("진행", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    try {
                                                        Intent intent = new Intent(context, SendFaceTalk.class);
                                                        intent.putExtra("room", chatdata.room);
                                                        intent.putExtra("youid", chatdata.youid);
                                                        intent.putExtra("name", chatdata.name);
                                                        intent.putExtra("index", jsonObject1.getString("index"));
                                                        context.startActivity(intent);
                                                        ((Activity) context).finish();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                            builder.setNeutralButton("취소", null);
                                            builder.show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        simpleMultiPartRequest.setShouldCache(false);
                        simpleMultiPartRequest.addStringParam("index", jsonObject1.getString("index"));
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(simpleMultiPartRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            holder5.ShopDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shopinfo = chatdata.msg;
                    try {
                        JSONObject jsonObject = new JSONObject(shopinfo);
                        Intent intent = new Intent(context, ShopInfomation.class);
                        intent.putExtra("Type", "recommender");
                        intent.putExtra("shopcode", jsonObject.getString("shopcode"));
                        intent.putExtra("shopname", jsonObject.getString("shopname"));
                        intent.putExtra("shoptype", jsonObject.getString("shoptype"));
                        intent.putExtra("address", jsonObject.getString("shopaddress"));
                        intent.putExtra("starttime", jsonObject.getString("shopstarttime"));
                        intent.putExtra("endtime", jsonObject.getString("endtime"));
                        intent.putExtra("roomnum", jsonObject.getString("roomnum"));
                        intent.putExtra("reservetime", jsonObject.getString("reservetime"));
                        intent.putExtra("index", jsonObject.getString("index"));
                        intent.putExtra("status", jsonObject.getString("status"));
                        intent.putExtra("username", username);
                        intent.putExtra("imgurl", imgurl);

                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } else if (!chatdata.id.equals(id) && chatdata.type.equals("zone")) {
            CustomViewHolder6 holder6 = (CustomViewHolder6) holder;
            Glide.with(holder6.itemView.getContext()).load(chatdata.img).into(holder6.profileimg);
            holder6.YouName.setText(chatdata.name);
            try {
                JSONObject jsonObject = new JSONObject(chatdata.msg);
                if (jsonObject.getString("status").equals("0")) {
                    holder6.YouItemName.setText(chatdata.name + "님이 장소 추천을 하셧습니다.");
                } else if (jsonObject.getString("status").equals("1")) {
                    holder6.YouItemName.setText(chatdata.name + "님이 장소 추천을 수락 하셧습니다.");
                    holder6.YouShopDetail.setVisibility(View.GONE);
                    holder6.YouFaceTalk.setVisibility(View.VISIBLE);
                } else {
                    holder6.YouItemName.setText(chatdata.name + "님이 장소 추천을 거절 하셧습니다.");
                    holder6.YoubtnCancel.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder6.YouFaceTalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        JSONObject jsonObject1 = new JSONObject(chatdata.msg);
                        String url = "http://3.36.21.126/Android/mediahit.php";
                        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        if (Integer.parseInt(jsonObject.getString("hit")) == 99) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("선택완료");
                                            builder.setMessage("이미 선택이 완료되었습니다.");
                                            builder.setPositiveButton("확인", null);
                                            builder.show();

                                        } else if (Integer.parseInt(jsonObject.getString("hit")) >= 6) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("횟수초과");
                                            builder.setMessage("해당 추천의 허용가능한 페이스톡 횟수를 초과하였습니다.");
                                            builder.setPositiveButton("확인", null);
                                            builder.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("가능횟수");
                                            String hit = jsonObject.getString("hit");
                                            builder.setMessage("페이스톡 가능횟수" + String.valueOf((Integer.parseInt(hit) / 2)) + "/3\n\n페이스톡을 진행하시겠습니까?");
                                            builder.setPositiveButton("진행", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    try {
                                                        Intent intent = new Intent(context, SendFaceTalk.class);
                                                        intent.putExtra("room", chatdata.room);
                                                        intent.putExtra("youid", chatdata.id);
                                                        intent.putExtra("name", chatdata.name);
                                                        intent.putExtra("index", jsonObject1.getString("index"));
                                                        context.startActivity(intent);
                                                        ((Activity) context).finish();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                            builder.setNeutralButton("취소", null);
                                            builder.show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        simpleMultiPartRequest.setShouldCache(false);
                        simpleMultiPartRequest.addStringParam("index", jsonObject1.getString("index"));
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(simpleMultiPartRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            holder6.YouShopDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shopinfo = chatdata.msg;
                    try {
                        JSONObject jsonObject = new JSONObject(shopinfo);
                        Intent intent = new Intent(context, ShopInfomation.class);
                        intent.putExtra("Type", "recommend");
                        intent.putExtra("shopcode", jsonObject.getString("shopcode"));
                        intent.putExtra("shopname", jsonObject.getString("shopname"));
                        intent.putExtra("shoptype", jsonObject.getString("shoptype"));
                        intent.putExtra("address", jsonObject.getString("shopaddress"));
                        intent.putExtra("starttime", jsonObject.getString("shopstarttime"));
                        intent.putExtra("endtime", jsonObject.getString("endtime"));
                        intent.putExtra("roomnum", jsonObject.getString("roomnum"));
                        intent.putExtra("reservetime", jsonObject.getString("reservetime"));
                        intent.putExtra("index", jsonObject.getString("index"));
                        intent.putExtra("status", jsonObject.getString("status"));
                        intent.putExtra("youid", chatdata.id);
                        intent.putExtra("username", username);
                        intent.putExtra("imgurl", imgurl);

                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if (chatdata.type.equals("finalok")) {
            CustomViewHolder7 holder7 = (CustomViewHolder7) holder;
            holder7.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("인덱스", ""+chatdata.msg);
                    loadshop(chatdata.msg);
                }
            });
        } else if (chatdata.type.equals("finalcancel")) {
            CustomViewHolder7 holder7 = (CustomViewHolder7) holder;
            holder7.title.setText("예약이 성사되지 못 했습니다.");
            holder7.button.setVisibility(View.GONE);
        } else if (chatdata.id.equals(id) && (chatdata.type.equals("ChooseOK") || chatdata.type.equals("ChooseCancel"))) {
            CustomViewHolder8 holder8 = (CustomViewHolder8) holder;
            if (chatdata.type.equals("ChooseOK")) {
                holder8.title.setText(chatdata.name + "님 수락완료");
            } else {
                holder8.title.setText(chatdata.name + "님 거절");
            }
            Glide.with(context).load(chatdata.img).into(holder8.circleImageView);
        } else if (!chatdata.id.equals(id) && (chatdata.type.equals("ChooseOK") || chatdata.type.equals("ChooseCancel"))) {
            CustomViewHolder9 holder9 = (CustomViewHolder9) holder;
            holder9.name.setText(chatdata.name);
            if (chatdata.type.equals("ChooseOK")) {
                holder9.title.setText(chatdata.name + "님 수락완료");
            } else {
                holder9.title.setText(chatdata.name + "님 거절");
            }
            Glide.with(context).load(chatdata.img).into(holder9.imageView);
            Glide.with(context).load(chatdata.img).into(holder9.circleImageView);

        }
    }


//        if (chatdata.getType().equals("msg")) {
//            if (chatdata.getId().equals("Server")) {
//                holder.youpro.setVisibility(View.GONE);
//                holder.mypro.setVisibility(View.GONE);
//                holder.serverChatTextView.setText(chatdata.getMsg());
//            } else if (chatdata.getId().equals(id)) {
//                holder.youpro.setVisibility(View.GONE);
//                holder.serverChat.setVisibility(View.GONE);
//                holder.mymsg.setText(chatdata.getMsg());
//                holder.myChaImg.setVisibility(View.GONE);
//
//            } else {
//                holder.mypro.setVisibility(View.GONE);
//                holder.serverChat.setVisibility(View.GONE);
//
//                Glide.with(holder.itemView.getContext())
//                        .load("http://3.36.21.126/Android/" + chatdata.getImg())
//                        .override(Target.SIZE_ORIGINAL)
//                        .into(holder.img);
//
//
//                holder.msgText.setText(chatdata.getMsg());
//                holder.inname.setText(chatdata.getName());
//                holder.youChatImg.setVisibility(View.GONE);
//                holder.img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), Image.class);
//                        intent.putExtra("img", arrayList.get(position).getImg());
//                        ((Activity) v.getContext()).startActivityForResult(intent, 300);
//                    }
//                });
//
//            }
//        }
//        if (chatdata.getType().equals("img")) {
//            if (chatdata.getId().equals("Server")) {
//                holder.youpro.setVisibility(View.GONE);
//                holder.mypro.setVisibility(View.GONE);
//                holder.serverChatTextView.setText(chatdata.getMsg());
//            } else if (chatdata.getId().equals(id)) {
//                holder.youpro.setVisibility(View.GONE);
//                holder.serverChat.setVisibility(View.GONE);
//                holder.myProChat.setVisibility(View.GONE);
//
//                Glide.with(holder.itemView.getContext())
//                        .load(chatdata.getMsg())
//                        .override(Target.SIZE_ORIGINAL)
//                        .into(holder.myChaImg);
//
//
//            } else {
//
//                holder.mypro.setVisibility(View.GONE);
//                holder.serverChat.setVisibility(View.GONE);
//
//                Glide.with(holder.itemView.getContext())
//                        .load("http://3.36.21.126/Android/" + chatdata.getImg())
//                        .override(Target.SIZE_ORIGINAL)
//                        .into(holder.img);
//                Glide.with(holder.itemView.getContext())
//                        .load(chatdata.getMsg())
//                        .override(Target.SIZE_ORIGINAL)
//                        .into(holder.youChatImg);
//
//                holder.youChatRelative.setVisibility(View.GONE);
//                holder.inname.setText(chatdata.getName());
////                holder.youChatImg.setVisibility(View.GONE);
//                holder.img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), Image.class);
//                        intent.putExtra("img", arrayList.get(position).getImg());
//                        ((Activity) v.getContext()).startActivityForResult(intent, 300);
//                    }
//                });

//            }
//        }
//}

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", context.MODE_PRIVATE);
        String id = sharedPreferences.getString("loginId", "");
        chatdata chatdata = arrayList.get(position);
        if (chatdata.id.equals(id) && chatdata.type.equals("msg")) {
            return 0;
        } else if (chatdata.id.equals(id) && chatdata.type.equals("img")) {
            return 1;
        } else if (chatdata.type.equals("msg")) {
            return 2;
        } else if (chatdata.type.equals("img")) {
            return 3;
        } else if (chatdata.id.equals(id) && chatdata.type.equals("zone")) {
            return 4;
        } else if (!chatdata.id.equals(id) && chatdata.type.equals("zone")) {
            return 5;
        } else if (chatdata.type.equals("finalok") || chatdata.type.equals("finalcancel")) {
            return 6;
        } else if (chatdata.id.equals(id) && (chatdata.type.equals("ChooseOK") || chatdata.type.equals("ChooseCancel"))) {
            return 7;
        } else {
            return 8;
        }


    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {


        TextView msgText;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.msgText = itemView.findViewById(R.id.mychat);

        }
    }

    public class CustomViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imageViewMy;

        public CustomViewHolder2(@NonNull View itemView) {
            super(itemView);
            this.imageViewMy = itemView.findViewById(R.id.myChatImg);

        }
    }

    public class CustomViewHolder3 extends RecyclerView.ViewHolder {
        ImageView imageViewYourProfile;
        TextView textViewYourName;
        TextView textViewYourMsg;

        public CustomViewHolder3(@NonNull View itemView) {
            super(itemView);
            this.imageViewYourProfile = itemView.findViewById(R.id.chatprofile);
            this.textViewYourName = itemView.findViewById(R.id.chat_name);
            this.textViewYourMsg = itemView.findViewById(R.id.you_chat);
        }
    }

    public class CustomViewHolder4 extends RecyclerView.ViewHolder {
        ImageView imageViewYourProfile;
        TextView textViewYourName;
        ImageView imageViewYourImg;

        public CustomViewHolder4(@NonNull View itemView) {
            super(itemView);
            this.imageViewYourProfile = itemView.findViewById(R.id.chatprofile_img);
            this.textViewYourName = itemView.findViewById(R.id.chat_name_img);
            this.imageViewYourImg = itemView.findViewById(R.id.youChatImg);
        }
    }

    public class CustomViewHolder5 extends RecyclerView.ViewHolder {

        TextView MyItemName;
        Button ShopDetail, Facetalk, btncancel;

        public CustomViewHolder5(@NonNull View itemView) {
            super(itemView);
            MyItemName = itemView.findViewById(R.id.MyzoneaddTextItem);
            ShopDetail = itemView.findViewById(R.id.btnShopDetail);
            Facetalk = itemView.findViewById(R.id.btnfacetalk);
            btncancel = itemView.findViewById(R.id.btncancel);

        }
    }

    public class CustomViewHolder6 extends RecyclerView.ViewHolder {
        ImageView profileimg;
        TextView YouName, YouItemName;
        Button YouShopDetail, YouFaceTalk, YoubtnCancel;

        public CustomViewHolder6(@NonNull View itemView) {
            super(itemView);
            profileimg = itemView.findViewById(R.id.chatprofile_zone_img);
            YouName = itemView.findViewById(R.id.tvYouChatZoneYou);
            YouItemName = itemView.findViewById(R.id.ZoneItemTextViewYou);
            YouShopDetail = itemView.findViewById(R.id.btnYouShopDetail);
            YouFaceTalk = itemView.findViewById(R.id.Youbtnfacetalk);
            YoubtnCancel = itemView.findViewById(R.id.Youbtncancel);

        }
    }

    public class CustomViewHolder7 extends RecyclerView.ViewHolder {
        TextView title;
        Button button;

        public CustomViewHolder7(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvfinal);
            button = itemView.findViewById(R.id.btnfinal);
        }
    }

    public class CustomViewHolder8 extends RecyclerView.ViewHolder {
        TextView title;
        ImageView circleImageView;

        public CustomViewHolder8(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvChoiceMy);
            circleImageView = itemView.findViewById(R.id.cvChoiceMy);
        }
    }

    public class CustomViewHolder9 extends RecyclerView.ViewHolder {
        TextView title, name;
        ImageView circleImageView, imageView;

        public CustomViewHolder9(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvChoiceYouOr);
            name = itemView.findViewById(R.id.tvChoiceYou);
            circleImageView = itemView.findViewById(R.id.cvChoiceYou);
            imageView = itemView.findViewById(R.id.chatprofile_Choice_img);
        }
    }

    public void mediahit(String index) {
        String url = "http://3.36.21.126/Android/mediahit.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("index", index);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
    }

    public void loadshop(String index) {
        String url = "http://3.36.21.126/Android/Shoploadinfo.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("loginId", Context.MODE_PRIVATE);
                        String imgurl = sharedPreferences.getString("imgurl", "");
                        String name = sharedPreferences.getString("loginName", "");
                        Intent intent = new Intent(context, ShopInfomation.class);
                        intent.putExtra("Type", "recommender");
                        intent.putExtra("index", jsonObject.getString("index"));
                        intent.putExtra("shopcode", jsonObject.getString("shopcode"));
                        intent.putExtra("shopname", jsonObject.getString("shopname"));
                        intent.putExtra("shoptype", jsonObject.getString("shoptype"));
                        intent.putExtra("address", jsonObject.getString("shopaddress"));
                        intent.putExtra("starttime", jsonObject.getString("shopstarttime"));
                        intent.putExtra("endtime", jsonObject.getString("endtime"));
                        intent.putExtra("username", name);
                        intent.putExtra("roomnum", jsonObject.getString("roomnum"));
                        intent.putExtra("imgurl", imgurl);
                        intent.putExtra("reservetime", jsonObject.getString("reservetime"));
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("index", index);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
    }
}
