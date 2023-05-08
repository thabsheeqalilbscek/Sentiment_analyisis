package com.example.jamia_virtual_shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class View_Products extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView li;
    SharedPreferences sh;
    String[]  id,name, price ,quantity ,desc,photo;
    String ip, url, sid;



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),view_near_by_store.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        li=findViewById(R.id.lst);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = sh.getString("url", "");
        url = ip + "and_view_product";
        sid=getIntent().getStringExtra("sid");
//        Toast.makeText(this, ""+url+sid, Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {


//

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                id = new String[js.length()];
                                name = new String[js.length()];
                                price = new String[js.length()];
                                quantity = new String[js.length()];
                                desc = new String[js.length()];
                                photo = new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    id[i] = u.getString("id");//dbcolumn name in double quotes
                                    name[i] = u.getString("name");
                                    price[i] = u.getString("price");
                                    quantity[i] = u.getString("quantity");
                                    desc[i] = u.getString("des");
                                    photo[i] = u.getString("photo");


//                                    Toast.makeText(getApplicationContext(), "rr"+com[i], Toast.LENGTH_SHORT).show();

                                }
                                li.setAdapter(new custom_view_product(getApplicationContext(), id,name, price ,quantity ,desc,photo));//custom_view_service.xml and li is the listview object


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
                params.put("store_id",sid);
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

        li.setOnItemClickListener(this);




    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       String p=id[i];
//        Toast.makeText(this, "ys"+p, Toast.LENGTH_SHORT).show();
        Intent q=new Intent(getApplicationContext(),quantity.class);
        q.putExtra("pid",p);
        startActivity(q);

    }
}