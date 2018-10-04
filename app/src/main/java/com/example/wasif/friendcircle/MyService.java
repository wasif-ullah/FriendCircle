package com.example.wasif.friendcircle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service implements Repository.FriendLocationListener {
    public static final String KEY_MY_LOCATION = "KEY_MY_LOCATION";
    public static final String KEY_FRIEND_LOCATION = "KEY_FRIEND_LOCATION";
    public static final String KEY_MY_FRIEND_NAME = "KEY_MY_FRIEND_NAME";
    public static final String KEY_MY_FRIEND_PHONE_NUMBER = "KEY_MY_FRIEND_PHONE_NUMBER";
    public static final String KEY_MY_NAME = "KEY_MY_NAME";
    public static final String KEY_FRIEND_NAME = "KEY_FRIEND_NAME";
    public static final String KEY_MY_PHONE_NUMBER = "KEY_MY_PHONE_NUMBER";

    // for updating activity
    public static final String BROADCAST_ACTION_LOCATION_UPDATE = "com.androodtech.locationsharemodule.MyService";

    private static final String TAG = MyService.class.getName();
    private static final int LOCATION_INTERVAL = 6000;
    private static final float LOCATION_DISTANCE = 2.0f;

    private static String myName = "";
    private static String myPhoneNumber = "";
    private static String myFriendName = "";
    private static String myFriendPhoneNumber = "";

    static {
        Repository.FIREBASE.init();
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private Intent intent;
    private LocationManager mLocationManager = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String myName = intent.getStringExtra(KEY_MY_NAME);
        String myPhoneNumber = intent.getStringExtra(KEY_MY_PHONE_NUMBER);

        if (myName != null && myPhoneNumber != null) {
            MyService.myName = myName;
            MyService.myPhoneNumber = myPhoneNumber;
            Repository.FIREBASE.setMyInfo(MyService.myName, MyService.myPhoneNumber);
        }

        String friendName = intent.getStringExtra(KEY_MY_FRIEND_NAME);
        String friendPhoneNumber = intent.getStringExtra(KEY_MY_FRIEND_PHONE_NUMBER);
        if (friendName != null && friendPhoneNumber != null) {
            //Repository.FIREBASE.deleteFriends();
            MyService.myFriendName = friendName;
            MyService.myFriendPhoneNumber = friendPhoneNumber;
            Repository.FIREBASE.setMyFriendInfo(MyService.myFriendName, MyService.myFriendPhoneNumber);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        intent = new Intent(BROADCAST_ACTION_LOCATION_UPDATE);

        initializeLocationManager();
        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }*/

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    mLocationManager.removeUpdates(mLocationListener);
                    Repository.FIREBASE.deleteMyInfo();
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void updateMyLocation(Location location, String myName) {
        intent.putExtra(KEY_MY_LOCATION, location);
        intent.putExtra(KEY_MY_NAME, myName);
        Repository.FIREBASE.setMyLocation(location);
        sendBroadcast(intent);
    }

    @Override
    public void onFriendLocationReceived(Location friendLocation, String friendName) {
        intent.putExtra(KEY_FRIEND_LOCATION, friendLocation);
        intent.putExtra(KEY_FRIEND_NAME, friendName);
        sendBroadcast(intent);

    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            Repository.FIREBASE.getFriendLocation(MyService.this);
            updateMyLocation(location, myName);

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
}
