package com.example.mana.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mana.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mana.R.color.appThemeColor;

public class IdealProfileLoad extends AppCompatActivity {
    TextView tvIdealInfoHeight, tvIdealInfoAge, tvIdealInfoWeight, tvIdealInfoArea, tvIdealInfoJabGroup, tvIdealInfoJab, tvIdealInfoHobby, tvIdealInfoForte, tvIdealInfoFavorite, tvIdealInfoDislike, tvIdealInfoDrink;
    CircleImageView  ProfileImage1, ProfileImage2, ProfileImage3, ProfileImage4, ProfileImage5;
    Button btnChatSubscription;
    String RecommendName, RecommendToken, RecommendID, id, name;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideal_profile_load);
        tvIdealInfoHeight = findViewById(R.id.tvIdealinfoPageHeght);
        tvIdealInfoAge = findViewById(R.id.tvIdealInfoPageAge);
        tvIdealInfoWeight = findViewById(R.id.tvIdealInfoPageWeight);
        tvIdealInfoArea = findViewById(R.id.tvIdealInfoPageArea);
        tvIdealInfoJabGroup = findViewById(R.id.tvIdealInfoPageJabGroup);
        tvIdealInfoJab = findViewById(R.id.tvIdealInfoPageJab);
        tvIdealInfoHobby = findViewById(R.id.tvIdealInfoPageHobby);
        tvIdealInfoForte = findViewById(R.id.tvIdealInfoPageForte);
        tvIdealInfoFavorite = findViewById(R.id.tvIdealInfoPageFavorite);
        tvIdealInfoDislike = findViewById(R.id.tvIdealInfoPagedislike);
        tvIdealInfoDrink = findViewById(R.id.tvIdealInfoPageDrink);
        ProfileImage1 = findViewById(R.id.cvProfileImage1);
        ProfileImage2 = findViewById(R.id.cvProfileImage2);
        ProfileImage3 = findViewById(R.id.cvProfileImage3);
        ProfileImage4 = findViewById(R.id.cvProfileImage4);
        ProfileImage5 = findViewById(R.id.cvProfileImage5);
        btnChatSubscription = findViewById(R.id.btnChatSubScription);
        intent = getIntent();

        if (intent.hasExtra("shinchung")) {
            btnChatSubscription.setVisibility(View.GONE);
        } else {
            btnChatSubscription.setVisibility(View.VISIBLE);
        }
        RecommendID = intent.getStringExtra("id");
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        id = sharedPreferences.getString("loginId", "");
        name = sharedPreferences.getString("loginName", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarIdealInformation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("프로필");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        btnChatSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.hasExtra("after")) {
                    afterGetTokenDialog();
                } else {
                    GetTokenDialog();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("아이디", RecommendID);
        LoadRecommendProfile(RecommendID);

    }

    public void LoadRecommendProfile(String id) {
        String url = "http://3.36.21.126/Android/LoadRecommend.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    boolean success = jsonObject1.getBoolean("success");
                    if (success) {
                        tvIdealInfoHeight.setText(jsonObject1.getString("height"));
                        int age = Integer.parseInt(jsonObject1.getString("birthday"));
                        tvIdealInfoAge.setText(String.valueOf(age - 1));
                        tvIdealInfoWeight.setText(jsonObject1.getString("weight"));
                        tvIdealInfoArea.setText(jsonObject1.getString("area"));
                        tvIdealInfoJabGroup.setText(jsonObject1.getString("jabGroup"));
                        tvIdealInfoJab.setText(jsonObject1.getString("job"));
                        tvIdealInfoHobby.setText(jsonObject1.getString("hobby"));
                        tvIdealInfoForte.setText(jsonObject1.getString("forte"));
                        tvIdealInfoFavorite.setText(jsonObject1.getString("favorite"));
                        tvIdealInfoDislike.setText(jsonObject1.getString("dislike"));
                        tvIdealInfoDrink.setText(DrinkPattern(jsonObject1));
                        RecommendName = jsonObject1.getString("name");
                        RecommendToken = jsonObject1.getString("token");
                        if (jsonObject1.getString("profileImage1") != null) {
                            Glide.with(IdealProfileLoad.this).load(jsonObject1.getString("profileImage1")).error(R.drawable.ic_baseline_add_circle_24).into(ProfileImage1);
                        }
                        if (!jsonObject1.getString("profileImage2").equals("null")) {
                            Glide.with(IdealProfileLoad.this)
                                    .load(jsonObject1.getString("profileImage2"))
                                    .placeholder(R.drawable.ic_baseline_add_circle_24)
                                    .error(R.drawable.ic_baseline_add_circle_24)
                                    .into(ProfileImage2);
                        } else {
                            ProfileImage2.setVisibility(View.GONE);
                        }
                        if (!jsonObject1.getString("profileImage3").equals("null")) {
                            Glide.with(IdealProfileLoad.this).load(jsonObject1.getString("profileImage3")).error(R.drawable.ic_baseline_add_circle_24).into(ProfileImage3);
                        } else {
                            ProfileImage3.setVisibility(View.GONE);
                        }
                        if (!jsonObject1.getString("profileImage4").equals("null")) {
                            Glide.with(IdealProfileLoad.this).load(jsonObject1.getString("profileImage4")).error(R.drawable.ic_baseline_add_circle_24).into(ProfileImage4);
                        } else {
                            ProfileImage4.setVisibility(View.GONE);
                        }
                        if (!jsonObject1.getString("profileImage5").equals("null")) {
                            Glide.with(IdealProfileLoad.this).load(jsonObject1.getString("profileImage5")).error(R.drawable.ic_baseline_add_circle_24).into(ProfileImage5);
                        } else {
                            ProfileImage5.setVisibility(View.GONE);
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
        simpleMultiPartRequest.addStringParam("id", id);
        RequestQueue requestQueue = Volley.newRequestQueue(IdealProfileLoad.this);
        requestQueue.add(simpleMultiPartRequest);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
    }

    public String DrinkPattern(JSONObject jsonObject1) {
        String[] drinksplit = new String[0];
        String pattern;
        try {
            drinksplit = jsonObject1.getString("drink").split("@");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> arrayList = new ArrayList<>();
        if (drinksplit.length > 1) {
            for (int i = 0; i < drinksplit.length; i++) {
                String[] str = drinksplit[i].split(",");
                arrayList.add(str[1]);
            }
        }
        if (drinksplit.length == 3) {
            pattern = new String(Character.toChars(0x1F4C6)) + arrayList.get(0) + " " + new String(Character.toChars(0x1F37A)) + arrayList.get(1) + " " + new String(Character.toChars(0x1F377)) + arrayList.get(2);
            return pattern;
        } else if (drinksplit.length == 4) {
            pattern = new String(Character.toChars(0x1F4C6)) + arrayList.get(0) + " " + new String(Character.toChars(0x23F3)) + arrayList.get(1) + " " + new String(Character.toChars(0x1F37A)) + arrayList.get(2) + " " + new String(Character.toChars(0x1F377)) + arrayList.get(3);
            return pattern;
        } else {
            pattern = "음주 안함";
            return pattern;

        }

    }

    public int getAge(String birthday) {
        String[] birthdaysplit = birthday.split("-");
        int birthYear = Integer.parseInt(birthdaysplit[0]);
        int birthMonth = Integer.parseInt(birthdaysplit[1]);
        int birthDay = Integer.parseInt(birthdaysplit[2]);
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
    protected void onStop() {
        super.onStop();

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
                    JSONObject data = new JSONObject();
                    data.put("fromid", id);
                    data.put("room", "main");
                    data.put("type", "server");
                    data.put("message", "새로운 대화신청이 왔습니다.");
                    data.put("title", "부없만");
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

    public void GetTokenDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(IdealProfileLoad.this);
        dialog = builder.setTitle("대화신청").setMessage(RecommendName + "님에게 대화신청을 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "http://3.36.21.126/Android/GetToken.php";
                SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            boolean success = jsonObject1.getBoolean("success");
                            if (success) {
                                Log.e("토큰이야", jsonObject1.getString("token"));
                                FCMsend(jsonObject1.getString("token"));
                                AlertDialog dialog1;
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(IdealProfileLoad.this);
                                dialog1 = builder1.setMessage(RecommendName+"님에게 대화신청을 보냈습니다.").setPositiveButton("확인", null).show();
                            } else {
                                AlertDialog dialog1;
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(IdealProfileLoad.this);
                                dialog1 = builder1.setMessage("이미 보낸 대화신청이 있습니다.").setPositiveButton("확인", null).show();
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
                SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
                String MyId = sharedPreferences.getString("loginId", "");
                simpleMultiPartRequest.addStringParam("YouId", RecommendID);
                simpleMultiPartRequest.addStringParam("MyId", MyId);
                RequestQueue requestQueue = Volley.newRequestQueue(IdealProfileLoad.this);
                requestQueue.add(simpleMultiPartRequest);
            }
        }).show();
    }

    public void afterGetTokenDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(IdealProfileLoad.this);
        dialog = builder.setTitle("지난추천 대화신청").setMessage("지난추천 대상에게 대화신청하시려면 1000Credit이 소모됩니다.\n" + RecommendName + "님에게 대화신청을 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "http://3.36.21.126/Android/afterGetToken.php";
                SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            boolean success = jsonObject1.getBoolean("success");
                            if (success) {
                                Log.e("토큰이야", jsonObject1.getString("token"));
                                FCMsend(jsonObject1.getString("token"));
                                AlertDialog dialog1;
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(IdealProfileLoad.this);
                                dialog1 = builder1.setMessage(RecommendName+"님에게 대화신청을 보냈습니다.").setPositiveButton("확인", null).show();
                            } else {
                                if (jsonObject1.getBoolean("money")) {
                                    AlertDialog dialog1;
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(IdealProfileLoad.this);
                                    dialog1 = builder1.setMessage("크레딧이 부족합니다.").setPositiveButton("확인", null).show();
                                } else {
                                    AlertDialog dialog1;
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(IdealProfileLoad.this);
                                    dialog1 = builder1.setMessage("이미 보낸 대화신청이 있습니다.").setPositiveButton("확인", null).show();
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
                SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
                String MyId = sharedPreferences.getString("loginId", "");
                simpleMultiPartRequest.addStringParam("YouId", RecommendID);
                simpleMultiPartRequest.addStringParam("MyId", MyId);
                RequestQueue requestQueue = Volley.newRequestQueue(IdealProfileLoad.this);
                requestQueue.add(simpleMultiPartRequest);
            }
        }).show();
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