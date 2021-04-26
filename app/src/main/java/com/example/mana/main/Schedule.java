package com.example.mana.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.ChatPage.chatPage;
import com.example.mana.NewSubscriptionAdapter;
import com.example.mana.NewSubscriptionBox;
import com.example.mana.R;
import com.example.mana.ZoneAdd;
import com.example.mana.chating.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.mana.R.color.appThemeColor;

public class Schedule extends AppCompatActivity {
    RecyclerView recyclerView;
    ScheduleAdapter scheduleAdapter;
    ArrayList<ScheduleData> arrayList;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSchedule);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내 일정");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        recyclerView = findViewById(R.id.rcvSchedule);
    }

    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        id = sharedPreferences.getString("loginId", "");
        super.onStart();
        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(Schedule.this));
        scheduleAdapter = new ScheduleAdapter(Schedule.this, arrayList);
        recyclerView.setAdapter(scheduleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(Schedule.this, 1));
        loadSchedule(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadSchedule(String id) {
        String url = "http://3.36.21.126/Android/loadSchedule.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String shopcode = jsonObject1.getString("shopcode");
                            String shopname = jsonObject1.getString("shopname");
                            String shoptype = jsonObject1.getString("shoptype");
                            String shopaddress = jsonObject1.getString("shopaddress");
                            String shopstarttime = jsonObject1.getString("shopstarttime");
                            String endtime = jsonObject1.getString("endtime");
                            String roomnum = jsonObject1.getString("roomnum");
                            String reservetime = jsonObject1.getString("reservetime");
                            String index = jsonObject1.getString("index");
                            String name = jsonObject1.getString("name");
                            String profilethumimg = jsonObject1.getString("profilethumimg");
                            String dday = jsonObject1.getString("dday");

                            ScheduleData scheduleData = new ScheduleData(id, shopcode, shopname, shoptype, shopaddress, shopstarttime, endtime, roomnum, reservetime, index, name, profilethumimg, dday);
                            arrayList.add(scheduleData);
                        }
                        scheduleAdapter.notifyDataSetChanged();
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
        simpleMultiPartRequest.addStringParam("id", id);
        RequestQueue requestQueue = Volley.newRequestQueue(Schedule.this);
        requestQueue.add(simpleMultiPartRequest);
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