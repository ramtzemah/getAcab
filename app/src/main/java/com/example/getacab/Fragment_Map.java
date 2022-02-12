package com.example.getacab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Fragment_Map extends Fragment {

    private AppCompatActivity activity;
    private CallBack_Map callBack_map;
    private double lan;
    private double lon;
    private static GoogleMap mMap;
    private static SupportMapFragment supportMapFragment;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBack_map(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);


        return view;
    }

    public static void onMPP(MapReady mapReady){
        LatLng latLng1 = new LatLng(-34, 151);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng1);
                markerOptions.title(latLng1.latitude+ " : "+latLng1.longitude);
                //googleMap.clear();
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        latLng1,10
//                ));
                googleMap.addMarker(markerOptions);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng1);
                        markerOptions.title("ME");
                        //googleMap.clear();
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                latLng1,10
//                        ));
                        googleMap.addMarker(markerOptions);
                        if(mapReady!=null){
                            mapReady.mapIsReady();
                        }
                    }
                });

            }
        });
    }

    public void setOnMap(String lat, String lot)  {
        lan = Double.parseDouble(lat);
        lon = Double.parseDouble(lot);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(lan,lon);
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.businessman));
        mMap.addMarker(markerOptions);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                latLng,10
//        ));
    }

    public void setOnMapCabs(ArrayList<Driver> cabs) {
//        mMap.clear();
        ArrayList<MarkerOptions> markerOptionscabs = new ArrayList<>(cabs.size());
        for (int i = 0; i < cabs.size() ; i++) {
            LatLng latLng = new LatLng(Double.parseDouble(cabs.get(i).getLocationNow().getLatitude()),Double.parseDouble(cabs.get(i).getLocationNow().getLongitude()));
            markerOptionscabs.add(new MarkerOptions());
            markerOptionscabs.get(i).position(latLng);
            markerOptionscabs.get(i).title(cabs.get(i).getName());
            if (cabs.get(i).isAvailable()) {
                markerOptionscabs.get(i).icon(BitmapDescriptorFactory.fromResource(R.drawable.cab));
            }else{
                if(cabs.get(i).isTakeInvite()){
                    markerOptionscabs.get(i).icon(BitmapDescriptorFactory.fromResource(R.drawable.catch_by_me));
                }else{
                    markerOptionscabs.get(i).icon(BitmapDescriptorFactory.fromResource(R.drawable.catch_taxi));
                }
            }
            mMap.addMarker(markerOptionscabs.get(i));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    latLng,10
//            ));
        }
    }


    public void setOnMapPass(ArrayList<Passenger> pass) {
//        mMap.clear();
        ArrayList<MarkerOptions> markerOptionspass = new ArrayList<>(pass.size());
        for (int i = 0; i < pass.size() ; i++) {
            LatLng latLng = new LatLng(Double.parseDouble(pass.get(i).getLocationNow().getLatitude()),Double.parseDouble(pass.get(i).getLocationNow().getLongitude()));
            markerOptionspass.add(new MarkerOptions());
            markerOptionspass.get(i).position(latLng);
            markerOptionspass.get(i).title(pass.get(i).getName());
            markerOptionspass.get(i).icon(BitmapDescriptorFactory.fromResource(R.drawable.businessman));
            mMap.addMarker(markerOptionspass.get(i));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    latLng,10
//            ));
        }
    }

    public void myPosition(String latitude, String longitude) {
        LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng,16
        ));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.businessman));
        mMap.addMarker(markerOptions);
    }

    public void myPositionCab(String latitude, String longitude) {
        LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng,16
        ));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cab));
        mMap.addMarker(markerOptions);
    }

    public void setOnMapcab(Driver cab) {
//        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(Double.parseDouble(cab.getLocationNow().getLatitude()),Double.parseDouble(cab.getLocationNow().getLongitude()));
            markerOptions.position(latLng);
            markerOptions.title(cab.getName());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.catch_by_me));
            mMap.addMarker(markerOptions);

    }

    public void mapClear() {
        mMap.clear();
    }
}