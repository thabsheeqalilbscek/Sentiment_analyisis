package com.example.jamia_virtual_shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class view_cart extends AppCompatActivity {
    ListView li;
    TextView gt;
    Button b;
    SharedPreferences sh;
    String[]  pn, price ,q ,tp , photo,d;
    String ip, url,am,mid;

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), Home.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        li=findViewById(R.id.lc);
        gt=findViewById(R.id.textView3);
        b=findViewById(R.id.button7);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = sh.getString("url", "");
        url = ip + "and_view_cart";
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
                                gt.setText(jsonObj.getString("sum"));



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    pn[i] = u.getString("name");
                                    price[i] = u.getString("price");
                                    q[i] = u.getString("qty");
                                    tp[i] = u.getString("amount");
                                    d[i] = u.getString("date");
                                    photo[i] = u.getString("photo");


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

       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                am=gt.getText().toString();




               AlertDialog.Builder builder = new AlertDialog.Builder(view_cart.this);
               builder.setTitle("options");
               builder.setItems(new CharSequence[]
                               {"Place Order","Cancel"},
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
                                       ed1.putString("amount", am);
                                       ed1.putString("master", mid);
                                       ed1.commit();

                                       String url = "http://" + ip + ":3000/offlinep";


                                       RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                String url = null;
                                       StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                               new Response.Listener<String>() {
                                                   @Override
                                                   public void onResponse(String response) {
                                                       //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                                       try {
                                                           JSONObject jsonObj = new JSONObject(response);
                                                           if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                                               Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                                               Intent i =new Intent(getApplicationContext(),view_cart.class);
                                                               startActivity(i);
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





//                                requestQueue.add(postRequest);
                                   }


                                   break;

////                            case 1:
////                            {
////                                SharedPreferences sh2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////                                String ip = sh2.getString("ip", "");
////                                final String sid1 = sh2.getString("lid", "");
//////                                params.put("lid", sp.getString("lid", ""));
////
////
////                                String url = "http://" + ip + ":5000/tockens";
////
////
//////                                requestQueue.add(postRequest);
////                            }
////
////
////
////
////
////                            break;
//

                                   case 2:

                                       break;


                               }
                           }
                       });
               builder.create().show();


           }
       });

    }
}
