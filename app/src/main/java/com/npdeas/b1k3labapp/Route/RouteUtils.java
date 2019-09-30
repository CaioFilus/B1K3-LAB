package com.npdeas.b1k3labapp.Route;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by NPDEAS on 6/15/2018.
 */

public class RouteUtils {

    public static ArrayList<Route> listRoutes() {
        ArrayList<Route> result = new ArrayList<>();


        if (RouteConstants.ROOT_FILE.exists()) {
            File[] foldersAux = RouteConstants.ROOT_FILE.listFiles();
            for (int i = 0; i < foldersAux.length; i++) {
                result.add(new Route(foldersAux[i]));
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public static ArrayList<Route> getUnsendedRoutes() {
        ArrayList<Route> result = new ArrayList<>();
        if (RouteConstants.ROOT_FILE.exists()) {
            File[] foldersAux = RouteConstants.ROOT_FILE.listFiles();
            for (int i = 0; i < foldersAux.length; i++) {
                File[] routeFileAux = foldersAux[i].listFiles();
                boolean sendedRoute = false;
                for (int j = 0; j < routeFileAux.length; j++) {
                    if (routeFileAux[j].getName().endsWith("ckd")) {
                        sendedRoute = true;
                    }
                }
                if (!sendedRoute) {
                    result.add(new Route(foldersAux[i]));
                }
            }
        }
        return result;
    }
    public static void setSendedRoutes(ArrayList<Route> routes){
        for(Route route: routes){
            File routeFolder = route.getRoutePath();
            File ckd = new File(routeFolder.getAbsolutePath(),"db.ckd");
            if(!ckd.exists()){
                try{
                    ckd.createNewFile();
                }catch (Exception e){
                }
            }
        }
    }
    public void getAllRoutes(){

    }


}
