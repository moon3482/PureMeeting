package com.example.mana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.remotemonster.sdk.RemonCall;
import com.remotemonster.sdk.data.CloseType;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class onFaceTalkSend extends AppCompatActivity {
    SurfaceViewRenderer surfRendererLocal, surfRendererRemote;
    RemonCall remonCall;
    String room;
    TextView tvtime;
    boolean start = true;
    timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_face_talk_send);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        surfRendererLocal = findViewById(R.id.surfRendererLocalSend);
        surfRendererRemote = findViewById(R.id.surfRendererRemoteSend);
        tvtime = findViewById(R.id.tvReciveTimeSend);

        tvtime.setTextColor(Color.parseColor("#FFFFFF"));
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        remonCall = RemonCall.builder()
                .context(onFaceTalkSend.this)
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
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        remonCall.connect("Buupman" + room);
        remonCall.onConnect(new RemonCall.OnConnectCallback() {
            @Override
            public void onConnect(String chid) {
                Toast.makeText(onFaceTalkSend.this, "연결이 되었습니다.", Toast.LENGTH_LONG).show();
                timer = new timer(start, tvtime, 60);
                timer.start();
            }
        });

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
                Log.e("타이머", "" + time);
                if (time <= 0) {
                    status = false;
                    timer.status = false;
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
}