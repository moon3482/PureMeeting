package com.example.mana;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

public class Customdialog2 {
    private Context context;
    private String day, hit, kind, bottle;


    public Customdialog2(Context context, String day, String hit, String kind, String bottle) {
        this.context = context;
        this.day = day;
        this.hit = hit;
        this.kind = kind;
        this.bottle = bottle;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setBottle(String bottle) {
        this.bottle = bottle;
    }

    public String getDay() {
        return day;
    }

    public String getHit() {
        return hit;
    }

    public String getKind() {
        return kind;
    }

    public String getBottle() {
        return bottle;
    }

    public void CallDrinkDialog(TextView textView) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_drink);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final CheckBox cbDrink = dialog.findViewById(R.id.cb_MyInfo_Drink);
        final Spinner DrinkDay = dialog.findViewById(R.id.spinner_MyInfo_Drink_day);
        final Spinner DrinkHit = dialog.findViewById(R.id.spinner_MyInfo_Drink_Hit);
        final Spinner DrinkKind = dialog.findViewById(R.id.spinner_MyInfo_Drink_kind);
        final Spinner DrinkBottle = dialog.findViewById(R.id.spinner_MyInfo_Drink_Bottle);
        final Button btCancel = dialog.findViewById(R.id.customDialogDrink_cancel);
        final Button btOk = dialog.findViewById(R.id.customDialogDrink_Ok);
        final String[] daysplit = getDay().split(",");
        final String[] hitsplit = getHit().split(",");
        final String[] kindsplit = getKind().split(",");
        final String[] bottlesplit = getBottle().split(",");
        ArrayAdapter<CharSequence> DrinkDayArrayAdapter = ArrayAdapter.createFromResource(context, R.array.drink_day, R.layout.spinner_gravity);
        DrinkDayArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrinkDay.setAdapter(DrinkDayArrayAdapter);
        DrinkDay.setSelection(Integer.parseInt(daysplit[0]));
        DrinkDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MyProfileLoad) MyProfileLoad.myinfocontext).spinnerGetStrDrinkDay = position + "," + DrinkDay.getSelectedItem().toString();
                if (DrinkDay.getSelectedItem().toString().equals("매일")) {
                    DrinkHit.setVisibility(View.GONE);
                } else {
                    DrinkHit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> DrinkHitArrayAdapter = ArrayAdapter.createFromResource(context, R.array.drink_num, R.layout.spinner_gravity);
        DrinkHitArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrinkHit.setAdapter(DrinkHitArrayAdapter);
        DrinkHit.setSelection(Integer.parseInt(hitsplit[0]));
        DrinkHit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MyProfileLoad) MyProfileLoad.myinfocontext).spinnerGetStrDrinkHit = position + "," + DrinkHit.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> DrinkKindArrayAdapter = ArrayAdapter.createFromResource(context, R.array.drink_kind, R.layout.spinner_gravity);
        DrinkKindArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrinkKind.setAdapter(DrinkKindArrayAdapter);
        DrinkKind.setSelection(Integer.parseInt(kindsplit[0]));
        DrinkKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MyProfileLoad) MyProfileLoad.myinfocontext).spinnerGetStrDrinkKind = position + "," + DrinkKind.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> DrinkBottleArrayAdapter = ArrayAdapter.createFromResource(context, R.array.drink_bottle, R.layout.spinner_gravity);
        DrinkBottleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrinkBottle.setAdapter(DrinkBottleArrayAdapter);
        DrinkBottle.setSelection(Integer.parseInt(bottlesplit[0]));
        DrinkBottle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MyProfileLoad) MyProfileLoad.myinfocontext).spinnerGetStrDrinkBottle = position + "," + DrinkBottle.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cbDrink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DrinkDay.setEnabled(false);
                    DrinkDay.setAlpha(0.5f);
                    DrinkHit.setEnabled(false);
                    DrinkHit.setAlpha(0.5f);
                    DrinkKind.setEnabled(false);
                    DrinkKind.setAlpha(0.5f);
                    DrinkBottle.setEnabled(false);
                    DrinkBottle.setAlpha(0.5f);
                } else {
                    DrinkDay.setEnabled(true);
                    DrinkDay.setAlpha(1);
                    DrinkHit.setEnabled(true);
                    DrinkHit.setAlpha(1);
                    DrinkKind.setEnabled(true);
                    DrinkKind.setAlpha(1);
                    DrinkBottle.setEnabled(true);
                    DrinkBottle.setAlpha(1);
                }
            }
        });
        if (daysplit[1].equals("음주 안함")) {
            cbDrink.setChecked(true);
        }

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbDrink.isChecked()) {
                    ((MyProfileLoad) MyProfileLoad.myinfocontext).check = true;
                    textView.setText("음주 안함");
                    dialog.dismiss();
                } else {
                    if (DrinkDay.getSelectedItem().toString().equals("매일")) {
                        textView.setText(new String(Character.toChars(0x1F4C6)) + DrinkDay.getSelectedItem().toString() + "  " + new String(Character.toChars(0x1F37A)) + DrinkKind.getSelectedItem().toString() + "  " + new String(Character.toChars(0x1F377)) + DrinkBottle.getSelectedItem().toString());

                        dialog.dismiss();
                    } else {
                        textView.setText(new String(Character.toChars(0x1F4C6)) + DrinkDay.getSelectedItem().toString() + "  " + new String(Character.toChars(0x23F3)) + DrinkHit.getSelectedItem().toString() + "  " + new String(Character.toChars(0x1F37A)) + DrinkKind.getSelectedItem().toString() + "  " + new String(Character.toChars(0x1F377)) + DrinkBottle.getSelectedItem().toString());
                        dialog.dismiss();
                    }
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}

