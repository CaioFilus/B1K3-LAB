package com.npdeas.b1k3labapp.Route;

import android.content.Context;
import android.graphics.Bitmap;

import com.npdeas.b1k3labapp.Maps.MapsUtils;
import com.npdeas.b1k3labapp.Route.Npdeas.FileNames;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Npdeas.NpDeasReader;
import com.npdeas.b1k3labapp.Route.Npdeas.NpDeasWriter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by NPDEAS on 6/15/2018.
 */

public class Route {

    private ArrayList<RouteNode> route = new ArrayList<>();
    private float maxSpeed = 0;
    private float avarageSpeed = 0;
    private int maxDB = 0;
    private int avarageDB = 0;
    private int routeSize = 0;
    private int distance = 0;
    private long time = 0;
    private NpDeasReader reader = null;
    private NpDeasWriter writer = null;
    private Bitmap img = null;
    private int nodeNum = 0;
    private String name;
    private String dataTime = "";

    private double oldLat = 0;
    private double oldLng = 0;

    public Route(Context context) {
        String name = FileNames.fileName(context);
        writer = new NpDeasWriter(name);
        time = System.currentTimeMillis();
    }

    public Route(File route) {
        reader = new NpDeasReader(route);
        loadRoute();
    }
    public Route(String routePath){
        reader = new NpDeasReader(new File(routePath));
        loadRoute();
    }

    private void loadRoute() {
        RouteNode routeNode;
        if (reader != null) {
            while ((routeNode = reader.getFileStruct()) != null) {
                addNodeInRoute(routeNode);
            }
            routeSize = nodeNum;
            avarageSpeed /= nodeNum;
            avarageDB /= nodeNum;
            time /= 600;
            img = reader.getImage();
            name = reader.getFileName().substring(0,reader.getFileName().length() - 4 );

            dataTime = reader.getDateTime();
        }
    }
    public int getRouteSize(){
        return routeSize;
    }

    public void remove(){
        if(reader != null){
            reader.remove();
        }
    }

    public void addRouteNode(RouteNode routeNode) {

        route.add(routeNode);
        nodeNum++;
        routeSize = nodeNum;
        avarageSpeed /= nodeNum;
        avarageDB /= nodeNum;
    }
    public boolean saveRoute(Bitmap bitmap){

        if(writer != null){
            for(RouteNode node : route){
                writer.addNewLine(node);
            }
        }
        writer.close(bitmap);
        return false;
    }

    public int getDistance(){
        return distance;
    }
    public RouteNode getRouteNode(int index) {
        return route.get(index);
    }

    public long getTime(){
        return time;
    }
    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAvarageSpeed() {
        return avarageSpeed;
    }

    public File getRoutePath(){
        return reader.getFolderPath();
    }
    public Bitmap getImg() {
        return img;
    }

    public String getDataTime() {
        return dataTime;
    }
    public String getRouteName() {
        return name;
    }

    private void addNodeInRoute(RouteNode routeNode){
        avarageDB += routeNode.getDb();
        avarageSpeed += routeNode.getSpeed();

        if (routeNode.getSpeed() > maxSpeed) {
            maxSpeed = routeNode.getSpeed();
        }
        if (routeNode.getDb() > maxDB) {
            maxDB = routeNode.getDb();
        }
        if((oldLat != 0) && (oldLng != 0)) {
            float distanceAux = MapsUtils.getDistance(oldLat, oldLng, routeNode.getLatitude(),
                    routeNode.getLongetude());
            distance += distanceAux;
            routeNode.setDistance(distance);

        }
        /*if(routeNode.getSpeed() != 0){
            time += distance/ routeNode.getSpeed();
        }*/
        oldLat = routeNode.getLatitude();
        oldLng = routeNode.getLongetude();
        route.add(routeNode);
        nodeNum++;
    }

}
