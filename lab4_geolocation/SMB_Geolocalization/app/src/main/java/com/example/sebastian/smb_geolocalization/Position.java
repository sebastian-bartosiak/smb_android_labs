package com.example.sebastian.smb_geolocalization;

import android.location.Location;

import java.util.UUID;

class Position {
    private final double latitude;
    private final double longitude;
    private UUID id;
    private final String name;
    private final String desc;
    private final double radius;

    public Position(UUID Id, String name, String desc, double radius, double lat, double lang) {
        this.id = Id;
        this.name = name;
        this.desc = desc;
        this.radius = radius;
        this.longitude = lat;
        this.latitude = lang;
    }

    public UUID getId(){
        return id;
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public double getRadius() {
        return radius;
    }
}
