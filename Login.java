package com.example.jamia_virtual_shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

public class Login extends AppCompatActivity {
    EditText  e1,e2;
    Button b1;
    TextView t1;
    String url;
    SharedPreferences sh;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1=findViewById(R.id.editTextTextPersonName2);
        e2=findViewById(R.id.editTextTextPassword);

        e1.setText("abay@gmail.com");
        e2.setText("abay123");
//        e1.setText("c");
//        e2.setText("c");
        b1=findViewById(R.id.button2);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String s1=e1.getText().toString();
                final String s2=e2.getText().toString();
                url=sh.getString("url","")+"clogin";


                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        String id = jsonObj.getString("lid");
                                        String typ = jsonObj.getString("type");
                                        SharedPreferences.Editor ed1 = sh.edit();
                                        ed1.putString("lid", id);
                                        ed1.commit();
                                        Intent ij = new Intent(getApplicationContext(),Home.class);
                                        startActivity(ij);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error" + url.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), "eror,,,,,", Toast.LENGTH_SHORT).show();
                            }

                        }
                ) {

                    //                value Passing android to python
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("username", s1);//passing to python
                        params.put("password", s2);


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
        t1=findViewById(R.id.reg);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });
    }
}