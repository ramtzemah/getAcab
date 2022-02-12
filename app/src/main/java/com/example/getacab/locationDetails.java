package com.example.getacab;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class locationDetails {
    private Geocoder geocoder;
    private List<Address> addresses;
    private String city;
    private String street;

    public locationDetails(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public void resetData(double latitude, double longitude) throws IOException {
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        street = addresses.get(0).getThoroughfare(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }
}
