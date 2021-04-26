package com.example.mana;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.chating.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CustomDialog3 {
    Context context;
    Socket socket;
    DataOutputStream out;
    public TextView tvHaveCredit;
    public String cd;
    Dialog dialog2;
    String pay;
    boolean aBoolean;

    public CustomDialog3(Context context, boolean aBoolean) {
        this.context = context;
        this.aBoolean = aBoolean;

    }


    public void Colldialog(String id, String index, String name, String imgurl, String rnum, String youid) throws IOException {
        dialog2 = new Dialog(context);
        dialog2.setContentView(R.layout.choose);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.show();
       new Thread(new Runnable() {
           @Override
           public void run() {
               socket = new Socket();
               try {
                   socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
                   out = new DataOutputStream(socket.getOutputStream());
                   out.writeUTF("map");
                   out.writeUTF(id);
                   out.writeUTF(rnum);
                   out.flush();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       }).start();

        final TextView tvTitle = dialog2.findViewById(R.id.tvConfirmPay);
        tvHaveCredit = dialog2.findViewById(R.id.tvHaveCredit);
        final TextView tvPayCredit = dialog2.findViewById(R.id.tvPayCredit);
        final TextView tvConfirmText = dialog2.findViewById(R.id.tvchoose);
        final EditText etConfirmEditText = dialog2.findViewById(R.id.etchoose);
        final Button btnOk = dialog2.findViewById(R.id.btnChoiceOk);
        final Button btnCancel = dialog2.findViewById(R.id.btnChoiceCancel);
        LoadCredit(id);
        tvTitle.setText("예약을 하기위해서는 보유하신 크레딧이 차감됩니다.\n(예약금+예약보증금)\n단순변심으로 취소 할 시에 환급되지 않습니다. 신중하게 선택해주세요\n 그래도 동의하시면 아래의 빨간 글자를 입력해주세요.");
        pay = "15000";
        tvPayCredit.setText(pay);

        etConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvConfirmText.getText().toString().equals(etConfirmEditText.getText().toString())) {
                    tvConfirmText.setTextColor(Color.parseColor("#33CC33"));
                    btnOk.setEnabled(true);
                } else {
                    tvConfirmText.setTextColor(Color.parseColor("#FF0000"));
                    btnOk.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnOk.setEnabled(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok(id, index, name, imgurl, rnum, youid);
                aBoolean = false;
                dialog2.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(id, index, name, imgurl, rnum, youid);
                aBoolean = false;
                dialog2.dismiss();

            }
        });
        dialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {


            }
        });

    }

    public void LoadCredit(String id) {
        String url = "http://3.36.21.126/Android/LoadCredit.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Log.e("통신함", "통신함");
                        cd = jsonObject.getString("Credit");
                        tvHaveCredit.setText(cd);
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
        Log.e("다이얼로그 아이디 ", "" + id);
        simpleMultiPartRequest.addStringParam("id", id);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
    }

    public void ok(String id, String index, String name, String imgurl, String rnum, String youid) {
        String url = "http://3.36.21.126/Android/finalzone.php";
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
                                        jsonObject.put("type", "ChooseOK");
                                        jsonObject.put("name", name);
                                        jsonObject.put("img", imgurl);
                                        jsonObject.put("msg", indexjson);
                                        jsonObject.put("id", id);
                                        jsonObject.put("room", rnum);
                                        jsonObject.put("youid", youid);
                                        String aa = jsonArray.put(jsonObject).toString();
                                        out.writeUTF(aa);
                                        out.flush();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                            finalcheck(index, pay, id, name, imgurl, rnum, youid);
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
        simpleMultiPartRequest.addStringParam("final", "ok");
        simpleMultiPartRequest.addStringParam("index", index);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
    }

    public void cancel(String id, String index, String name, String imgurl, String rnum, String youid) {
        String url = "http://3.36.21.126/Android/finalzone.php";
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
//                                        socket = new Socket();
//                                        socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
//                                        out = new DataOutputStream(socket.getOutputStream());
//                                        out.writeUTF("map");
//                                        out.writeUTF(id);
//                                        out.writeUTF(rnum);
//                                        out.flush();

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
                                        out.writeUTF(aa);
                                        out.flush();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                            finalcheck(index, pay, id, name, imgurl, rnum, youid);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
    }

    public void finalcheck(String index, String pay, String id, String name, String imgurl, String rnum, String youid) {
        String url = "http://3.36.21.126/Android/finalcheck.php";
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
                                        out.writeUTF(aa);
                                        out.flush();
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
                                        out.writeUTF(aa);
                                        out.flush();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(simpleMultiPartRequest);
    }

}
