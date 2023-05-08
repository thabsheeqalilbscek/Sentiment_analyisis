package com.example.jamia_virtual_shopping;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    EditText e1, e2, e3, e4, e5, e6, e7, e8;
    Button b1;
    Spinner d;
    ImageView i1;
    SharedPreferences sh;
    Bitmap bitmap = null;
    ProgressDialog pd;
    String url="" ;

//    String name, place, post,pin, district,phone, email, password,cpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        e1 = (EditText) findViewById(R.id.Cusnamereg3);
        e2 = (EditText) findViewById(R.id.addressrefg3);
        e3 = (EditText) findViewById(R.id.post1);
        e4 = (EditText) findViewById(R.id.pin1);
        d =(Spinner) findViewById(R.id.spinner);
        e5 = (EditText) findViewById(R.id.cusphnreg3);
        e6 = (EditText) findViewById(R.id.cusemailreg3);
        e7 = (EditText) findViewById(R.id.cuspasswor4);
        e8 = (EditText) findViewById(R.id.cuspasswor6);
        i1 = (ImageView) findViewById(R.id.imageView4);
        b1 = (Button) findViewById(R.id.register);


        b1.setOnClickListener(this);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        ip = sp.getString("ip", "");
//        apiURL = "http://" + ip + ":5000/regu";
             url=sh.getString("url","")+"and_registration";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

    }

    @Override
    public void onClick(View v) {


       String name = e1.getText().toString();
        String place = e2.getText().toString();
        String post = e3.getText().toString();
        String pin = e4.getText().toString();
        String district = d.getSelectedItem().toString();
        String email = e6.getText().toString();
        String phone = e5.getText().toString();
        String password = e7.getText().toString();
        String  cpass = e8.getText().toString();




        int flag=0;
        if (name.equalsIgnoreCase("")) {
            e1.setError("Enter  name");
            flag++;
        }
        if (place.equalsIgnoreCase("")) {
            e2.setError("Enter place");
            flag++;
        }
        if (post.equalsIgnoreCase("")) {
            e3.setError("Enter post");
            flag++;
        }
        if (pin.length()!=6){
            e4.setError("Enter pin");
            flag++;
        }
//        if (district.equalsIgnoreCase("")) {
//            d.setError("Enter dii");
//            flag++;
//        }
        if (phone.length() != 10) {
            e5.setError("Enter Valid phone");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e6.setError("Enter Valid Email");
            flag++;
        }

        if (password.equalsIgnoreCase("")) {
            e7.setError("Enter Valid password");
            flag++;
        }
        if (password.equalsIgnoreCase("")) {
            e8.setError("Enter confirm password");
            flag++;
        }
        if (bitmap==null) {
            Toast.makeText(this, "Select file", Toast.LENGTH_SHORT).show();
        }
        if(flag==0){
            uploadBitmap(name,place,post,pin,district,phone,email,password,cpass);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                i1.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //converting to bitarray
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final String name, final String place, final String post, final String pin, final String district,final String phone,final String email,final String password,final String cpass) {


        pd = new ProgressDialog(Registration.this);
        pd.setMessage("Uploading....");
        pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            pd.dismiss();
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);

                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.getString("status");
                            if (obj.getString("status").equalsIgnoreCase("ok"))
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getApplicationContext(), login.class));
                            if (res.equalsIgnoreCase("none")) {
                                Toast.makeText(getApplicationContext(), "registration failed, password does not match", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(getApplicationContext(), login.class));
                            }
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("name", name);//passing to python
                params.put("place", place);
                params.put("post", post);
                params.put("pin", pin);
                params.put("district", district);
                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                params.put("cpass", cpass);

                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}

//    }
//}