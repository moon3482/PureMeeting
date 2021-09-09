package com.example.mana.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.ChatPage.chatPage;
import com.example.mana.MainPage.mainPage;
import com.example.mana.R;
import com.example.mana.ServerIP;
import com.example.mana.ShopInfomation.ShopInfomation;
import com.example.mana.ZoneAdd;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.example.mana.R.color.appThemeColor;

public class mapPage extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_map);
        /*******************선언부**************************/
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivHomeButton = (ImageView) findViewById(R.id.MapbnHomeButton);
        ivMessageBoxButton = findViewById(R.id.MapbnMessageBoxButton);
        ivMapButton = findViewById(R.id.MapbnMapButton);
        ivMyPageButton = findViewById(R.id.MapbnMyPageButton);
        /************이미지뷰에 버튼 이미지 set**************/
        ivHomeButton.setImageResource(R.drawable.ic_baseline_home_24);
        ivMessageBoxButton.setImageResource(R.drawable.ic_baseline_mode_comment_24);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        ivMyPageButton.setImageResource(R.drawable.ic_baseline_person_24);
        DrawableCompat.setTint(ivMessageBoxButton.getDrawable(), ContextCompat.getColor(mapPage.this, appThemeColor));
        DrawableCompat.setTint(ivMyPageButton.getDrawable(), ContextCompat.getColor(mapPage.this, appThemeColor));
        DrawableCompat.setTint(ivHomeButton.getDrawable(), ContextCompat.getColor(mapPage.this, appThemeColor));
        /************툴바 선언부***************************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMapPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("매장 검색");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(appThemeColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        /****메인페이지버튼********/
        ivHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mapPage.this, mainPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        /*****메세지박스버튼********/
        ivMessageBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mapPage.this, chatPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        /**********마이페이지 버튼***********/
        ivMyPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mapPage.this, MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        /**********지도 부분********************/
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::onMapReady);



    }

    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;


        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5221336,126.920243),16);
        naverMap.setCameraPosition(cameraPosition);
        LoadShopMap(naverMap);


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

        mapView.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(mapPage.this, mainPage.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        /************선택 이미지뷰 디자인 변경***************/
        ImageView ivMapButton = findViewById(R.id.MapbnMapButton);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        DrawableCompat.setTint(ivMapButton.getDrawable(), ContextCompat.getColor(mapPage.this, R.color.White));
        ivMapButton.setBackgroundColor(Color.parseColor("#E91E63"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageView ivHomeButton, ivMessageBoxButton, ivMapButton, ivMyPageButton;
        ivMapButton = findViewById(R.id.MapbnMapButton);
        ivMapButton.setImageResource(R.drawable.ic_baseline_location_on_24);
        DrawableCompat.setTint(ivMapButton.getDrawable(), ContextCompat.getColor(mapPage.this, appThemeColor));
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
    public void LoadShopMap(NaverMap naverMap){
        String url = new ServerIP().http+"Android/LoadShopMap.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("맵등록", ""+response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String shopcode = jsonObject1.getString("shopcode");
                            Log.e("맵아이템", ""+shopcode);
                            String name = jsonObject1.getString("name");
                            Log.e("맵아이템", ""+name);
                            String shoptype = jsonObject1.getString("shoptype");
                            String address = jsonObject1.getString("address");
                            String starttime = jsonObject1.getString("starttime");
                            String endtime = jsonObject1.getString("endtime");
                            Double mapx = jsonObject1.getDouble("mapx");
                            Log.e("맵아이템", ""+mapx);
                            Double mapy = jsonObject1.getDouble("mapy");
                            Log.e("맵아이템", ""+mapy);
                            Marker marker = new Marker(new LatLng(mapx , mapy));
                            marker.setMap(naverMap);
                            InfoWindow infoWindow = new InfoWindow();
                            infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(mapPage.this) {
                                @NonNull
                                @Override
                                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                                    return name;
                                }
                            });
                            infoWindow.open(marker);
                            marker.setOnClickListener(new Overlay.OnClickListener() {
                                @Override
                                public boolean onClick(@NonNull Overlay overlay) {
                                    Intent intent = new Intent(mapPage.this, ShopInfomation.class);
                                    intent.putExtra("Type", "search");
                                    intent.putExtra("shopcode",shopcode);
                                    intent.putExtra("name",name);
                                    intent.putExtra("shoptype",shoptype);
                                    intent.putExtra("address",address);
                                    intent.putExtra("starttime",starttime);
                                    intent.putExtra("endtime",endtime);
                                    startActivity(intent);
                                    return true;
                                }
                            });

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mapPage.this);
        requestQueue.add(stringRequest);
    }
}
