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

public abstract class custom_view_orders extends BaseAdapter {
    private final Context context;
    String[]  mid,date, tot;
    ImageView img;


    public custom_view_orders(Context applicationContext, String[] mid, String[] date, String[] tot) {

        this.context = applicationContext;
        this.mid = mid;
        this.date = date;
        this.tot = tot;
    }

    @Override
    public int getCount() {
        return mid.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_orders, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv4 = (TextView) gridView.findViewById(R.id.otp1);
        TextView tv5 = (TextView) gridView.findViewById(R.id.od1);


        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv4.setText(tot[i]);
        tv5.setText(date[i]);


        return gridView;


    }
    }
