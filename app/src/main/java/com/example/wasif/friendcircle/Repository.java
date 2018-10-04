package com.example.wasif.friendcircle;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public enum Repository {
    FIREBASE;
    public static final String FRIENDS = "friends";
    @SuppressWarnings("unused")
    private static final String TAG = Repository.class.getName();
    public static final String ROOT = "users";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_REQUEST = "requests";
    public static final String KEY_MUTUAL = "mutual";
    private static String myName;
    private static String myPhoneNumber;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private static String myFriendName;
    private static String myFriendPhoneNumber;
    private DatabaseReference mDatabase;
    private ArrayList<String> list;

    Repository() {
        init();
    }

    public void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getFriendLocation(final FriendLocationListener listener) {
        Log.i("TAG", "my friend number : " + myFriendPhoneNumber);
        if (myFriendPhoneNumber != null)
            mDatabase.child(ROOT).child(myFriendPhoneNumber).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            updateMyFriendLocation(dataSnapshot, listener);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
    }

    private void updateMyFriendLocation(DataSnapshot dataSnapshot, FriendLocationListener listener) {
        String latitude = (String) dataSnapshot.child(KEY_LATITUDE).getValue();
        String longitude = (String) dataSnapshot.child(KEY_LONGITUDE).getValue();
        String username = (String) dataSnapshot.child(KEY_USER_NAME).getValue();
        String mLatitude = (String) dataSnapshot.child(KEY_MUTUAL).child(KEY_LATITUDE).getValue();
        String mLongitude = (String) dataSnapshot.child(KEY_MUTUAL).child(KEY_LONGITUDE).getValue();
        // update location

        if (latitude != null && longitude != null && username != null) {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
            listener.onFriendLocationReceived(location, username);
        }
    }

    public void setMyInfo(String name, String phoneNumber) {
        myName = name;
        myPhoneNumber = phoneNumber;
    }

    public void setMyFriendInfo(String nameFriend, String phoneNumberFriend) {
        myFriendName = nameFriend;
        myFriendPhoneNumber = phoneNumberFriend;
    }

    public void sendLocationRequest(String phoneNumber) {
        mDatabase.child(ROOT).child(KEY_REQUEST).child(phoneNumber).push().setValue(myPhoneNumber);
    }

    public void getLocationRequests(String myPhoneNumber, final RequestListLoadedListener listener) {

        mDatabase.child(ROOT).child(KEY_REQUEST).child(myPhoneNumber).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get a list of requests from friends...
                        List<String> requestList = new ArrayList<>();// ha g 1 r list
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.i(TAG, "Got request from : " + snapshot.getValue());
                            requestList.add(String.valueOf(snapshot.getValue()));
                        }
                        if (requestList.size() > 0)
                            listener.onRequestListLoaded(requestList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public void setMyLocation(Location location) {
        // TODO update location in database
        List<String> friends = new ArrayList<>();
        friends.add(myFriendPhoneNumber);
        User user = new User(myName, myPhoneNumber, location, friends,list);
//        User user = new User(myName, myPhoneNumber, location);
        mDatabase.child(ROOT).child(user.getNumber()).setValue(user);
    }

    public void deleteMyInfo() {
        // TODO delete location from database
        mDatabase.child(ROOT).child(myName).removeValue();
    }

    public void confirmRequest(List<String> requestList) {
        deleteRequests();
        for (String requestNumber : requestList) {
            mDatabase.child(ROOT).child(myPhoneNumber).child(FRIENDS).child("0").setValue(requestNumber);
        }
    }

//    public void onRequestAcceptedListener(final OnNumberAddedListener listener) {
//        mDatabase.child(ROOT).child(myPhoneNumber).child(FRIENDS).addChildEventListener(
//                new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            listener.onFriendAdded(String.valueOf(dataSnapshot1.getValue()));
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            listener.onFriendAdded(String.valueOf(dataSnapshot1.getValue()));
//                        }
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                }
//        );
//    }

    public void deleteRequests() {
        mDatabase.child(ROOT).child(KEY_REQUEST).child(myPhoneNumber).removeValue();
    }

    public void deleteFriends() {
        mDatabase.child(ROOT).child(myPhoneNumber).child(FRIENDS).removeValue();
    }

    public void setMutualPoint(LatLng mutualPoint) {
        mDatabase.child(ROOT).child(KEY_MUTUAL).setValue(mutualPoint);
    }

    public List<String> getMutualPoint() {
        mDatabase.child(ROOT).child(KEY_MUTUAL).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list=new ArrayList<>();
                list.add(0, dataSnapshot.child(KEY_LATITUDE).getValue().toString());
                list.add(0, dataSnapshot.child(KEY_LONGITUDE).getValue().toString());

                /*for (DataSnapshot data :
                        dataSnapshot.getChildren()) {
                    if(data.getValue()!=null)
                   list.add(data.getValue().toString());
                }*/
             //   Log.i("TAG","list Repository :"+list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.i("TAG","list returned");
        return list;
    }

    public interface FriendLocationListener {
        void onFriendLocationReceived(Location friendLocation, String friendName);
    }

    public interface RequestListLoadedListener {
        void onRequestListLoaded(List<String> requestList);
    }

    public interface OnNumberAddedListener {
        void onFriendAdded(String number);
    }
}
