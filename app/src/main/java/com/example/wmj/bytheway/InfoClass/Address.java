package com.example.wmj.bytheway.InfoClass;

/**
 * Created by st4rlight on 2018/1/5.
 */

public class Address {
    private String address;
    private String longitude;
    private String latitude;

    public Address(String address, String longitude, String latitude) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
