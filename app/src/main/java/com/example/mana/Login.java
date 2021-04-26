package com.example.mana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.cache.LruImageCache;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.ImageCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.ui.NetworkImageView;
import com.bumptech.glide.Glide;
import com.example.mana.chating.Client;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {
    TextView email, name, age, gender, area, jab;
    CircleImageView profile;
    String imgurl;
    Button chat;
    String id;
    Handler handler = new Handler();
    JSONObject jsonObject = null;
    String bitstring;
    Bitmap bitmap;
    EditText roomnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        chat = findViewById(R.id.chat);
        email = findViewById(R.id.login_Email);
        name = findViewById(R.id.login_name);
        age = findViewById(R.id.login_age);
        gender = findViewById(R.id.login_gender);
        area = findViewById(R.id.login_area);
        jab = findViewById(R.id.login_jab);
        profile = findViewById(R.id.login_image);
        roomnum = findViewById(R.id.roomNumberInput);
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        id = sharedPreferences.getString("loginId", "");
        Log.e("쉐어드로그인", id);


//        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageCache() {
//            LruImageCache lruImageCache = new LruImageCache();
//
//            @Override
//            public Bitmap getBitmap(String url) {
//                return lruImageCache.getBitmap(url);
//            }
//
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//                lruImageCache.putBitmap(url, bitmap);
//            }
//
//            @Override
//            public void invalidateBitmap(String url) {
//
//            }
//
//            @Override
//            public void clear() {
//
//            }
//        });
//        NetworkImageView profileImageView = (NetworkImageView) findViewById(R.id.login_image);
//        profileImageView.setImageUrl(imgurl, imageLoader);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Client.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("room", roomnum.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread r = new Thread(new Runnable() {
            @Override
            public void run() {
                /** 로그인된 정보 GET으로 요청 보내기 2020-11-27 */
                String serverUrl = "http://3.36.21.126/Android/main.php";
                SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String iad = jsonObject.getString("id");
                            Log.e("리스폰 ID", iad);
                            Log.e("로그인", String.valueOf(success));
                            if (success) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            email.setText(jsonObject.getString("id"));
                                            name.setText(jsonObject.getString("name"));
                                            gender.setText(jsonObject.getString("gender"));
                                            area.setText(jsonObject.getString("area"));
                                            jab.setText(jsonObject.getString("jabs"));
                                            imgurl = jsonObject.getString("profileimg");
                                            Log.e("이미지 경로", imgurl);
                                            if (imgurl.length() != 0) {
                                                Log.e("글라이드 ", "동작함");
                                                Glide.with(Login.this)
                                                        .load( imgurl)
                                                        .into(profile);

                                            }

                                            String ss[] = jsonObject.getString("birthday").split("-");
                                            int realage = getAge(jsonObject.getString("birthday"));
                                            age.setText(String.valueOf(realage));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                smpr.addStringParam("email", id);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);
            }
        });
        r.start();


    }

    public int getAge(String birthday) {
        String[] birthdaysplit = birthday.split("-");
        int birthYear=Integer.parseInt(birthdaysplit[0]);
        int birthMonth=Integer.parseInt(birthdaysplit[1]);
        int birthDay=Integer.parseInt(birthdaysplit[2]);
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


}