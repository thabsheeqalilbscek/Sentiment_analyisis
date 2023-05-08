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

public abstract class custom_view_cart extends BaseAdapter {
    private final Context context;
    String[]  pn, price ,q ,tp,d, photo;
    ImageView img;


    public custom_view_cart(Context applicationContext,  String[] pn, String[] price, String[] q, String[] tp, String[] photo,String[] d) {

        this.context = applicationContext;
        this.pn = pn;
        this.price = price;
        this.q = q;
        this.tp = tp;
        this.d = d;
        this.photo = photo;
    }

    @Override
    public int getCount() {
        return pn.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_cart, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.pn1);
        TextView tv2 = (TextView) gridView.findViewById(R.id.pr1);
        TextView tv3 = (TextView) gridView.findViewById(R.id.pq1);
        TextView tv4 = (TextView) gridView.findViewById(R.id.tp1);
        TextView tv5 = (TextView) gridView.findViewById(R.id.pd1);
        img = (ImageView) gridView.findViewById(R.id.pic);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText(pn[i]);
        tv2.setText(price[i]);

        tv3.setText(q[i]);
        tv4.setText(tp[i]);
        tv5.setText(d[i]);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("url", "");
        String url = ip +  photo[i];
        Picasso.with(context).load(url).transform(new CircleTransform()).into(img);//circle

        return gridView;


    }

}
