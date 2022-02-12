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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ThePick extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TYPE = "TYPE";
    public static final String UID = "UID";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private TimerTask ts;
    int counter = 0;
    private Fragment_Map fragmentMap;
    private ArrayList<Driver> cabs = new ArrayList<>();
    private ArrayList<Passenger> pass = new ArrayList<>();
    private String type = "";
    private String uID = "";
    private Driver d;
    private Passenger p;
    private GPS_Service mService = null;
    private boolean mBound = false;
    private String myName;
    private String partnerName;
    private String fstreet;
    private String fcity;
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
    private int intForStartTicker = 0;
    private MaterialButton break_driver;
    private MaterialButton break_return;
    private MaterialButton button_pick;
    private MaterialButton button_take_drive;
    private MaterialButton button_cancel_driver;
    private MaterialButton button_search;
    private MaterialButton button_cancel;
    private MaterialButton button_stop_driver;
    private MaterialButton history_book;
    private DatabaseReference myRefPass;
    private DatabaseReference myRefCab;
    private LottieAnimationView lottie_marker;
    private TextView map;
    private TextView hours;
    private TextView min;
    private TextView sec;
    private TextView cost;
    private int colorButtomTakeDrive = 0;
    private int secend = 0;
    private int minute = 0;
    private int hour = 0;
    private double costText = 2.5;
    private int pickPass = 0;
    private Passenger temp;
    private MyDB myDB;
    private locationDetails locationDetails;

    private BroadcastReceiver fileBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(GPS_Service.EXTRA_LOCATION);
            if (location != null) {
                if (type.equals("driver")) {
                    if (d != null) {
                        d.getLocationNow().setLatitude(String.valueOf(location.getLatitude()));
                        d.getLocationNow().setLongitude(String.valueOf(location.getLongitude()));
                        myRefCab.child(d.getMyuIdCab()).setValue(d);
                    }
                } else {
                    if (p != null) {
                        p.getLocationNow().setLatitude(String.valueOf(location.getLatitude()));
                        p.getLocationNow().setLongitude(String.valueOf(location.getLongitude()));
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
        locationDetails = new locationDetails(this);
        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            type = extras.getString(TYPE);
            uID = extras.getString(UID);
        }
        findViews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefPass = database.getReference(ProfileActivity.PASSNEGERS);
        myRefCab = database.getReference(ProfileActivity.CABS);

        String js = MSPV.getMe().getString("MY_DB", "");
        if (!js.equals("")) {
            myDB = new Gson().fromJson(js, MyDB.class);
        } else {
            myDB = new MyDB();
        }

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
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
//                if (counter == 1) {
//                    if (p != null) {
//
//                    }
////                    else{
////                        fragmentMap.setOnMapCabs(cabs);
////                    }
//                }
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
//                if (counter == 1) {
//                    if (d != null) {
//                        fragmentMap.mapClear();
//                        if (d.getpId() == -1) {
//                            fragmentMap.setOnMapPass(pass);
//                        } else {
//                            temp = findPassengerById(d.getpId());
//                            fragmentMap.setOnMap(temp.getLocationNow().getLatitude(), temp.getLocationNow().getLongitude());
//                        }
//                        fragmentMap.setOnMapCabs(cabs);
//                    }
////                    else{
////                        fragmentMap.setOnMapPass(pass);
////                    }
//                }
            }
        };
        MyFirebaseDB.getAllCars(callBack_cars);

        MapReady mapReady = new MapReady() {
            @Override
            public void mapIsReady() {
                counter = 1;
                fragmentMap.mapClear();
                fragmentMap.setOnMapCabs(cabs);
                if (type.equals("driver")) {
                    fragmentMap.setOnMapPass(pass);
                    if (d != null)
                        fragmentMap.myPositionCab(d.getLocationNow().getLatitude(), d.getLocationNow().getLongitude());
                } else {
                    if (p != null) {
                        fragmentMap.setOnMap(p.getLocationNow().getLatitude(), p.getLocationNow().getLongitude());
                        fragmentMap.myPosition(p.getLocationNow().getLatitude(), p.getLocationNow().getLongitude());
                    }
                }
            }
        };
        fragmentMap.onMPP(mapReady);

        button_search.setOnClickListener(v -> {
            pickPass = 0;
            if (p != null) {
                p.setdId(minimumDistance());
                if (!p.getdId().equals("-1")) {
                    Driver tempDriver = findDriverById(p.getdId());
                    addPassngerForDriverAndSaveForFB(tempDriver, p);
                    addDriverForPassngerAndSaveForFB(p, tempDriver);
                } else {
                    Toast.makeText(this, "sorry, there is no available cab", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_cancel.setOnClickListener(v -> {
            if(!p.getdId().equals("-1")){
                Driver tempDriver = findDriverById(p.getdId());
                freeDriverAndSaveForFB(tempDriver);
            }
            freePassngerAndSaveForFB(p);
        });
        break_driver.setOnClickListener(v -> {
            d.setAvailable(false);
            break_return.setVisibility(View.VISIBLE);
            break_driver.setVisibility(View.GONE);
            myRefCab.child(d.getMyuIdCab()).setValue(d);
            myRefPass.child("-2").setValue("1");
        });
        button_cancel_driver.setOnClickListener(v -> {
            if (!d.getpId().equals("-1")) {
                temp = findPassengerById(d.getpId());
                freePassngerAndSaveForFB(temp);
                freeDriverAndSaveForFB(d);
                button_take_drive.setVisibility(View.GONE);
                button_pick.setVisibility(View.GONE);
                break_driver.setVisibility(View.VISIBLE);
            }
        });
        break_return.setOnClickListener(v -> {
            d.setAvailable(true);
            break_return.setVisibility(View.GONE);
            break_driver.setVisibility(View.VISIBLE);
            myRefCab.child(d.getMyuIdCab()).setValue(d);
            myRefPass.child("-2").removeValue();
        });
        button_take_drive.setOnClickListener(v -> {
            button_pick.setVisibility(View.VISIBLE);
            button_take_drive.setVisibility(View.GONE);
            d.setTakeInvite(true);
            myRefCab.child(d.getMyuIdCab()).setValue(d);
        });
        button_pick.setOnClickListener(v -> {
            temp = findPassengerById(d.getpId());
            if(meterDistanceBetweenPoints(Float.parseFloat(d.getLocationNow().getLatitude()),
                    Float.parseFloat(d.getLocationNow().getLongitude()),
                    Float.parseFloat(temp.getLocationNow().getLatitude()),
                    Float.parseFloat(temp.getLocationNow().getLongitude()))<100) {
                resetViewClock();
                button_stop_driver.setVisibility(View.VISIBLE);
                button_cancel_driver.setVisibility(View.GONE);
                temp.setPicked(true);
                addDriverForPassngerAndSaveForFB(temp, d);
                button_pick.setVisibility(View.GONE);
                d.setPick(true);
                myRefCab.child(d.getMyuIdCab()).setValue(d);
                myName = temp.getName();
                partnerName = temp.getPhoneNumber();
                getLocationData(Double.parseDouble(d.getLocationNow().getLatitude()), Double.parseDouble(d.getLocationNow().getLongitude()));
            }else{
                Toast.makeText(this, "You need to be closer to the passenger for pick the drive",
                    Toast.LENGTH_SHORT).show();
            }
        });
        button_stop_driver.setOnClickListener(v -> {
            temp = findPassengerById(d.getpId());
            freeDriverAndSaveForFB(d);
            button_cancel_driver.setVisibility(View.VISIBLE);
            break_driver.setVisibility(View.VISIBLE);
            hours.setVisibility(View.GONE);
            min.setVisibility(View.GONE);
            sec.setVisibility(View.GONE);
            button_stop_driver.setVisibility(View.GONE);
            cost.setVisibility(View.GONE);

            freePassngerAndSaveForFB(temp);
            stopTicker();
            try {
                locationDetails.resetData(Double.parseDouble(d.getLocationNow().getLatitude()), Double.parseDouble(d.getLocationNow().getLongitude()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            myDB.getIn(myName, partnerName, fcity, fstreet, locationDetails.getCity(), locationDetails.getStreet(), String.valueOf(costText));
            String json = new Gson().toJson(myDB);
            MSPV.getMe().putString("MY_DB", json);
        });
        history_book.setOnClickListener(v -> {
            Intent intent = new Intent(ThePick.this, History.class);
            Bundle bundle = new Bundle();
            if (type.equals("driver")) {
                bundle.putString(History.PHONENUMBER, d.getPhoneNumber());
                bundle.putString(History.NAME, d.getName());
            } else {
                bundle.putString(History.PHONENUMBER, p.getPhoneNumber());
                bundle.putString(History.NAME, p.getName());
            }
            bundle.putString(History.JOB, type);
            intent.putExtra("Bundle", bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

    }

    private void updateViewForCatchDriver(Driver driver) {
        if (driver.isTakeInvite()) {
            if(driver.isPick()){
                button_pick.setVisibility(View.GONE);
                break_driver.setVisibility(View.GONE);
                button_take_drive.setVisibility(View.GONE);
            }else{
                button_pick.setVisibility(View.VISIBLE);
                break_driver.setVisibility(View.GONE);
                button_take_drive.setVisibility(View.GONE);
            }
            intForStartTicker = 0;
        } else {
            break_driver.setVisibility(View.GONE);
            button_take_drive.setVisibility(View.VISIBLE);
            button_pick.setVisibility(View.GONE);
            if(intForStartTicker == 0){
                startTicker();
                intForStartTicker = 1;
            }
        }
    }
    private void stopTicker() {
        ts.cancel();
    }

    private void startTicker(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(ts = new TimerTask() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                runOnUiThread(() -> {
                    colorButtomTakeDrive++;
                    if (colorButtomTakeDrive == 2) {
                        colorButtomTakeDrive = 0;
                        runClock();
                    }
                    if (colorButtomTakeDrive % 2 == 1) {
                        button_take_drive.setBackgroundTintList(ContextCompat.getColorStateList(ThePick.this, R.color.yellow));
                    } else {
                        button_take_drive.setBackgroundTintList(ContextCompat.getColorStateList(ThePick.this, R.color.yellowWhite));
                    }
                });
            }
        }, 0, 500);
    }

    private void addPassngerForDriverAndSaveForFB(Driver driver, Passenger passenger) {
        driver.setAvailable(false);
        driver.setpId(passenger.getMyuIdPass());
        myRefCab.child(String.valueOf(passenger.getdId())).setValue(driver);
    }

    private void addDriverForPassngerAndSaveForFB(Passenger passenger, Driver driver) {
        passenger.setdId(driver.getMyuIdCab());
        passenger.setSearchForCab(false);
        myRefPass.child(passenger.getMyuIdPass()).setValue(passenger);
    }

    private void freeDriverAndSaveForFB(Driver driver) {
        driver.setAvailable(true);
        driver.setpId("-1");
        driver.setPick(false);
        driver.setTakeInvite(false);
        myRefCab.child(driver.getMyuIdCab()).setValue(driver);
    }

    private void getLocationData(double latitude, double longitude) {
        try {
            locationDetails.resetData(latitude, longitude);
            fcity = locationDetails.getCity();
            fstreet = locationDetails.getStreet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Passenger freePassngerAndSaveForFB(Passenger passenger) {
        passenger.setPicked(false);
        passenger.setdId("-1");
        passenger.setSearchForCab(true);
        myRefPass.child(passenger.getMyuIdPass()).setValue(passenger);
        return passenger;
    }

    private void resetViewClock() {
        sec.setText("00");
        min.setText("00:");
        hours.setText("00:");
        secend = 0;
        minute = 0;
        hour = 0;
        costText = 2.5;
        cost.setText("2.5$");
        hours.setVisibility(View.VISIBLE);
        min.setVisibility(View.VISIBLE);
        sec.setVisibility(View.VISIBLE);
        cost.setVisibility(View.VISIBLE);
    }

    private void runClock() {
        secend++;
        secend = secend % 60;
        if (secend % 60 == 30 || secend % 60 == 0) {
            costText = costText + 0.4;
            cost.setText("" + String.format("%,.1f", costText) + "$");
        }
        if (secend == 0) {
            minute++;
            minute = minute % 60;
            if (minute == 0) {
                hour++;
            }
        }
        if (secend < 10) {
            sec.setText("0" + secend);
        } else {
            sec.setText("" + secend);
        }
        if (minute < 10) {
            min.setText("0" + minute + ":");
        } else {
            min.setText("" + minute + ":");
        }
        if (hour < 10) {
            hours.setText("0" + hour + ":");
        } else {
            hours.setText("" + hour + ":");
        }
    }

    private Passenger findPassengerById(String getpId) {
        for (int i = 0; i < pass.size(); i++) {
            if (getpId.equals(pass.get(i).getMyuIdPass())) {
                return pass.get(i);
            }
        }
        return null;
    }

    private Driver findDriverById(String getdId) {
        for (int i = 0; i < cabs.size(); i++) {
            if (getdId.equals(cabs.get(i).getMyuIdCab())) {
                return cabs.get(i);
            }
        }
        return null;
    }

    private String minimumDistance() {
        HashMap<Double, Driver> distaceForDriver = new HashMap<Double, Driver>();
        ArrayList<Double> distance = new ArrayList<>();
        for (int i = 0; i < cabs.size(); i++) {
            if (cabs.get(i).isAvailable() == true) {
                Double dista = meterDistanceBetweenPoints(Float.parseFloat(p.getLocationNow().getLatitude()), Float.parseFloat(p.getLocationNow().getLongitude()), Float.parseFloat(cabs.get(i).getLocationNow().getLatitude()), Float.parseFloat(cabs.get(i).getLocationNow().getLongitude()));
                distance.add(dista);
                distaceForDriver.put(dista, cabs.get(i));
            }
        }
        if (distance.isEmpty()) {
            return "-1";
        }
        Collections.sort(distance);
        return distaceForDriver.get(distance.get(0)).getMyuIdCab();
    }

    private double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f / Math.PI);

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
        button_stop_driver = findViewById(R.id.button_stop_driver);
        hours = findViewById(R.id.hours);
        min = findViewById(R.id.min);
        sec = findViewById(R.id.sec);
        cost = findViewById(R.id.cost);
        break_driver = findViewById(R.id.break_driver);
        break_return = findViewById(R.id.break_return);
        button_pick = findViewById(R.id.button_pick);
        button_take_drive = findViewById(R.id.button_take_drive);
        button_cancel_driver = findViewById(R.id.button_cancel_driver);
        history_book = findViewById(R.id.history_book);
        button_search = findViewById(R.id.button_search);
        button_cancel = findViewById(R.id.button_cancel);
        lottie_marker = findViewById(R.id.lottie_marker);
        break_return.setVisibility(View.GONE);
        button_pick.setVisibility(View.GONE);
        button_take_drive.setVisibility(View.GONE);
        button_stop_driver.setVisibility(View.GONE);
        hours.setVisibility(View.GONE);
        min.setVisibility(View.GONE);
        sec.setVisibility(View.GONE);
        cost.setVisibility(View.GONE);
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
                if (!d.getpId().equals("-1")) {
                    updateViewForCatchDriver(d);
                    temp = findPassengerById(d.getpId());
                } else {
                    button_take_drive.setVisibility(View.GONE);
                    button_pick.setVisibility(View.GONE);
                    if (d.isAvailable()) {
                        break_driver.setVisibility(View.VISIBLE);
                        break_return.setVisibility(View.GONE);
                    }
                }
                if (counter == 1) {
                    fragmentMap.mapClear();
                    if (d.getpId().equals("-1")) {
                        fragmentMap.setOnMapPass(pass);
                    } else {
                        fragmentMap.setOnMap(temp.getLocationNow().getLatitude(), temp.getLocationNow().getLongitude());
                    }
                    fragmentMap.setOnMapCabs(cabs);
                }
            }
        }
    }


    private void findPassenger() {
        for (int i = 0; i < pass.size(); i++) {
            if (uID.equals(pass.get(i).getMyuIdPass())) {
                p = pass.get(i);
            }
        }
            if (counter == 1) {
                fragmentMap.mapClear();
                fragmentMap.setOnMap(p.getLocationNow().getLatitude(), p.getLocationNow().getLongitude());
                if (!p.getdId().equals("-1") && p.isPicked()) {
                    Driver tempDriver = findDriverById(p.getdId());
                    fragmentMap.setOnMapcab(tempDriver);
                } else {
                    fragmentMap.setOnMapCabs(cabs);
                }
                fragmentMap.myPosition(p.getLocationNow().getLatitude(), p.getLocationNow().getLongitude());
            }
            if (p.isPicked()) {
                if (pickPass == 0) {
                    resetViewClock();
                    startTicker();
                    Driver temp = findDriverById(p.getdId());
                    myName = temp.getName();
                    partnerName = temp.getPhoneNumber();
                    getLocationData(Double.parseDouble(p.getLocationNow().getLatitude()), Double.parseDouble(p.getLocationNow().getLongitude()));
                    pickPass++;
                    button_cancel.setVisibility(View.GONE);
                    button_search.setVisibility(View.GONE);
                }
            } else {
                if (pickPass == 1) {
                stopTicker();
                hours.setVisibility(View.GONE);
                min.setVisibility(View.GONE);
                sec.setVisibility(View.GONE);
                cost.setVisibility(View.GONE);
                button_cancel.setVisibility(View.VISIBLE);
                button_search.setVisibility(View.VISIBLE);
                    try {
                        locationDetails.resetData(Double.parseDouble(p.getLocationNow().getLatitude()), Double.parseDouble(p.getLocationNow().getLongitude()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myDB.getIn(myName, partnerName, fcity, fstreet, locationDetails.getCity(), locationDetails.getStreet(), String.valueOf(costText));
                    String json = new Gson().toJson(myDB);
                    MSPV.getMe().putString("MY_DB", json);
                }
            }
    }


    @Override
    protected void onStart() {
        super.onStart();
        fragmentMap.getActivity();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        requestPermissions();

        bindService(new Intent(this, GPS_Service.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(fileBroadcastReceiver,
                new IntentFilter(GPS_Service.ACTION_BROADCAST));
        if (type.equals("driver")) {
            if (d != null)
                myRefCab.child(d.getMyuIdCab()).setValue(d);
        } else if (p != null) {
            myRefPass.child(p.getMyuIdPass()).setValue(p);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        if (type.equals("driver")) {
            if (!d.getpId().equals("-1")) {
                temp = findPassengerById(d.getpId());
                freePassngerAndSaveForFB(temp);
            }
            freeDriverAndSaveForFB(d);
            myRefCab.child(d.getMyuIdCab()).removeValue();
        } else {
            if (!p.getdId().equals("-1")) {
                Driver tempDriver = findDriverById(p.getdId());
                freeDriverAndSaveForFB(tempDriver);
            }
            freePassngerAndSaveForFB(p);
            myRefPass.child(p.getMyuIdPass()).removeValue();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (type.equals("driver")) {
            if (d != null) {
                if (!d.getpId().equals("-1")) {
                    temp = findPassengerById(d.getpId());
                    freePassngerAndSaveForFB(temp);
                }
                freeDriverAndSaveForFB(d);
                myRefCab.child(d.getMyuIdCab()).removeValue();
            }
        } else if (p != null) {
            if (!p.getdId().equals("-1")) {
                Driver tempDriver = findDriverById(p.getdId());
                freeDriverAndSaveForFB(tempDriver);
            }
            freePassngerAndSaveForFB(p);
            myRefPass.child(p.getMyuIdPass()).removeValue();
        }
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
                //TODO
                mService.requestLocationUpdates();
            }
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }


    public interface CallBack_Service {
        void dataReady();
    }
}