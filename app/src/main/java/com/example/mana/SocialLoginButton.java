package com.example.mana;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SocialLoginButton extends LinearLayout {

    public SocialLoginButton(Context context) {
        super(context);
        init(null);
    }

    public SocialLoginButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SocialLoginButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        inflate(getContext(), R.layout.btn_social_login, this);
        ImageView logo = findViewById(R.id.socialLogo);
        TextView script = findViewById(R.id.script);
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attributeSet,
                    R.styleable.SocialLoginButton);

            int logoResId = typedArray.getResourceId(
                    R.styleable.SocialLoginButton_logoSrc,
                    0);
            int logoPadding = typedArray.getDimensionPixelSize(
                    R.styleable.SocialLoginButton_logoPadding,
                    0);
            int textColor = typedArray.getColor(
                    R.styleable.SocialLoginButton_textColor,
                    script.getCurrentTextColor());
            String loginText = typedArray.getString(
                    R.styleable.SocialLoginButton_loginText);
            float radius = typedArray.getDimension(
                    R.styleable.SocialLoginButton_radius,
                    0f
            );
            int borderWidth = typedArray.getDimensionPixelSize(
                    R.styleable.SocialLoginButton_borderWidth, 0);
            int borderColor = typedArray.getColor(
                    R.styleable.SocialLoginButton_borderColor,
                    Color.TRANSPARENT);

            if (logoResId != 0) {
                logo.setImageResource(logoResId);
            }
            if (logoPadding > 0) {
                logo.setPadding(logoPadding, logoPadding, logoPadding, logoPadding);
            }
            if (loginText != null) {
                script.setText(loginText);
            }
            script.setTextColor(textColor);
            applyBackgroundWithBorder(radius, borderWidth, borderColor);
            typedArray.recycle();
        }
    }

    private void applyBackgroundWithBorder(float radius, int borderWidth, int borderColor) {
        GradientDrawable shape = new GradientDrawable();


        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            shape.setColor(((ColorDrawable) background).getColor());
        }

        if (radius > 0) {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(radius);
        }

        if (borderWidth > 0) {
            shape.setStroke(borderWidth, borderColor);
        }

        setBackground(shape);
    }
}
