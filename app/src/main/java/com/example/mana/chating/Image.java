package com.example.mana.chating;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ui.PhotoView;
import com.bumptech.glide.Glide;
import com.example.mana.Login;
import com.example.mana.R;

public class Image extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.chat_image_view);

        Intent intent = getIntent();
        String img = intent.getStringExtra("img");

        Glide.with(Image.this)
                .load(img)
                .into(imageView);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Image.this,Client.class);
        setResult(RESULT_OK,intent);
    }

}
