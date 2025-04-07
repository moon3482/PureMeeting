package com.example.mana.shopInfomation;

import static com.example.mana.R.color.appThemeColor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mana.CustomDialogRecommend;
import com.example.mana.R;
import com.example.mana.ServerIP;
import com.example.mana.chating.Client;
import com.example.mana.shopimage.ImageDataClass;
import com.example.mana.shopimage.ShopImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class ShopInfomation extends AppCompatActivity {
    ShopImageAdapter adapter;
    ViewPager viewPager;
    TextView ShopName, ShopAddress, ShopType, RcommendTime, tvRecommendDone, tvRecommendCancel;
    ArrayList<ImageDataClass> arrayList;
    ArrayList<ShopInfoMenuData> menuArrayList;
    RecyclerView recyclerView;
    ShopInfoAdapter shopInfoAdapter;
    String InType, username, roomnum, youid;
    Button btnReserve, RecommendOK, RecommendCencel;
    LinearLayout LilRecommend, Lilbutton;
    public static ShopInfomation activity = null;
    String reservtime, index, userid;
    Socket socket;
    DataOutputStream dataOutputStream;
    String imgurl;
    String SC;
    String name;
    String shoptype;
    String ad;
    String st;
    String et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_infomation);
        activity = this;
        ShopName = findViewById(R.id.tvShopName);
        ShopAddress = findViewById(R.id.tvShopAdressDetail);
        ShopType = findViewById(R.id.tvShopType);
        btnReserve = findViewById(R.id.btnreserve);
        RcommendTime = findViewById(R.id.tvReserveTime);
        RecommendCencel = findViewById(R.id.Recommend_Cencel);
        RecommendOK = findViewById(R.id.Recommend_Ok);
        LilRecommend = findViewById(R.id.LilReservetime);
        tvRecommendDone = findViewById(R.id.tvRecommendDone);
        tvRecommendCancel = findViewById(R.id.tvRecommendCancel);
        Lilbutton = findViewById(R.id.LilRecommendButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarShopInformation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("매장 정보");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        userid = sharedPreferences.getString("loginId", "");
        imgurl = sharedPreferences.getString("imgurl", "");
        arrayList = new ArrayList<>();
        menuArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.rcvshopinfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShopInfomation.this));
        shopInfoAdapter = new ShopInfoAdapter(menuArrayList, ShopInfomation.this);
        recyclerView.setAdapter(shopInfoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(ShopInfomation.this, 1));
        viewPager = (ViewPager) findViewById(R.id.shopInfoImageViewPager);
        Intent intent = getIntent();
        youid = intent.getStringExtra("youid");

        if (intent.hasExtra("Type")) {
            InType = intent.getStringExtra("Type");

            if (InType.equals("reserve")) {
                btnReserve.setVisibility(View.VISIBLE);
                LilRecommend.setVisibility(View.GONE);
            } else if (InType.equals("recommend")) {
                LilRecommend.setVisibility(View.VISIBLE);
                reservtime = intent.getStringExtra("reservetime");
                index = intent.getStringExtra("index");
                RecommendStatus(index);
            } else if (InType.equals("recommender")) {
                Lilbutton.setVisibility(View.GONE);
                reservtime = intent.getStringExtra("reservetime");
                index = intent.getStringExtra("index");
                RecommendStatus(index);
            } else if (InType.equals("search")) {
                LilRecommend.setVisibility(View.GONE);
                Lilbutton.setVisibility(View.GONE);
            }


        }

        SC = intent.getStringExtra("shopcode");
        name = intent.getStringExtra("shopname");
        shoptype = intent.getStringExtra("shoptype");
        ad = intent.getStringExtra("address");
        st = intent.getStringExtra("starttime");
        et = intent.getStringExtra("endtime");
        username = intent.getStringExtra("username");
        roomnum = intent.getStringExtra("roomnum");
        String imgurl = intent.getStringExtra("imgurl");
        ShopName.setText(name);
        ShopType.setText(shoptype);
        ShopAddress.setText(ad);
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogRecommend customDialogRecommend = new CustomDialogRecommend(ShopInfomation.this);
                customDialogRecommend.CallDialog(userid, username, imgurl, roomnum, SC, name, shoptype, ad, st, et, youid);
            }
        });
        RecommendOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopInfomation.this);
                builder.setTitle("추천 일정 확인");
                builder.setMessage("정말 수락을 하시겠습니까?");
                builder.setPositiveButton("수락", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scheduleCheck(userid, reservtime, "OK", index);

                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();


//                RecommendYesOrNo("OK", SC, name, shoptype, ad, st, et, roomnum, reservtime, index, username, imgurl, userid);
            }
        });
        RecommendCencel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopInfomation.this);
                builder.setTitle("추천 일정 확인");
                builder.setMessage("정말 거절을 하시겠습니까?");
                builder.setPositiveButton("거절", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RecommendYesOrNo("NO", index);
                                socket = new Socket();
                                try {
                                    socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
                                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                    dataOutputStream.writeUTF("map");
                                    dataOutputStream.writeUTF(userid);
                                    dataOutputStream.writeUTF(roomnum);
                                    JSONObject msg = new JSONObject();
                                    JSONObject root = new JSONObject();
                                    msg.put("shopcode", SC);
                                    msg.put("shopname", name);
                                    msg.put("shoptype", shoptype);
                                    msg.put("shopaddress", ad);
                                    msg.put("shopstarttime", st);
                                    msg.put("endtime", et);
                                    msg.put("roomnum", roomnum);
                                    msg.put("reservetime", reservtime);
                                    msg.put("index", index);
                                    msg.put("status", "2");
                                    root.put("type", "zone");
                                    root.put("name", username);
                                    root.put("img", imgurl);
                                    root.put("msg", msg);
                                    root.put("id", userid);
                                    root.put("room", roomnum);
                                    root.put("youid", youid);
                                    JSONArray jsonArray = new JSONArray();
                                    String okmsg = jsonArray.put(root).toString();
                                    dataOutputStream.writeUTF(okmsg);
                                    socket.close();
                                    sendFCM(username + "님이 추천을 거절하였습니다.");
                                    Intent intent1 = new Intent(ShopInfomation.this, Client.class);
                                    intent1.putExtra("youid", youid);
                                    intent1.putExtra("room", roomnum);
                                    startActivity(intent1);
                                    finish();

                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
//                RecommendYesOrNo("No", SC, name, shoptype, ad, st, et, roomnum, reservtime, index, username, imgurl, userid);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent.hasExtra("Type")) {
            InType = intent.getStringExtra("Type");

            if (InType.equals("reserve")) {
                btnReserve.setVisibility(View.VISIBLE);
            } else {
                btnReserve.setVisibility(View.GONE);
            }
            if (InType.equals("recommend")) {
                LilRecommend.setVisibility(View.VISIBLE);
                reservtime = intent.getStringExtra("reservetime");
                index = intent.getStringExtra("index");
                RecommendStatus(index);
            } else if (InType.equals("recommender")) {
                Lilbutton.setVisibility(View.GONE);
                reservtime = intent.getStringExtra("reservetime");
                index = intent.getStringExtra("index");
                RecommendStatus(index);
            } else {

            }
        }
        RcommendTime.setText("예약시간 : " + reservtime);
        LoadShopInfo(SC);
    }

    public void LoadShopInfo(String ShopCode) {
        String url = new ServerIP().http+"Android/LoadShopInfo.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("mainimage"));
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            ImageDataClass imageDataClass = new ImageDataClass(jsonObject1.getString("imagepath"));
//                            arrayList.add(imageDataClass);
//                        }
//                        JSONArray jsonArray1 = new JSONArray(jsonObject.getString("menuimage"));
//                        for (int i = 0; i < jsonArray1.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
//                            String imagepath = jsonObject1.getString("imagepath");
//                            String meunname = jsonObject1.getString("meunname");
//                            String price = jsonObject1.getString("price");
//                            String explanation = jsonObject1.getString("explanation");
//
//                            ShopInfoMenuData shopInfoMenuData = new ShopInfoMenuData(meunname, imagepath, price, explanation);
//                            menuArrayList.add(shopInfoMenuData);
//                        }
//                        shopInfoAdapter.notifyDataSetChanged();
//                        Log.e("메뉴 이미지", "" + jsonObject.getString("menuimage"));
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                adapter = new ShopImageAdapter(ShopInfomation.this, arrayList);
//                viewPager.setAdapter(adapter);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("shopcode", ShopCode);
//
//                return param;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(ShopInfomation.this);
//        requestQueue.add(stringRequest);
    }

    public void scheduleCheck(String id, String time, String type, String index) {
        String url = new ServerIP().http+"Android/scheduleCheck.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("리스폰받기", ""+response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        RecommendYesOrNo(type, index);
//                    } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ShopInfomation.this);
//                        builder.setMessage("이미 예정 된 스케쥴이 있습니다.");
//                        builder.setPositiveButton("확인", null);
//                        builder.show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.setShouldCache(false);
//        simpleMultiPartRequest.addStringParam("id", id);
//        simpleMultiPartRequest.addStringParam("time", time);
//        Log.e("시간", "" + time);
//        RequestQueue requestQueue = Volley.newRequestQueue(ShopInfomation.this);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void RecommendYesOrNo(String type, String index) {
        String url = new ServerIP().http+"Android/RecommendYesOrNo.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        if (jsonObject.getString("Type").equals("OK")) {
//                            Lilbutton.setVisibility(View.GONE);
//                            tvRecommendDone.setVisibility(View.VISIBLE);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//
////                                RecommendYesOrNo("OK", index);
//
//                                    try {
//                                        socket = new Socket();
//                                        socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
//                                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                                        dataOutputStream.writeUTF("map");
//                                        dataOutputStream.writeUTF(userid);
//                                        dataOutputStream.writeUTF(roomnum);
//                                        JSONObject msg = new JSONObject();
//                                        JSONObject root = new JSONObject();
//                                        msg.put("shopcode", SC);
//                                        msg.put("shopname", name);
//                                        msg.put("shoptype", shoptype);
//                                        msg.put("shopaddress", ad);
//                                        msg.put("shopstarttime", st);
//                                        msg.put("endtime", et);
//                                        msg.put("roomnum", roomnum);
//                                        msg.put("reservetime", reservtime);
//                                        msg.put("index", index);
//                                        msg.put("status", "1");
//                                        root.put("type", "zone");
//                                        root.put("name", username);
//                                        root.put("img", imgurl);
//                                        root.put("msg", msg);
//                                        root.put("id", userid);
//                                        root.put("room", roomnum);
//                                        root.put("youid", youid);
//                                        JSONArray jsonArray = new JSONArray();
//                                        String okmsg = jsonArray.put(root).toString();
//                                        Log.e("수락보내기", "" + okmsg);
//                                        dataOutputStream.writeUTF(okmsg);
//                                        socket.close();
//                                        sendFCM(username + "님이 추천을 수락하였습니다.");
//                                        Intent intent1 = new Intent(ShopInfomation.this, Client.class);
//                                        intent1.putExtra("youid", youid);
//                                        intent1.putExtra("room", roomnum);
//                                        startActivity(intent1);
//
//                                    } catch (IOException | JSONException e) {
//
//                                    }
//                                }
//                            }).start();
//                        } else if (jsonObject.getString("Type").equals("NO")) {
//                            Lilbutton.setVisibility(View.GONE);
//                            tvRecommendCancel.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.addStringParam("Type", type);
//        simpleMultiPartRequest.addStringParam("index", index);
//        RequestQueue requestQueue = Volley.newRequestQueue(ShopInfomation.this);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void RecommendStatus(String index) {
        String url = new ServerIP().http+"Android/RecommendStatus.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1") || status.equals("5")) {
//                            Lilbutton.setVisibility(View.GONE);
//                            tvRecommendDone.setVisibility(View.VISIBLE);
//                        } else if (status.equals("2")) {
//                            Lilbutton.setVisibility(View.GONE);
//                            tvRecommendCancel.setVisibility(View.VISIBLE);
//                        } else {
//
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.addStringParam("index", index);
//        RequestQueue requestQueue = Volley.newRequestQueue(ShopInfomation.this);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void sendFCM(String msg) {

        String url = new ServerIP().http+"Android/GetMsgToken.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.e("FCM보내기", "" + response);
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        FCMsend(jsonObject.getString("token"), msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.addStringParam("youid", youid);
//        RequestQueue requestQueue = Volley.newRequestQueue(ShopInfomation.this);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void FCMsend(String token, String msg) {
        final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
        final String SERVER_KEY = "AAAAUNI4Hf4:APA91bFglA8a3jd2AN4tVMBUj1BMDcca2EwI4DxElRQ4ky64fjjTGFlwxOYRqSGr4DbZv0f5qfhqwgqntthe6e-oF2jjzlDyXNy8xx65_0UglGqX1ofGo_4Du2YsogT1hnTUECRthsNi";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    JSONObject root = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("fromid", userid);
                    data.put("type", "msg");
                    data.put("room", roomnum);
                    data.put("message", msg);
                    data.put("title", "부없만 새로운메세지");
                    root.put("to", token);
                    root.put("data", data);


//                    root.put("to", token);
                    Log.e("FCM생성", root.toString());
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                onBackPressed();
                return true;


            default:
//                Toast.makeText(Client.this, "문제 있음", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}