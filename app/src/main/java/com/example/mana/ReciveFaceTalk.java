package com.example.mana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mana.chating.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ReciveFaceTalk extends AppCompatActivity {
    ImageView ivrecive, ivcancel;
    TextView tvrecive, tvcancel, tvfrom;
    String myID, YouID, RoomNum, Read, name, index;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Vibrator vibrator;
    VibrationEffect vibrationEffect;
    long[] pattern = {1500, 800, 800, 800};
    Thread thread;
    boolean mtime = true;
    public static Context receivelight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive_face_talk);
        receivelight = this;
        light();


        ivrecive = findViewById(R.id.iiiiiii);
        ivcancel = findViewById(R.id.imageView2);
        tvrecive = findViewById(R.id.tvreciveok);
        tvcancel = findViewById(R.id.tvrecivecancel);
        tvfrom = findViewById(R.id.tvReceiveFaceTalk);
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        myID = sharedPreferences.getString("loginId", "");
        Intent intent = getIntent();
        YouID = intent.getStringExtra("fromid");
        RoomNum = intent.getStringExtra("room");
        name = intent.getStringExtra("name");
        index = intent.getStringExtra("index");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createWaveform(pattern, 0);
        } else {
//            vibrator.vibrate(150);
        }

        tvfrom.setText(name + "님에게 온 페이스톡");
        Glide.with(ReciveFaceTalk.this).load(R.raw.call_reject).into(ivcancel);
        Glide.with(ReciveFaceTalk.this).load(R.raw.call_accept).into(ivrecive);
        tvrecive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            jsonObject.put("type", "face");
                            jsonObject.put("action", "OK");
                            jsonObject.put("room", RoomNum);
                            jsonArray.put(jsonObject).toString();
                            String send = jsonArray.toString();
                            dataOutputStream.writeUTF(send);
                            dataOutputStream.flush();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        vibrator.cancel();
                        Intent intent1 = new Intent(ReciveFaceTalk.this, OnFaceTalkReceive.class);
                        intent1.putExtra("room", RoomNum);
                        intent1.putExtra("index", index);
                        intent1.putExtra("youid", YouID);
                        startActivity(intent1);
                        finish();
                    }
                }).start();

            }
        });
        tvcancel.setOnClickListener(new View.OnClickListener() {
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
                            jsonObject.put("room", RoomNum);
                            String send = jsonArray.put(jsonObject).toString();
                            dataOutputStream.writeUTF(send);
                            dataOutputStream.flush();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
                vibrator.cancel();
                Intent intent1 = new Intent(ReciveFaceTalk.this, Client.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("room", RoomNum);
                intent1.putExtra("index", index);
                intent1.putExtra("youid", YouID);
                startActivity(intent1);
                finish();


            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        light();
        Intent intent2 = getIntent();
        YouID = intent2.getStringExtra("fromid");
        RoomNum = intent2.getStringExtra("room");
        name = intent2.getStringExtra("name");
        index = intent2.getStringExtra("index");
    }

    @Override
    protected void onStart() {
        super.onStart();
        light();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 60;

                while (mtime) {
                    Log.e("시간", "" + time);
                    time--;
                    try {
                        Thread.sleep(1000);
                        if (time <= 0) {
                            Thread.interrupted();
                            Log.e("시간오바", "종료");
                            mtime = false;
                            vibrator.cancel();
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("쓰레드 종료", "종료");
            }
        }).start();


    }


    @Override
    protected void onResume() {
        super.onResume();


        long[] pattern = {1500, 800, 800, 800};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createWaveform(pattern, 0);
        } else {
//            vibrator.vibrate(150);
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("msg");
                    dataOutputStream.writeUTF(myID);
                    dataOutputStream.writeUTF(RoomNum);

                    while (true) {
                        System.out.println("스레드 동작중");
                        Read = dataInputStream.readUTF();
                        JSONArray jsonArray = new JSONArray(Read);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String action = jsonObject.getString("action");
                        if (action.equals("NO")) {
                            mtime = false;
                            Intent intent1 = new Intent(ReciveFaceTalk.this, Client.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent1.putExtra("room", RoomNum);
                            intent1.putExtra("index", index);
                            intent1.putExtra("youid", YouID);
                            startActivity(intent1);


                        }
                    }
                } catch (IOException | JSONException e) {
                    System.out.println("스레드 에러뜸@@@@@@@@@@@");
                    e.printStackTrace();
                }
                System.out.println("스레드 종료된다@@@");
            }
        });
        thread.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(vibrationEffect);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mtime) {
                        vibrator.vibrate(150);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();


        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();

            System.out.println("소켓 닫힘");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mtime = false;
        vibrator.cancel();

        thread.interrupt();
        finish();
    }

    public void light() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}