package com.example.mana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mana.MainPage.IdealProfileLoad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mana.R.color.appThemeColor;

public class MyProfileLoad extends AppCompatActivity {
    CircleImageView profileImage1, profileImage2, profileImage3, profileImage4, profileImage5;
    int PROFILEIMAGE2 = 201, PROFILEIMAGE3 = 301, PROFILEIMAGE4 = 401, PROFILEIMAGE5 = 501;
    String loginId, spinnerGetStrHeight, spinnerGetStrWeight, spinnerGetStrArea, spinnerGetStrJabGroup, spinnerGetStrDrinkDay, spinnerGetStrDrinkHit, spinnerGetStrDrinkKind, spinnerGetStrDrinkBottle;
    String getDrinkDya, getDrinkHit, getDrinkKind, getDrinkBottle;
    TextView tvTitleHobby, tvTitleForte, tvTitleFavorite, tvTitledislike, tvSecession;
    TextView tvAge, tvHobby, tvForte, tvFavorite, tvdislike, tvDrink;
    Spinner spinnerHeight, spinnerWeight, spinnerArea, spinnerJabGroup;
    EditText etjab;
    boolean check = true;
    HashMap<String, String> updateHashData = new HashMap<>();
    public static Context myinfocontext;

    public String getSpinnerGetStrDrinkDay() {
        return spinnerGetStrDrinkDay;
    }

    public String getSpinnerGetStrDrinkHit() {
        return spinnerGetStrDrinkHit;
    }

    public String getSpinnerGetStrDrinkKind() {
        return spinnerGetStrDrinkKind;
    }

    public String getSpinnerGetStrDrinkBottle() {
        return spinnerGetStrDrinkBottle;
    }

    public void setSpinnerGetStrDrinkDay(String spinnerGetStrDrinkDay) {
        this.spinnerGetStrDrinkDay = spinnerGetStrDrinkDay;
    }

    public void setSpinnerGetStrDrinkHit(String spinnerGetStrDrinkHit) {
        this.spinnerGetStrDrinkHit = spinnerGetStrDrinkHit;
    }

    public void setSpinnerGetStrDrinkKind(String spinnerGetStrDrinkKind) {
        this.spinnerGetStrDrinkKind = spinnerGetStrDrinkKind;
    }

    public void setSpinnerGetStrDrinkBottle(String spinnerGetStrDrinkBottle) {
        this.spinnerGetStrDrinkBottle = spinnerGetStrDrinkBottle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_infomation);
        profileImage1 = findViewById(R.id.profileImage1);
        profileImage2 = findViewById(R.id.profileImage2);
        profileImage3 = findViewById(R.id.profileImage3);
        profileImage4 = findViewById(R.id.profileImage4);
        profileImage5 = findViewById(R.id.profileImage5);
        tvTitleHobby = findViewById(R.id.tvMyInfoHobby);
        tvTitleForte = findViewById(R.id.tvMyInfoForte);
        tvTitleFavorite = findViewById(R.id.tvMyInfoFavorite);
        tvTitledislike = findViewById(R.id.tvMyInfodislike);
        tvAge = findViewById(R.id.tvMyinfoPageAge);
        tvHobby = findViewById(R.id.tvMyInfoPageHobby);
        tvForte = findViewById(R.id.tvMyInfoPageForte);
        tvFavorite = findViewById(R.id.tvMyInfoPageFavorite);
        tvdislike = findViewById(R.id.tvMyInfoPagedislike);
        tvDrink = findViewById(R.id.tvMyInfoPageDrink);
        spinnerHeight = findViewById(R.id.spinnerMyinfoPageHeght);
        spinnerWeight = findViewById(R.id.spinnerMyinfoPageWeight);
        spinnerArea = findViewById(R.id.spinnerMyinfoPageArea);
        spinnerJabGroup = findViewById(R.id.spinnerMyinfoPageJabGroup);
        etjab = findViewById(R.id.etMyInfoPageJab);
        tvSecession = findViewById(R.id.tvsecession);
        myinfocontext = this;

        /**툴바**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMyInformation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내 정보");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        /**스피너**/
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
                updateHashData.put("height", position + "," + spinnerHeight.getSelectedItem().toString());
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
                updateHashData.put("weight", position + "," + spinnerWeight.getSelectedItem().toString());
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
                updateHashData.put("area", position + "," + spinnerArea.getSelectedItem().toString());
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
                updateHashData.put("jabgroup", position + "," + spinnerJabGroup.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**로그인 아이디 사진 정보**/
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        String mainImageStr = sharedPreferences.getString("loginbit", "");
        loginId = sharedPreferences.getString("loginId", "");
        Bitmap mainImageBit = StringToBitmap(mainImageStr);
        profileImage1.setImageBitmap(mainImageBit);
        /**프로필 로딩**/


        profileImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PROFILEIMAGE2);
            }
        });

        profileImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PROFILEIMAGE3);
            }
        });

        profileImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PROFILEIMAGE4);
            }
        });

        profileImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PROFILEIMAGE5);
            }
        });
        tvHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customdialog1 customdialog1 = new Customdialog1(MyProfileLoad.this);
                customdialog1.CallCustomDialog(tvTitleHobby.getText().toString(), tvHobby.getText().toString(), tvHobby);
            }
        });
        tvForte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customdialog1 customdialog1 = new Customdialog1(MyProfileLoad.this);
                customdialog1.CallCustomDialog(tvTitleForte.getText().toString(), tvForte.getText().toString(), tvForte);
            }
        });
        tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customdialog1 customdialog1 = new Customdialog1(MyProfileLoad.this);
                customdialog1.CallCustomDialog(tvTitleFavorite.getText().toString(), tvFavorite.getText().toString(), tvFavorite);
            }
        });
        tvdislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customdialog1 customdialog1 = new Customdialog1(MyProfileLoad.this);
                customdialog1.CallCustomDialog(tvTitledislike.getText().toString(), tvdislike.getText().toString(), tvdislike);
            }
        });
        tvDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customdialog2 customdialog2 = new Customdialog2(MyProfileLoad.this, spinnerGetStrDrinkDay, spinnerGetStrDrinkHit, spinnerGetStrDrinkKind, spinnerGetStrDrinkBottle);

                customdialog2.CallDrinkDialog(tvDrink);

            }
        });
        tvSecession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MyProfileLoad.this);
                TextView textView = new TextView(MyProfileLoad.this);
                EditText editText = new EditText(MyProfileLoad.this);
                LinearLayout linearLayout = new LinearLayout(MyProfileLoad.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                textView.setText("동의하고 탈퇴");
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setTextColor(Color.parseColor("#FF0000"));
                textView.setTypeface(null, Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 0, 15, 0);
                editText.setLayoutParams(params);
                editText.setHint("동의하고 탈퇴");
                editText.setBackgroundTintList(getResources().getColorStateList(appThemeColor));
                editText.setGravity(Gravity.CENTER);
                linearLayout.addView(textView);
                linearLayout.addView(editText);
                dialog = builder1.setTitle("계정 탈퇴")
                        .setMessage("정말 탈퇴를 하시겠습니까?\n현재 계정에 있는 크레딧은 모두 소멸 됩니다.\n 그래도 탈퇴를 원하시면 아래의 글자를 따라 써주십시오.")
                        .setView(linearLayout)
                        .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (textView.getText().toString().equals(editText.getText().toString())) {
                                    userSecession();
                                } else {
                                    AlertDialog dialog1;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileLoad.this);
                                    dialog1 = builder.setMessage("글자가 일치하지 않습니다.").setPositiveButton("확인", null).create();
                                    dialog1.show();
                                }
                            }
                        }).setNeutralButton("취소", null).create();

                dialog.show();

            }
        });

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 201:
                if (resultCode == RESULT_OK) {
                    upimage(data, profileImage2, "profileImage2");
                }
                break;

            case 301:
                if (resultCode == RESULT_OK) {
                    upimage(data, profileImage3, "profileImage3");
                }
                break;

            case 401:
                if (resultCode == RESULT_OK) {
                    upimage(data, profileImage4, "profileImage4");
                }
                break;

            case 501:
                if (resultCode == RESULT_OK) {
                    upimage(data, profileImage5, "profileImage5");
                }
                break;
        }

    }

    public void upimage(Intent data, CircleImageView view, String imageNumber) {
        String url = new ServerIP().http+"Android/myProFileImageUpdate.php";
        Uri uri = data.getData();
//        String path = getRealPathFromUri(uri);
        String path;
        int orientarionInt = exifOrientation(data);
        Log.e("아아아", String.valueOf(filesize(uri)));
        Log.e("회전", String.valueOf(exifOrientation(data)));

        if (filesize(uri) > 5242880) {
            path = reSizeBase64(data);
        } else {
            path = imagebase64(data);
        }
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    boolean success = jsonObject1.getBoolean("success");
                    if (success) {
                        Log.e("로그", jsonObject1.getString("imagepath"));
                        Log.e("exif", jsonObject1.getString("exif"));
//                        Bitmap bit = getBitmapFromURL();

                        String path = jsonObject1.getString("imagepath");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Glide.with(MyProfileLoad.this).load(path).into(view);
//                            }
//                        });
                        StartingImageLoad();

                    }
                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        simpleMultiPartRequest.setShouldCache(false);
        simpleMultiPartRequest.addStringParam("id", loginId);
        simpleMultiPartRequest.addStringParam("profileImage", imageNumber);
        simpleMultiPartRequest.addStringParam("image", path);
        simpleMultiPartRequest.addStringParam("rotate", String.valueOf(orientarionInt));
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfileLoad.this);
        requestQueue.add(simpleMultiPartRequest);
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
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

    public void StartingImageLoad() {
        String url = new ServerIP().http+"Android/myprofileload.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("마이페이지", response);
                try {
                    JSONObject jsonObject2 = new JSONObject(response);
                    spinnerHeight.setSelection(Integer.parseInt(jsonObject2.getString("height")));
                    spinnerWeight.setSelection(Integer.parseInt(jsonObject2.getString("weight")));
                    spinnerArea.setSelection(Integer.parseInt(jsonObject2.getString("area")));
                    spinnerJabGroup.setSelection(Integer.parseInt(jsonObject2.getString("jabGroup")));
                    tvAge.setText(String.valueOf(getAge(jsonObject2.getString("birthday"))));
                    etjab.setText(jsonObject2.getString("job"));
                    tvHobby.setText(jsonObject2.getString("hobby"));
                    tvForte.setText(jsonObject2.getString("forte"));
                    tvFavorite.setText(jsonObject2.getString("favorite"));
                    tvdislike.setText(jsonObject2.getString("dislike"));
                    String[] drinksplit = jsonObject2.getString("drink").split("@");
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (drinksplit.length > 1) {
                        for (int i = 0; i < drinksplit.length; i++) {
                            String[] str = drinksplit[i].split(",");
                            arrayList.add(str[1]);
                        }
                    }
                    if (drinksplit.length == 3) {
                        tvDrink.setText(new String(Character.toChars(0x1F4C6)) + arrayList.get(0) + " " + new String(Character.toChars(0x1F37A)) + arrayList.get(1) + " " + new String(Character.toChars(0x1F377)) + arrayList.get(2));
                        spinnerGetStrDrinkDay = drinksplit[0];
                        spinnerGetStrDrinkHit = "0,1~3회";
                        spinnerGetStrDrinkKind = drinksplit[1];
                        spinnerGetStrDrinkBottle = drinksplit[2];
                        check = false;
                    } else if (drinksplit.length == 4) {
                        tvDrink.setText(new String(Character.toChars(0x1F4C6)) + arrayList.get(0) + " " + new String(Character.toChars(0x23F3)) + arrayList.get(1) + " " + new String(Character.toChars(0x1F37A)) + arrayList.get(2) + " " + new String(Character.toChars(0x1F377)) + arrayList.get(3));
                        spinnerGetStrDrinkDay = drinksplit[0];
                        spinnerGetStrDrinkHit = drinksplit[1];
                        spinnerGetStrDrinkKind = drinksplit[2];
                        spinnerGetStrDrinkBottle = drinksplit[3];
                        check = false;
                    } else {
                        tvDrink.setText("음주 안함");

                        spinnerGetStrDrinkDay = "0,음주 안함";
                        spinnerGetStrDrinkHit = "0,음주 안함";
                        spinnerGetStrDrinkKind = "0,음주 안함";
                        spinnerGetStrDrinkBottle = "0,음주 안함";
                    }

                    if (!jsonObject2.getString("profileImage2").equals("null")) {
                        Glide.with(MyProfileLoad.this)
                                .load(jsonObject2.getString("profileImage2"))
                                .placeholder(R.drawable.ic_baseline_add_circle_24)
                                .error(R.drawable.ic_baseline_add_circle_24)
                                .into(profileImage2);
                    } else {
                    }
                    if (!jsonObject2.getString("profileImage3").equals("null")) {
                        Glide.with(MyProfileLoad.this).load(jsonObject2.getString("profileImage3")).error(R.drawable.ic_baseline_add_circle_24).into(profileImage3);
                    } else {
                    }
                    if (!jsonObject2.getString("profileImage4").equals("null")) {
                        Glide.with(MyProfileLoad.this).load(jsonObject2.getString("profileImage4")).error(R.drawable.ic_baseline_add_circle_24).into(profileImage4);
                    } else {
                    }
                    if (!jsonObject2.getString("profileImage5").equals("null")) {
                        Glide.with(MyProfileLoad.this).load(jsonObject2.getString("profileImage5")).error(R.drawable.ic_baseline_add_circle_24).into(profileImage5);
                    } else {
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
        simpleMultiPartRequest.addStringParam("id", loginId);
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfileLoad.this);
        requestQueue.add(simpleMultiPartRequest);
    }

    public CircleImageView Gliderolling(int a) {
        if (a == 0) {
            return profileImage2;
        } else if (a == 1) {
            return profileImage3;
        } else if (a == 2) {
            return profileImage4;
        } else if (a == 3) {
            return profileImage5;
        }

        return null;
    }

    public String reSizeBase64(Intent data) {
        Uri uri = data.getData();
        String path = getRealPathFromUri(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap imageSouce = BitmapFactory.decodeFile(path, options);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageSouce.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public String imagebase64(Intent data) {
        Uri uri = data.getData();
        String path = getRealPathFromUri(uri);
        Bitmap imageSouce = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageSouce.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public long filesize(Uri uri) {
        File file = new File(getRealPathFromUri(uri));
        long fileSizeByte = file.length();
        return fileSizeByte;
    }

    public int exifOrientation(Intent data) {
        ExifInterface exif = null;
        Uri uri = data.getData();
        String path = getRealPathFromUri(uri);
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            Log.e("로테이트", "실패");
            e.printStackTrace();
        }
        int exifint = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        return exifint;
    }

    public int getAge(String birthday) {
        String[] birthdaysplit = birthday.split("-");
        int birthYear = Integer.parseInt(birthdaysplit[0]);
        int birthMonth = Integer.parseInt(birthdaysplit[1]);
        int birthDay = Integer.parseInt(birthdaysplit[2]);
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

    @Override
    protected void onStop() {

        if (spinnerArea.getSelectedItem().toString().equals("지역")) {
            super.onStop();
        } else if (spinnerJabGroup.getSelectedItem().toString().equals("직업군")) {
            super.onStop();
        } else if (etjab.getText().toString().replaceAll(" ", "").length() == 0) {
            super.onStop();
        } else {

            String url = new ServerIP().http+"Android/updatemyInfo.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        System.out.println(jsonObject1.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String[] hospl = tvHobby.getText().toString().split(",");
                    String[] fospl = tvForte.getText().toString().split(",");
                    String[] forspl = tvFavorite.getText().toString().split(",");
                    String[] disspl = tvdislike.getText().toString().split(",");
                    updateHashData.put("id", loginId);
                    updateHashData.put("jab", etjab.getText().toString().trim());
                    updateHashData.put("hobby", hospl[0] + "#" + hospl[1]);
                    updateHashData.put("forte", fospl[0] + "#" + fospl[1]);
                    updateHashData.put("favorite", forspl[0] + "#" + forspl[1]);
                    updateHashData.put("dislike", disspl[0] + "#" + disspl[1]);
                    if (check) {
                        updateHashData.put("drink", "none@");
                    } else if (spinnerGetStrDrinkDay.equals("2,매일")) {
                        updateHashData.put("drink", spinnerGetStrDrinkDay + "@" + spinnerGetStrDrinkKind + "@" + spinnerGetStrDrinkBottle);
                    } else {
                        updateHashData.put("drink", spinnerGetStrDrinkDay + "@" + spinnerGetStrDrinkHit + "@" + spinnerGetStrDrinkKind + "@" + spinnerGetStrDrinkBottle);
                    }

                    return updateHashData;
                }

            };
            stringRequest.setShouldCache(false);
            RequestQueue requestQueue = Volley.newRequestQueue(MyProfileLoad.this);
            requestQueue.add(stringRequest);
            super.onStop();
        }

    }

    @Override
    public void onBackPressed() {
        if (spinnerArea.getSelectedItem().toString().equals("지역")) {
            Toast.makeText(MyProfileLoad.this, "지역을 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if (spinnerJabGroup.getSelectedItem().toString().equals("직업군")) {
            Toast.makeText(MyProfileLoad.this, "직업군을 선택해주세요", Toast.LENGTH_SHORT).show();
        } else if (etjab.getText().toString().replaceAll(" ", "").length() == 0) {
            Toast.makeText(MyProfileLoad.this, "직업을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

    }

    public void userSecession() {
        String url = new ServerIP().http+"Android/secession.php";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(MyProfileLoad.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MyProfileLoad.this, MainActivity.class);
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
        simpleMultiPartRequest.addStringParam("id", loginId);
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfileLoad.this);
        requestQueue.add(simpleMultiPartRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        StartingImageLoad();
    }
}