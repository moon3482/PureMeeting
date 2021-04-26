package com.example.mana;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapConverter {

        /*
         * String형을 BitMap으로 변환시켜주는 함수
         * */
        public static Bitmap StringToBitmap(String encodedString) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }

        /*
         * Bitmap을 String형으로 변환
         * */
        public static String BitMapToString(Bitmap bitmap){
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            String temp=Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }

        /*
         * Bitmap을 byte배열로 변환
         * */
        public static byte[] BitmapToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            return baos.toByteArray();
        }
    }

