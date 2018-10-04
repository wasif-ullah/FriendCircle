package com.example.wasif.friendcircle;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wasif.friendcircle.MyService.KEY_FRIEND_LOCATION;
import static com.example.wasif.friendcircle.MyService.KEY_FRIEND_NAME;
import static com.example.wasif.friendcircle.MyService.KEY_MY_FRIEND_NAME;
import static com.example.wasif.friendcircle.MyService.KEY_MY_FRIEND_PHONE_NUMBER;
import static com.example.wasif.friendcircle.MyService.KEY_MY_LOCATION;
import static com.example.wasif.friendcircle.MyService.KEY_MY_NAME;
import static com.example.wasif.friendcircle.MyService.KEY_MY_PHONE_NUMBER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {


    private static final String TAG = MapsActivity.class.getName();
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 123;
    private final MarkerOptions myLocationMarkerOption = new MarkerOptions();
    private final MarkerOptions myFriendLocationMarkerOption = new MarkerOptions();
    Marker myFriendLocationMarker;
    Marker myLocationMarker;
    ArrayList<LatLng> MarkerPoints;
    private GoogleMap googleMap;
    private Button test;
    private Button get;
    private List<String> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    private int i = 0;
    private LatLng currentLatLng;
    private LatLng friendLatLng;
    private Button mutualPoint;
    private ImageView locationPin;
    private LatLng mainLatLng;
    private int color = Color.RED;
    private DatabaseReference mDatabase;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // received intent sent from the services
            // and update the ui
            Log.i(TAG, "Received location data from service");
            updateUI(intent);
        }
    };
    private ArrayList<Object> mutualList;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;

    private GoogleMap mMap;
    private Circle circle;
    private double Latitude;
    private double Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getPermissions();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog enableGPSDialog = new AlertDialog.Builder(this)
                    .setTitle("Enable GPS")
                    .setMessage("Your GPS in not enabled")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enableGPS();
                        }
                    }).show();
//            drawCircle(new LatLng(Latitude, Longitude));

        }


        Log.i("TAG", "oncreate");
        img1 = findViewById(R.id.im_1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showShareLocationDialog();
            }
        });

        img2 = findViewById(R.id.im_2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSharingLocation();
            }
        });
        img3 = findViewById(R.id.im_3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFriendLocationDialog();
            }
        });
        img4 = findViewById(R.id.im_4);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMutualPoint();
            }
        });
//        img5 =findViewById(R.id.im_5);
//        img5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                circle = googleMap.addCircle(new CircleOptions()
//                        .center(currentLatLng)
//                        .radius(200)
//                        .strokeWidth(4)
//                        .strokeColor(Color.GREEN)
//                        .fillColor(Color.argb(128, 255, 0, 0))
//                        .clickable(true));

//            }
//        });
       /* List<String> list = Repository.FIREBASE.getMutualPoint();
        if (list!=null) {
            double lat = Double.parseDouble(list.get(0));
            double lng = Double.parseDouble(list.get(1));
            mainLatLng = new LatLng(lat, lng);
            Log.i("TAG", "mutual point selected");
        }else{
            Log.i("TAG", "mutual point not selected");
        }*/


    }


    private void enableGPS() {
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    private void getPermissions() {
        if (Permissions.hasLocationPermissions(this)) {
            //startMapsActivity();
        } else {
            Permissions.getMultiplePermissions(this, REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void updateUI(Intent intent) {

        Log.i("TAG", "Update UI");

        String myName = intent.getStringExtra(KEY_MY_NAME);
        Location myLocation = intent.getParcelableExtra(KEY_MY_LOCATION);
        String friendName = intent.getStringExtra(KEY_FRIEND_NAME);
        Location friendLocation = intent.getParcelableExtra(KEY_FRIEND_LOCATION);
        if (myLocation != null) {
            // TODO set my location on the map
            LatLng locationLatLng = new LatLng(
                    myLocation.getLatitude(), myLocation.getLongitude()
            );
            if (myLocationMarker == null) {
                myLocationMarkerOption.position(locationLatLng);
                currentLatLng = locationLatLng;

                mDatabase.child(Repository.ROOT).child(Repository.KEY_MUTUAL).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.i("TAG", "datasnapshot" + dataSnapshot);

                        com.example.wasif.friendcircle.Location location = dataSnapshot.getValue(
                                com.example.wasif.friendcircle.Location.class
                        );
                        if (location != null) {
                            Log.i("TAG", "latitude" + location.getLatitude());
                            Log.i("TAG", "getLongitude" + location.getLongitude());
                            mainLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //   Log.i("TAG","mylocation lat long"+mainLatLng);
                if (mainLatLng != null) {
                    googleMap.addMarker(new MarkerOptions().position(mainLatLng).title("Mutual point in my location"));
                } else {
                    Log.i("TAG", "mainlatlng null in my location");
                }
                myLocationMarker = googleMap.addMarker(myLocationMarkerOption);

                /***********************************************/
                Log.i("TAG", "draw line in my location");
                if (mainLatLng != null) {
                    color = Color.GREEN;
                    String url = getUrl(currentLatLng, mainLatLng);
                    //   Log.d("onMapClick", url);
                    FetchUrl FetchUrl = new FetchUrl(); // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                } else {
                    Toast.makeText(this, "my latlng is null", Toast.LENGTH_SHORT).show();
                }
                /**************************************************/

                //TODO mylocation marker latlong
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 16.0f));
            } else {
                myLocationMarker.setPosition(locationLatLng);
            }
        }
        if (myName != null) {
            // TODO set friend name on the map
            if (myLocationMarker != null)
                myLocationMarker.setTitle(myName);
        }
        if (friendName != null) {
            // TODO set friend name on the map
            if (myFriendLocationMarker != null)
                myFriendLocationMarker.setTitle(friendName);
        }
        if (friendLocation != null) {
            // TODO set friend location on the map
            LatLng locationLatLng = new LatLng(
                    friendLocation.getLatitude(), friendLocation.getLongitude()
            );
            if (myFriendLocationMarker == null) {
                //TODO friend location marker on map
                friendLatLng = locationLatLng;

                /**************draw line start code ************/
                Log.i("TAG", "drawline in my friend location");

                if (mainLatLng != null) {
                    googleMap.addMarker(new MarkerOptions().position(mainLatLng).title("Mutual point in my location"));
                } else {
                    Log.i("TAG", "mainlatlng null in frnd locaion");
                }
                if (mainLatLng != null) {
                    color = Color.GRAY;
                    String url = getUrl(friendLatLng, mainLatLng);
                    googleMap.addMarker(new MarkerOptions().position(mainLatLng).title("Friend Mutual point"));
                    FetchUrl FetchUrl = new FetchUrl();
                    FetchUrl.execute(url);
                } else {
                    Toast.makeText(this, "friend latlng is null", Toast.LENGTH_SHORT).show();
                }

                /**************draw line end code************/

                if (mainLatLng != null)
                    googleMap.addMarker(new MarkerOptions().position(mainLatLng).title("Mutual point in my friend location"));
                myFriendLocationMarkerOption.position(locationLatLng);
                myFriendLocationMarker = googleMap.addMarker(myLocationMarkerOption);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 16.0f));
            } else {
                myFriendLocationMarker.setPosition(locationLatLng);
            }
        }


        //  LatLng dest = latLng;

       /* Toast.makeText(this, "draw line", Toast.LENGTH_SHORT).show();
        // Getting URL to the Google Directions API
        if(friendLocation!=null &&currentLatLng!=null){
            String url = getUrl(friendLatLng, currentLatLng);
            Log.d("onMapClick", url);
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        }else{
            Toast.makeText(this, "value null", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share_my_location:
                showShareLocationDialog();
                return true;
            case R.id.stop_sharing_my_location:
                stopSharingLocation();
                return true;
            case R.id.get_friend_location:
                showFriendLocationDialog();
                return true;
            case R.id.set_mutual_point:
                setMutualPoint();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMutualPoint() {
        Toast.makeText(this, "Long click on map to select mutual point", Toast.LENGTH_SHORT).show();
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                //  googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Mutual Position"));
                Repository.FIREBASE.setMutualPoint(latLng);

/*
                mDatabase.child(Repository.ROOT).child(Repository.KEY_MUTUAL).addChildEventListener(new ChildEventListener() {
                    public double lat,lng;

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        mutualList=new ArrayList<>();

                        if(s==null){
                             lat= (double) dataSnapshot.getValue();
                        }else if(Objects.equals(s, KEY_LATITUDE)){
                            lng = (double) dataSnapshot.getValue();
                        }

                        if(lat!=0){
                            Log.i("TAG","lat"+lat);
                            mutualList.add(0,lat);

                        }else{
                            Log.i("TAG","else lat is null");
                        }
                        if(lng!=0){

                            Log.i("TAG","lng"+lng);
                            mutualList.add(1,lng);
                        }else{
                            Log.i("TAG","else lng is null");
                        }

                        Log.i("TAG","final list"+mutualList);
                        Log.i("TAG","final list size"+mutualList.size());


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.i("TAG","onChildChanged"+dataSnapshot.getValue());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                        Log.i("TAG","onChildRemoved");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.i("TAG","onChildMoved");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("TAG","onCancelled");

                    }

                });
*/



               /*  List<String> list = Repository.FIREBASE.getMutualPoint();
                if (list!=null) {
                    double lat = Double.parseDouble(list.get(0));
                    double lng = Double.parseDouble(list.get(1));
                    mainLatLng = new LatLng(lat, lng);
                    Log.i("TAG", "mutual point selected :" + list.size());
                }*/

                Toast.makeText(MapsActivity.this, "Destination Selected", Toast.LENGTH_SHORT).show();

                /*************************************************/
              /*  if (currentLatLng != null) {
                    Log.i("TAG", "draw line in my location");
                    color = Color.GREEN;
                    String url = getUrl(currentLatLng, latLng);
                    Log.d("onMapClick", url);
                    FetchUrl FetchUrl = new FetchUrl(); // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                }*/
                /****************************************************/


            }
        });
    }

    private void showFriendLocationDialog() {
        // TODO show friend id input dialog
        final FriendLocationInputDialog dialog = new FriendLocationInputDialog(this);
        dialog.show();
        dialog.setButtonStartSharingOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Now start location service
                Intent intent = new Intent(MapsActivity.this, MyService.class);
                intent.putExtra(KEY_MY_FRIEND_NAME, dialog.getName());
                intent.putExtra(KEY_MY_FRIEND_PHONE_NUMBER, dialog.getPhoneNumber());
                startService(intent);
                dialog.dismiss();

                Repository.FIREBASE.sendLocationRequest(dialog.getPhoneNumber());

            }
        });
    }

    private void stopSharingLocation() {
        // TODO stop sharing location
        stopService(new Intent(this, MyService.class));
    }

    private void showShareLocationDialog() {
        // TODO setup sharing location
        final MyLocationInputDialog dialog = new MyLocationInputDialog(this);
        dialog.show();
        dialog.setButtonStartSharingOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Now start location service
                Intent intent = new Intent(MapsActivity.this, MyService.class);
                intent.putExtra(KEY_MY_NAME, dialog.getName());
                intent.putExtra(KEY_MY_PHONE_NUMBER, dialog.getPhoneNumber());
                startService(intent);
                dialog.dismiss();

                Repository.FIREBASE.getLocationRequests(dialog.getPhoneNumber(), new Repository.RequestListLoadedListener() {
                    @Override
                    public void onRequestListLoaded(final List<String> requestList) {

                        Toast.makeText(MapsActivity.this, "Request", Toast.LENGTH_SHORT).show();
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(MapsActivity.this);
                        }
                        builder.setTitle("Accept Request")
                                .setMessage("Accept requests from your friends")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Confirm all requests
                                        Repository.FIREBASE.confirmRequest(requestList);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        Repository.FIREBASE.deleteRequests();
                                        //  Repository.FIREBASE.deleteFriends();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "onResume");
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.BROADCAST_ACTION_LOCATION_UPDATE));
    }

    @Override
        public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupGoogleMap();

//        googleMap.setOnMyLocationChangeListener(this);

//        CircleOptions circleOptions = new CircleOptions()
//                .center(new LatLng(37.4, -122.1))
//                .radius(1000); // In meters
//
//// Get back the mutable Circle
//        Circle circle = googleMap.addCircle(circleOptions);

//        circle = googleMap.addCircle(new CircleOptions()
//                .center(new LatLng(33.49020666666667, 73.09904166666666))
//                .radius(30)
//                .strokeWidth(10)
//                .strokeColor(Color.GREEN)
//                .fillColor(Color.argb(128, 255, 0, 0))
//                .clickable(true));


        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {

                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });

       /* try {
            Log.i("TAG","try");
            List<String> latLng= Repository.FIREBASE.getMutualPoint();
            double lat= Double.parseDouble(latLng.get(0));
            double lng= Double.parseDouble(latLng.get(1));
            LatLng mutaulPointLatLng=new LatLng(lat,lng);
        }catch (Exception ignored){}*/

    }

    private void setupGoogleMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        UiSettings mapSettings = googleMap.getUiSettings();
        mapSettings.setMyLocationButtonEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setAllGesturesEnabled(true);
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setZoomGesturesEnabled(true);
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMyLocationChange(Location location) {
//        googleMap.clear();
//
//        Toast.makeText(this, "location changed", Toast.LENGTH_SHORT).show();
//        circle = googleMap.addCircle(new CircleOptions()
//                .center(new LatLng(location.getLatitude(),location.getLongitude()))
//                .radius(200)
//                .strokeWidth(4)
//                .strokeColor(Color.GREEN)
//                .fillColor(Color.argb(128, 255, 0, 0))
//                .clickable(true));
//
}

    class MyLocationInputDialog extends Dialog {

        private Activity activity;
        private Button buttonStartSharing;
        private EditText editTextName;
        private EditText editTextPhoneNumber;

        MyLocationInputDialog(Activity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.my_location_input_dialog);

            buttonStartSharing = findViewById(R.id.button_start_sharing);
            editTextName = findViewById(R.id.edit_text_name);
            editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        }

        void setButtonStartSharingOnClickListener(View.OnClickListener listener) {
            if (listener != null)
                if (buttonStartSharing != null)
                    buttonStartSharing.setOnClickListener(listener);
        }

        String getName() {
            return editTextName.getText().toString();
        }

        String getPhoneNumber() {
            return editTextPhoneNumber.getText().toString();
        }
    }

    class FriendLocationInputDialog extends Dialog {

        private Activity activity;
        private Button buttonStartSharing;
        private EditText editTextName;
        private EditText editTextPhoneNumber;

        FriendLocationInputDialog(Activity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.friend_location_input_dialog);

            buttonStartSharing = findViewById(R.id.button_start_sharing);
            editTextName = findViewById(R.id.edit_text_name);
            editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        }

        void setButtonStartSharingOnClickListener(View.OnClickListener listener) {
            if (listener != null)
                if (buttonStartSharing != null)
                    buttonStartSharing.setOnClickListener(listener);
        }

        String getName() {
            return editTextName.getText().toString();
        }

        String getPhoneNumber() {
            return editTextPhoneNumber.getText().toString();
        }
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = ProgressDialog.show(MapsActivity.this,
                    "Loading...", "Please Wait", true, false);
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    public class DataParser {

        /**
         * Receives a JSONObject and returns a list of lists containing latitude and longitude
         */
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude));
                                hm.put("lng", Double.toString((list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }


            return routes;
        }


        /**
         * Method to decode polyline points
         * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(color);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

//    private void drawCircle(LatLng point){
//
//        // Instantiating CircleOptions to draw a circle around the marker
//        CircleOptions circleOptions = new CircleOptions();
//
//        // Specifying the center of the circle
//        circleOptions.center(point);
//
//        // Radius of the circle
//        circleOptions.radius(20);
//
//        // Border color of the circle
//        circleOptions.strokeColor(Color.BLACK);
//
//        // Fill color of the circle
//        circleOptions.fillColor(0x30ff0000);
//
//        // Border width of the circle
//        circleOptions.strokeWidth(2);
//
//        // Adding the circle to the GoogleMap
//        googleMap.addCircle(circleOptions);
//
//    }



//    private void addCircle(LatLng latLng, double radius)
//    {
//        double R = 6371d; // earth's mean radius in km
//        double d = radius/R; //radius given in km
//        double lat1 = Math.toRadians(latLng.latitude);
//        double lon1 = Math.toRadians(latLng.longitude);
//        PolylineOptions options = new PolylineOptions();
//        for (int x = 0; x <= 360; x++)
//        {
//            double brng = Math.toRadians(x);
//            double latitudeRad = Math.asin(Math.sin(lat1)*Math.cos(d) + Math.cos(lat1)*Math.sin(d)*Math.cos(brng));
//            double longitudeRad = (lon1 + Math.atan2(Math.sin(brng)*Math.sin(d)*Math.cos(lat1), Math.cos(d)-Math.sin(lat1)*Math.sin(latitudeRad)));
//            options.add(new LatLng(Math.toDegrees(latitudeRad), Math.toDegrees(longitudeRad)));
//        }
//        mMap.addPolyline(options.color(Color.BLACK).width(2));
//}

//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
////            location.getLatitude();
////            location.getLongitude();
////            circle = googleMap.addCircle(new CircleOptions()
////                    .center(new LatLng(location.getLatitude(),location.getLongitude()))
////                    .radius(30)
////                    .strokeWidth(10)
////                    .strokeColor(Color.GREEN)
////                    .fillColor(Color.argb(128, 255, 0, 0))
////                    .clickable(true));
//            //your code here
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };

}

