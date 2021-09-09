package com.example.mana;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL=new ServerIP().http+"Android/Id_Duplicate_Check.php";
    private Map<String,String> map;

    public ValidateRequest(String userID, Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("email",userID);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}