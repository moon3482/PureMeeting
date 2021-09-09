package com.example.mana.chating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.ChargeCredit;
import com.example.mana.ChatPage.chatPage;
import com.example.mana.CustomDialog3;
import com.example.mana.R;
import com.example.mana.ServerIP;
import com.example.mana.SoftKeyboardDectectorView;
import com.example.mana.ZoneAdd;
import com.example.mana.socketclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;

public class Client extends AppCompatActivity {
    String rnum, index;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = new ServerIP().ip;
    private int port = 9000;
    DataOutputStream outs;
    private ArrayList<chatdata> arrayList;
    private Chatingadapter chatingadapter;
    private RecyclerView recyclerView;
    String id;
    ImageButton chatbutton;
    TextView chatView;
    EditText message;
    String sendmsg;
    String read, name;
    TextView roomnum;
    int er;
    DataInputStream in;
    int GETSHOPRESULTCODE = 5609;
    String imgurl;
    Button sendImage;
    int IMAGE_PICK = 899;
    DBHelper helper;
    SQLiteDatabase db;
    String youid;
    Intent intent;
    CustomDialog3 dialog3;
    boolean thread = true;
    boolean dialogcheck;
    public socketclass socketclass;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        id = sharedPreferences.getString("loginId", "");
        name = sharedPreferences.getString("loginName", "");
        SharedPreferences NowRoomInsert = getSharedPreferences("loginId", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = NowRoomInsert.edit();
        editor.putString("NowRoom", rnum);
        editor.apply();
        profileimgload();
        message = (EditText) findViewById(R.id.message);
        sendImage = findViewById(R.id.chatImageSend);
        chatbutton = (ImageButton) findViewById(R.id.chatbutton);
        roomnum = findViewById(R.id.tvroomnum);
        roomnum.setText(rnum);
        helper = new DBHelper(Client.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);
        recyclerView = findViewById(R.id.chatrcv);
        dialog3 = new CustomDialog3(Client.this, dialogcheck);

//        Cursor cursor;
//        cursor = db.rawQuery("select * from chat where room ='" + rnum + "'order by date asc", null);
//        while (cursor.moveToNext()) {
//            String type = cursor.getString(2);
//            String name = cursor.getString(3);
//            String id = cursor.getString(4);
//            String img = cursor.getString(5);
//            String msg = cursor.getString(6);
//            System.out.println(id);
//            chatdata chatdata = new chatdata(type, name, msg, img, id);
//            arrayList.add(chatdata);
////            if (arrayList.size() > 0) {
////                int i = 0;
////                chatingadapter.notifyItemInserted(arrayList.size() - 1);
////                i++;
////            }
//
//        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.chattoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setBackgroundColor(Color.parseColor("#E91E63"));
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        intent = getIntent();
        rnum = sharedPreferences.getString("ClickRoom", "");
        youid = sharedPreferences.getString("youid", "");
        if (intent.hasExtra("action")) {
            dialogcheck = true;
            index = intent.getStringExtra("index");
            AlertDialog.Builder builder = new AlertDialog.Builder(Client.this);
            builder.setTitle("예약 확인");

            builder.setMessage("상대방과 추천장소에서 만남을 하시겠습니까?\n확인을 누르시면 결제과정이 진행됩니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CheckCredit(id);
                }
            });

            builder.setNeutralButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    cancel(id, index, name, imgurl, rnum, youid);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }


        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] chat = {"갤러리", "카메라"};
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(Client.this);
                builder.setItems(chat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(Client.this, "갤러리 선택", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Intent.ACTION_PICK);
                            intent1.setType("image/*");
                            startActivityForResult(intent1, IMAGE_PICK);
                        } else if (which == 1) {

                            Toast.makeText(Client.this, "카메라 선택", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                builder.setTitle("사진전송");
                builder.show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        id = sharedPreferences.getString("loginId", "");
        name = sharedPreferences.getString("loginName", "");
        rnum = sharedPreferences.getString("ClickRoom", "");
        youid = sharedPreferences.getString("youid", "");
        SharedPreferences NowRoomInsert = getSharedPreferences("loginId", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = NowRoomInsert.edit();
        editor.putString("NowRoom", rnum);
        editor.apply();
        profileimgload();
        Intent intent = getIntent();
        if (intent.hasExtra("room")) {
            rnum = intent.getStringExtra("room");
        }
        if (intent.hasExtra("youid")) {
            youid = intent.getStringExtra("youid");
        }


        /**채팅보내기 버튼 눌렀을때*/
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg = message.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray jsonArray = new JSONArray();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "msg");
                            jsonObject.put("name", name);
                            jsonObject.put("img", imgurl);
                            jsonObject.put("msg", sendmsg);
                            jsonObject.put("id", id);
                            jsonObject.put("room", rnum);
                            jsonObject.put("youid", youid);
                            String aa = jsonArray.put(jsonObject).toString();

                            if (sendmsg != null && sendmsg.length() > 0) {
                                outs.writeUTF(aa);
                                outs.flush();
                                sendFCM(name + " : " + sendmsg);

                            }
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.scrollToPosition(arrayList.size() - 1);
                                    message.getText().clear();
                                }
                            }, 0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
//                    @Override
//                    public void run() {
//
//                    }
                }.start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        thread = true;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Client.this);

        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        chatingadapter = new Chatingadapter(arrayList, Client.this);
        recyclerView.setAdapter(chatingadapter);
        recyclerView.setItemAnimator(null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(chatingadapter.getItemCount() - 1);
            }
        });

        Intent intent = getIntent();
        conSocket();
        LoadMsg(rnum);
        new TedKeyboardObserver(this).listen(isShow -> {
            recyclerView.scrollToPosition(arrayList.size() - 1);
        });
    }

    class msgUpdate implements Runnable {
        private String msg;

        public msgUpdate(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
//            chatView.setText(chatView.getText().toString() + msg + "\n");
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
                TimeZone time;
                time = TimeZone.getTimeZone("Asia/Seoul");
                dateFormat.setTimeZone(time);
                Date date = new Date();
                JSONArray jsonArray = new JSONArray(msg);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                chatdata chatdata = new chatdata(jsonObject.getString("type"), jsonObject.getString("name"), jsonObject.getString("msg"), jsonObject.getString("img"), jsonObject.getString("id"), jsonObject.getString("room"), youid);
                arrayList.add(chatdata);

                chatingadapter.notifyItemInserted(arrayList.size() - 1);


                ContentValues values = new ContentValues();
                values.put("room", jsonObject.getString("room"));
                values.put("type", jsonObject.getString("type"));
                values.put("chat_id", jsonObject.getString("id"));
                values.put("chat", jsonObject.getString("name"));
                values.put("txt", jsonObject.getString("msg"));
                values.put("img", jsonObject.getString("img"));
                values.put("date", dateFormat.format(date).toString());
                db.insert("chat", null, values);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("실패", "여기1");
            }


        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(Client.this, "포즈", Toast.LENGTH_SHORT).show();
        SharedPreferences NowRoomInsert = getSharedPreferences("loginId", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = NowRoomInsert.edit();
        editor.putString("NowRoom", "null");
        editor.apply();
        try {
            if (socket.isConnected()) {
                in.close();
                outs.close();
                socket.close();
                thread = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 외부 URL이미지 스트림 받아오는 메서드 2020-12-20 작동이 제대로 되지않아서 주석처리함//
     * //    private Bitmap LoadImage(String $imagePath) {
     * //        // TODO Auto-generated method stub
     * //        InputStream inputStream = OpenHttpConnection($imagePath);
     * //        Bitmap bm = BitmapFactory.decodeStream(inputStream);
     * //
     * //        return bm;
     * //    }
     * //
     * //    private InputStream OpenHttpConnection(String $imagePath) {
     * //        // TODO Auto-generated method stub
     * //        InputStream stream = null;
     * //        try {
     * //            URL url = new URL($imagePath);
     * //            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
     * //            urlConnection.setRequestMethod("GET");
     * //            urlConnection.connect();
     * //            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
     * //                stream = urlConnection.getInputStream();
     * //            }
     * //        } catch (MalformedURLException e) {
     * //            // TODO Auto-generated catch block
     * //            e.printStackTrace();
     * //        } catch (IOException e) {
     * //            // TODO Auto-generated catch block
     * //            e.printStackTrace();
     * //        }
     * //        return stream;
     * //    }
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {

            }

        }

        if (requestCode == 3378) {
            if (resultCode == RESULT_OK) {
//                Toast.makeText(Client.this, "리절트", Toast.LENGTH_SHORT).show();


                SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
                id = sharedPreferences.getString("loginId", "");
                name = sharedPreferences.getString("loginName", "");
                rnum = sharedPreferences.getString("ClickRoom", "");
                youid = sharedPreferences.getString("youid", "");
                String index = data.getExtras().getString("index");
                Log.e("인덱스값리절트", "" + index);
                dialog3 = new CustomDialog3(Client.this, dialogcheck);
                try {
                    dialog3.Colldialog(id, index, name, imgurl, rnum, youid);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                LoadCredit(id);


            }
        }
        if (requestCode == IMAGE_PICK) {

            if (resultCode == RESULT_OK) {


                String url = new ServerIP().http+"Android/chatImg.php";
                Uri uri = data.getData();


                String imguri = getRealPathFromUri(uri);
//                new AlertDialog.Builder(this).setMessage(uri.toString() + "\n" + imguri).create().show();
                SimpleMultiPartRequest chatImageRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String chatImgUrl = jsonResponse.getString("url");
                            if (success) {


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            JSONArray jsonArray = new JSONArray();

                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("type", "img");
                                            jsonObject.put("name", name);
                                            jsonObject.put("img", imgurl);
                                            if (chatImgUrl.length() > 0) {
                                                jsonObject.put("msg", chatImgUrl);
                                            }
                                            jsonObject.put("id", id);
                                            jsonObject.put("room", rnum);


                                            String aa = jsonArray.put(jsonObject).toString();

                                            if (success) {
                                                outs.writeUTF(aa);
                                                outs.flush();


                                            }
//                                            Handler handler = new Handler(Looper.getMainLooper());
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    message.getText().clear();
//                                                }
//                                            }, 0);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            } else {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
//                    chatImageRequst.addStringParam("id", id);
                chatImageRequest.addFile("img", imguri);

                RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
                requestQueue.add(chatImageRequest);


            }
        }

    }

    /**
     * 선택된 이미지 파일에서 파일경로 받아오기
     */
    String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void conSocket() {
        new Thread() {
            public void run() {
                try {

                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), 1000);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    outs = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    socketclass = new socketclass(socket);
                    outs.writeUTF("msg");
                    outs.writeUTF(id);
                    outs.writeUTF(rnum);
                    outs.flush();


                    while (thread) {

                        read = in.readUTF();


                        if (read != null) {
                            Log.e("메시지", "내용 : " + read);

//                            mHandler.post(new msgUpdate(read));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    mHandler.post(new msgUpdate(read));
                                    try {

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
                                        TimeZone time;
                                        time = TimeZone.getTimeZone("Asia/Seoul");
                                        dateFormat.setTimeZone(time);
                                        Date date = new Date();
                                        JSONArray jsonArray = new JSONArray(read);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        chatdata chatdata = new chatdata(jsonObject.getString("type"), jsonObject.getString("name"), jsonObject.getString("msg"), jsonObject.getString("img"), jsonObject.getString("id"), jsonObject.getString("room"), jsonObject.getString("youid"));
                                        arrayList.add(chatdata);
                                        chatingadapter.notifyItemInserted(arrayList.size() - 1);
                                        String name = jsonObject.getString("name");
                                        String msg = jsonObject.getString("msg");

//                                        chatdata chatdata = new chatdata(jsonObject.getString("type"), jsonObject.getString("name"), jsonObject.getString("msg"), jsonObject.getString("img"), jsonObject.getString("id"));
//                                        arrayList.add(chatdata);


//                                        ContentValues values = new ContentValues();
//                                        values.put("room", jsonObject.getString("room"));
//                                        values.put("type", jsonObject.getString("type"));
//                                        values.put("chat_id", jsonObject.getString("id"));
//                                        values.put("chat", jsonObject.getString("name"));
//                                        values.put("txt", jsonObject.getString("msg"));
//                                        values.put("img", jsonObject.getString("img"));
//                                        values.put("date", dateFormat.format(date).toString());
//                                        db.insert("chat", null, values);

//                                        Cursor cursor;
//                                        cursor = db.rawQuery("select * from chat where room ='" + rnum + "'and id = last_insert_rowid()", null);
//                                        while (cursor.moveToNext()) {
//                                            String type = cursor.getString(2);
//                                            String name = cursor.getString(3);
//                                            String id = cursor.getString(4);
//                                            String img = cursor.getString(5);
//                                            String msg = cursor.getString(6);


//                                        }
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                recyclerView.scrollToPosition(arrayList.size() - 1);
                                                message.requestFocus();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                        Log.e("실패", "여기1");
                                    }


                                }
                            });
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                }
//                            }, 200);
                        }
                    }


                } catch (SocketTimeoutException ss) {
                    er = 1;
                    System.out.println(er);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            AlertDialog dialog;
//                            AlertDialog.Builder builder = new AlertDialog.Builder(Client.this);
//                            dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다")
//                                    .setPositiveButton("확인", null)
//                                    .create();
//                            dialog.show();
                            Toast.makeText(Client.this, "서버에 접속하지 못했습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("이런", "이런");
//                            finish();
                        }
                    });


                } catch (IOException e) {

                    Log.e("소켓끊김", "소켓끊김");

                    e.printStackTrace();

                }
            }
        }.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.zone, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.zonebutton:
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Client.this, ZoneAdd.class);
                intent.putExtra("username", name);
                intent.putExtra("roomnumber", rnum);
                intent.putExtra("imgurl", imgurl);
                intent.putExtra("youid", youid);
                startActivityForResult(intent, GETSHOPRESULTCODE);
                return true;
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                Intent intent1 = new Intent(Client.this, chatPage.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                return true;


            default:
//                Toast.makeText(Client.this, "문제 있음", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadMsg(String roomnum) {
        String url = new ServerIP().http+"Android/LoadMsg.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("채팅불러오기", "" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String type = jsonObject1.getString("type");
                            String name = jsonObject1.getString("name");
                            String msg = jsonObject1.getString("msg");
                            String img = jsonObject1.getString("img");
                            String id = jsonObject1.getString("id");
                            String room = jsonObject1.getString("room");
                            String youid = jsonObject1.getString("youid");
                            chatdata chatdata = new chatdata(type, name, msg, img, id, room, youid);
                            arrayList.add(chatdata);
                        }
                        chatingadapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(arrayList.size() - 1);
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
        simpleMultiPartRequest.addStringParam("room", roomnum);
        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
        requestQueue.add(simpleMultiPartRequest);
    }

    public void sendFCM(String msg) {
        Log.e("아이디는?", youid);
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
        simpleMultiPartRequest.addStringParam("youid", youid);
        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
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
                    data.put("fromid", id);
                    data.put("type", "msg");
                    data.put("room", rnum);
                    data.put("message", msg);
                    data.put("title", "부없만 새로운메세지");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ClickRoom", "null");
        editor.putString("youid", "null");
        editor.apply();
        Intent intent1 = new Intent(Client.this, chatPage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
    }

    public void cancel(String id, String index, String name, String imgurl, String rnum, String youid) {
        String url = new ServerIP().http+"Android/finalzone.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {


                                        JSONArray jsonArray = new JSONArray();
                                        JSONObject indexjson = new JSONObject();
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("type", "ChooseCancel");
                                        jsonObject.put("name", name);
                                        jsonObject.put("img", imgurl);
                                        jsonObject.put("msg", indexjson);
                                        jsonObject.put("id", id);
                                        jsonObject.put("room", rnum);
                                        jsonObject.put("youid", youid);
                                        String aa = jsonArray.put(jsonObject).toString();
                                        outs.writeUTF(aa);
                                        outs.flush();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                            finalcheck(index, "0", id, name, imgurl, rnum, youid);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("id", id);
        simpleMultiPartRequest.addStringParam("final", "cancel");
        simpleMultiPartRequest.addStringParam("index", index);
        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
        requestQueue.add(simpleMultiPartRequest);
    }

    public void finalzone(String sign, AlertDialog dialog) {
        String url = new ServerIP().http+"Android/finalzone.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("다이얼로그2", "" + response);
                    String success = jsonObject.getString("success");
                    if (success.equals("money")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Client.this)
                                .setTitle("여유 금액 부족").setMessage("결제금액이 부족합니다.\n충전화면으로 이동하시겠습니까?").setPositiveButton("이동", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("취소", null).create();
                        alertDialog.show();
                    } else if (success.equals("ok")) {
                        int hit = jsonObject.getInt("hit");
                        int succe = jsonObject.getInt("succe");
                        if (hit == 2 && succe == 2) {
                            JSONArray jsonArray = new JSONArray();

                            JSONObject jsonObject2 = new JSONObject();
                            JSONObject indexjson = new JSONObject();
                            indexjson.put("index", index);
                            jsonObject2.put("type", "finalok");
                            jsonObject2.put("name", name);
                            jsonObject2.put("img", imgurl);
                            jsonObject2.put("msg", indexjson);
                            jsonObject2.put("id", id);
                            jsonObject2.put("room", rnum);
                            jsonObject2.put("youid", youid);
                            String aa = jsonArray.put(jsonObject2).toString();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        outs.writeUTF(aa);
                                        outs.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                            dialog.dismiss();
                        } else if (hit == 2 && succe < 2) {
                            JSONArray jsonArray = new JSONArray();

                            JSONObject jsonObject2 = new JSONObject();
                            JSONObject indexjson = new JSONObject();
                            indexjson.put("index", index);
                            jsonObject2.put("type", "finalcancel");
                            jsonObject2.put("name", name);
                            jsonObject2.put("img", imgurl);
                            jsonObject2.put("msg", indexjson);
                            jsonObject2.put("id", id);
                            jsonObject2.put("room", rnum);
                            jsonObject2.put("youid", youid);
                            String aa = jsonArray.put(jsonObject2).toString();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        outs.writeUTF(aa);
                                        outs.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
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
        Log.e("다이얼로그2 인덱스", "" + index);
        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("id", id);
        simpleMultiPartRequest.addStringParam("pay", "15000");
        simpleMultiPartRequest.addStringParam("index", index);
        simpleMultiPartRequest.addStringParam("final", sign);
        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
        requestQueue.add(simpleMultiPartRequest);

    }

    public void profileimgload() {
        String url = new ServerIP().http+"/Android/main.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        imgurl = jsonObject.getString("profilethumimg");


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
        simpleMultiPartRequest.addStringParam("email", id);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(simpleMultiPartRequest);
    }


    public void CheckCredit(String id) {
        String url = new ServerIP().http+"Android/CheckCredit.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        dialog3 = new CustomDialog3(Client.this, dialogcheck);
                        try {
                            dialog3.Colldialog(id, index, name, imgurl, rnum, youid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Dialog dialog = new Dialog(Client.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Client.this);
                        dialog = builder.setTitle("크레딧 부족").setMessage("크레딧이 부족합니다 충전하시겠습니까?").setPositiveButton("충천", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Client.this, ChargeCredit.class);
                                intent.putExtra("charge", "charge");
                                intent.putExtra("index", index);
                                startActivityForResult(intent, 3378);

                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancel(id, index, name, imgurl, rnum, youid);
                            }
                        }).create();
                        dialog.show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
        requestQueue.add(simpleMultiPartRequest);
    }

    //    public void LoadCredit(String id) {
//        String url = new ServerIP().http+"Android/LoadCredit.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        Log.e("통신함", "통신함");
//                        dialog3.cd = jsonObject.getString("Credit");
//                        dialog3.tvHaveCredit.setText(dialog3.cd);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.setShouldCache(false);
//        Log.e("다이얼로그 아이디 ", "" + id);
//        simpleMultiPartRequest.addStringParam("id", id);
//
//        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
//        requestQueue.add(simpleMultiPartRequest);
//    }
    public void finalcheck(String index, String pay, String id, String name, String imgurl, String rnum, String youid) {
        String url = new ServerIP().http+"Android/finalcheck.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("ok")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONArray jsonArray = new JSONArray();
                                    JSONObject jsonObject2 = new JSONObject();
                                    JSONObject indexjson = new JSONObject();
                                    try {
                                        indexjson.put("index", index);
                                        jsonObject2.put("type", "finalok");
                                        jsonObject2.put("name", name);
                                        jsonObject2.put("img", imgurl);
                                        jsonObject2.put("msg", indexjson);
                                        jsonObject2.put("id", id);
                                        jsonObject2.put("room", rnum);
                                        jsonObject2.put("youid", youid);
                                        String aa = jsonArray.put(jsonObject2).toString();
                                        outs.writeUTF(aa);
                                        outs.flush();
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                        } else if (success.equals("cancel")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONArray jsonArray = new JSONArray();

                                    JSONObject jsonObject2 = new JSONObject();
                                    JSONObject indexjson = new JSONObject();
                                    try {
                                        indexjson.put("index", index);
                                        jsonObject2.put("type", "finalcancel");
                                        jsonObject2.put("name", name);
                                        jsonObject2.put("img", imgurl);
                                        jsonObject2.put("msg", indexjson);
                                        jsonObject2.put("id", id);
                                        jsonObject2.put("room", rnum);
                                        jsonObject2.put("youid", youid);
                                        String aa = jsonArray.put(jsonObject2).toString();
                                        outs.writeUTF(aa);
                                        outs.flush();
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("index", index);
        simpleMultiPartRequest.addStringParam("pay", pay);
        RequestQueue requestQueue = Volley.newRequestQueue(Client.this);
        requestQueue.add(simpleMultiPartRequest);
    }
}







