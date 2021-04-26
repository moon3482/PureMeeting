package com.example.mana.shopimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mana.R;

import java.util.ArrayList;

public class ShopImageAdapter extends PagerAdapter {

    private ArrayList<ImageDataClass> arrayList = null;
    private LayoutInflater inflater;
    private Context context;
    private ImageDataClass imageDataClass;
//    private int[] a = {R.drawable.basicprofile, R.drawable.basicprofile, R.drawable.basicprofile, R.drawable.basicprofile};

    @Override
    public int getCount() {
        /******본 코드*****/
        return arrayList.size();

        /******실험 코드*****/
//        return a.length;
    }

    public ShopImageAdapter(Context context, ArrayList<ImageDataClass> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    public Object instantiateItem(ViewGroup viewGroup, int position) {
        imageDataClass = arrayList.get(position);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.shopinforecy, viewGroup, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView3);
        TextView textView = (TextView) view.findViewById(R.id.textView4);
        /*************본 set ImageView***************/
        Glide.with(view.getContext()).load(imageDataClass.getUrl()).into(imageView);
        textView.setText(arrayList.indexOf(imageDataClass)+1 + "/" + arrayList.size());
        /***********테스트 set ImageView ***************/
//        imageView.setImageResource(a[position]);
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
