package com.example.jamia_virtual_shopping;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public abstract class custom_product_status extends BaseAdapter {
    private final Context context;
    String[] tid, ts, td;

    public custom_product_status(Context applicationContext, String[] tid, String[] ts, String[] td) {

        this.context = applicationContext;
        this.tid = tid;
        this.ts = ts;
        this.td = td;
    }

    @Override
    public int getCount() {
        return tid.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_product_status, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.ds1);
        TextView tv2 = (TextView) gridView.findViewById(R.id.dd1);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);


        tv1.setText(ts[i]);
        tv2.setText(td[i]);


        return gridView;


    }
}
