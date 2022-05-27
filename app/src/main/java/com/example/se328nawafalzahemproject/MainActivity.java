package com.example.se328nawafalzahemproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    Button FB,SQLITE,WEATHERAPI;
    RequestQueue rq;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq= Volley.newRequestQueue(this);
        rq.add(weatherHelper.weather(this));

        FB=(Button) findViewById(R.id.FireBaseButton);
        SQLITE=(Button) findViewById(R.id.SQLITEbutton);
        WEATHERAPI=(Button) findViewById(R.id.WEATHERAPIbutton);

      FB.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(MainActivity.this,Firebase.class));
          }
      });

        SQLITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SQLITE.class));
            }
        });



        WEATHERAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,weather.class));
            }
        });

    }
}