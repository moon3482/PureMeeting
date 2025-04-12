package com.example.mana;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.mana.feature.camera.PureMeetCamera;
import com.example.mana.feature.common.Constants;
import com.example.mana.logging.Tag;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import timber.log.Timber;

public class SignupInformationInput extends AppCompatActivity {
    private ImageView profileImage;
    private TextView birthday;
    private Button submit;
    private Uri currentPhotoUri;
    private Spinner gender;

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Timber.tag(Tag.SIGNUP_INFO_INPUT).d("Result Ok");
                    Intent data = result.getData();
                    if (data == null) {
                        Timber.tag(Tag.SIGNUP_INFO_INPUT).e("Can Not Load Photo Data");
                        return;
                    }

                    String photoUriString = data.getStringExtra(Constants.BundleKey.MANA_PHOTO_URI);
                    String photoPath = data.getStringExtra(Constants.BundleKey.MANA_PHOTO_PATH);

                    Timber.tag(Tag.SIGNUP_INFO_INPUT).d("PhotoUri : %s", photoUriString);
                    Timber.tag(Tag.SIGNUP_INFO_INPUT).d("PhotoPath : %s", photoPath);

                    if (photoUriString == null) {
                        Timber.tag(Tag.SIGNUP_INFO_INPUT).e("Can Not Load Photo Uri");
                        return;
                    }

                    currentPhotoUri = Uri.parse(photoUriString);
                    Glide.with(this)
                            .load(currentPhotoUri)
                            .optionalCircleCrop()
                            .into(profileImage);

                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Timber.tag(Tag.SIGNUP_INFO_INPUT).d("Result Canceled");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_infomation_input);
        initToolbar();
        initProfileImage();
        initBirthdate();
        initGenderSpinner();
        initSummit();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.signup_information_ToolBar);
        toolbar.setTitle(getString(R.string.enter_member_info));
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);
    }

    private void initProfileImage() {
        profileImage = findViewById(R.id.profileImage);
        Glide.with(this)
                .load(R.drawable.basicprofile)
                .optionalCircleCrop()
                .into(profileImage);
        profileImage.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SignupInformationInput.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            Intent takePhotoIntent = new Intent(SignupInformationInput.this, PureMeetCamera.class);
            takePictureLauncher.launch(takePhotoIntent);
        });
    }

    private void initBirthdate() {
        birthday = findViewById(R.id.birthdate);
        birthday.setOnClickListener(view -> {
            MaterialDatePicker<Long> birthdatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.choose_birthdate))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            birthdatePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance(Locale.KOREA);
                calendar.setTimeInMillis(selection);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                birthday.setText(year + "-" + month + "-" + day);
            });
            birthdatePicker.show(getSupportFragmentManager(), "BIRTHDATE_PICKER");
        });
    }

    private void initGenderSpinner() {
        gender = findViewById(R.id.gender);
    }

    private void initSummit() {
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            String invalidate = validationCheck();
            if (!invalidate.isEmpty()) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage(invalidate);
                alertDialog.setPositiveButton(getString(R.string.confirm), null);
                alertDialog.show();
            } else {
                //안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl = new ServerIP().http + "Android/insertDB.php";

                //Volley plus Library를 이용해서
                //파일 전송하도록..
                //Volley+는 AndroidStudio에서 검색이 안됨 [google 검색 이용]

                //파일 전송 요청 객체 생성[결과를 String으로 받음]
//                        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                JSONObject jsonResponse = null;
//                                try {
//                                    jsonResponse = new JSONObject(response);
//                                    boolean success = jsonResponse.getBoolean("success");
//                                    if (success) {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
//                                        dialog = builder.setMessage("회원가입이 완료되어습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent intent = new Intent(Signup_information_input.this, MainActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        }).create();
//                                        dialog.show();
//
//                                        validate = true; //검증 완료
//
//                                    } else {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_information_input.this);
//                                        dialog = builder.setMessage("가입실패").setNegativeButton("확인", null).create();
//                                        dialog.show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(Signup_information_input.this, "ERROR", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        //요청 객체에 보낼 데이터를 추가
//                        smpr.setShouldCache(false);
//                        smpr.addStringParam("email", ID);
//                        Log.e("가입 아이디:", "" + ID);
//                        smpr.addStringParam("password", Password);
//                        smpr.addStringParam("birthday", birth);
//                        smpr.addStringParam("name", input_name);
//                        smpr.addStringParam("gender", sign_Gender);
//                        if (intent_kakaoid != null) {
//                            Log.e("카카오톡 아이디", "" + intent_kakaoid);
//                            smpr.addStringParam("kakaoid", intent_kakaoid);
//                        }
//                        //이미지 파일 추가
//
//                        smpr.addFile("img", img);
//
//                        //요청객체를 서버로 보낼 우체통 같은 객체 생성
//
//                        RequestQueue requestQueue = Volley.newRequestQueue(Signup_information_input.this);
//                        requestQueue.add(smpr);
            }
        });
    }

    private String validationCheck() {
        StringBuilder invalidateBuilder = new StringBuilder();
        ArrayList<String> invalidates = new ArrayList<>();
        String birth = birthday.getText().toString();

        if (birth.isEmpty()) {
            invalidates.add(getString(R.string.birthdate));
        }
        if (gender.getSelectedItem() == null || gender.getSelectedItemPosition() == 0) {
            invalidates.add(getString(R.string.gender));
        }
        if (!invalidates.isEmpty()) {
            String invalidatesToString = String.join(",", invalidates);
            invalidateBuilder.append(invalidatesToString);
            invalidateBuilder.append(" ");
            invalidateBuilder.append(getString(R.string.check_please));
        }
        Timber.tag(Tag.SIGNUP_INFO_INPUT).d(invalidateBuilder.toString());
        return invalidateBuilder.toString();
    }
}
