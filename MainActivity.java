package com.example.jamia_virtual_shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    Button b1;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.ipaddress);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1 = findViewById(R.id.button);

        e1.setText(sh.getString("ip", ""));
//        e1.setText("192.168.58.62");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip=e1.getText().toString();
//                Toast.makeText(MainActivity.this, ""+ip, Toast.LENGTH_SHORT).show();

                String url = "http://"+ip+":3000/";
                SharedPreferences.Editor e1=sh.edit();
                e1.putString("ip",ip);
                e1.putString("url",url);
//                e1.apply();
                e1.commit();
                Intent ij=new Intent(getApplicationContext(),Login.class);
                startActivity(ij);

            }
        });
    }
}