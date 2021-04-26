package com.example.mana;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Customdialog1 {

    Context context;

    public Customdialog1(Context context) {
        this.context = context;
    }


    public String CallCustomDialog(String type, String values, TextView textView) {

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final TextView title = dialog.findViewById(R.id.tvMyInofCustomDialog);
        final EditText value1 = dialog.findViewById(R.id.ettvMyInofCustomDialog1);
        final EditText value2 = dialog.findViewById(R.id.ettvMyInofCustomDialog2);
        final Button btCancel = dialog.findViewById(R.id.customDialog_cancel);
        final Button btOk = dialog.findViewById(R.id.customDialog_Ok);
        final String[] split = values.split(",");
        title.setText(type);
        value1.setText(split[0]);
        value2.setText(split[1]);
        value1.setFilters(new InputFilter[]{filterAlphaNum});
        value2.setFilters(new InputFilter[]{filterAlphaNum});
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value1.getText().toString().replaceAll(" ", "").length() > 0 && value2.getText().toString().replaceAll(" ", "").length() > 0) {
                    textView.setText(value1.getText().toString() + "," + value2.getText().toString());
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return null;
    }
    public InputFilter filterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅣ가-힣]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

}
