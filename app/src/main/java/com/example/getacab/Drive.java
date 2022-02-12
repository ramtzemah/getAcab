package com.example.getacab;

public class Drive {
    private String name;
    private String phone;
    private String fCity;
    private String fStreet;
    private String eCity;
    private String eStreet;
    private String cost;

    public Drive(String myName, String partnerName, String fLat, String fLot, String eLat, String eLot, String cost) {
        this.name = myName;
        this.phone = partnerName;
        this.fCity = fLat;
        this.fStreet = fLot;
        this.eCity = eLat;
        this.eStreet = eLot;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getfCity() {
        return fCity;
    }

    public String getfStreet() {
        return fStreet;
    }

    public String geteCity() {
        return eCity;
    }

    public String geteStreet() {
        return eStreet;
    }

    public String getCost() {
        return cost;
    }
}
