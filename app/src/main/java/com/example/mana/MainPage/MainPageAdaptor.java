package com.example.mana.MainPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.mana.R;
import com.example.mana.chating.Client;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainPageAdaptor extends RecyclerView.Adapter<MainPageAdaptor.MainPageViewHolder> {
    private ArrayList<MainLoadDataClass> arrayList = null;
    private Context context;
    MainLoadDataClass mainLoadDataClass;
    MainPageAdaptor.MainPageViewHolder holder;


    public MainPageAdaptor(ArrayList<MainLoadDataClass> arrayList) {
        this.arrayList = arrayList;
    }

    public MainPageAdaptor(Context context) {
        this.context = context;


    }

    @NonNull
    @Override
    public MainPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_item, parent, false);
        holder = new MainPageAdaptor.MainPageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainPageViewHolder holder, int position) {

        mainLoadDataClass = arrayList.get(position);
        holder.userid = mainLoadDataClass.getUserid();

        String[] realage = mainLoadDataClass.getAge().split("-");
        holder.tvMainPageItemName.setText(mainLoadDataClass.name);
        holder.tvMainPageItemAge.setText(String.valueOf(getAge(Integer.parseInt(realage[0]), Integer.parseInt(realage[1]), Integer.parseInt(realage[2])))+"세");
        holder.tvMainPageItemArea.setText(mainLoadDataClass.getArea());
        Glide.with(holder.itemView.getContext())
                .load( mainLoadDataClass.profileImg)
                .into(holder.ivMainPageItemProfileImg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            holder.LilyMainItem.setOutlineAmbientShadowColor(Color.parseColor("#E91E63"));
            holder.LilyMainItem.setOutlineSpotShadowColor(Color.parseColor("#E91E63"));
        }
        holder.LilyMainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), IdealProfileLoad.class);
                intent.putExtra("id", holder.userid);
                ((Activity) v.getContext()).startActivity(intent);
                /**대화신청  다이얼로그(실험 로직) **/
                //                AlertDialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
//                dialog = builder.setTitle("대화신청").setMessage(mainLoadDataClass.getName() + "님에게 대화신청을 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String url = new ServerIP().http+"Android/GetToken.php";
//                        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                try {
//                                    JSONObject jsonObject1 = new JSONObject(response);
//                                    boolean success = jsonObject1.getBoolean("success");
//                                    if(success){
//                                        FCMsend(jsonObject1.getString("token"));
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//                        });
//                        simpleMultiPartRequest.addStringParam("id", holder.userid);
//                        RequestQueue requestQueue = Volley.newRequestQueue(holder.itemView.getContext());
//                        requestQueue.add(simpleMultiPartRequest);
//                    }
//                }).show();
/**채팅방 넘어가는 로직(실험 로직)**/
//                String GetRommUrl = new ServerIP().http+"Android/GetRoomNumber.php";
//                SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, GetRommUrl, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if(success){
//                                Log.e("방번호",jsonObject.getString("RoomNumber"));
//                                Intent intent = new Intent(v.getContext(), Client.class);
//                                intent.putExtra("room",jsonObject.getString("RoomNumber"));
//                                intent.putExtra("name",jsonObject.getString("name"));
//                                ((Activity) v.getContext()).startActivity(intent);
//                            }else {
//                                Log.e("실패", "실패");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("loginId", Context.MODE_PRIVATE);
//
//                String id = sharedPreferences.getString("loginId", "");
//                Log.e("뷰클릭", id);
//                Log.e("뷰클릭", holder.userid);
//                simpleMultiPartRequest.addStringParam("userfrom", id);
//                simpleMultiPartRequest.addStringParam("userto", holder.userid);
//                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
//                requestQueue.add(simpleMultiPartRequest);
            }
        });

    }

    public int getAge(int birthYear, int birthMonth, int birthDay) {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear + 1;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MainPageViewHolder extends RecyclerView.ViewHolder {
        String userid;
        ImageView ivMainPageItemProfileImg;
        TextView tvMainPageItemName;
        TextView tvMainPageItemAge;
        TextView tvMainPageItemArea;
        LinearLayout LilyMainItem;

        public MainPageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMainPageItemProfileImg = itemView.findViewById(R.id.ivMainPageItemImg);
            tvMainPageItemName = itemView.findViewById(R.id.tvMainPageItemName);
            tvMainPageItemAge = itemView.findViewById(R.id.tvMainPageItemAge);
            tvMainPageItemArea = itemView.findViewById(R.id.tvMainPageItemArea);
            LilyMainItem = itemView.findViewById(R.id.LilyMainItem);
        }


    }

    public void FCMsend(String token) {
        final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
        final String SERVER_KEY = "AAAAUNI4Hf4:APA91bFglA8a3jd2AN4tVMBUj1BMDcca2EwI4DxElRQ4ky64fjjTGFlwxOYRqSGr4DbZv0f5qfhqwgqntthe6e-oF2jjzlDyXNy8xx65_0UglGqX1ofGo_4Du2YsogT1hnTUECRthsNi";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", "대화초대메세지가 왔습니다.");
                    notification.put("title", "부없만");
                    root.put("notification", notification);
                    root.put("to", token);
                    Log.e("FCM생성",root.toString());
                    // FMC 메시지 생성 end

                    URL Url = new URL(FCM_URL);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
