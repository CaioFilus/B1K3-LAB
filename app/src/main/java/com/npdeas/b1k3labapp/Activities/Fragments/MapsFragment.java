package com.npdeas.b1k3labapp.Activities.Fragments;


import android.content.Context;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.npdeas.b1k3labapp.Maps.FragmentGPS;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.R;
import com.google.android.gms.maps.MapView;


/**
 * Created by NPDEAS on 22/03/2018.
 */

public class MapsFragment extends Fragment implements FragmentGPS.OnLocationChanged {

    private FragmentGPS maps;
    private GpsFragmentEvent fragmentsCommunication;

    private View v;
    private MapView mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = v.findViewById(R.id.mapView);
        maps = new FragmentGPS(getActivity(), savedInstanceState, mapFragment);
        maps.addOnLocationChanged(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentsCommunication = (GpsFragmentEvent) getActivity();
            fragmentsCommunication.onFragmentCreate();
        } catch (Exception e) {
            Log.e("MapsFragment", e.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapFragment.isEnabled()) {
            mapFragment.onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment.isEnabled()) {
            mapFragment.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    @Override
    public void OnLocationChanged() {
        if (fragmentsCommunication != null) {
            RouteNode struct = new RouteNode();
            struct.setLongetude(maps.getLongitude());
            struct.setLatitude(maps.getLatitude());
            struct.setSpeed(maps.getSpeed());
        }
    }
    public void startRoute(){
        maps.startRoute();
    }
    public void finishRoute(){
        maps.finishRoute();
    }

    public Bitmap getScreenshot(){
        return maps.getBitmap();
    }

    public interface GpsFragmentEvent {
        void onFragmentCreate();
    }

}
