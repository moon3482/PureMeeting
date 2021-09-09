package com.example.mana;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mana.ShopInfomation.ShopInfomation;
import com.example.mana.chating.Client;
import com.example.mana.chating.chatdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class SendFaceTalk extends AppCompatActivity {
    TextView tvCancel;
    ImageView ivCancel;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    boolean mtime = true;
    String roomnum, youid, userid, Read, myname, name, index;
    Thread thread;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_face_talk);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        tvCancel = findViewById(R.id.tvsendrecivecancel);
        ivCancel = findViewById(R.id.imageView4);
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        userid = sharedPreferences.getString("loginId", "");
        myname = sharedPreferences.getString("loginName", "");
        Intent intent = getIntent();
        roomnum = intent.getStringExtra("room");
        youid = intent.getStringExtra("youid");
        index = intent.getStringExtra("index");

        sendFCM("영상통화");

        Glide.with(SendFaceTalk.this).load(R.raw.aaaaaa).into(ivCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            jsonObject.put("type", "face");
                            jsonObject.put("action", "NO");
                            jsonObject.put("room", roomnum);
                            jsonObject.put("index", index);
                            String send = jsonArray.put(jsonObject).toString();
                            dataOutputStream.writeUTF(send);
                            dataOutputStream.flush();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Intent intent1 = new Intent(SendFaceTalk.this, Client.class);
                startActivity(intent1);

                ;
            }
        });
        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("msg");
                    dataOutputStream.writeUTF(userid);
                    dataOutputStream.writeUTF(roomnum);
                    while (true) {
                        Read = dataInputStream.readUTF();
                        System.out.println("스레드 동작중");
                        if (Read != null) {
                            JSONArray jsonArray = new JSONArray(Read);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String action = jsonObject.getString("action");
                            if (action.equals("OK")) {
                                Intent intent = new Intent(SendFaceTalk.this, onFaceTalkReceive.class);
                                intent.putExtra("room", roomnum);
                                intent.putExtra("index", index);
                                intent.putExtra("youid", youid);
                                startActivity(intent);
                                mtime = false;
                                thread.interrupted();
                                finish();
                            } else if (action.equals("NO")) {
                                mtime = false;
                                thread.interrupted();
                                Intent intent = new Intent(SendFaceTalk.this, Client.class);
                                intent.putExtra("room", roomnum);
                                intent.putExtra("index", index);
                                intent.putExtra("youid", youid);
                                startActivity(intent);
                                finish();
                            } else {
                            }
                        }
                    }

                } catch (IOException | JSONException e) {
                    System.out.println("스레드 에러@@@@");
                    e.printStackTrace();
                }
                System.out.println("스레드 종료중");
            }

        });
        thread.start();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    public void sendFCM(String msg) {

        String url = new ServerIP().http+"Android/GetMsgToken.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("FCM보내기", "" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        FCMsend(jsonObject.getString("token"), msg);
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
        simpleMultiPartRequest.addStringParam("youid", youid);
        RequestQueue requestQueue = Volley.newRequestQueue(SendFaceTalk.this);
        requestQueue.add(simpleMultiPartRequest);
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
                    data.put("type", "face");
                    data.put("room", roomnum);
                    data.put("message", msg);
                    data.put("name", myname);
                    data.put("index", index);
                    data.put("title", "부없만 새로운메세지");
                    root.put("data", data);
                    root.put("priority", "high");
                    root.put("to", token);


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
    protected void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    jsonObject.put("type", "face");
                    jsonObject.put("action", "NO");
                    jsonObject.put("room", roomnum);
                    String send = jsonArray.put(jsonObject).toString();
                    dataOutputStream.writeUTF(send);
                    dataOutputStream.flush();
                    mtime = false;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                try {
                    dataOutputStream.close();
                    dataInputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
        finish();
    }
}