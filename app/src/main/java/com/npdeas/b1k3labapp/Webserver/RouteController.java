package com.npdeas.b1k3labapp.Webserver;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.npdeas.b1k3labapp.Database.RouteCRUD;
import com.npdeas.b1k3labapp.Route.Route;

import java.util.ArrayList;

public class RouteController extends AsyncTaskLoader<Void> {

    private Context context;
    private RouteDAO routeDAO;
    private RouteCRUD routeCRUD;
    private ArrayList<Route> routes;

    private RouteController(Context context){
        super(context);
        this.context = context;
        routeCRUD = new RouteCRUD(context);
        //routeDAO = new RouteDAO(context);
        routes = routeCRUD.getUnsendedRoutes();
    }

    @Override
    public Void loadInBackground() {
        for(Route route : routes){

        }


        return null;
    }
}
