package com.pikachu.convenientdelivery.model;

import com.amap.api.services.core.LatLonPoint;

/**
 * Result
 */

public class Result {
    String name;
    String address;
    LatLonPoint point;

    public Result(String name, String address, LatLonPoint point) {
        this.name = name;
        this.address = address;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLonPoint getPoint() {
        return point;
    }

    public void setPoint(LatLonPoint point) {
        this.point = point;
    }
}