package com.npdeas.b1k3labapp.Route;

import android.content.Context;
import android.graphics.Bitmap;

import com.npdeas.b1k3labapp.UI.Activities.MainActivity;
import com.npdeas.b1k3labapp.Database.RouteCRUD;
import com.npdeas.b1k3labapp.Database.RouteNodeCRUD;
import com.npdeas.b1k3labapp.Maps.MapsUtils;
import com.npdeas.b1k3labapp.Route.Npdeas.FileConstants;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Npdeas.NpDeasReader;
import com.npdeas.b1k3labapp.Route.Npdeas.NpDeasWriter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by NPDEAS on 6/15/2018.
 */

public class Route {

    private long id;
    private long webId;
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
    private ModalType modal;
    private int nodeNum = 0;
    private String name;
    private String dataTime = "";
    private long splitId = 0;
    private boolean isSended = false;


    private RouteCRUD routeCRUD;
    private RouteNodeCRUD routeNodeDao;

    private double oldLat = 0;
    private double oldLng = 0;


    public Route(Context context, boolean isNewRoute) {
        routeCRUD = new RouteCRUD(context);
        routeNodeDao = new RouteNodeCRUD(context);
        if(isNewRoute){
            time = 0;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(FileConstants.DATA_FORMAT);
            dataTime = sdf.format(cal.getTime());
            modal = ModalType.NONE;
            id = routeCRUD.addRoute(this);
        }
    }

    public Route(File route) {
        reader = new NpDeasReader(route);
        loadRoute();
    }

    public Route(String routePath) {
        reader = new NpDeasReader(new File(routePath));
        loadRoute();
    }

    public static ArrayList<Route>loadRoutes(Context context){
        RouteCRUD routeCRUD = new RouteCRUD(context);
        ArrayList<Route> result = routeCRUD.getAllRoutes();
        return result;

    }
    public void loadRoute() {
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
            name = reader.getFileName().substring(0, reader.getFileName().length() - 4);

            dataTime = reader.getDateTime();
        }
    }

    public int getRouteSize() {
        return routeSize;
    }

    public void remove() {
        routeCRUD.delete(id);
    }



    public boolean saveRoute(Bitmap bitmap) {
        if (route.size() <= 3) {
            routeCRUD.delete(id);
            return false;
        }
        this.setImg(bitmap);
        routeCRUD.update(this);

        for (RouteNode node : route) {
            node.setRouteId(id);
            routeNodeDao.addRouteNode(node);
        }

        routeCRUD.close();
        routeNodeDao.close();
        return true;
    }

    public ArrayList<RouteNode> getRoute() {
        return route;
    }

    public int getDistance() {
        return distance;
    }

    public RouteNode getRouteNode(int index) {
        return route.get(index);
    }

    public long getTime() {
        return time;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAvarageSpeed() {
        return avarageSpeed;
    }

    public File getRoutePath() {
        if (reader != null) {
            return reader.getFolderPath();
        }
        return writer.getRouteFolder();
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

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public void addNodeInRoute(RouteNode routeNode) {
        avarageDB += routeNode.getDb();
        avarageSpeed += routeNode.getSpeed();

        if (routeNode.getSpeed() > maxSpeed) {
            maxSpeed = routeNode.getSpeed();
        }
        if (routeNode.getDb() > maxDB) {
            maxDB = routeNode.getDb();
        }
        if ((oldLat != 0) && (oldLng != 0)) {
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
    public void loadFinish(){
        if(nodeNum != 0) {
            avarageSpeed /= nodeNum;
            avarageDB /= nodeNum;
        }
    }

    public ModalType getModal() {
        return this.modal;
    }

    public void setModal(ModalType modal) {
        this.modal = modal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateTime(String dateTime) {
        this.dataTime = dateTime;
    }

    public String getDateTime() {
        return this.dataTime;
    }

    public void setRouteNodes(ArrayList<RouteNode> routeNodes) {
        this.route = routeNodes;
    }

    public ArrayList<RouteNode> getRouteNodes() {
        return this.route;
    }

    public void setImg(Bitmap bitmap) {
        this.img = bitmap;
    }


    public long getWebId() {
        return webId;
    }

    public void setWebId(long webId) {
        this.webId = webId;
    }

    public boolean isSended() {
        return isSended;
    }

    public void setSended(boolean sended) {
        isSended = sended;
    }

    public long getSplitId() {
        return splitId;
    }

    public void setSplitId(long splitId) {
        this.splitId = splitId;
    }
}
