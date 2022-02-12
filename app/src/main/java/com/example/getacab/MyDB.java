package com.example.getacab;


import java.util.ArrayList;

public class MyDB {
    private ArrayList<Drive> drives;
    private int score;
    private String lot;
    private String lat;

    public MyDB (){
        drives = new ArrayList(10);
    }

    public MyDB(ArrayList<Drive> bestScore, int score, String lot, String lat) {
        this.score = score;
        this.lot = lot;
        this.lat = lat;
    }

    public boolean getIn(String myName, String partnerName, String fLat, String fLot, String eLat, String eLot, String costText){

        drives.add(new Drive(myName, partnerName, fLat, fLot, eLat, eLot,costText));

        return true;
    }

    public ArrayList<Drive> getDrives() {
        ArrayList<Drive> revArrayList = new ArrayList<Drive>();
        for (int i = drives.size() - 1; i >= 0; i--) {
            revArrayList.add(drives.get(i));
        }
        return revArrayList;
    }
}