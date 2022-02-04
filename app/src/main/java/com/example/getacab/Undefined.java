package com.example.getacab;

public class Undefined {
    private String type;
    private String name;
    private String phoneNumber;
    private String id;

    public Undefined(String type, String name, String phoneNumber,String id) {
        this.type = type;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getId() {
        return id;
    }
}
