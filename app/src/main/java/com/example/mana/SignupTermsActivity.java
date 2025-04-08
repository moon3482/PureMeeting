package com.example.mana;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignupTermsActivity extends AppCompatActivity {
    CheckBox service, info;
    String email, img, gender, kakaoid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_terms);

        Intent intent = getIntent();
        if (intent.hasExtra("kakaoid")) {
            kakaoid = intent.getExtras().getString("kakaoid");
            email = intent.getExtras().getString("email");
            gender = intent.getExtras().getString("gender");
        }


        //이용약관 내용 생성
        String Terms1_text = new Terms_Text().terms_Text1;
        String Terms2_text = new Terms_Text().terms_Text2;

        //이용약관 동의 여부 체크 변수
        final boolean[] terms1 = {false};
        final boolean[] terms2 = {false};

        //툴바 생성후 툴바 타이틀 변경
        Toolbar toolbar = (Toolbar) findViewById(R.id.signup_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("이용약관");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        //버튼 선언부분
        service = findViewById(R.id.check_Terms_service);
        info = findViewById(R.id.check_Terms_info);
        Button sign_Up_Next_Button = (Button) findViewById(R.id.sign_Next_Button);

        //약관 스크롤 뷰 선언

        ScrollView terms2_Text_Scrollview = (ScrollView) findViewById(R.id.terms_TextView_ScrollView2);

        //약관 텍스트뷰 선언
        TextView terms1_Text_View = (TextView) findViewById(R.id.terms1_Text_View);
        TextView terms2_Text_View = (TextView) findViewById(R.id.terms2_Text_View);

        //약관내용 셋업
        terms1_Text_View.setText(Terms1_text);
        terms2_Text_View.setText(Terms2_text);

        //초기버튼 및 약관내용 숨김


        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service.isChecked()) {
                    terms1[0] = true;
                    service.setChecked(true);
                } else {
                    terms1[0] = false;
                    service.setChecked(false);
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.isChecked()) {
                    terms2[0] = true;
                    info.setChecked(true);
                } else {
                    terms2[0] = false;
                    info.setChecked(false);
                }
            }
        });


        //다음 버튼 눌렀을때
        sign_Up_Next_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!terms1[0]) {
                    dialog("서비스 이용약관");
                } else if (!terms2[0]) {
                    dialog("개인정보 이용약관");
                } else {
                    Intent intent = new Intent(SignupTermsActivity.this, SignupInformationInput.class);
                    if (email != null) {
                        intent.putExtra("email", email);
                    }


                    if (gender!= null) {
                        intent.putExtra("gender", gender);
                    }
                    if (kakaoid!= null) {
                        intent.putExtra("kakaoid", kakaoid);
                    }
                    startActivity(intent);

                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 다이얼로그 띄우는 클래스 2020-11-19
     */
    void dialog(String a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("약관동의");
        builder.setMessage(a + "에 동의해주세요");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


}
