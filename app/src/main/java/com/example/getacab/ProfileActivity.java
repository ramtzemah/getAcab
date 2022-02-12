package com.example.getacab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class    ProfileActivity extends AppCompatActivity {
    public static final String PHONENUMBER = "PHONENUMBER";
    public static final String PASSNEGERS = "passengers";
    public static final String CABS = "cabs";
    private TextInputLayout form_EDT_name;
    private CheckBox passenger;
    private CheckBox driver;
    private MaterialButton form_BTN_submit;
    private String phoneNunber;
    private  DatabaseReference myRefPass;
    private DatabaseReference myRefCab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findviews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefPass = database.getReference(PASSNEGERS);
        myRefCab = database.getReference(CABS);
        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            phoneNunber = extras.getString(PHONENUMBER);
        }

        Undefined undefined;
        String js = MSPV.getMe().getString("type", "");
        if (!js.isEmpty()) {
            undefined = new Gson().fromJson(js, Undefined.class);
            if(undefined.getType().equals("driver")){
                Driver d = new Driver(undefined.getName(),undefined.getPhoneNumber());
                myRefCab.child(d.getMyuIdCab()).setValue(d);
                Main main = new Main(ProfileActivity.this,"driver",d.getMyuIdCab());
                finish();
            }else{
                Passenger p = new Passenger(undefined.getName(),undefined.getPhoneNumber());
                myRefPass.child(p.getMyuIdPass()).setValue(p);
                Main main = new Main(ProfileActivity.this,"passenger",p.getMyuIdPass());
                finish();
            }

//            Intent intent = new Intent(ProfileActivity.this,ThePick.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
        }



        passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passenger.setChecked(true);
                driver.setChecked(false);
            }
        });
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver.setChecked(true);
                passenger.setChecked(false);
            }
        });
        form_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfUser = form_EDT_name.getEditText().getText().toString();
                if(nameOfUser.isEmpty()){
                    form_EDT_name.setError("Name is required");
                    form_EDT_name.requestFocus();
                    return;
                }
                if(!driver.isChecked() && !passenger.isChecked()){
                    Toast.makeText( ProfileActivity.this, "Need to pick a type", Toast.LENGTH_SHORT).show();
                    return;
                }
                Undefined undefined;
                if(driver.isChecked()){
                    Driver driver = new Driver(nameOfUser,phoneNunber);
                    undefined = new Undefined("driver",nameOfUser,phoneNunber,driver.getMyuIdCab());
                    myRefCab.child(driver.getMyuIdCab()).setValue(driver);
                    String json = new Gson().toJson(undefined);
                    MSPV.getMe().putString("type", json);
                    Main main = new Main(ProfileActivity.this,"driver",driver.getMyuIdCab());
                    finish();
                }else {
                    Passenger passenger = new Passenger(nameOfUser,phoneNunber);
                    undefined = new Undefined("passenger",nameOfUser,phoneNunber,passenger.getMyuIdPass());
                    myRefPass.child(passenger.getMyuIdPass()).setValue(passenger);
                    String json = new Gson().toJson(undefined);
                    MSPV.getMe().putString("type", json);
                    Main main = new Main(ProfileActivity.this,"passenger",passenger.getMyuIdPass());
                    finish();
                }
//                String json = new Gson().toJson(undefined);
//                MSPV.getMe().putString("type", json);
//                Intent intent = new Intent(ProfileActivity.this,ThePick.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            }
        });
    }


    private void findviews() {
        form_EDT_name = findViewById(R.id.form_EDT_name);
        passenger = (CheckBox) findViewById(R.id.passenger);
        driver = (CheckBox) findViewById(R.id.driver);
        form_BTN_submit = findViewById(R.id.form_BTN_submit);
    }
}