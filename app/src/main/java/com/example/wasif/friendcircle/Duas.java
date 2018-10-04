package com.example.wasif.friendcircle;

//import android.support.v7.app.AlertController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Duas extends AppCompatActivity {

    Toolbar toolbar;

   private RecyclerView recyclerView;
   private List<Integer>images;
    customAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duas);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Dua's");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        images = new ArrayList<>();


        images.add(R.drawable.u1);
        images.add(R.drawable.u2);
        images.add(R.drawable.u3);
        images.add(R.drawable.u4);
        images.add(R.drawable.u5);
        images.add(R.drawable.u6);
        images.add(R.drawable.u7);
        images.add(R.drawable.u8a);
        images.add(R.drawable.u8b);
        images.add(R.drawable.u9);
        images.add(R.drawable.u10);
        images.add(R.drawable.u11);
        images.add(R.drawable.u12);
        images.add(R.drawable.u13);
        images.add(R.drawable.u14);
        images.add(R.drawable.u15);
        images.add(R.drawable.u16);
        images.add(R.drawable.u17);
        images.add(R.drawable.u17b);
        images.add(R.drawable.u18);
        images.add(R.drawable.u19);
        images.add(R.drawable.u20);
        images.add(R.drawable.u21);
        images.add(R.drawable.u22);
        images.add(R.drawable.u23);
        images.add(R.drawable.u24);
        images.add(R.drawable.u25);
        images.add(R.drawable.u26);
        images.add(R.drawable.u27);
        images.add(R.drawable.u28);
        images.add(R.drawable.u29);
        images.add(R.drawable.u30);
        images.add(R.drawable.u31);
        images.add(R.drawable.u32);
        images.add(R.drawable.u33a);
        images.add(R.drawable.u33b);
        images.add(R.drawable.u34);
        images.add(R.drawable.u35);
        images.add(R.drawable.u36);
        images.add(R.drawable.u37);
        images.add(R.drawable.u38);
        images.add(R.drawable.u39);
        images.add(R.drawable.u40);
        images.add(R.drawable.u41);





        recyclerView = findViewById(R.id.recyclerview);
        customAdapter = new customAdapter(this,images);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customAdapter);



    }
}
