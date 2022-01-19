package com.example.getacab;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Passenger {
    private static int NextuIdPass = 0;
    private int myuIdPass;
//    private static int uIdPass = 0;
    public String name;
    public boolean searchForCab = true;
    public LocationNow locationNow;
    public String phoneNumber;
    public Driver D = null;

    public Passenger() {
    }

    public Passenger(String name, String phoneNumber) {
        this.name = name;
        locationNow = new LocationNow();
        this.phoneNumber=phoneNumber;
        myuIdPass = NextuIdPass++;
        // uIdPass=uIdPass+1;
    }

    public Passenger(Passenger p) {
        this.name = p.name;
        this.phoneNumber = p.phoneNumber;
        this.searchForCab = p.searchForCab;
        this.myuIdPass = p.myuIdPass;
        this.locationNow = new LocationNow(p.locationNow);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation (){
        locationNow.getLocation();
    }

    private void findDrive(){
        searchForCab = false;
    }

    private void wantDrive(){
        searchForCab = true;
    }

    public void addpass() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("passengers");
        myRef.child(phoneNumber).setValue(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchForCab() {
        return searchForCab;
    }

    public void setSearchForCab(boolean searchForCab) {
        this.searchForCab = searchForCab;
    }

    public LocationNow getLocationNow() {
        return locationNow;
    }

    public void setLocationNow(LocationNow locationNow) {
        this.locationNow = locationNow;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMyuIdPass() {
        return String.valueOf(myuIdPass);
    }

    public void setMyuIdPass(String myuIdPass) {
        this.myuIdPass = Integer.parseInt(myuIdPass);
    }

//    public Driver getD() {
//        return D;
//    }
//
//    public void setD(Driver d) {
//        D = d;
//    }

    //    public static int getuIdPass() {
//        return uIdPass;
//    }
//
//    public static void setuIdPass(int uIdPass) {
//        Passenger.uIdPass = uIdPass;
//    }
}
