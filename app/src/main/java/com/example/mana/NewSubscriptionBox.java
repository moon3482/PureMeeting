package com.example.mana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.ChatPage.chatPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mana.R.color.appThemeColor;

public class NewSubscriptionBox extends AppCompatActivity {
    RecyclerView recyclerView;
    NewSubscriptionAdapter newSubscriptionAdapter;
    ArrayList<NewSubscriptionData> arrayList = new ArrayList<>();
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription_box);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewSubscription);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("초대함");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        linearLayout = findViewById(R.id.LilNoneNewMessage);
        recyclerView = findViewById(R.id.rcvNewSubscription);
        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(NewSubscriptionBox.this));
        newSubscriptionAdapter = new NewSubscriptionAdapter(arrayList, NewSubscriptionBox.this, recyclerView, linearLayout);
        recyclerView.setAdapter(newSubscriptionAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(NewSubscriptionBox.this, 1));

        LoadNewSubscription();
    }

    public void LoadNewSubscription() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String ID = sharedPreferences.getString("loginId", "");
        String url = new ServerIP().http+"Android/LoadNewMessage.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("메시지박스", response);
                    JSONObject jsonObject1 = new JSONObject(response);
                    boolean success = jsonObject1.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("어레이 사이즈", String.valueOf(i));
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String img = jsonObject2.getString("profileimg");
                            String name = jsonObject2.getString("name");
                            NewSubscriptionData newSubscriptionData = new NewSubscriptionData(ID, id, img, name);
                            arrayList.add(newSubscriptionData);


                        }
                        newSubscriptionAdapter.notifyDataSetChanged();

                        if (arrayList.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
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
        Log.e("메시지박스", ID);
        simpleMultiPartRequest.addStringParam("id", ID);
        RequestQueue requestQueue = Volley.newRequestQueue(NewSubscriptionBox.this);
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