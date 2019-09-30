package com.npdeas.b1k3labapp.Route.Npdeas;

/**
 * Created by NPDEAS on 12/04/2018.
 */

public class RouteNode {

    private long routeId;

    private double longetude;
    private double latitude;
    private float speed;
    private int db;
    private int overtaking;
    private long time;
    private int distance;
    private int[] distances;

    public double getLongetude() {
        return longetude;
    }

    public void setLongetude(double longetude) {
        this.longetude = longetude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public int getOvertaking() {
        return overtaking;
    }

    public void setOvertaking(int overtaking) {
        this.overtaking = overtaking;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int[] getDistances() {
        return distances;
    }

    public void setDistances(int[] distances) {
        this.distances = distances;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }
}
