package com.example.mana;


import com.android.volley.Response;
import com.android.volley.request.StringRequest;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Sign_insert extends StringRequest {


    //서버 URL 설정(php 파일 연동)
    final static private String URL = new ServerIP().http+"insert.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public Sign_insert(String UserEmail,String userPassword,String userName,String userBirthday,String userGender,String userJabs,String userArea, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("email", UserEmail);
        map.put("password", userPassword);
        map.put("name", userName);
        map.put("birthday", userBirthday);
        map.put("gender", userGender);
        map.put("jabs", userJabs);
        map.put("area", userArea);




    }

    @Override
    protected Map<String, String>getParams() {
        return map;
    }
}

