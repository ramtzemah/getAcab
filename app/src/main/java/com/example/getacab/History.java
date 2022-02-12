package com.example.getacab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    public static final String PHONENUMBER = "PHONENUMBER";
    public static final String NAME = "NAME";
    public static final String JOB = "JOB";
    private Fragment_List fragmentList;
    private ArrayList<Drive> drives;
    private Adapter_Drive adapter_drives;
    private TextView name;
    private TextView phoneNumber;
    private TextView job;
    private ImageView imageView;
    private String job1;
    private MaterialButton return_to_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        findViews();
        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            name.setText(extras.getString(NAME));
            phoneNumber.setText(extras.getString(PHONENUMBER));
            job1 = extras.getString(JOB);
            job.setText(job1);
        }
        if (job1.equals("driver")) {
            imageView.setImageResource(R.drawable.taxi_user);
        }else {
            imageView.setImageResource(R.drawable.programmer);
        }
        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();


        String js = MSPV.getMe().getString("MY_DB", "");
        MyDB myDB = new Gson().fromJson(js, MyDB.class);
        if (myDB!=null){
            drives = myDB.getDrives();
        }
        onStart();

        adapter_drives = new Adapter_Drive(this, drives);


        fragmentList.getMain_LST_drives().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        fragmentList.getMain_LST_drives().setHasFixedSize(true);
        fragmentList.getMain_LST_drives().setItemAnimator(new DefaultItemAnimator());
        fragmentList.getMain_LST_drives().setAdapter(adapter_drives);


        adapter_drives.setDriveItemClickListener(new Adapter_Drive.DriveItemClickListener() {
            @Override
            public void driveItemClicked() {
                return;
            }
        });

        return_to_map.setOnClickListener(v -> {
            finish();
        });
    }

    private void findViews() {
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        job = findViewById(R.id.job);
        imageView = findViewById(R.id.imageView);
        return_to_map = findViewById(R.id.return_to_map);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragmentList.getActivity();
        int x = 9;
    }
}