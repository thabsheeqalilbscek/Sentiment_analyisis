package com.example.jamia_virtual_shopping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class View_profile extends AppCompatActivity {
    EditText name;
    EditText place,post,pin,district;
    EditText phone;
    EditText email;
//    String dis;
    //    RadioButton r1, r2;
    ImageView img;
    SharedPreferences sh;
    String url = "",ip ;
//    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        name = (EditText) findViewById(R.id.namedb2);
        place = (EditText) findViewById(R.id.place);
        post = (EditText) findViewById(R.id.post);
        pin = (EditText) findViewById(R.id.pinn);
        district = (EditText) findViewById(R.id.ds1);
        phone = (EditText) findViewById(R.id.phnd);
        email = (EditText) findViewById(R.id.email);
        img = (ImageView) findViewById(R.id.imgdb);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = sh.getString("url", "");
        url = ip + "andprofile";
//        Toast.makeText(this, "" + url, Toast.LENGTH_SHORT).show();


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONObject jj = jsonObj.getJSONObject("data");

                                name.setText(jj.getString("user_name"));
                                place.setText(jj.getString("place"));
                                post.setText(jj.getString("post"));
                                pin.setText(jj.getString("pin"));
                                district.setText(jj.getString("district"));
                                email.setText(jj.getString("email"));
                                phone.setText(jj.getString("phone_number"));
//                                photo.setText(jj.getString("photo"));
//                                pin.setText(jj.getString("pin"));

                                String image = jj.getString("photo");
                                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String ip = sh.getString("url", "");
                                String url =  ip + image;
                                Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).into(img);//circle

                            } else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            //                value Passing android to python
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", sh.getString("lid",""));//passing to python

                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


    }
}