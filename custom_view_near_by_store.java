package com.example.jamia_virtual_shopping;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class custom_view_near_by_store extends BaseAdapter {
    private final Context context;
    String[] sid, sn, sl, se, sp;

    public custom_view_near_by_store(Context applicationContext, String[] sid, String[] sn, String[] sl, String[] se, String[] sp) {
        this.context = applicationContext;
        this.sid = sid;
        this.sn = sn;
        this.sl = sl;

        this.se = se;
        this.sp = sp;
    }

    @Override
    public int getCount() {
        return sid.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_view_near_by_store, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.si);
        TextView tv2 = (TextView) gridView.findViewById(R.id.sn);
        TextView tv3 = (TextView) gridView.findViewById(R.id.sp);
        TextView tv4 = (TextView) gridView.findViewById(R.id.se);
        TextView tv5 = (TextView) gridView.findViewById(R.id.ph1);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText(sid[i]);
        tv2.setText(sn[i]);
        tv3.setText(sl[i]);
        tv4.setText(se[i]);
        tv5.setText(sp[i]);

        return gridView;
    }
}