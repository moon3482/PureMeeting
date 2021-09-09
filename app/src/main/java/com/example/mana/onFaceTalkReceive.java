package com.example.mana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.chating.Client;
import com.remotemonster.sdk.RemonCall;
import com.remotemonster.sdk.data.CloseType;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class onFaceTalkReceive extends AppCompatActivity {
    SurfaceViewRenderer surfRendererLocal, surfRendererRemote;
    RemonCall remonCall;
    String room, index, youid;
    TextView tvtime;
    boolean start = true;
    timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_face_talk_receive);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        surfRendererLocal = findViewById(R.id.surfRendererLocalReceive);
        surfRendererRemote = findViewById(R.id.surfRendererRemoteReceive);
        tvtime = findViewById(R.id.tvReciveTime);


        tvtime.setTextColor(Color.parseColor("#FFFFFF"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        }
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        index = intent.getStringExtra("index");
        youid = intent.getStringExtra("youid");
        remonCall = RemonCall.builder()
                .context(onFaceTalkReceive.this)
                .localView(surfRendererLocal)        //나의 Video Renderer
                .remoteView(surfRendererRemote)      //상대방 video Renderer
                .serviceId(this.getString(R.string.monster_app_key))    // RemoteMonster 사이트에서 등록했던 당신의 id를 입력하세요.
                .key(this.getString(R.string.monster_app_sckey))    // RemoteMonster로부터 받은 당신의 key를 입력하세요.
                .build();

        surfRendererRemote.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
        surfRendererLocal.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
        surfRendererLocal.setZOrderMediaOverlay(true);
        surfRendererRemote.setZOrderMediaOverlay(false);
        remonCall.onClose(new RemonCall.OnCloseCallback() {
            @Override
            public void onClose(CloseType closeType) {
                start = false;
                timer.status = false;
                Intent intent1 = new Intent(onFaceTalkReceive.this, Client.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("action", "choose");
                intent1.putExtra("room", room);
                intent1.putExtra("youid", youid);
                intent1.putExtra("index", index);
                startActivity(intent1);

            }
        });
        remonCall.onConnect(new RemonCall.OnConnectCallback() {
            @Override
            public void onConnect(String chid) {
                Toast.makeText(onFaceTalkReceive.this, "연결이 되었습니다.", Toast.LENGTH_LONG).show();
                hitupdate();
                timer = new timer(start, tvtime, 60);
                timer.start();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        remonCall.connect("Buupman" + room);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class timer extends Thread {
        public boolean status;
        TextView textView;
        int time;

        public timer(boolean status, TextView textView, int time) {
            this.status = status;
            this.textView = textView;
            this.time = time;
        }

        @Override
        public void run() {
            super.run();
            while (status) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.valueOf(time));
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time--;
                if (time <= 0) {
                    status = false;
                    remonCall.close();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        start = false;
//        timer.status = false;
//        remonCall.close();
//        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        start = false;
        timer.status = false;
        remonCall.close();
        finish();
    }

    public void hitupdate() {
        String url = new ServerIP().http+"Android/hitupdate.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {

                    }else {
                        Toast.makeText(onFaceTalkReceive.this,"네트워크 연결에 문제가 있습니다.",Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(onFaceTalkReceive.this, Client.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.putExtra("action", "choose");
                        intent1.putExtra("room", room);
                        intent1.putExtra("youid", youid);
                        intent1.putExtra("index", index);
                        startActivity(intent1);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(onFaceTalkReceive.this,"네트워크 연결에 문제가 있습니다.",Toast.LENGTH_LONG).show();
//                Intent intent1 = new Intent(onFaceTalkReceive.this, Client.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent1.putExtra("action", "choose");
//                intent1.putExtra("room", room);
//                intent1.putExtra("youid", youid);
//                intent1.putExtra("index", index);
//                startActivity(intent1);
//                finish();
            }
        });

        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("index", index);
        RequestQueue requestQueue = Volley.newRequestQueue(onFaceTalkReceive.this);
        requestQueue.add(simpleMultiPartRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        remonCall.close();
    }
}