package com.npdeas.b1k3labapp.Webserver;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.UI.Activities.LoginActivity;
import com.npdeas.b1k3labapp.Database.RouteCRUD;
import com.npdeas.b1k3labapp.Database.UserCRUD;
import com.npdeas.b1k3labapp.Route.Route;

import java.util.ArrayList;

public class WebserviceDAO extends AsyncTaskLoader<Void> {

    private static WebserviceDAO thisObject;

    private Context context;
    private LoginDAO loginDAO;
    private RouteDAO routeDAO;
    private RouteCRUD routeCRUD;
    private UserCRUD userCRUD;
    private User user;

    private WebserviceDAO(Context context) {
        super(context);
        this.context = context;
        //loginDAO = new LoginDAO(context);
        routeCRUD = new RouteCRUD(context);
        userCRUD = new UserCRUD(context);

        user = userCRUD.getUser();
        if (user == null) {
            Intent authIntent = new Intent(context, LoginActivity.class);
            context.startActivity(authIntent);
            return;
        }
        auth(user);
    }

    public void login(){
        user = userCRUD.getUser();
        if (user == null) {
            Intent authIntent = new Intent(context, LoginActivity.class);
            context.startActivity(authIntent);
        }
    }

    public static WebserviceDAO getInstance(Context context) {
        if (thisObject == null) {
            thisObject = new WebserviceDAO(context);
        }
        return thisObject;
    }

    /***
     *
     * @param user usuario logado
     *
     */
    public void auth(User user) {
        this.user = user;
        this.routeDAO = new RouteDAO(context, user);
        forceLoad();
    }

    public void sendRoutes() {
        routeCRUD.getUnsendedRoutes();

    }

    @Override
    public Void loadInBackground() {
        routeDAO.signIn(user);
        ArrayList<Route> routes = routeCRUD.getUnsendedRoutes();
        for (Route route : routes) {
            if (route.getWebId() == 0) {
                Route routeSended = routeDAO.addRoute(route);
                if (routeSended != null) {
                    route.setWebId(routeSended.getWebId());
                    routeCRUD.update(route);
                    while (!route.isSended()) {
                        if (routeDAO.addNode(route) != null) {
                            routeCRUD.update(route);
                        } else {
                            break;
                        }
                    }
                } else {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Error: " + routeDAO.getErrorString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
        return null;
    }
}