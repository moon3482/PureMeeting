package com.example.mana;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.MainPage.mainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsertMyinfoDetail extends AppCompatActivity {
    Spinner spinnerHeight, spinnerWeight, spinnerJabGroup, spinnerDrinkDay, spinnerDrinkHit, spinnerDrinkBottle, spinnerDrinkKind, spinnerArea;
    EditText etJab, etHobby1, etHobby2, etforte1, etforte2, etfavorite1, etfavorite2, etdislike1, etdislike2;
    CheckBox cbDrink;
    Button btnInsert;
    String str, spinnerGetStrHeight, spinnerGetStrWeight, spinnerGetStrJabGroup, spinnerGetStrArea, spinnerGetStrDrinkDay, spinnerGetStrDrinkHit, spinnerGetStrDrinkKind, spinnerGetStrDrinkBottle;
    SharedPreferences sharedPreferences;
    SimpleMultiPartRequest simpleMultiPartRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_myinfo_detail);
        spinnerHeight = findViewById(R.id.spinner_InsertMyInfoDetail_height);
        spinnerWeight = findViewById(R.id.spinner_InsertMyInfoDetail_Weight);
        spinnerJabGroup = findViewById(R.id.spinner_InsertMyInfoDetail_JabGroup);
        etJab = findViewById(R.id.etInsertMyInfoDetail_Jab);
        etHobby1 = findViewById(R.id.etInsertMyInfoDetail_hobby1);
        etHobby2 = findViewById(R.id.etInsertMyInfoDetail_hobby2);
        etforte1 = findViewById(R.id.etInsertMyInfoDetail_forte1);
        etforte2 = findViewById(R.id.etInsertMyInfoDetail_forte2);
        etfavorite1 = findViewById(R.id.etInsertMyInfoDetail_favorite1);
        etfavorite2 = findViewById(R.id.etInsertMyInfoDetail_favorite2);
        etdislike1 = findViewById(R.id.etInsertMyInfoDetail_dislike1);
        etdislike2 = findViewById(R.id.etInsertMyInfoDetail_dislike2);
        spinnerDrinkDay = findViewById(R.id.spinner_InsertMyInfoDetail_Drink_day);
        spinnerDrinkHit = findViewById(R.id.spinner_InsertMyInfoDetail_Drink_Hit);
        spinnerDrinkKind = findViewById(R.id.spinner_InsertMyInfoDetail_Drink_kind);
        spinnerDrinkBottle = findViewById(R.id._spinner_InsertMyInfoDetail_Drink_Bottle);
        cbDrink = findViewById(R.id.cb_InsertMyInfoDetail_Drink);
        btnInsert = findViewById(R.id.btn_InsertMyInfoDetail);
        spinnerArea = findViewById(R.id.spinner_InsertMyInfoDetail_Area);
        sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String id = sharedPreferences.getString("loginId", "");
        String url = new ServerIP().http+"Android/InsertMyProfileDetail.php";
        simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {

                                Toast.makeText(InsertMyinfoDetail.this, id + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InsertMyinfoDetail.this, mainPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);



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

        ArrayList<String> heightArrayList = new ArrayList<>();
        for (int i = 100; i < 240; i++) {
            heightArrayList.add("" + i + "cm");
        }
        ArrayAdapter<String> heightArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, heightArrayList);
        spinnerHeight.setAdapter(heightArrayAdapter);
        spinnerHeight.setSelection((heightArrayList.size() / 3) + 4);
        spinnerHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrHeight = position + "," + spinnerHeight.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> weightArrayList = new ArrayList<>();
        for (int j = 30; j < 200; j++) {
            weightArrayList.add("" + j + "kg");
        }
        ArrayAdapter<String> weightArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, weightArrayList);
        spinnerWeight.setAdapter(weightArrayAdapter);
        spinnerWeight.setSelection((weightArrayList.size() / 3) - 16);
        spinnerWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrWeight = position + "," + spinnerWeight.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> jabGroupAdapter = ArrayAdapter.createFromResource(this, R.array.jabs, android.R.layout.simple_spinner_dropdown_item);
        spinnerJabGroup.setAdapter(jabGroupAdapter);
        spinnerJabGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrJabGroup = position + "," + spinnerJabGroup.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> AreaArrayAdapter = ArrayAdapter.createFromResource(this, R.array.area, android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(AreaArrayAdapter);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrArea = position + "," + spinnerArea.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> DrinkDayArrayAdapter = ArrayAdapter.createFromResource(this, R.array.drink_day, R.layout.spinner_gravity);
        DrinkDayArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrinkDay.setAdapter(DrinkDayArrayAdapter);
        spinnerDrinkDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrDrinkDay = position + "," + spinnerDrinkDay.getSelectedItem().toString();
                if(spinnerDrinkDay.getSelectedItem().toString().equals("매일")){
                    spinnerDrinkHit.setVisibility(View.GONE);
                }else {
                    spinnerDrinkHit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> DrinkHitArrayAdapter = ArrayAdapter.createFromResource(this, R.array.drink_num, R.layout.spinner_gravity);
        DrinkHitArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrinkHit.setAdapter(DrinkHitArrayAdapter);
        spinnerDrinkHit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrDrinkHit = position + "," + spinnerDrinkHit.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> DrinkKindArrayAdapter = ArrayAdapter.createFromResource(this, R.array.drink_kind, R.layout.spinner_gravity);
        DrinkKindArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrinkKind.setAdapter(DrinkKindArrayAdapter);
        spinnerDrinkKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrDrinkKind = position + "," + spinnerDrinkKind.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> DrinkBottleArrayAdapter = ArrayAdapter.createFromResource(this, R.array.drink_bottle, R.layout.spinner_gravity);
        DrinkBottleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrinkBottle.setAdapter(DrinkBottleArrayAdapter);
        spinnerDrinkBottle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerGetStrDrinkBottle = position + "," + spinnerDrinkBottle.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cbDrink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerEnable(isChecked);
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insetValidation()) {

                            InsertRequest();


                }
            }
        });

    }

    public void spinnerEnable(Boolean check) {
        if (check) {
            spinnerDrinkDay.setEnabled(false);
            spinnerDrinkDay.setAlpha(0.5f);
            spinnerDrinkHit.setEnabled(false);
            spinnerDrinkHit.setAlpha(0.5f);
            spinnerDrinkKind.setEnabled(false);
            spinnerDrinkKind.setAlpha(0.5f);
            spinnerDrinkBottle.setEnabled(false);
            spinnerDrinkBottle.setAlpha(0.5f);
        } else {
            spinnerDrinkDay.setEnabled(true);
            spinnerDrinkDay.setAlpha(1);
            spinnerDrinkHit.setEnabled(true);
            spinnerDrinkHit.setAlpha(1);
            spinnerDrinkKind.setEnabled(true);
            spinnerDrinkKind.setAlpha(1);
            spinnerDrinkBottle.setEnabled(true);
            spinnerDrinkBottle.setAlpha(1);
        }
    }


    public void InsertRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(simpleMultiPartRequest);


    }

    public boolean insetValidation() {
        AlertDialog dialog;
        String jab = etJab.getText().toString().replaceAll(" ", "");
        String hobby1 = etHobby1.getText().toString().replaceAll(" ", "");
        String hobby2 = etHobby2.getText().toString().replaceAll(" ", "");
        String forte1 = etforte1.getText().toString().replaceAll(" ", "");
        String forte2 = etforte2.getText().toString().replaceAll(" ", "");
        String favorite1 = etfavorite1.getText().toString().replaceAll(" ", "");
        String favorite2 = etfavorite2.getText().toString().replaceAll(" ", "");
        String dislike1 = etdislike1.getText().toString().replaceAll(" ", "");
        String dislike2 = etdislike2.getText().toString().replaceAll(" ", "");
        String drink = drinkSpinner();
        sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String ID = sharedPreferences.getString("loginId", "");
        if (spinnerJabGroup.getSelectedItem().toString().equals("직업군")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("직업군을 선택해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
            return false;
        }
        if (spinnerArea.getSelectedItem().toString().equals("지역")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("지역을 선택해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
            return false;
        }
        if (jab.length() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("직업을 입력해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
            return false;

        } else {
            simpleMultiPartRequest.addStringParam("jab", etJab.getText().toString().trim());


        }
        if (hobby1.length() == 0 || hobby2.length() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("취미를 입력해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
            return false;

        } else {
            simpleMultiPartRequest.addStringParam("hobby1", etHobby1.getText().toString().trim());
            simpleMultiPartRequest.addStringParam("hobby2", etHobby2.getText().toString().trim());

        }


        if (forte1.length() == 0 || forte2.length() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("특기를 입력해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();

            return false;
        } else {
            simpleMultiPartRequest.addStringParam("forte1", etforte1.getText().toString().trim());
            simpleMultiPartRequest.addStringParam("forte2", etforte2.getText().toString().trim());

        }

        if (favorite1.length() == 0 || favorite2.length() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("좋아하는 것을 입력해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();

            return false;
        } else {
            simpleMultiPartRequest.addStringParam("favorite1", etfavorite1.getText().toString().trim());
            simpleMultiPartRequest.addStringParam("favorite2", etfavorite2.getText().toString().trim());
        }

        if (dislike1.length() == 0 || dislike2.length() == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setMessage("싫어하는 것을 입력해주세요")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();

            return false;
        } else {
            simpleMultiPartRequest.addStringParam("dislike1", etdislike1.getText().toString().trim());
            simpleMultiPartRequest.addStringParam("dislike2", etdislike2.getText().toString().trim());
        }

        simpleMultiPartRequest.addStringParam("height", spinnerGetStrHeight);
        simpleMultiPartRequest.addStringParam("weight", spinnerGetStrWeight);
        simpleMultiPartRequest.addStringParam("jabGroup", spinnerGetStrJabGroup);
        simpleMultiPartRequest.addStringParam("jab", jab);
        simpleMultiPartRequest.addStringParam("area", spinnerGetStrArea);
        simpleMultiPartRequest.addStringParam("drink", drink);
        simpleMultiPartRequest.addStringParam("id", ID);


        return true;
    }

    public String drinkSpinner() {


        String drinkStyle = null;
        if (cbDrink.isChecked()) {
            drinkStyle = "none@";
        } else {
            if (spinnerDrinkDay.getSelectedItem().toString().equals("매일")) {
                drinkStyle = spinnerGetStrDrinkDay + "@" + spinnerGetStrDrinkKind + "@" + spinnerGetStrDrinkBottle;
            } else {
                drinkStyle = spinnerGetStrDrinkDay + "@" + spinnerGetStrDrinkHit + "@" + spinnerGetStrDrinkKind + "@" + spinnerGetStrDrinkBottle;
            }

        }
        return drinkStyle;
    }
}