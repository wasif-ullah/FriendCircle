package com.example.wasif.friendcircle;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {
    private String username;
    private String latitude;
    private String longitude;
    private List<String> friends;
    private List<String> mutualPoint;
    private String number;
    private List<String> requests;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String number, Location location) {
        this.username = number;
        this.latitude = String.valueOf(location.getLatitude());
        this.longitude = String.valueOf(location.getLongitude());
    }

//    public User(String username, String number, Location location) {
//        this.username = username;
//        this.number = number;
//        this.latitude = String.valueOf(location.getLatitude());
//        this.longitude = String.valueOf(location.getLongitude());
//    }

    public User(String username, String number, Location location, List<String> friends,List<String> mutualPoint) {
        this.username = username;
        this.number = number;
        this.latitude = String.valueOf(location.getLatitude());
        this.longitude = String.valueOf(location.getLongitude());
        this.friends = friends;
        this.mutualPoint=mutualPoint;
    }

    public List<String> getMutualPoint() {
        return mutualPoint;
    }

    public void setMutualPoint(List<String> mutualPoint) {
        this.mutualPoint = mutualPoint;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", friends=" + friends +
                ", mutualPoint=" + mutualPoint +
                ", number='" + number + '\'' +
                '}';
    }

    /* @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", latitude='" + latitude + '\'' +
                ", friends=" + friends +
                ", longitude='" + longitude + '\'' +
                ", number='" + number + '\'' +
                '}';
    }*/

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
