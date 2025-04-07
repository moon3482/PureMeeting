package com.example.mana.chatPage;

import static com.example.mana.R.color.appThemeColor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mana.mainPage.MainPage;
import com.example.mana.R;
import com.example.mana.ServerIP;
import com.example.mana.chating.Client;
import com.example.mana.main.MyPage;
import com.example.mana.main.MapPage;

import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {
    public ArrayList<ChatListData> arrayList;
    LinearLayout LilNullMassage, LilMassageRcv;
    RecyclerView recyclerView;
    public ChatPageAdapter chatPageAdapter;
    ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
    public static Context chatcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        chatcontext = this;
        Intent intent = getIntent();
        if (intent.hasExtra("notifi")) {
            if (intent.getStringExtra("notifi").equals("gomsg")) {
                Intent intent1 = new Intent(ChatPage.this, Client.class);
                intent1.putExtra("room", intent.getStringExtra("room"));
                intent1.putExtra("youid", intent.getStringExtra("youid"));
                startActivity(intent1);

            }
        }
        /********선언부*********/
        ivHomeButton = findViewById(R.id.MbnHomeButton);
        ivMessageBoxButton = findViewById(R.id.MbnMessageBoxButton);
        ivMapButton = findViewById(R.id.MbnMapButton);
        ivMyPageButton = findViewById(R.id.MbnMyPageButton);

        LilNullMassage = findViewById(R.id.LilNullmassege);
        LilMassageRcv = findViewById(R.id.LilNullmassegeRcv);

        /************툴바 선언부*********/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChatPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("대화");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        recyclerView = findViewById(R.id.rcvMassegePage);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatPage.this));
        /****메인페이지버튼********/
        ivHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatPage.this, MainPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        /*******맵 버튼************/
        ivMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatPage.this, MapPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        /**********마이페이지 버튼***********/
        ivMyPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatPage.this, MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        /************이미지뷰에 버튼 이미지 set**************/
        ivHomeButton.setImageResource(R.drawable.ic_baseline_home_24);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMapButton.getDrawable(), ContextCompat.getColor(ChatPage.this, appThemeColor));
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(ChatPage.this, appThemeColor));
        DrawableCompat.setTint(ivHomeButton.getDrawable(), ContextCompat.getColor(ChatPage.this, appThemeColor));
        /********선택 이미지뷰 디자인 변경******/
        DrawableCompat.setTint(ivMessageBoxButton.getDrawable(), ContextCompat.getColor(ChatPage.this, R.color.White));
        ivMessageBoxButton.setBackgroundColor(Color.parseColor("#E91E63"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadChatList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivMessageBoxButton = findViewById(R.id.MbnMessageBoxButton);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        DrawableCompat.setTint(ivMessageBoxButton.getDrawable(), ContextCompat.getColor(ChatPage.this, appThemeColor));
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatPage.this, MainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String id = sharedPreferences.getString("loginId", "");
        String url = new ServerIP().http+"Android/LoadChatList.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("채팅방", response);
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    Log.e("어레이", String.valueOf(jsonArray));
//
//
//                    arrayList = new ArrayList<>();
//                    chatPageAdapter = new ChatPageAdapter(arrayList, chatPage.this);
//                    recyclerView.setAdapter(chatPageAdapter);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        Log.e("어레이", String.valueOf(i));
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String roomnum = jsonObject.getString("RoomNumber");
//                        String name = jsonObject.getString("name");
//                        String profileimg = jsonObject.getString("profileimg");
//                        String id = jsonObject.getString("id");
//                        String lastmessage = jsonObject.getString("lastmessage");
//                        ChatListData chatListData = new ChatListData(roomnum, name, profileimg, id, lastmessage);
//                        arrayList.add(chatListData);
//                    }
//                    chatPageAdapter.notifyDataSetChanged();
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
//        simpleMultiPartRequest.setShouldCache(false);
//        simpleMultiPartRequest.addStringParam("id", id);
//        RequestQueue requestQueue = Volley.newRequestQueue(chatPage.this);
//        requestQueue.add(simpleMultiPartRequest);
    }
}