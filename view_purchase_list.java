package com.example.jamia_virtual_shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_purchase_list extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView li;

    SharedPreferences sh;
    String[]  pid, pn, price ,q ,tp , photo,d;
    String ip, url,am,mid;

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), view_purchase.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_lidt);
        li=findViewById(R.id.lc);
       li.setOnItemClickListener(this);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = sh.getString("url", "");
        url = ip + "and_view_purchase_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

//                                JSONArray jj = jsonObj.getJSONArray("mid");//from python

//

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                pn = new String[js.length()];
                                price = new String[js.length()];
                                q = new String[js.length()];
                                tp = new String[js.length()];
                                d = new String[js.length()];
                                photo = new String[js.length()];
                                pid = new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    pn[i] = u.getString("name");
                                    price[i] = u.getString("price");
                                    q[i] = u.getString("qty");
                                    tp[i] = u.getString("amount");
                                    d[i] = sh.getString("dt", "");
                                    photo[i] = u.getString("photo");
                                    pid[i] = u.getString("product_id");


                                }
                                li.setAdapter(new custom_view_cart(getApplicationContext(), pn, price, q, tp, photo, d) {
                                });//custom_view_service.xml and li is the listview object


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
                params.put("mid", sh.getString("mid",""));//passing to python
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view_purchase_list.this);
        builder.setTitle("options");
        builder.setItems(new CharSequence[]
                        {"Review","Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:

                            {
                                SharedPreferences sh2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String ip = sh2.getString("ip", "");
//                                final String sid1 = sh2.getString("lid", "");
//                                params.put("lid", sp.getString("lid", ""));

                                SharedPreferences.Editor ed1 = sh2.edit();
                                ed1.putString("pid", pid[i]);
                                ed1.commit();

                                Intent ij=new Intent(getApplicationContext(), send_review.class);
                                startActivity(ij);






//                                requestQueue.add(postRequest);
                            }


                            break;


                            case 1:

                                break;


                        }
                    }
                });
        builder.create().show();
    }
}
