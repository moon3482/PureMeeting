package com.example.mana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.chating.Client;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

import static com.example.mana.R.color.appThemeColor;


public class ChargeCredit extends AppCompatActivity {
    LinearLayout LilCredit10000, LilCredit20000, LilCredit30000, LilCredit40000, LilCredit50000;
    public static int SUCCESSCREDITCODE = 7894;
    String itemName, loginId, index;
    int payMoney, getCredit;
    View view;
    Intent intent;
    String time1;
    private int stuck = 10;
    boolean charge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_credit);
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", "");
        LilCredit10000 = findViewById(R.id.Lil10000Credit);
        LilCredit20000 = findViewById(R.id.Lil20000Credit);
        LilCredit30000 = findViewById(R.id.Lil30000Credit);
        LilCredit40000 = findViewById(R.id.Lil40000Credit);
        LilCredit50000 = findViewById(R.id.Lil50000Credit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreditCharge);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("크레딧 충전");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        Intent intent = getIntent();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date time = new Date();
        time1 = format1.format(time);
        intent = getIntent();
        if (intent.hasExtra("index")) {
            index = intent.getStringExtra("index");
            Log.e("인덱스값", "" + index);
        }
        if (intent.hasExtra("charge")) {
            charge = true;
            LilCredit10000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemName = "10000Credit";
                    getCredit = 10000;
                    onClick_request(100);
                }
            });
            LilCredit20000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemName = "20000Credit";
                    getCredit = 20000;
                    onClick_request(100);
                }
            });
            LilCredit30000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "30000Credit";
                    getCredit = 30000;
                    onClick_request(100);
                }
            });
            LilCredit40000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "40000Credit";
                    getCredit = 40000;
                    onClick_request(100);
                }
            });
            LilCredit50000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "50000Credit";
                    getCredit = 50000;
                    onClick_request(100);
                }
            });
        } else {
            LilCredit10000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "10000Credit";
                    getCredit = 10000;
                    onClick_request(100);
                }
            });
            LilCredit20000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "20000Credit";
                    getCredit = 20000;
                    onClick_request(100);
                }
            });
            LilCredit30000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "30000Credit";
                    getCredit = 30000;
                    onClick_request(100);
                }
            });
            LilCredit40000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "40000Credit";
                    getCredit = 40000;
                    onClick_request(100);
                }
            });
            LilCredit50000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName = "50000Credit";
                    getCredit = 50000;
                    onClick_request(100);
                }
            });
        }
        BootpayAnalytics.init(this, "602db6ed5b29480027520640");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7894:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
    }

    public void onClick_request(int payMoney) {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        String pun = telManager.getLine1Number();
        // 결제호출
        BootUser bootUser = new BootUser().setPhone(pun);
        BootExtra bootExtra = new BootExtra().setQuotas(new int[]{0, 2, 3});

        Bootpay.init(getFragmentManager())
                .setApplicationId("602db6ed5b29480027520640") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.KCP) // 결제할 PG 사
                .setMethod(Method.KAKAO) // 결제수단
                .setContext(this)
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName(itemName) // 결제할 상품명
                .setOrderId(time1) // 결제 고유번호expire_month
                .setPrice(payMoney) // 결제할 금액
                .addItem(itemName, 1, "Credit " + itemName, payMoney) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {

                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        String url = new ServerIP().http+"Android/paydone.php";
                        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        simpleMultiPartRequest.addStringParam("id", loginId);
                        simpleMultiPartRequest.addStringParam("getCredit", String.valueOf(getCredit));
                        RequestQueue requestQueue = Volley.newRequestQueue(ChargeCredit.this);
                        requestQueue.add(simpleMultiPartRequest);

                        Log.d("done", message);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {
                        Toast.makeText(ChargeCredit.this, "결제가 취소 되었습니다.", Toast.LENGTH_LONG).show();
                        onBackPressed();

                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Toast.makeText(ChargeCredit.this, "결제도중 오류가 발생 되었습니다.", Toast.LENGTH_LONG).show();
                        onBackPressed();
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                if (charge) {
                                    Intent intent = new Intent();
                                    Log.e("인덱스값2", "" + index);
                                    intent.putExtra("index", index);
                                    intent.putExtra("chargereturn", "chargereturn");
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    finish();
                                }

                                Log.d("close", "close");
                            }
                        })
                .request();
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