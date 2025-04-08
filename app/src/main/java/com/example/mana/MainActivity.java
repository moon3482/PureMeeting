package com.example.mana;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mana.logging.Tag;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    //로그인 입력 변수
    String EditTextGetId;
    String EditTextGetPassword;
    String kakaoid, token;
    public static String userid;
    //    private SessionCallback sessionCallback = new SessionCallback();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText login_input_id, login_input_password;
    Activity activity;
    Button login_Button, login_Button_false;
    Function2<? super OAuthToken, ? super Throwable, Unit> kakaoLoginCallback = (Function2<OAuthToken, Throwable, Unit>) (oAuthToken, throwable) -> {
        if (throwable != null) {
            Timber.tag(Tag.KAKAO_LOGIN).d("Login Failure : %s", throwable.getMessage());
        } else if (oAuthToken != null) {
            Timber.tag(Tag.KAKAO_LOGIN).d("Login Success : %s", oAuthToken);
        }
        return null;
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        login_input_id = (EditText) findViewById(R.id.login_Input_Id);
        login_input_password = (EditText) findViewById(R.id.login_Input_Password);
        login_Button = (Button) findViewById(R.id.login_Button);
        login_Button_false = (Button) findViewById(R.id.login_Button_false);
        TextView sign_Up_Button = (TextView) findViewById(R.id.signup_Button);
        ImageView kakaoLogin = (ImageView) findViewById(R.id.kakaologin);
        getHashKey();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 11);

            }
        }
        //앱 해쉬키
        //        getHashKey();

        //가입버튼
        sign_Up_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signupTermsActivity.class);
                startActivity(intent);

            }
        });
        //로그인 버튼
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverUrl = new ServerIP().http + "Android/login.php";
                EditTextGetId = login_input_id.getText().toString();
                EditTextGetPassword = login_input_password.getText().toString();
                //TODO("로그인")
//                SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response);
//                            boolean login = jsonObject.getBoolean("success");
//                            if (login) {
//                                String st = jsonObject.getString("profilethumimg");
//                                String name = jsonObject.getString("name");
//
//
//                                sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
//                                new insertshar().execute(st);
//                                editor = sharedPreferences.edit();
//                                editor.putString("imgurl", st);
//                                editor.putString("loginId", EditTextGetId);
//                                editor.putString("loginName", name);
//
//                                editor.commit();
//                                userid = EditTextGetId;
//                                if (jsonObject.getString("first").equals("0")) {
//                                    Intent intent = new Intent(MainActivity.this, InsertMyinfoDetail.class);
//                                    intent.putExtra("id", EditTextGetId);
//                                    startActivity(intent);
//                                } else {
//                                    Toast.makeText(MainActivity.this, "로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(MainActivity.this, mainPage.class);
//
//                                    startActivity(intent);
//                                }
//                            } else {
//                                Toast.makeText(MainActivity.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//                simpleMultiPartRequest.setShouldCache(false);
//                simpleMultiPartRequest.addStringParam("email", EditTextGetId);
//                simpleMultiPartRequest.addStringParam("pasw", EditTextGetPassword);
//                simpleMultiPartRequest.addStringParam("token", token);
//                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//                requestQueue.add(simpleMultiPartRequest);
            }
        });

        kakaoLogin.setOnClickListener(v -> {
            boolean available = UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this);
            if (available) {
                Timber.tag(Tag.KAKAO_LOGIN).d("KakaoTalk Login Available");
                UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, kakaoLoginCallback);
            } else {
                Timber.tag(Tag.KAKAO_LOGIN).d("KakaoTalk Login UnAvailable");
                UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, kakaoLoginCallback);
            }
        });
//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.w("FirebaseSettingEx", "getInstanceId failed", task.getException());
//                return;
//            }

        //TODO("토큰 저장")
        // 토큰을 읽고, 텍스트 뷰에 보여주기
//            token = task.getResult().getToken();
//            SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("token", token);
//            editor.commit();
//        });
    }


    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

//    public class SessionCallback implements ISessionCallback {
//        String email;
//
//        // 로그인에 성공한 상태
//        @Override
//        public void onSessionOpened() {
//            requestMe();
//        }
//
//        // 로그인에 실패한 상태
//        @Override
//        public void onSessionOpenFailed(KakaoException exception) {
//            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
//        }
//
//        // 사용자 정보 요청
//        public void requestMe() {
//            UserManagement.getInstance()
//                    .me(new MeV2ResponseCallback() {
//                        @Override
//                        public void onSessionClosed(ErrorResult errorResult) {
//                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
//                        }
//
//                        @Override
//                        public void onFailure(ErrorResult errorResult) {
//                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
//                        }
//
//                        @Override
//                        public void onSuccess(MeV2Response result) {
//
//                            Log.i("KAKAO_API", "사용자 아이디: " + result.getId());
//                            kakaoid = String.valueOf(result.getId());
//                            UserAccount kakaoAccount = result.getKakaoAccount();
//                            if (kakaoAccount != null) {
//
//                                // 이메일
//                                email = kakaoAccount.getEmail();
//
//                                if (email != null) {
//                                    Log.i("KAKAO_API", "email: " + email);
//
//                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
//                                    // 동의 요청 후 이메일 획득 가능
//                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
//
//                                } else {
//                                    // 이메일 획득 불가
//                                }
//
//                                // 프로필
//                                Profile profile = kakaoAccount.getProfile();
//                                String f = kakaoAccount.getBirthday();
//                                AgeRange a = kakaoAccount.getAgeRange();
//                                Gender g = kakaoAccount.getGender();
//
//
//                                if (profile != null) {
//                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
//                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
//                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
//                                    Log.d("KAKAO_API", "생일: " + a + f + g);
//                                    String URL = new ServerIP().http + "Android/kakaologin.php";
//                                    SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            JSONObject jsonObject = null;
//                                            try {
//                                                jsonObject = new JSONObject(response);
//                                                boolean login = jsonObject.getBoolean("success");
//                                                if (login) {
//                                                    String st = jsonObject.getString("profilethumimg");
//                                                    String name = jsonObject.getString("name");
//                                                    sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
//                                                    editor = sharedPreferences.edit();
//                                                    editor.putString("loginId", kakaoid);
//                                                    new insertshar().execute(st);
//                                                    editor.putString("imgurl", st);
//                                                    editor.putString("loginName", name);
//                                                    editor.commit();
//                                                    if (jsonObject.getString("first").equals("0")) {
//                                                        Intent intent1 = new Intent(MainActivity.this, InsertMyinfoDetail.class);
//                                                        intent1.putExtra("id", EditTextGetId);
//                                                        startActivity(intent1);
//                                                    } else {
//                                                        Toast.makeText(MainActivity.this, "로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
//                                                        Intent intent1 = new Intent(MainActivity.this, mainPage.class);
//
//
//                                                        startActivity(intent1);
//                                                    }
//                                                } else {
//                                                    Intent intent = new Intent(MainActivity.this, Signup_Terms_Activity.class);
//                                                    intent.putExtra("kakaoid", kakaoid);
//                                                    intent.putExtra("email", email);
//                                                    intent.putExtra("gender", String.valueOf(g));
//                                                    startActivity(intent);
//                                                }
//
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//
//                                        }
//                                    });
//                                    simpleMultiPartRequest.setShouldCache(false);
//                                    simpleMultiPartRequest.addStringParam("kakaoid", kakaoid);
//                                    simpleMultiPartRequest.addStringParam("token", token);
//                                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//                                    requestQueue.add(simpleMultiPartRequest);
//
//
//                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
//                                    // 동의 요청 후 프로필 정보 획득 가능
//
//
//                                } else {
//                                    // 프로필 획득 불가
//                                }
//                            }
//                        }
//                    });
//        }
//
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 11:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            Log.e("로그 ", String.valueOf(url));
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

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //바이트 배열을 차례대로 읽어 들이기위한 ByteArrayOutputStream클래스 선언
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//bitmap을 압축 (숫자 70은 70%로 압축한다는 뜻)
        byte[] bytes = baos.toByteArray();//해당 bitmap을 byte배열로 바꿔준다.
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);//Base 64 방식으로byte 배열을 String으로 변환
        return temp;//String을 retrurn
    }


//    private class insertshar extends AsyncTask<String, Void, Bitmap> {
//        Bitmap bit;
//
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            String url = strings[0];
//            bit = getBitmapFromURL(url);
//            return bit;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            String bittoSt = BitmapToString(bitmap);
//            editor.putString("loginbit", bittoSt);
//            editor.apply();
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        System.exit(0);


    }

    @Override
    protected void onStart() {
        super.onStart();

        login_input_id.setText("");
        login_input_password.setText("");
        login_input_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (login_input_id.getText().toString().trim().length() > 0 && login_input_password.getText().toString().trim().length() > 0) {
                    login_Button_false.setVisibility(View.GONE);
                    login_Button.setVisibility(View.VISIBLE);
                } else {
                    login_Button.setVisibility(View.GONE);
                    login_Button_false.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        login_input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (login_input_id.getText().toString().trim().length() > 0 && login_input_password.getText().toString().trim().length() > 0) {
                    login_Button_false.setVisibility(View.GONE);
                    login_Button.setVisibility(View.VISIBLE);
                } else {
                    login_Button.setVisibility(View.GONE);
                    login_Button_false.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
