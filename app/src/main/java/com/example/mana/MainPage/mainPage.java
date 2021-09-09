package com.example.mana.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.MainActivity;
import com.example.mana.NewSubscriptionBox;
import com.example.mana.R;
import com.example.mana.main.MyPage;
import com.example.mana.ChatPage.chatPage;
import com.example.mana.main.mapPage;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mana.R.color.appThemeColor;

public class mainPage extends AppCompatActivity {
    private ArrayList<MainLoadDataClass> arrayList;
    private MainPageAdaptor mainPageAdaptor;
    private RecyclerView recyclerView;
    private ArrayList<MainLoadDataClass> arrayList2;
    private MainPageAdaptor2 mainPageAdaptor2;
    private RecyclerView recyclerView2;
    ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton, ivNewMessage;
    ImageButton ibtnNewSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        /********선언부*********/
        ivNewMessage = findViewById(R.id.newSubscription);
        ibtnNewSubscription = findViewById(R.id.btnNewSubscription);
        ivHomeButton = (ImageView) findViewById(R.id.bnHomeButton);
        ivMessageBoxButton = findViewById(R.id.bnMessageBoxButton);
        ivMapButton = findViewById(R.id.bnMapButton);
        ivMyPageButton = findViewById(R.id.bnMyPageButton);
        /************이미지뷰에 버튼 이미지 set**************/
        ivHomeButton.setImageResource(R.drawable.ic_baseline_home_24);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMapButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
        DrawableCompat.setTint(ivMessageBoxButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
        /********선택 이미지뷰 디자인 변경******/
        DrawableCompat.setTint(ivHomeButton.getDrawable(), ContextCompat.getColor(mainPage.this, R.color.White));
        ivHomeButton.setBackgroundColor(Color.parseColor("#E91E63"));
        /************툴바 선언부*********/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMainPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("부없만");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        recyclerView = findViewById(R.id.MainPageRecy);
        recyclerView2 = findViewById(R.id.MainPagePreviousRecy);

        /*****메세지박스버튼********/
        ivMessageBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainPage.this, chatPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        /*******맵 버튼************/
        ivMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainPage.this, mapPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        /**********마이페이지 버튼***********/
        ivMyPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainPage.this, MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        ibtnNewSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainPage.this, NewSubscriptionBox.class);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mainPage.this);
        dialog = builder.setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mainPage.this, MainActivity.class);
                        Toast.makeText(mainPage.this, "로그아웃이 되었습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivHomeButton = (ImageView) findViewById(R.id.bnHomeButton);
        ivMessageBoxButton = findViewById(R.id.bnMessageBoxButton);
        ivMapButton = findViewById(R.id.bnMapButton);
        ivMyPageButton = findViewById(R.id.bnMyPageButton);
        ivHomeButton.setImageResource(R.drawable.ic_baseline_home_24);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivHomeButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
    }

    private void loadmain() {


        String url = "http://3.36.21.126/Android/lllo.php";
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String id = sharedPreferences.getString("loginId", "");
        System.out.println(id);

        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject2 = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject2.getString("user"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String age = jsonObject.getString("birthday");
                        String area = jsonObject.getString("area");
                        String img = jsonObject.getString("proFileImg");
                        String userid = jsonObject.getString("userid");
                        MainLoadDataClass mainLoadDataClass = new MainLoadDataClass(name, img, age, area, userid);
                        arrayList.add(mainLoadDataClass);
                    }
                    mainPageAdaptor.notifyDataSetChanged();
                    JSONArray jsonArray1 = new JSONArray(jsonObject2.getString("after"));
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(j);
                        String name = jsonObject.getString("name");
                        String age = jsonObject.getString("birthday");
                        String area = jsonObject.getString("area");
                        String img = jsonObject.getString("proFileImg");
                        String userid = jsonObject.getString("userid");
                        MainLoadDataClass mainLoadDataClass = new MainLoadDataClass(name, img, age, area, userid);
                        arrayList2.add(mainLoadDataClass);
                    }
                    mainPageAdaptor2.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                TextView tv = (TextView)findViewById(R.id.tvMainPageItemName);
//                                tv.setText(response);
//                            }
//                        });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("userID", id);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(simpleMultiPartRequest);
    }


    @Override
    protected void onStart() {

        super.onStart();
        LoadNewSub();
        /************이미지뷰에 버튼 이미지 set**************/
        ivHomeButton.setImageResource(R.drawable.ic_baseline_home_24);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMapButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
        DrawableCompat.setTint(ivMessageBoxButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(mainPage.this, appThemeColor));
        /********선택 이미지뷰 디자인 변경******/
        DrawableCompat.setTint(ivHomeButton.getDrawable(), ContextCompat.getColor(mainPage.this, R.color.White));
        ivHomeButton.setBackgroundColor(Color.parseColor("#E91E63"));
        /*********오늘의 리사이클러뷰 선언부*********/

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        mainPageAdaptor = new MainPageAdaptor(arrayList);
        recyclerView.setAdapter(mainPageAdaptor);
        /*********이전추천 리사이클러뷰 선언부*********/

        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        arrayList2 = new ArrayList<>();
        mainPageAdaptor2 = new MainPageAdaptor2(arrayList2);
        recyclerView2.setAdapter(mainPageAdaptor2);

        loadmain();
    }

    public void LoadNewSub() {

        String url = "http://3.36.21.126/Android/loadNewMessageNumRow.php";
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String id = sharedPreferences.getString("loginId", "");
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        if (jsonObject.getInt("num") > 0) {
                            ivNewMessage.setVisibility(View.VISIBLE);
                        } else {
                            ivNewMessage.setVisibility(View.GONE);
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
        simpleMultiPartRequest.addStringParam("id", id);
        RequestQueue requestQueue = Volley.newRequestQueue(mainPage.this);
        requestQueue.add(simpleMultiPartRequest);
    }
}