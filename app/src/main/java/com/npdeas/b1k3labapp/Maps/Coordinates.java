package com.npdeas.b1k3labapp.Maps;

import android.content.Context;
import android.location.Location;


/**
 * Created by NPDEAS on 05/04/2018.
 */

public class Coordinates extends GoogleMapAPI {

    private Context context;
    private OnLocationChanged locationChanged;

    public Coordinates(Context context){
        super(context);
        this.context = context;
    }
    public void addOnlocationChangedEvent(OnLocationChanged locationChanged){
        this.locationChanged = locationChanged;
    }

    @Override
    public void onGetLocation(android.location.Location var1) {
        if(locationChanged != null){
            locationChanged.OnLocationChanged();
        }
    }

    public interface OnLocationChanged{
       void OnLocationChanged();
    }
}
