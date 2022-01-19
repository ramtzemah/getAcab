package com.example.getacab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

public class ThePick extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TYPE = "TYPE";
    public static final String UID = "UID";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    TimerTask ts;
    int counter = 0;
    //public static final Object OBJ = "OBJ";
    private Fragment_Map fragmentMap;
    private ArrayList<Driver> cabs = new ArrayList<>();
    private ArrayList<Passenger> pass = new ArrayList<>();
    private String type = "";
    private String uID = "";
    private Driver d;
    private Passenger p;
    //  private MyReceiver myReceiver;
    private GPS_Service mService = null;
    private boolean mBound = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPS_Service.LocalBinder binder = (GPS_Service.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    private MaterialButton break_driver;
    private MaterialButton button_cancel_driver;
    private MaterialButton button_search;
    private MaterialButton button_cancel;
    private DatabaseReference myRefPass;
    private DatabaseReference myRefCab;
    private LottieAnimationView lottie_marker;

    private BroadcastReceiver fileBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(GPS_Service.EXTRA_LOCATION);
            if (location != null) {
                if (type.equals("driver")) {
                    if (d != null) {
                        d.locationNow.setLatitude(String.valueOf(location.getLatitude()));
                        d.locationNow.setLongitude(String.valueOf(location.getLongitude()));
                        myRefCab.child(d.getMyuIdCab()).setValue(d);
                    }
                } else {
                    if (p != null) {
                        p.locationNow.setLatitude(String.valueOf(location.getLatitude()));
                        p.locationNow.setLongitude(String.valueOf(location.getLongitude()));
                        myRefPass.child(p.getMyuIdPass()).setValue(p);
                    }
                }
//            Toast.makeText(context, Utils.getLocationText(location),
//                    Toast.LENGTH_SHORT).show();
            }
        }

        ;
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_page);
        findViews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefPass = database.getReference(ProfileActivity.PASSNEGERS);
        myRefCab = database.getReference(ProfileActivity.CABS);

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            type = extras.getString(TYPE);
            uID = extras.getString(UID);
        }

        fragmentMap = new Fragment_Map();
        fragmentMap.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();
        onStart();

        MyFirebaseDB.CallBack_passengers callBack_passengers = new MyFirebaseDB.CallBack_passengers() {
            @Override
            public void dataReady(ArrayList<Passenger> passengers) {
                pass = new ArrayList<>();
                for (int i = 0; i < passengers.size(); i++) {
                    pass.add(passengers.get(i));
                }
                if (type.equals("passenger")) {
                    findPassenger();
                }
                if (counter == 1) {
                    fragmentMap.setOnMapCabs(cabs);
                    fragmentMap.setOnMap(p.locationNow.getLatitude(), p.locationNow.getLongitude());
                }
            }
        };
        MyFirebaseDB.getAllpassengers(callBack_passengers);

        MyFirebaseDB.CallBack_Cabs callBack_cars = new MyFirebaseDB.CallBack_Cabs() {
            @Override
            public void dataReady(ArrayList<Driver> drivers) {
                cabs = new ArrayList<>();
                for (int i = 0; i < drivers.size(); i++) {
                    cabs.add(drivers.get(i));
                }
                if (type.equals("driver")) {
                    findDriver();
                }
                if (counter == 1) {
                    fragmentMap.setOnMapCabs(cabs);
                    fragmentMap.setOnMapPass(pass);
                }
            }
        };
        MyFirebaseDB.getAllCars(callBack_cars);

        MapReady mapReady = new MapReady() {
            @Override
            public void mapIsReady() {
                counter = 1;
                fragmentMap.setOnMapCabs(cabs);
                if (type.equals("driver")) {
                    fragmentMap.setOnMapPass(pass);
                    if (d != null)
                        fragmentMap.myPosition(d.locationNow.getLatitude(), d.locationNow.getLongitude());
                } else {
                    if (p != null) {
                        fragmentMap.setOnMap(p.locationNow.getLatitude(), p.locationNow.getLongitude());
                        fragmentMap.myPosition(p.locationNow.getLatitude(), p.locationNow.getLongitude());
                    }
                }
            }
        };
        fragmentMap.onMPP(mapReady);
        button_search.setOnClickListener(v -> {
          //  p.setD(minimumDistance());
            p.searchForCab=false;
        });
        button_cancel.setOnClickListener(v -> {
            p.setSearchForCab(true);
          //  p.setD(null);
        });
        break_driver.setOnClickListener(v -> {
            d.setAvailable(false);
        });
        button_cancel_driver.setOnClickListener(v -> {
            d.setAvailable(true);
        });

    }

    private Driver minimumDistance() {
        HashMap<Double,Driver>distaceForDriver =  new HashMap<Double, Driver>();
        ArrayList<Double>distance = new ArrayList<>();
        for (int i = 0; i < cabs.size(); i++) {
            if(cabs.get(i).isAvailable()==true) {
                Double dista = meterDistanceBetweenPoints(Float.parseFloat(p.getLocationNow().getLatitude()), Float.parseFloat(p.getLocationNow().getLongitude()), Float.parseFloat(d.getLocationNow().getLatitude()), Float.parseFloat(p.getLocationNow().getLongitude()));
                distance.add(dista);
                distaceForDriver.put(dista, cabs.get(i));
            }
        }
        Collections.sort(distance);
        return distaceForDriver.get(distance.get(0));
    }

    private double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f/Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    @SuppressLint("WrongViewCast")
    private void findViews() {
        break_driver = findViewById(R.id.break_driver);
        button_cancel_driver = findViewById(R.id.button_cancel_driver);
        button_search = findViewById(R.id.button_search);
        button_cancel = findViewById(R.id.button_cancel);
        lottie_marker = findViewById(R.id.lottie_marker);
        if (type.equals("driver")) {
            button_search.setVisibility(View.GONE);
            button_cancel.setVisibility(View.GONE);
        } else {
            break_driver.setVisibility(View.GONE);
            button_cancel_driver.setVisibility(View.GONE);
        }
    }


    private void findDriver() {
        for (int i = 0; i < cabs.size(); i++) {
            if (uID.equals(cabs.get(i).getMyuIdCab())) {
                d = cabs.get(i);
            }
        }
    }

    private void findPassenger() {
        for (int i = 0; i < pass.size(); i++) {
            if (uID.equals(pass.get(i).getMyuIdPass())) {
                p = pass.get(i);
            }
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //  fragmentMap.getAllowEnterTransitionOverlap();
//        fragmentMap.getActivity();
//        int x = 9;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        fragmentMap.getActivity();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        requestPermissions();
//        if (!checkPermissions()) {
//            requestPermissions();
//        } else {
//            //TODO
//            mService.requestLocationUpdates();
//        }

        bindService(new Intent(this, GPS_Service.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
//        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(fileBroadcastReceiver,
                new IntentFilter(GPS_Service.ACTION_BROADCAST));
    }

//    private void updateUI() {
//        startTicker();
//    }

//    private void startTicker() {
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(ts = new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(() -> {
//                    if (counter == 1) {
//                        fragmentMap.setOnMapCabs(cabs);
//                        if (type.equals("driver")) {
//                            fragmentMap.setOnMapPass(pass);
//                        } else {
//                            if (p != null) {
//                                fragmentMap.setOnMap(p.locationNow.getLatitude(), p.locationNow.getLongitude());
//                            }
//                        }
//                    }
//                });
//            }
//        }, 0, 200);
//    }

    @Override
    protected void onStop() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {

        } else {
            ActivityCompat.requestPermissions(ThePick.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("pttt", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mService.requestLocationUpdates();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }


    public interface CallBack_Service {
        void dataReady();
    }
}


