package com.npdeas.b1k3labapp.Webserver;

import android.content.Context;
import android.util.Log;


import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Route;
import com.npdeas.b1k3labapp.Webserver.Error.Error;
import com.npdeas.b1k3labapp.Webserver.Error.RouteError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO extends LoginDAO implements DAO {


    private static final String CMD_ADD_ROUTE = "routes/addroute";
    private static final String CMD_ADD_NODE = "routes/addnode";

    private static final String ENCAPSULAMENT = "routeInfo";

    //private static RouteDAO thisObject;

    private Context context;
    private Route route;

    public RouteDAO(Context context, User user) {
        super(context, user);
        this.context = context;
        this.user = user;
    }

    public Route addRoute(Route route) {
        JSONObject content = getContent(route);
        JSONObject response = super.sendJSON(content, CMD_ADD_ROUTE, ENCAPSULAMENT);
        if (response != null) {
            try {
                int error = response.getInt("status");
                if (error == Error.NO_ERROR) {
                    route.setWebId(response.getLong("id"));
                    return route;

                } else {
                    super.error = new Error(error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Route addNode(Route route) {
        JSONObject routeRouteNode = getNodeContent(route);
        JSONObject response = super.sendJSON(routeRouteNode, CMD_ADD_NODE, ENCAPSULAMENT);
        if (response != null) {
            try {
                int error = response.getInt("status");
                if (error == Error.NO_ERROR) {
                    route.setWebId(response.getLong("id"));
                    return route;
                } else {
                    if (error == RouteError.ERROR_ROUTE_ALREADY_SENDED.getInt()) {
                        route.setSended(true);
                        return route;
                    }
                    route.setSplitId(route.getSplitId() - 1);
                    super.error = new Error(error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private JSONObject getNodeContent(Route route) {
        JSONObject result = new JSONObject();
        JSONArray jsonNodes = new JSONArray();
        try {
            result.put("id", route.getWebId());
            result.put("send_id", route.getSplitId());
            result.put("split_size", route.getSplitId());
            ArrayList<RouteNode> nodes = route.getRouteNodes();
            long splitId = route.getSplitId() + 1;
            int routeOffset = (int) (splitId - 1) * 255;
            while ((routeOffset < nodes.size()) && ((routeOffset - splitId * 255) < 255)) {
                JSONObject jsonNode = new JSONObject();
                jsonNode.put("lat", nodes.get(routeOffset).getLatitude());
                jsonNode.put("lng", nodes.get(routeOffset).getLongetude());
                jsonNode.put("speed", nodes.get(routeOffset).getSpeed());
                jsonNode.put("time", nodes.get(routeOffset).getTime());
                jsonNode.put("overtaking", nodes.get(routeOffset).getOvertaking());
                jsonNode.put("decibel", nodes.get(routeOffset).getDb());
                jsonNodes.put(jsonNode);
                routeOffset++;
            }
            if (routeOffset >= nodes.size()) {
                route.setSended(true);
            }
            result.put("nodes", jsonNodes);
        } catch (Exception e) {
            Log.i("Webserver_get_content", e.getMessage());
        }
        return result;
    }

    private Route getUser(JSONObject routeResponse, Route route) throws JSONException {
        int userStatus = routeResponse.getInt("status");
        if (userStatus == Error.NO_ERROR) {
            route.setWebId(routeResponse.getInt("id"));
            return route;
        } else {
            error = new Error(userStatus);
            return null;
        }
    }

    private JSONObject getContent(Route route) {
        JSONObject result = new JSONObject();
        try {
            Field[] fields = Route.class.getDeclaredFields();
            for (Field field : fields) {
                if (List.class.equals(field.getType())) {

                } else {
                    result.put(field.getName(), field.get(route));
                }
            }
        } catch (Exception e) {
            Log.i("Webserver_get_content", e.getMessage());
        }
        return result;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public Class<? extends Enum> getErrorClass() {
        return RouteError.class;
    }
}
