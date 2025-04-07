package com.example.mana;

import static com.example.mana.R.color.appThemeColor;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MyIdealTypeSetting extends AppCompatActivity {
    Spinner spinnerIdealJabGroup, spinnerIdealHeight;
    RadioGroup radioGroupIdealHeight;
    RadioButton radioButtonIdealHeightMore, radioButtonIdealHeightLess;
    CheckBox checkBoxIdealJabGroupNo, checkBoxIdealHeightNo, checkBoxIdealYoungerAge, checkBoxOrderAge, checkBoxSameAge;
    String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ideal_type_setting);

        spinnerIdealJabGroup = findViewById(R.id.spinnerIdealJabGroup);
        spinnerIdealHeight = findViewById(R.id.spinnerIdealHeight);
        radioGroupIdealHeight = findViewById(R.id.radioGroupHeight);
        radioButtonIdealHeightMore = findViewById(R.id.radioBtHeightMore);
        radioButtonIdealHeightLess = findViewById(R.id.radioBtHeightLess);
        checkBoxIdealJabGroupNo = findViewById(R.id.cbIdealJabGroupNo);
        checkBoxIdealHeightNo = findViewById(R.id.cbIdealHeightNo);
        checkBoxIdealYoungerAge = findViewById(R.id.cbIdealYoungerAge);
        checkBoxOrderAge = findViewById(R.id.cbIdealOrderAge);
        checkBoxSameAge = findViewById(R.id.cbIdealSameAge);
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarIdealSettingPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("이상형 설정");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        /**스피너 **/
        ArrayList<String> heightArrayList = new ArrayList<>();
        for (int i = 100; i < 240; i++) {
            heightArrayList.add("" + i);
        }
        ArrayAdapter<String> heightArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, heightArrayList);
        spinnerIdealHeight.setAdapter(heightArrayAdapter);
        spinnerIdealHeight.setSelection((heightArrayList.size() / 3) + 4);

        ArrayAdapter<CharSequence> jabGroupAdapter = ArrayAdapter.createFromResource(this, R.array.jabs, android.R.layout.simple_spinner_dropdown_item);
        spinnerIdealJabGroup.setAdapter(jabGroupAdapter);


        checkBoxIdealJabGroupNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerIdealJabGroup.setEnabled(false);
                } else {
                    spinnerIdealJabGroup.setEnabled(true);
                }
            }
        });

        radioGroupIdealHeight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtHeightMore) {


                } else if (checkedId == R.id.radioBtHeightLess) {


                }
            }
        });
        radioButtonIdealHeightMore.setChecked(true);

        checkBoxIdealHeightNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerIdealHeight.setEnabled(false);
                    radioButtonIdealHeightMore.setEnabled(false);
                    radioButtonIdealHeightLess.setEnabled(false);
                } else {
                    spinnerIdealHeight.setEnabled(true);
                    radioButtonIdealHeightMore.setEnabled(true);
                    radioButtonIdealHeightLess.setEnabled(true);
                }
            }
        });
        checkBoxIdealYoungerAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && checkBoxSameAge.isChecked()) {
                    checkBoxOrderAge.setChecked(false);
                } else if (isChecked && checkBoxOrderAge.isChecked()) {
                    checkBoxSameAge.setChecked(false);
                }
            }
        });
        checkBoxSameAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && checkBoxOrderAge.isChecked()) {
                    checkBoxIdealYoungerAge.setChecked(false);
                } else if (isChecked && checkBoxIdealYoungerAge.isChecked()) {
                    checkBoxOrderAge.setChecked(false);
                }
            }
        });
        checkBoxOrderAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && checkBoxIdealYoungerAge.isChecked()) {
                    checkBoxSameAge.setChecked(false);
                } else if (isChecked && checkBoxSameAge.isChecked()) {
                    checkBoxIdealYoungerAge.setChecked(false);
                }
            }
        });
        MyldealLoad();
    }

    //    RadioGroup.OnCheckedChangeListener radioGroupCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            if (checkedId == R.id.radioBtHeightMore) {
//                Toast.makeText(MyIdealTypeSetting.this, "라디오 그룹 이상 눌렸습니다.", Toast.LENGTH_SHORT).show();
//
//
//            } else if (checkedId == R.id.radioBtHeightLess) {
//                Toast.makeText(MyIdealTypeSetting.this, "라디오 그룹 이하 눌렸습니다.", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    };
    public String AgeCheck() {
        if (checkBoxIdealYoungerAge.isChecked() && checkBoxSameAge.isChecked()) {
            return ">=";
        } else if (checkBoxIdealYoungerAge.isChecked() && checkBoxOrderAge.isChecked()) {
            return "<>";
        } else if (checkBoxSameAge.isChecked() && checkBoxOrderAge.isChecked()) {
            return "<=";
        } else if (checkBoxIdealYoungerAge.isChecked()) {
            return ">";
        } else if (checkBoxSameAge.isChecked()) {
            return "=";
        } else if (checkBoxOrderAge.isChecked()) {
            return "<";
        } else {
            return "X";
        }

    }

    public void MyldealLoad() {
        String url = new ServerIP().http+"Android/MyIdealLoad.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject1 = new JSONObject(response);
//                    boolean success = jsonObject1.getBoolean("success");
//                    if (success) {
//                        Log.e("dsadad",jsonObject1.getString("jobgroup"));
//                        if (jsonObject1.getString("jobgroup").equals("null")) {
//                        } else if (jsonObject1.getString("jobgroup").equals("상관없음")) {
//                            checkBoxIdealJabGroupNo.setChecked(true);
//                        } else {
//                            String[] jabgroupsplit = jsonObject1.getString("jobgroup").split(",");
//                            spinnerIdealJabGroup.setSelection(Integer.parseInt(jabgroupsplit[0]));
//                        }
//                        if (jsonObject1.getString("spinnerheight").equals("255")) {
//                            checkBoxIdealHeightNo.setChecked(true);
//
//                        } else {
//                            spinnerIdealHeight.setSelection(Integer.parseInt(jsonObject1.getString("spinnerheight")));
//                        }
//                        if(jsonObject1.getString("heightcompare").equals(">=")){
//radioButtonIdealHeightMore.setChecked(true);
//                        }else {
//                            radioButtonIdealHeightLess.setChecked(true);
//                        }
//                        if (jsonObject1.getString("idealage").equals("X")) {
//
//                        } else if (jsonObject1.getString("idealage").equals(">=")) {
//                            checkBoxIdealYoungerAge.setChecked(true);
//                            checkBoxSameAge.setChecked(true);
//                        } else if (jsonObject1.getString("idealage").equals("<>")) {
//                            checkBoxIdealYoungerAge.setChecked(true);
//                            checkBoxOrderAge.setChecked(true);
//                        } else if (jsonObject1.getString("idealage").equals("<=")) {
//                            checkBoxOrderAge.setChecked(true);
//                            checkBoxSameAge.setChecked(true);
//                        } else if (jsonObject1.getString("idealage").equals(">")) {
//                            checkBoxIdealYoungerAge.setChecked(true);
//                        } else if (jsonObject1.getString("idealage").equals("=")) {
//                            checkBoxSameAge.setChecked(true);
//                        } else if (jsonObject1.getString("idealage").equals("<")) {
//                            checkBoxOrderAge.setChecked(true);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        simpleMultiPartRequest.addStringParam("id", loginId);
//        RequestQueue requestQueue = Volley.newRequestQueue(MyIdealTypeSetting.this);
//        requestQueue.add(simpleMultiPartRequest);
    }

    public void MyIdealUpdate() {
        String url = new ServerIP().http+"Android/idealprofile.php";
//        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject1 = new JSONObject(response);
//                    boolean success = jsonObject1.getBoolean("success");
//                    if (success) {
//                        Log.e("저장", "이상형 저장됨");
//                        Log.e("저장", response);
//                    } else {
//                        Log.e("저장", "이상형 저장실패");
//                        Log.e("저장", response);
//
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
//        if (checkBoxIdealJabGroupNo.isChecked()) {
//            simpleMultiPartRequest.addStringParam("jabgroup", "상관없음");
//            Log.e("로그", "NULL1");
//        } else {
//            String value = spinnerIdealJabGroup.getSelectedItemPosition() + "," + spinnerIdealJabGroup.getSelectedItem().toString();
//            simpleMultiPartRequest.addStringParam("jabgroup", value);
//            Log.e("로그", value);
//        }
//        if (checkBoxIdealHeightNo.isChecked()) {
//            simpleMultiPartRequest.addStringParam("height", ">#255,255");
//            Log.e("로그", "NULL2");
//        } else if (radioButtonIdealHeightMore.isChecked()) {
//            String value = ">=#" + spinnerIdealHeight.getSelectedItemPosition() + "," + spinnerIdealHeight.getSelectedItem().toString();
//            simpleMultiPartRequest.addStringParam("height", value);
//            Log.e("로그", value);
//        } else {
//            String value = "<=#" + spinnerIdealHeight.getSelectedItemPosition() + "," + spinnerIdealHeight.getSelectedItem().toString();
//            simpleMultiPartRequest.addStringParam("height", value);
//            Log.e("로그", value);
//        }
//        simpleMultiPartRequest.addStringParam("idealage", AgeCheck());
//        Log.e("로그", AgeCheck());
//        simpleMultiPartRequest.addStringParam("id", loginId);
//
//        simpleMultiPartRequest.setShouldCache(false);
//        RequestQueue requestQueue = Volley.newRequestQueue(MyIdealTypeSetting.this);
//        requestQueue.add(simpleMultiPartRequest);

    }

    @Override
    protected void onStop() {
        MyIdealUpdate();
        super.onStop();
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