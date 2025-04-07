package com.example.mana;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mana.shopInfomation.ShopInfomation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomDialogRecommend {
    Context context;
    Socket socket;
    DataOutputStream outputStream;

    public CustomDialogRecommend(Context context) {

        this.context = context;
    }

    public void CallDialog(String userid, String username, String imgurl, String roomnum, String shopcod, String shopname, String shoptype, String shopadress, String starttinme, String endtime, String youid) {
        final Dialog dialog = new Dialog(context);
        String dayPick;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recommend_dialog_item);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final TextView date = dialog.findViewById(R.id.tvPicktime);
        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        final Button next = dialog.findViewById(R.id.btnDialogNEXT);
        final Button previous = dialog.findViewById(R.id.btnDialogPrevious);
        final Button done = dialog.findViewById(R.id.btnDialogDone);
        final TextView time = dialog.findViewById(R.id.tvTimePick);
        long now = System.currentTimeMillis();
        Date date1 = new Date(now);
        TimeZone tz;
        SimpleDateFormat sdfyear = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat sdfday = new SimpleDateFormat("dd", Locale.KOREA);
        SimpleDateFormat sdfhor = new SimpleDateFormat("HH", Locale.KOREA);
        SimpleDateFormat sdfminute = new SimpleDateFormat("mm", Locale.KOREA);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        sdfyear.setTimeZone(tz);
        sdfmonth.setTimeZone(tz);
        sdfday.setTimeZone(tz);
        sdfhor.setTimeZone(tz);
        sdfminute.setTimeZone(tz);
        int year = Integer.parseInt(sdfyear.format(date1));
        int month = Integer.parseInt(sdfmonth.format(date1)) - 1;
        int day = Integer.parseInt(sdfday.format(date1));
        Calendar mindate = Calendar.getInstance();
        mindate.set(year, month, day);
        String mo = "" + month;
        String dd = "" + day;
        if (month < 10) {
            mo = "0" + (month + 1);
        }
        if (day < 10) {
            dd = "0" + day;
        }
        date.setText(year + "-" + mo + "-" + dd);
        time.setText(sdfhor.format(date1) + ":" + sdfminute.format(date1));
        timePicker.setIs24HourView(true);


        datePicker.setMinDate(mindate.getTime().getTime());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setVisibility(View.GONE);
                previous.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] min = time.getText().toString().split(":");
                if (min[1].equals("00") || min[1].equals("30")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                socket = new Socket();
                                socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
                                outputStream = new DataOutputStream(socket.getOutputStream());
                                outputStream.writeUTF("map");
                                outputStream.writeUTF(userid);
                                outputStream.writeUTF(roomnum);

                                try {
                                    JSONObject root = new JSONObject();
                                    JSONObject message = new JSONObject();
                                    message.put("shopcode", shopcod);
                                    message.put("shopname", shopname);
                                    message.put("shoptype", shoptype);
                                    message.put("shopaddress", shopadress);
                                    message.put("shopstarttime", starttinme);
                                    message.put("endtime", endtime);
                                    message.put("roomnum", roomnum);
                                    message.put("reservetime", date.getText().toString() + " " + time.getText().toString());
                                    message.put("status", 0);
                                    root.put("type", "zone");
                                    root.put("name", username);
                                    root.put("img", imgurl);
                                    root.put("msg", message);
                                    root.put("id", userid);
                                    root.put("room", roomnum);
                                    root.put("youid", youid);
                                    JSONArray msg = new JSONArray();
                                    String order = msg.put(root).toString();
                                    outputStream.writeUTF(order);
                                    socket.close();
                                    sendFCM(username + "님이 장소추천을 보냈습니다.", userid, youid, roomnum);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (SocketTimeoutException ss) {
                                    Log.e("추천 소켓", "타임아웃");
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    dialog.dismiss();
                    ZoneAdd activity = (ZoneAdd) ZoneAdd.activity;
                    ShopInfomation activiy2 = (ShopInfomation) ShopInfomation.activity;
                    activiy2.finish();
                    activity.finish();

                } else {
                    Toast.makeText(context, "시간을 30분 단위로 설정해주세요", Toast.LENGTH_LONG).show();

                }


            }
        });

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String mm = "" + (monthOfYear + 1);
                String dd = "" + dayOfMonth;
                if (monthOfYear < 9) {
                    mm = "0" + (monthOfYear + 1);
                }
                if (dayOfMonth < 10) {
                    dd = "0" + dayOfMonth;
                }
                String datePick = String.valueOf(year) + "-" + mm + "-" + dd;

                date.setText(datePick);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String minu;
                String hour;
                if (hourOfDay < 10) {
                    hour = "0" + hourOfDay;
                } else {
                    hour = "" + hourOfDay;
                }

                if (minute < 10) {
                    minu = "0" + minute;
                } else {
                    minu = "" + minute;
                }

                String timet = hour + ":" + minu;
                time.setText(timet);
            }


        });

    }

    public void sendFCM(String msg, String userid, String youid, String roomnum) {
        //TODO("FCM 전송 처리")
//        String url = new ServerIP().http+"Android/GetMsgToken.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.e("FCM보내기", "" + response);
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        FCMsend(jsonObject.getString("token"), msg, userid, roomnum);
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
//        simpleMultiPartRequest.addStringParam("youid", youid);
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void FCMsend(String token, String msg, String userid, String roomnum) {
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
                    data.put("type", "msg");
                    data.put("room", roomnum);
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


}
