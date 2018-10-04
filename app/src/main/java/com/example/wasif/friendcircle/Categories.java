package com.example.wasif.friendcircle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Categories extends AppCompatActivity {
    Button btnJammarat;
    Button btnMosque;
    Button btnHos;
    Button btnClinic;
    Button btnStation;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catogries);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Locator's");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnJammarat =findViewById(R.id.btnJammarat);
        btnMosque =findViewById(R.id.btnMosque);
        btnHos =findViewById(R.id.btnHos);
        btnClinic =findViewById(R.id.btnClinic);
        btnStation =findViewById(R.id.btnStation);



        btnMosque.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=mosque");
        Intent mapIntent =new Intent(Intent.ACTION_VIEW,gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
           }
        });


        btnJammarat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=refreshment");
        Intent mapIntent =new Intent(Intent.ACTION_VIEW,gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

           }
        });

        btnHos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospital");
                Intent mapIntent =new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        btnClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=clinic");
                Intent mapIntent =new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        btnStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=station");
                Intent mapIntent =new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
    }
}
