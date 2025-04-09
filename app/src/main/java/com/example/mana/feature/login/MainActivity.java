package com.example.mana.feature.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mana.R;
import com.example.mana.SocialLoginButton;
import com.example.mana.logging.Tag;
import com.example.mana.SignupTermsActivity;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    SocialLoginButton kakaoLogin;
    SocialLoginButton naverLogin;
    Function2<? super OAuthToken, ? super Throwable, Unit> kakaoLoginCallback = (Function2<OAuthToken, Throwable, Unit>) (oAuthToken, throwable) -> {
        if (throwable != null) {
            Timber.tag(Tag.KAKAO_LOGIN).d("Login Failure : %s", throwable.getMessage());
        } else if (oAuthToken != null) {
            Timber.tag(Tag.KAKAO_LOGIN).d("Login Success : %s", oAuthToken);
            onLoginSuccess();
        }
        return null;
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kakaoLogin = findViewById(R.id.kakaoLoginButton);
        naverLogin = findViewById(R.id.naverLoginButton);

        kakaoLogin.setOnClickListener(view -> {
            boolean available = UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this);
            if (available) {
                Timber.tag(Tag.KAKAO_LOGIN).d("KakaoTalk Login Available");
                UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, kakaoLoginCallback);
            } else {
                Timber.tag(Tag.KAKAO_LOGIN).d("KakaoTalk Login UnAvailable");
                UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, kakaoLoginCallback);
            }
        });

        naverLogin.setOnClickListener(view -> {
            NaverIdLoginSDK.INSTANCE.authenticate(this, new OAuthLoginCallback() {
                @Override
                public void onSuccess() {
                    Timber.tag(Tag.NAVER_LOGIN).d("onSuccess");
                    Timber.tag(Tag.NAVER_LOGIN).d("AccessToken : %s", NaverIdLoginSDK.INSTANCE.getAccessToken());
                    Timber.tag(Tag.NAVER_LOGIN).d("RefreshToken : %s", NaverIdLoginSDK.INSTANCE.getRefreshToken());
                    Timber.tag(Tag.NAVER_LOGIN).d("ExpiresTokenAt : %s", NaverIdLoginSDK.INSTANCE.getExpiresAt());
                    Timber.tag(Tag.NAVER_LOGIN).d("TokenType : %s", NaverIdLoginSDK.INSTANCE.getTokenType());
                    Timber.tag(Tag.NAVER_LOGIN).d("State : %s", NaverIdLoginSDK.INSTANCE.getState());
                    Timber.tag(Tag.NAVER_LOGIN).d("Version : %s", NaverIdLoginSDK.INSTANCE.getVersion());
                    onLoginSuccess();
                }

                @Override
                public void onFailure(int i, @NonNull String s) {
                    Timber.tag(Tag.NAVER_LOGIN).d("onFailure : %s", s);
                    Timber.tag(Tag.NAVER_LOGIN).d("Code : %s", NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode());
                    Timber.tag(Tag.NAVER_LOGIN).d("Description : %s", NaverIdLoginSDK.INSTANCE.getLastErrorCode().getDescription());
                    Timber.tag(Tag.NAVER_LOGIN).d("ErrorDescription : %s", NaverIdLoginSDK.INSTANCE.getLastErrorDescription());
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Timber.tag(Tag.NAVER_LOGIN).d("onError : %s", s);
                }
            });
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

    private void onLoginSuccess(){
        Intent intent = new Intent(MainActivity.this, SignupTermsActivity.class);
        startActivity(intent);
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
}
