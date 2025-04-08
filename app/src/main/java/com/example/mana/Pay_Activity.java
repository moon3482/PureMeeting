package com.example.mana;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mana.logging.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.BootpayAnalytics;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
import timber.log.Timber;

public class Pay_Activity extends AppCompatActivity {
    private int stuck = 10;

    String itemName, loginId, index;
    int getCredit;
    double payMoney;
    View view;
    Intent intent;

    BootpayEventListener eventListener = new BootpayEventListener() {
        @Override
        public void onCancel(String data) {
            Timber.tag(Tag.BOOT_PAY).d("onCancel : %s", data);
        }

        @Override
        public void onError(String data) {
            Timber.tag(Tag.BOOT_PAY).d("onError : %s", data);
        }

        @Override
        public void onClose() {
            Timber.tag(Tag.BOOT_PAY).d("onClose");
            Bootpay.removePaymentWindow();
        }

        @Override
        public void onIssued(String data) {
            Timber.tag(Tag.BOOT_PAY).d("onIssued : %s", data);
        }

        @Override
        public boolean onConfirm(String data) {
            Timber.tag(Tag.BOOT_PAY).d("onConfirm : %s", data);
            Bootpay.transactionConfirm();
            return false;
        }

        @Override
        public void onDone(String data) {
            Timber.tag(Tag.BOOT_PAY).d("onDone : %s", data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_);
        intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        payMoney = intent.getIntExtra("pay", 0);
        getCredit = intent.getIntExtra("getCredit", 0);
        if (intent.hasExtra("action")) {
            index = intent.getStringExtra("index");
        }
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", "");


        BootpayAnalytics.init(this, "602db6ed5b29480027520640");
    }

    @Override
    protected void onResume() {
        super.onResume();
        onClick_request(view);
    }

    public void onClick_request(View v) {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        String pun = telManager.getLine1Number();
        // 결제호출
        BootUser bootUser = new BootUser().setPhone(pun);
        BootExtra bootExtra = new BootExtra().setCardQuota("0,2,3,4,5,6,7,8,9,10,11,12");
        List<BootItem> items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(100d);
        items.add(item1);
        Payload payload = new Payload();
        payload.setApplicationId("602db6ed5b29480027520640")
                .setOrderName("결제 테스트")
                .setPg("케이씨피")
                .setMethod("네이버페이")
                .setOrderId(UUID.randomUUID().toString())
                .setPrice(payMoney)
                .setUser(bootUser)
                .setItems(items)
                .setExtra(bootExtra);
        Bootpay.init(getSupportFragmentManager())
                .setPayload(payload)
                .setEventListener(eventListener)
                .requestPayment();
//                .setApplicationId("602db6ed5b29480027520640") // 해당 프로젝트(안드로이드)의 application id 값
//                .setPG(PG.KCP) // 결제할 PG 사
//                .setMethod(Method.KAKAO) // 결제수단
//                .setContext(this)
//                .setBootUser(bootUser)
//                .setBootExtra(bootExtra)
//                .setUX(UX.PG_DIALOG)
////                .setUserPhone("010-1234-5678") // 구매자 전화번호
//                .setName(itemName) // 결제할 상품명
//                .setOrderId("1234") // 결제 고유번호expire_month
//                .setPrice(payMoney) // 결제할 금액
//                .addItem(itemName, 1, "Credit " + itemName, payMoney) // 주문정보에 담길 상품정보, 통계를 위해 사용
////                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
//                    @Override
//                    public void onConfirm(@Nullable String message) {
//
//                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
//                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
//                        Log.d("confirm", message);
//                    }
//                })
//                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
//                    @Override
//                    public void onDone(@Nullable String message) {
//                        String url = new ServerIP().http+"Android/paydone.php";
//                        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
////                                if (intent.hasExtra("charge")) {
////                                    Intent intent = new Intent();
////                                    setResult(RESULT_OK, intent);
////                                    finish();
////                                } else {
////                                    finish();
////                                }
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//                        });
//                        simpleMultiPartRequest.addStringParam("id", loginId);
//                        simpleMultiPartRequest.addStringParam("getCredit", String.valueOf(getCredit));
//                        RequestQueue requestQueue = Volley.newRequestQueue(Pay_Activity.this);
//                        requestQueue.add(simpleMultiPartRequest);

//                        Log.d("done", message);
//                    }
//                })
//                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
//                    @Override
//                    public void onReady(@Nullable String message) {
//                        Log.d("ready", message);
//                    }
//                })
//                .onCancel(new CancelListener() { // 결제 취소시 호출
//                    @Override
//                    public void onCancel(@Nullable String message) {
//                        Toast.makeText(Pay_Activity.this, "결제가 취소 되었습니다.", Toast.LENGTH_LONG).show();
//                        onBackPressed();
//
//                        Log.d("cancel", message);
//                    }
//                })
//                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
//                    @Override
//                    public void onError(@Nullable String message) {
//                        Toast.makeText(Pay_Activity.this, "결제도중 오류가 발생 되었습니다.", Toast.LENGTH_LONG).show();
//                        onBackPressed();
//                        Log.d("error", message);
//                    }
//                })
//                .onClose(
//                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
//                            @Override
//                            public void onClose(String message) {
//                                if (intent.hasExtra("charge")) {
//                                    Intent intent = new Intent();
//                                    setResult(RESULT_OK, intent);
//                                    finish();
//                                }
//
//                                Log.d("close", "close");
//                            }
//                        })
//                .request();
    }
}