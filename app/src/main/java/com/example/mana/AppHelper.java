package com.example.mana;

import com.android.volley.RequestQueue;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;

public class AppHelper {
    public static RequestQueue requestQueue;
    NaverMap naverMap;
    Marker marker;

    public void inmaker(String shopLocation){

        marker.setPosition(new LatLng(Double.parseDouble(shopLocation),Double.parseDouble(shopLocation)));
        marker.setMap(naverMap);
    }
}
