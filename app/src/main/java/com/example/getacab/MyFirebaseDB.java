package com.example.getacab;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFirebaseDB {

    public interface CallBack_passengers {
        void dataReady(ArrayList<Passenger> passengers);
    }

    public interface CallBack_Cabs {
        void dataReady(ArrayList<Driver> drivers);
    }
//    addListenerForSingleValueEvent
    public static void getAllCars(CallBack_Cabs callBack_cars) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ProfileActivity.CABS);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Driver> drivers = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        Driver d = child.getValue(Driver.class);
                        drivers.add(d);
                    } catch (Exception ex) {}

                }
                if (callBack_cars != null) {
                    callBack_cars.dataReady(drivers);
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getAllpassengers(CallBack_passengers callBack_passengers) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ProfileActivity.PASSNEGERS);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Passenger> passengers = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        Passenger p = child.getValue(Passenger.class);
                        passengers.add(p);
                    } catch (Exception ex) {}

                }
                if (callBack_passengers != null) {
                    callBack_passengers.dataReady(passengers);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
























