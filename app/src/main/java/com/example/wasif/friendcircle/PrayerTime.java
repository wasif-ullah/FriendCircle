package com.example.wasif.friendcircle;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class PrayerTime extends AppCompatActivity {


    private static final String TAG = "tag";
    //url
    String url;
    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";

    //ProgressDailog
    ProgressDialog pDialog;

    TextView mFajrTv ,mDhuhrTv ,mAsrTv ,mMaghribTv ,mIshaTv ,mLocationTv ,mDateTv, mTempTv, mPressureTv;

    EditText mSearchEt;
    Button mSearchBtn;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_time);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Prayer's Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mFajrTv = findViewById(R.id.fajrtv);
        mDhuhrTv = findViewById(R.id.dhuhrtv);
        mAsrTv = findViewById(R.id.asrtv);
        mMaghribTv = findViewById(R.id.magribtv);
        mIshaTv = findViewById(R.id.ishatv);
        mLocationTv = findViewById(R.id.locationtv);
        mDateTv = findViewById(R.id.datetv);
//        mTempTv = findViewById(R.id.Temptv);
//        mPressureTv = findViewById(R.id.Pressuretv);
        mSearchEt = findViewById(R.id.searchet);
        mSearchBtn = findViewById(R.id.searchbtn);


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get from edit text
                String mLocation = mSearchEt.getText().toString().trim();

                if (mLocation.isEmpty()) {
                    Toast.makeText(PrayerTime.this, "please enter location", Toast.LENGTH_SHORT).show();
                } else {

                    url = "http://muslimsalat.com/" + mLocation + ".json?key=a2053d18150e2d59771e2df64ca2e7e9";
                    searchLocation();
                }
            }
        });

    }




    private void searchLocation() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //get data from json



                        try {

                            //get location

                            String country= response.get("country").toString();
                            String state= response.get("state").toString();
                            String city= response.get("city").toString();
                            String location= country+","+state+","+city;

                            //get date
                            String date    = response.getJSONArray( "items").getJSONObject(0).get("date_for").toString();

                            //weather
//                            String pressure=response.get("pressure").toString();
//                            String temperature=response.get("temperature").toString();
//                            String weather=pressure+","+temperature;


//                            String weather    = response.getJSONArray( "today_weather").getJSONObject(0).get("pressure , temperature").toString();

                            // get namaz time


                            String mFajr    = response.getJSONArray( "items").getJSONObject(0).get("fajr").toString();
                            String mDhuhr   = response.getJSONArray( "items").getJSONObject(0).get("dhuhr").toString();
                            String mAsr     = response.getJSONArray( "items").getJSONObject(0).get("asr").toString();
                            String mMaghrib = response.getJSONArray( "items").getJSONObject(0).get("maghrib").toString();
                            String mIsha    = response.getJSONArray( "items").getJSONObject(0).get("isha").toString();
//                            String mTemp    = response.getJSONArray( "today_weather").getJSONObject(0).get("temperature").toString();
//                            String mPressure    = response.getJSONArray( "today_weather").getJSONObject(0).get("pressure").toString();

                            mFajrTv.setText(mFajr);
                            mDhuhrTv.setText(mDhuhr);
                            mAsrTv.setText(mAsr);
                            mMaghribTv.setText(mMaghrib);
                            mIshaTv.setText(mIsha);
                            mLocationTv.setText(location);
                            mDateTv.setText(date);
//                            mTempTv.setText(mTemp);
//                            mPressureTv.setText(mPressure);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pDialog.hide();
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(PrayerTime.this,"Turn On Internet",Toast.LENGTH_SHORT).show();

                // hide the progress dialog
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
