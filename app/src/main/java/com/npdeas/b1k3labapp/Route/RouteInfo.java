package com.npdeas.b1k3labapp.Route;

import com.npdeas.b1k3labapp.Database.Structs.Route;
import com.npdeas.b1k3labapp.Database.Structs.RouteNode;
import com.npdeas.b1k3labapp.Maps.MapsUtils;

public class RouteInfo {

    private Route route;

    private float maxSpeed = 0;
    private float avarageSpeed = 0;
    private int maxDB = 0;
    private int avarageDB = 0;
    private int distance = 0;
    private long time = 0;

    private int AVARAGE_COUNT = 30;
    private int[] arrDB;
    private float[] arrSpeed;
    private int count = 0;
    private int div = 1;

    public RouteInfo(Route route) {
        this.route = route;
        float speedSum = 0;
        int dbSum = 0;
        if(route.routeNodes.size() == 0){
            return;
        }
        RouteNode lastNode = route.routeNodes.get(0);
        for (RouteNode node : this.route.routeNodes) {
            if (maxSpeed < node.speed) {
                maxSpeed = node.speed;
            }
            if (maxDB < node.db) {
                maxDB = node.db;
            }
            distance += MapsUtils.getDistance(
                    lastNode.latitude, lastNode.longetude, node.latitude, node.longetude);
            speedSum += node.speed;
            dbSum += node.speed;
            lastNode = node;

        }
        if (route.routeNodes.size() != 0) {
            avarageSpeed = speedSum / route.routeNodes.size();
            avarageDB = dbSum / route.routeNodes.size();
        }
    }

    public RouteInfo() {
        arrDB = new int[AVARAGE_COUNT];
        arrSpeed = new float[AVARAGE_COUNT];
    }

    public void loadInfo(RouteNode node) {
        if (count == AVARAGE_COUNT) {
            count = 0;
        }
        arrSpeed[count] = node.speed;
        arrDB[count] = node.db;
        int sumDb = 0;
        float sumSpeed = 0;
        maxDB = 0;
        maxSpeed = 0;
        for (int i = 0; i < div; i++) {
            sumSpeed += arrSpeed[i];
            sumDb += arrDB[i];

            if (maxSpeed < arrSpeed[i]) {
                maxSpeed = arrSpeed[i];
            }
            if (maxDB < arrDB[i]) {
                maxDB = arrDB[i];
            }

        }
        avarageSpeed = sumSpeed / div;
        avarageDB = sumDb / div;

        if (div < AVARAGE_COUNT) {
            div++;
        }
        count++;
    }


    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAvarageSpeed() {
        return avarageSpeed;
    }

    public int getMaxDB() {
        return maxDB;
    }

    public int getAvarageDB() {
        return avarageDB;
    }

    public int getDistance() {
        return distance;
    }

    public long getTime() {
        return time;
    }
}
