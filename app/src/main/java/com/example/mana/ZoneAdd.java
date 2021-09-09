package com.example.mana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mana.ShopInfomation.ShopInfomation;
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

public class ZoneAdd extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private MapView mapView;
    private NaverMap naverMap;
    private int GETSHOPRESULTCODE = 5609;
    String username, roomnum, imgurl, youid;
    public static ZoneAdd activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_add);
        activity = this;
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomnum = intent.getStringExtra("roomnumber");
        youid = intent.getStringExtra("youid");
        SharedPreferences sharedPreferences = getSharedPreferences("loginId", MODE_PRIVATE);
        imgurl = sharedPreferences.getString("imgurl", "");
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        mapView = findViewById(R.id.ZoneAddMap);

        mapView.getMapAsync(this);


    }

    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;
//        naverMap.setLocationSource(locationSource);
//        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//        naverMap.getUiSettings().setLocationButtonEnabled(true);
        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5221336, 126.920243), 16);
        naverMap.setCameraPosition(cameraPosition);
        LoadShopMap(naverMap);
//        Marker marker = new Marker();
//        marker.setPosition(new LatLng(37.5221336, 126.920243));
//        marker.setMap(naverMap);
//        InfoWindow infoWindow = new InfoWindow();
//        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(ZoneAdd.this) {
//            @NonNull
//            @Override
//            public CharSequence getText(@NonNull InfoWindow infoWindow) {
//                return "힛더 스팟";
//            }
//        });
//        infoWindow.open(marker);
//
//        marker.setOnClickListener(new Overlay.OnClickListener() {
//            @Override
//            public boolean onClick(@NonNull Overlay overlay) {
//                Intent intent = new Intent(ZoneAdd.this, ShopInfomation.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//                startActivity(intent);
//                finish();
//                return true;
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void LoadShopMap(NaverMap naverMap) {
        String url = new ServerIP().http+"Android/LoadShopMap.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("맵등록", "" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String shopcode = jsonObject1.getString("shopcode");
                            Log.e("맵아이템", "" + shopcode);
                            String shopname = jsonObject1.getString("name");
                            Log.e("맵아이템", "" + shopname);
                            String shoptype = jsonObject1.getString("shoptype");
                            String address = jsonObject1.getString("address");
                            String starttime = jsonObject1.getString("starttime");
                            String endtime = jsonObject1.getString("endtime");
                            Double mapx = jsonObject1.getDouble("mapx");
                            Log.e("맵아이템", "" + mapx);
                            Double mapy = jsonObject1.getDouble("mapy");
                            Log.e("맵아이템", "" + mapy);
                            Marker marker = new Marker(new LatLng(mapx, mapy));
                            marker.setMap(naverMap);
                            InfoWindow infoWindow = new InfoWindow();
                            infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(ZoneAdd.this) {
                                @NonNull
                                @Override
                                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                                    return shopname;
                                }
                            });
                            infoWindow.open(marker);
                            marker.setOnClickListener(new Overlay.OnClickListener() {
                                @Override
                                public boolean onClick(@NonNull Overlay overlay) {
                                    Intent intent = new Intent(ZoneAdd.this, ShopInfomation.class);
                                    intent.putExtra("Type", "reserve");
                                    intent.putExtra("shopcode", shopcode);
                                    intent.putExtra("shopname", shopname);
                                    intent.putExtra("shoptype", shoptype);
                                    intent.putExtra("address", address);
                                    intent.putExtra("starttime", starttime);
                                    intent.putExtra("endtime", endtime);
                                    intent.putExtra("username", username);
                                    intent.putExtra("roomnum", roomnum);
                                    intent.putExtra("imgurl", imgurl);
                                    intent.putExtra("youid",youid);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ZoneAdd.this);
        requestQueue.add(stringRequest);
    }
}