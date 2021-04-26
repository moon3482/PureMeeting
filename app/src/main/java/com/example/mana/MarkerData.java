package com.example.mana;

import android.view.View;

import androidx.annotation.NonNull;

import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

public class MarkerData extends InfoWindow.ViewAdapter {
    Marker marker;
    InfoWindow infoWindow;

    @NonNull
    @Override
    public View getView(@NonNull InfoWindow infoWindow) {
        return null;
    }
}
