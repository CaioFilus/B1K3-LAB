package com.npdeas.b1k3labapp.Route;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by NPDEAS on 6/15/2018.
 */

public class RouteUtils {

    public static ArrayList<Route> listRoutes(){
        ArrayList<Route> result = new ArrayList<>();
        if (RouteConstants.ROOT_FILE.exists()) {
            File[] foldersAux = RouteConstants.ROOT_FILE.listFiles();
            for(int i = 0; i < foldersAux.length;i++){
                result.add(new Route(foldersAux[i]));
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}
