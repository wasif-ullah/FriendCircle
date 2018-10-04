package com.example.wasif.friendcircle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnGeneric,btncreate;
    Button btnhelp;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Stay_Together");
       //getSupportActionBar().setTitle("hajji stay_togather");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        btnGeneric = findViewById(R.id.btnGeneric);
        btncreate = findViewById(R.id.btnCreate);
        btnhelp =findViewById(R.id.help);


        btncreate.setOnClickListener(this);
        btnGeneric.setOnClickListener(this);

        btnhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Help.class));
            }
        });


        Thread t = new Thread() {
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate= findViewById(R.id.date);
                                long date= System.currentTimeMillis();
                                SimpleDateFormat sdf =new SimpleDateFormat("dd MMM yyyy\nhh-mm-ss a");
                                String dateString= sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }

                }
                catch (InterruptedException e){

                }

            }

        };
        t.start();


    }
/*

    public void mapActivity(View view) {
       startActivity(new Intent(MainActivity.this,MapsActivity.class));

    }

    public void generic(View view) {
        startActivity(new Intent(MainActivity.this, Categories.class));

    }
*/


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.setTitle("Stay Together");
        alertDialog.setMessage("Do you want to close application");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert= alertDialog.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGeneric:
                startActivity(new Intent(MainActivity.this,Generic.class));
                break;
            case R.id.btnCreate:
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
                break;

        }


    }
}
