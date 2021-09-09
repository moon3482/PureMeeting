package com.example.mana.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.ChargeCredit;
import com.example.mana.ChatPage.chatPage;
import com.example.mana.MainActivity;
import com.example.mana.MainPage.mainPage;
import com.example.mana.MyIdealTypeSetting;
import com.example.mana.MyProfileLoad;
import com.example.mana.R;
import com.example.mana.ServerIP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import static com.example.mana.R.color.appThemeColor;

public class MyPage extends AppCompatActivity {
    ImageView ivMyImage;
    String id;
    TextView tvMyName, tvMyAge, tvMyCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        /********선언부*********/
        //상단화면
        ivMyImage = findViewById(R.id.ivMyMainImage);
        tvMyName = findViewById(R.id.tvMyInfoName);
        tvMyAge = findViewById(R.id.tvMyInfoAge);
        tvMyCredit = findViewById(R.id.tvUserCredit);
        ImageView ivMyIdealType = findViewById(R.id.ivIdealTypeImage);

        LinearLayout liLayMyInfo = findViewById(R.id.liLayMyinfomation);
        LinearLayout liLayCreditCharge = findViewById(R.id.liLayCredit);
        LinearLayout liLayIdealType = findViewById(R.id.liLayIdealType);
        LinearLayout liLayLogOut = findViewById(R.id.liLayLogout);
        LinearLayout liLayMyschedule = findViewById(R.id.liLayMyschedule);
//        DrawableCompat.setTint(ivMyIdealType.getDrawable(), ContextCompat.getColor(MyPage.this, R.color.gray));

        //바텀
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivHomeButton = (ImageView) findViewById(R.id.MybnHomeButton);
        ivMessageBoxButton = findViewById(R.id.MybnMessageBoxButton);
        ivMapButton = findViewById(R.id.MybnMapButton);
        ivMyPageButton = findViewById(R.id.MybnMyPageButton);

        /************이미지뷰에 버튼 이미지 set**************/
        ivHomeButton.setImageResource(R.drawable.ic_baseline_home_24);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMapButton.getDrawable(), ContextCompat.getColor(MyPage.this, appThemeColor));
        DrawableCompat.setTint(ivMessageBoxButton.getDrawable(), ContextCompat.getColor(MyPage.this, appThemeColor));
        DrawableCompat.setTint(ivHomeButton.getDrawable(), ContextCompat.getColor(MyPage.this, appThemeColor));

        /********선택 이미지뷰 디자인 변경******/
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(MyPage.this, R.color.White));
        ivMyPageButton.setBackgroundColor(Color.parseColor("#E91E63"));

        /************툴바 선언부*********/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMyPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("마이페이지");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        /*****************쉐어드에서 로그인 정보 불러오기*************************************/
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        id = sharedPreferences.getString("loginId", "");


        liLayCreditCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, ChargeCredit.class);
                startActivity(intent);
            }
        });
        liLayLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPage.this);
                dialog = builder.setTitle("로그아웃")
                        .setMessage("정말 로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MyPage.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Toast.makeText(MyPage.this, "로그아웃이 되었습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();

                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });

        liLayMyschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, Schedule.class);
                startActivity(intent);
            }
        });

        liLayMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, MyProfileLoad.class);
                startActivity(intent);
            }
        });
        liLayIdealType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, MyIdealTypeSetting.class);
                startActivity(intent);
            }
        });


        /********************************
         **********바텀 부분**************
         ********************************/
        /****메인페이지버튼********/
        ivHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, mainPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ivMessageBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, chatPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        /*******맵 버튼************/
        ivMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, mapPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyPage.this, mainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ImageDonwTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            try {
                String imgurl = strings[0];
                URL url = new URL(imgurl);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ivMyImage.setImageBitmap(bitmap);
        }
    }

    public int getAge(int birthYear, int birthMonth, int birthDay) {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear + 1;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age;
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivMyPageButton = findViewById(R.id.MybnMyPageButton);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(MyPage.this, R.color.White));
        ivMyPageButton.setBackgroundColor(Color.parseColor("#E91E63"));
        /************정보 불러오기 리퀘스트 **************/
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = new ServerIP().http+"Android/myinfo.php";
                SimpleMultiPartRequest myInfomation = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            Boolean success = jsonObject1.getBoolean("success");

                            if (success) {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String agesplit[] = jsonObject1.getString("birthday").split("-");
                                            int age = getAge(Integer.parseInt(agesplit[0]), Integer.parseInt(agesplit[1]), Integer.parseInt(agesplit[2]));
                                            String credit = String.valueOf(jsonObject1.getInt("credit"));

                                            tvMyName.setText(jsonObject1.getString("name") + " / " + jsonObject1.getString("gender"));
                                            tvMyAge.setText(String.valueOf(age) + "세");
                                            tvMyCredit.setText(credit);
                                            SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
                                            String myinfoBitStr = sharedPreferences.getString("loginbit", "");
                                            Bitmap myinfoStrBit = StringToBitmap(myinfoBitStr);
                                            ivMyImage.setImageBitmap(myinfoStrBit);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            }
                            ;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                });
                Log.e("보내는 아이디", id);
                myInfomation.addStringParam("loginId", id);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(myInfomation);
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivMyPageButton = findViewById(R.id.MybnMyPageButton);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(MyPage.this, appThemeColor));
    }
}