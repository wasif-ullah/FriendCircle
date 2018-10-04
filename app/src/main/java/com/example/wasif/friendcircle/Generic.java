package com.example.wasif.friendcircle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Generic extends AppCompatActivity {
    Button btnDua;
    Button btnLocator;
    Button btnTime;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Generic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDua = findViewById(R.id.btnDua);
        btnLocator = findViewById(R.id.btnlocator);
        btnTime = findViewById(R.id.btntime);




        btnDua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Generic.this,Duas.class));

            }
        });
        btnLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Generic.this,Categories.class));
                //Toast.makeText(Generic.this, "testing", Toast.LENGTH_SHORT).show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Generic.this, "testing", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Generic.this,PrayerTime.class));
            }
        });
    }
    }
