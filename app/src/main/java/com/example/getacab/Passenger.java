package com.example.getacab;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Passenger {
 //   private static int NextuIdPass = 0;
    private String myuIdPass;
//    private static int uIdPass = 0;
    private String name;
    private boolean searchForCab = true;
    private LocationNow locationNow;
    private String phoneNumber;
    private String dId;
    private boolean picked =false;

    public Passenger() {
    }

    public Passenger(String name, String phoneNumber) {
        this.name = name;
        locationNow = new LocationNow();
        phoneNumber = "0509219009";
        this.phoneNumber=phoneNumber;
        myuIdPass = phoneNumber;
        dId = "-1";
    }

//    public Passenger(String name, String phoneNumber) {
//        this.name = name;
//        locationNow = new LocationNow();
//        this.phoneNumber=phoneNumber;
//        //this.phoneNumber = "0509219009";
//        myuIdPass = id;
//        dId = "-1";
//    }

    public Passenger(Passenger p) {
        this.name = p.name;
        this.phoneNumber = p.phoneNumber;
        //this.phoneNumber = "0509219009";
        this.searchForCab = p.searchForCab;
        this.myuIdPass = p.myuIdPass;
        this.locationNow = new LocationNow(p.locationNow);
        this.dId = p.dId;
        this.picked = p.picked;
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
        this.myuIdPass = myuIdPass;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
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
