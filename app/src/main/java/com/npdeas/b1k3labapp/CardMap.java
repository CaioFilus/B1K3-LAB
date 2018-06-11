package com.npdeas.b1k3labapp;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by NPDEAS on 18/04/2018.
 */

public class CardMap {
    private Bitmap imgMap;
    private String timeMap;
    private String dateMap;
    private String routeTitle;
    private File imgFile;
    private File routeFile;

    public CardMap(){
    }
    public  CardMap(Bitmap imgMap,String dateMap, String timeMap,String routeTitle ,File routeFile,File imgFile){
        this.setImgMap(imgMap);
        this.setTimeMap(timeMap);
        this.setDateMap(dateMap);
        this.setRouteTitle(routeTitle);
        this.setRouteFile(routeFile);
        this.setImgFile(imgFile);

    }

    public Bitmap getImgMap() {
        return imgMap;
    }

    public void setImgMap(Bitmap imgMap) {
        this.imgMap = imgMap;
    }

    public String getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(String timeMap) {
        this.timeMap = timeMap;
    }

    public String getDateMap() {
        return dateMap;
    }

    public void setDateMap(String dateMap) {
        this.dateMap = dateMap;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File file) {
        this.imgFile = file;
    }

    public File getRouteFile() {
        return routeFile;
    }

    public void setRouteFile(File routeFile) {
        this.routeFile = routeFile;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public void setRouteTitle(String routeTitle) {
        this.routeTitle = routeTitle;
    }
}
