package com.kalu.mainactivity.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kalu.mainactivity.R;

public class SlideAdapter extends PagerAdapter {


    Context context;
    LayoutInflater inflater;
    public int[] list_img={
            R.drawable.ic_locateevent,
            R.drawable.ic_people,
            R.drawable.ic_account,
            R.drawable.ic_scan
    };
    public  String[] list_title={
            "Real Time Events",
            "Comment",
            "Easy Locate",
            "Easy Share"
    };


    public  String[] list_descri={
            "Description 1",
            "Description 2",
            "Description 3",
            "Description 4"
    };
    public int[] list_bgd={
            Color.CYAN,
            Color.rgb(239,05,05),
            Color.rgb(110,49,29),
            Color.rgb(1,180,212),
    };

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.slide,container,false);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.silideliner);
        ImageView imageView=(ImageView)view.findViewById(R.id.slideimg);
        TextView txtDescri=(TextView)view.findViewById(R.id.txtdescri);
        TextView txtTitle=(TextView)view.findViewById(R.id.txttitle);
        linearLayout.setBackgroundColor(list_bgd[position]);
        imageView.setImageResource(list_img[position]);
        txtDescri.setText(list_descri[position]);
        txtTitle.setText(list_title[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

}
