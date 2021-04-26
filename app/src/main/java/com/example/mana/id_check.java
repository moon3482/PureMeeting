package com.example.mana;


import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class id_check extends StringRequest {


    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://3.36.21.126/Id_Duplicate_Check.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public id_check(String UserEmail,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("email", UserEmail);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}

