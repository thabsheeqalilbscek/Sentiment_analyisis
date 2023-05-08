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

public class custom_view_product extends BaseAdapter {
    String[] id;
    String[] name;
    String[] price;
    String[] quantity;
    String[] desc;
    String[] photo;
    ImageView img;
    private Context context;

    public custom_view_product(Context applicationContext, String[] id, String[] name, String[] price, String[] quantity, String[] desc, String[] photo) {

        this.context = applicationContext;
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.desc = desc;
        this.photo = photo;
    }

    @Override
    public int getCount() {
        return id.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_product, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.t1);
        TextView tv2 = (TextView) gridView.findViewById(R.id.t2);
        TextView tv3 = (TextView) gridView.findViewById(R.id.t3);
        TextView tv4 = (TextView) gridView.findViewById(R.id.t4);
        img = (ImageView) gridView.findViewById(R.id.imageView3);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);


        tv1.setText(name[i]);
        tv2.setText(price[i]);
        int s = 0;
        if (quantity[i].equalsIgnoreCase(String.valueOf(0))) {
            tv3.setText("OUT OF STOCK");

        } else {
            tv3.setText(quantity[i]);
        }
        tv4.setText(desc[i]);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("url", "");
        String url =  ip + photo[i];
        Picasso.with(context).load(url).transform(new CircleTransform()).into(img);//circle

        return gridView;


    }
}