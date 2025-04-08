package com.example.mana;



import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignupActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.signup_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("이용약관");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
    }


}
