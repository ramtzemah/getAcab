package com.example.getacab;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class LocationNow {
    private String latitude;
    private String longitude;
    private Activity activity;

//    public LocationNow() {
//    }
//
    public LocationNow() {
        this.latitude = "32.2857056";
        this.longitude = "34.8441235";
    }

    public LocationNow(LocationNow locationNow) {
        this.latitude = locationNow.getLatitude();
        this.longitude = locationNow.getLongitude();
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation(){
        LocationInCoordinate.CallBack_Loc callBack_loc = new LocationInCoordinate.CallBack_Loc() {
            @Override
            public void dataReady(String lag, String log) {
                latitude = lag;
                longitude = log;
            }
        };
        LocationInCoordinate locationInCoordinate =  new LocationInCoordinate();
        locationInCoordinate.getLastLocation(callBack_loc);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
