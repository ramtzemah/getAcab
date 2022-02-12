package com.example.getacab;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Driver {
    private static int NextuIdCab = 1;
    private String myuIdCab;
    private String name;
    private  LocationNow locationNow;
    private boolean available = true;
    private String phoneNumber;
    private String pId;
    private boolean pick = false;
    private boolean takeInvite = false;

    public Driver() {
    }

    public Driver(String name,String phoneNumber) {
        this.name = name;
        phoneNumber = "0509219909";
        this.phoneNumber = phoneNumber;
        locationNow = new LocationNow();
        myuIdCab = phoneNumber;
        pId = "-1";
    }

//    public Driver(String name,String phoneNumber) {
//        this.name = name;
//        this.phoneNumber = phoneNumber;
////        locationNow = new LocationNow();
//        myuIdCab = phoneNumber;
//        pId = -1;
//    }

    public Driver(Driver d) {
        this.name = d.getName();
        this.phoneNumber = d.getPhoneNumber();
       // this.phoneNumber = "0509219009";
        this.available = d.isAvailable();
        this.myuIdCab = d.getMyuIdCab();
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
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
        return myuIdCab;
    }

    public void setMyuIdCab(String myuIdCab) {
        this.myuIdCab = myuIdCab;
    }

    public boolean isPick() {
        return pick;
    }

    public void setPick(boolean pick) {
        this.pick = pick;
    }

    public boolean isTakeInvite() {
        return takeInvite;
    }

    public void setTakeInvite(boolean takeInvite) {
        this.takeInvite = takeInvite;
    }
}