package com.example.mana.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class PermissionHelper {
    public static final int CAMERA_PERMISSION_CODE = 5000;

    private static final String[] REQUIRE_PERMISSIONS_MORE_29 = {
            Manifest.permission.CAMERA
    };

    private static final String[] REQUIRE_PERMISSIONS_UNDER_29 = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static String[] getRequirePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            return REQUIRE_PERMISSIONS_MORE_29;
        } else {
            return REQUIRE_PERMISSIONS_UNDER_29;
        }
    }

    public static boolean allPermissionGranted(Context context) {
        return Arrays
                .stream(getRequirePermissions())
                .allMatch(permission ->
                        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                );
    }
}
