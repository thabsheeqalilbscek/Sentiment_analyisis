package com.example.jamia_virtual_shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class payment extends AppCompatActivity {
    EditText bn,ac,ifs;
    TextView a;
    Button submit;
    SharedPreferences sh;
    String url,ip,am,mid;


    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), view_orders.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        bn=findViewById(R.id.editText2);
        ac=findViewById(R.id.editText3);
        ifs=findViewById(R.id.editText4);
        a=findViewById(R.id.textView5);
        submit=findViewById(R.id.button4);

        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        am=sh.getString("amount", "");
        a.setText("am");


        mid=sh.getString("mid", "");


        a.setText(am);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String bn1=bn.getText().toString();
                final String ac1=ac.getText().toString();
                final String ifs1=ifs.getText().toString();

                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                ip = sh.getString("url", "");


                url = ip + "online_bank";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(payment.this, "send payment", Toast.LENGTH_SHORT).show();
                                        Intent i =new Intent(getApplicationContext(),view_cart.class);
                                        startActivity(i);
                                    } else if (jsonObj.getString("status").equalsIgnoreCase("insuff")) {
                                        Toast.makeText(getApplicationContext(), "Insufficient balance", Toast.LENGTH_LONG).show();
                                    }
                                        else {
                                        Toast.makeText(getApplicationContext(), "Invalid details", Toast.LENGTH_LONG).show();
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

                        params.put("bn", bn1);//passing to python
                        params.put("acc", ac1);//passing to python
                        params.put("iff", ifs1);//passing to python
                        params.put("amn", am);//passing to python

                        params.put("mid", mid);//passing to python



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
        });
    }
}
