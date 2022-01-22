package com.example.getacab;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Driver {
    private static int NextuIdCab = 1;
    private int myuIdCab;
    private String name;
    private  LocationNow locationNow;
    private boolean available = true;
    private String phoneNumber;
    private int pId;

    public Driver() {
    }

    public Driver(String name,String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        locationNow = new LocationNow();
        myuIdCab = NextuIdCab++;
        pId = -1;
    }

    public Driver(Driver d) {
        this.name = d.name;
        this.phoneNumber = d.phoneNumber;
        this.available = d.available;
        this.myuIdCab = d.myuIdCab;
        this.locationNow = new LocationNow(d.locationNow);
        this.pId = d.pId;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation (){
        locationNow.getLocation();
    }

    private void takeDrive(){
        available = false;
    }

    private void wantNewDrive(){
        available = true;
    }

    private void addCab() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cabs");
        myRef.child(phoneNumber).setValue(this);
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    //    public static int getuIdCab() {
//        return uIdCab;
//    }
//
//    public static void setuIdCab(int uIdCab) {
//        Driver.uIdCab = uIdCab;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationNow getLocationNow() {
        return locationNow;
    }

    public void setLocationNow(LocationNow locationNow) {
        this.locationNow = locationNow;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMyuIdCab() {
        return String.valueOf(myuIdCab);
    }

    public void setMyuIdCab(String myuIdCab) {
        this.myuIdCab = Integer.parseInt(myuIdCab);
    }
}