package com.example.mana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mana.Camera.Camera1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.*;

public class Signup_information_input extends AppCompatActivity {
    ImageView iv_image;
    Calendar calendar;
    int Year, Month, Day;
    EditText birthday, email, name;
    String password;
    String password_Check;
    boolean pw_Check = false;
    String sign_Gender;
    String sign_Area;
    String sign_Jabs;
    Button id_Check;
    String imgPath;
    String EmailCode;
    String sign_Name, sign_email, sign_Birthday;
    TextView timer;
    LinearLayout Email_code_Linear;
    Button submit;
    EditText input_email_code;
    EditText input_Pw;
    EditText input_Pw_Check;
    String intent_email, intent_img, intent_gender, intent_kakaoid, imgurl;
    Bitmap bm;
    String img;
    int CAMERA_RESULT_CODE = 335;
    private AlertDialog dialog;
    boolean validate, spinner_Gen, spinner_Jab, spinner_Area;
    int sec;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            timer.setText(sec + "초");
            sec--;
            handler.postDelayed(this, 1000);
            if (sec < 0) {

                handler.removeCallbacks(this);
//                timer.setVisibility(View.INVISIBLE);


            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_infomation_input);
        //툴바 선언
        Toolbar toolbar = (Toolbar) findViewById(R.id.signup_information_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("회원정보입력");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        //위젯 설정
        timer = findViewById(R.id.timer);
        birthday = findViewById(R.id.Sign_input_Birthday);
        iv_image = findViewById(R.id.imageView);

        input_Pw = (EditText) findViewById(R.id.sign_input_Pw);
        password = input_Pw.getText().toString();
        input_Pw_Check = (EditText) findViewById(R.id.sign_input_PwCheck);
        password_Check = input_Pw_Check.getText().toString();
        id_Check = findViewById(R.id.id_Check_Button);
        email = findViewById(R.id.sign_input_Email);
        name = findViewById(R.id.sing_input_name);
        Email_code_Linear = findViewById(R.id.email_code_insert_Linear);
        submit = findViewById(R.id.sign_Submit);
        Button button_email_code = (Button) findViewById(R.id.button_Email_Code);
        input_email_code = (EditText) findViewById(R.id.sign_input_code);
        Email_code_Linear.setVisibility(GONE);
        Intent intent = getIntent();

        if (intent.hasExtra("kakaoid")) {
            intent_kakaoid = intent.getExtras().getString("kakaoid");
        }
        if (intent.hasExtra("email")) {
            intent_email = intent.getExtras().getString("email");
            email.setText(intent_email);
            email.setEnabled(false);
            validate = true;
            id_Check.setBackgroundColor(Color.parseColor("#19FC00"));
            id_Check.setText("인증완료");


        }

        /**
         *
         *  카카오프로필 이미지주소를  인텐트로 받아서  이미지뷰에 표시하는 메서드
         *  2021-03-20 이제  쓸일이 없어짐
         */
//        if (intent.hasExtra("img")) {
//            intent_img = intent.getExtras().getString("img");
//
//
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {    // 오래 거릴 작업을 구현한다
//                    // TODO Auto-generated method stub
//                    try {
//                        // 걍 외우는게 좋다 -_-;
//
//                        URL url = new URL(intent_img);
//                        InputStream is = url.openStream();
//                        bm = BitmapFactory.decodeStream(is);
//                        handler.post(new Runnable() {
//
//                            @Override
//                            public void run() {  // 화면에 그려줄 작업
//
//                                iv_image.setImageBitmap(bm);
//                            }
//                        });
//                        iv_image.setImageBitmap(bm); //비트맵 객체로 보여주기
//
//                    } catch (Exception e) {
//
//                    }
//
//                }
//            });
//
//            t.start();
//            Glide.with(Signup_information_input.this)
//                    .load(imgPath)
//
//                    .into(iv_image);
//    }


        /** 스피너 **/
        //성별 스피너
        Spinner gender = (Spinner) findViewById(R.id.sign_gender_spinner);
        ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gender_adapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sign_Gender = parent.getItemAtPosition(position).toString();
                if (sign_Gender.equals("성별")) {
                    spinner_Gen = false;
                } else {
                    spinner_Gen = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

//        //지역 스피너
//        final Spinner area = (Spinner) findViewById(R.id.sign_area_spinner);
//        ArrayAdapter<CharSequence> area_adapter = ArrayAdapter.createFromResource(this, R.array.area, android.R.layout.simple_spinner_dropdown_item);
//        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        area.setAdapter(area_adapter);
//        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sign_Area = parent.getItemAtPosition(position).toString();
//                if (sign_Area.equals("지역")) {
//                    spinner_Area = false;
//                } else {
//                    spinner_Area = true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//
//        });
//        //직업 스피너
//        final Spinner jabs = (Spinner) findViewById(R.id.sign_jabs_spinner);
//        ArrayAdapter<CharSequence> jabs_adapter = ArrayAdapter.createFromResource(this, R.array.jabs, android.R.layout.simple_spinner_dropdown_item);
//        jabs_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        jabs.setAdapter(jabs_adapter);
//        jabs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sign_Jabs = parent.getItemAtPosition(position).toString();
//                if (sign_Jabs.equals("직업")) {
//                    spinner_Jab = false;
//                } else {
//                    spinner_Jab = true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        /**생일 입력 이벤트**/
        birthday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //캘린더 정의
                calendar = Calendar.getInstance(Locale.KOREA);
                Year = calendar.get(Calendar.YEAR);
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);

                //다이얼로그 생성
                DatePickerDialog datePickerDialog = new DatePickerDialog(Signup_information_input.this,
                        AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        String pickerY = String.valueOf(year);
                        String pickerM = String.valueOf(month + 1);
                        String pickerD = String.valueOf(day);
                        Log.i("월", pickerM);

                        birthday.setText(pickerY + "-" + pickerM + "-" + pickerD);

                    }
                }, Year, Month, Day);

                //Call show() to simply show the dialog
                datePickerDialog.show();
            }
        });
        /**********************************/
        /************사진등록***************/
        /**********************************/
        iv_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 11);


                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                            Log.d(TAG, "권한 설정 완료");
                        } else {
//                            Log.d(TAG, "권한 설정 요청");
                            ActivityCompat.requestPermissions(Signup_information_input.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    } else {

                    }
                }
                Intent intent1 = new Intent(Signup_information_input.this, Camera1.class);
                startActivityForResult(intent1, CAMERA_RESULT_CODE);

            }
        });

        //패스워드 재입력 체크
        input_Pw_Check.addTextChangedListener(new TextWatcher() {
            TextView match_Pw = (TextView) findViewById(R.id.match_Pw);
            TextView different_Pw = (TextView) findViewById(R.id.different_Pw);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_Pw.getText().toString().equals(input_Pw_Check.getText().toString())) {
                    different_Pw.setVisibility(GONE);
                    match_Pw.setVisibility(VISIBLE);
                    pw_Check = true;
                } else if (!input_Pw.getText().toString().equals(input_Pw_Check.getText().toString())) {
                    match_Pw.setVisibility(GONE);
                    different_Pw.setVisibility(VISIBLE);
                    pw_Check = false;
                }
                if (input_Pw_Check.getText().toString().length() == 0) {
                    different_Pw.setVisibility(GONE);
                    match_Pw.setVisibility(INVISIBLE);
                    pw_Check = false;

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //중복 검사 버튼
        id_Check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = email.getText().toString();
                if (validate) {
                    return;
                }
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                if (!isValidEmail(userID)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                    dialog = builder.setMessage("이메일 형식이 아닙니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            Log.e("아이디", String.valueOf(success));

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                                String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                                        "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                                String newCode = new String();

                                for (int x = 0; x < 8; x++) {
                                    int random = (int) (Math.random() * str.length);
                                    newCode += str[random];
                                }
                                EmailCode = newCode;
                                sendMail();
                                handler.removeCallbacks(runnable);
                                Email_code_Linear.setVisibility(VISIBLE);
                                sec = 60;

                                dialog = builder.setMessage("이메일 인증번호를 입력해주세요")
                                        .setTitle("이메일 인증")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                handler.postDelayed(runnable, 3500);
                                            }
                                        })
                                        .create();
                                dialog.show();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                                dialog = builder.setMessage("이미 가입 된 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Signup_information_input.this);
                queue.add(validateRequest);


            }


            /** 리퀘스트 보내는 2020-11-23 '중복검사 안되어서'
             AppHelper.requestQueue = Volley.newRequestQueue(Signup_information_input.this);
             AppHelper.requestQueue.add(request);
             println("요청보냄");**/

            /** 이메일 정규표현식 검사 메소드 */
            public boolean isValidEmail(String email) {
                boolean err = false;
                String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(email);
                if (m.matches()) {
                    err = true;
                }
                return err;
            }


        });
        /** 이메일 인증 후 변경하면 인증 풀림 */
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate = false;
                id_Check.setText("이메일 인증");
                id_Check.setBackgroundResource(android.R.drawable.btn_default);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /** 이메일 인증코드 입력후 인증버튼*/
        button_email_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String string_email_code = input_email_code.getText().toString();

                if (EmailCode.equals(string_email_code)) {
                    handler.removeCallbacks(runnable);
                    validate = true;
                    id_Check.setBackgroundColor(Color.parseColor("#19FC00"));
                    id_Check.setText("인증완료");

                    Email_code_Linear.setVisibility(GONE);
                    input_email_code.setText("");
                } else if (sec <= 0) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                    builder.setTitle("이메일 인증");
                    builder.setMessage("인증시간을 초과 하였습니다.");
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            validate = false;
                        }
                    });

                    builder.show();
                } else if (!EmailCode.equals(string_email_code)) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                    builder.setTitle("이메일 인증");
                    builder.setMessage("인증코드가 일치하지 않습니다.");
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            validate = false;
                        }
                    });

                    builder.show();
                }
            }
        });
        /**가입 완료버튼*/
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String ID = email.getText().toString();
                String Password = input_Pw_Check.getText().toString();
                String birth = birthday.getText().toString();
                String input_name = name.getText().toString();
                boolean name_check = Pattern.matches("^[가-힣]*$", input_name);
                Log.i(input_name, String.valueOf(name_check));
                if (validate) {
                    if (!pw_Check) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setMessage("비밀번호를 확인해주세요");
                        alertDialog.setPositiveButton("확인", null);
                        alertDialog.show();

                    } else if (input_name.length() < 1 || name_check == false) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setMessage("이름을 확인해주세요");
                        alertDialog.setPositiveButton("확인", null);
                        alertDialog.show();
                    } else if (birth.length() == 0) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setMessage("생일을 입력해주세요");
                        alertDialog.setPositiveButton("확인", null);
                        alertDialog.show();
                    } else if (sign_Gender.equals("성별")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setMessage("성별을 선택해주세요");
                        alertDialog.setPositiveButton("확인", null);
                        alertDialog.show();
                    } else {//안드로이드에서 보낼 데이터를 받을 php 서버 주소
                        String serverUrl = new ServerIP().http+"Android/insertDB.php";

                        //Volley plus Library를 이용해서
                        //파일 전송하도록..
                        //Volley+는 AndroidStudio에서 검색이 안됨 [google 검색 이용]

                        //파일 전송 요청 객체 생성[결과를 String으로 받음]
                        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonResponse = null;
                                try {
                                    jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                                        dialog = builder.setMessage("회원가입이 완료되어습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Signup_information_input.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).create();
                                        dialog.show();

                                        validate = true; //검증 완료

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                                        dialog = builder.setMessage("가입실패").setNegativeButton("확인", null).create();
                                        dialog.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Signup_information_input.this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //요청 객체에 보낼 데이터를 추가
                        smpr.setShouldCache(false);
                        smpr.addStringParam("email", ID);
                        Log.e("가입 아이디:", "" + ID);
                        smpr.addStringParam("password", Password);
                        smpr.addStringParam("birthday", birth);
                        smpr.addStringParam("name", input_name);
                        smpr.addStringParam("gender", sign_Gender);
                        if (intent_kakaoid != null) {
                            Log.e("카카오톡 아이디", "" + intent_kakaoid);
                            smpr.addStringParam("kakaoid", intent_kakaoid);
                        }
                        //이미지 파일 추가

                        smpr.addFile("img", img);

                        //요청객체를 서버로 보낼 우체통 같은 객체 생성

                        RequestQueue requestQueue = Volley.newRequestQueue(Signup_information_input.this);
                        requestQueue.add(smpr);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
                    dialog = builder.setMessage("이메일 인증을 받아주세요").setNegativeButton("확인", null).create();
                    dialog.show();
                }
            }


        });
    }


    private void sendMail() {


        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email.getText().toString(), "[부없만] 이메일 인증코드", "어플에서 코드를 입력하고 인증 버튼을 눌려주세요.\n" + "인증코드 : " + EmailCode);

        javaMailAPI.execute();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    if (uri != null) {


                        Glide.with(Signup_information_input.this)
                                .load(uri)

                                .into(iv_image);

                        //갤러리앱에서 관리하는 DB정보가 있는데, 그것이 나온다 [실제 파일 경로가 아님!!]
                        //얻어온 Uri는 Gallery앱의 DB번호임. (content://-----/2854)
                        //업로드를 하려면 이미지의 절대경로(실제 경로: file:// -------/aaa.png 이런식)가 필요함
                        //Uri -->절대경로(String)로 변환
                        imgPath = getRealPathFromUri(uri);   //임의로 만든 메소드 (절대경로를 가져오는 메소드)

                        //이미지 경로 uri 확인해보기
                        new AlertDialog.Builder(this).setMessage(uri.toString() + "\n" + imgPath).create().show();
                    }
                } else {
                    Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 335:
                if (resultCode == RESULT_OK) {
                    String imglog = data.getExtras().getString("이미지");
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try {
                                // 걍 외우는게 좋다 -_-;

                                img = "storage/emulated/0/camtest/" + imglog;

                                bm = BitmapFactory.decodeFile(img);
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {  // 화면에 그려줄 작업

                                        iv_image.setImageBitmap(bm);
                                    }
                                });
                                iv_image.setImageBitmap(bm); //비트맵 객체로 보여주기

                            } catch (Exception e) {

                            }

                        }
                    });

                    t.start();

                }
                break;
            /** 초기 이미지 받아오는 리절트 코드 확인절차 2020-11-25 새로운코드 작성으로 주석처리**/
//                if(resultCode==RESULT_OK){
//
//                    //선택한 사진의 경로(Uri)객체 얻어오기
//                    Uri uri= data.getData();
//                    if(uri!=null){
//                        Glide.with(Signup_information_input.this)
//                        .load(uri)
//                        .circleCrop()
//                        .into(iv_image);
//                    }
//
//                }else
//                {
//                    Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }

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


    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


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

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }

    }
}