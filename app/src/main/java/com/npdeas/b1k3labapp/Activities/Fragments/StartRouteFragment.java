package com.npdeas.b1k3labapp.Activities.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 5/17/2018.
 */

public class StartRouteFragment extends Fragment {

    private View v;

    private TextView txtViewInfoDis;
    private TextView txtViewInfoGps;
    private TextView txtViewInfoMic;
    private TextView txtViewInfoVelo;
    private TextView txtViewInfoTemp;
    private PointerSpeedometer velocimeter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_route, container, false);
        txtViewInfoDis = v.findViewById(R.id.textInfoDis);
        txtViewInfoGps = v.findViewById(R.id.txtViewInfoGPS);
        txtViewInfoMic = v.findViewById(R.id.textViewInfoMic);
        txtViewInfoVelo = v.findViewById(R.id.textViewInfoVelo);
        txtViewInfoTemp = v.findViewById(R.id.textViewInfoTemp);
        velocimeter = v.findViewById(R.id.velocimeter);
        return v;
    }

    public void setRoutePartion(RouteNode struct) {
        txtViewInfoGps.setText(struct.getLongetude() + ", " + struct.getLongetude());
        txtViewInfoVelo.setText(struct.getSpeed() + "m/s");
        txtViewInfoMic.setText(struct.getDb() + "db");
        velocimeter.speedTo((float) (struct.getDb()), 500);
        if (struct.getOvertaking() == 0) {
            txtViewInfoDis.setText("B1K3 Lab não conectado");
        } else {
            txtViewInfoDis.setText(struct.getOvertaking() + "m");
        }

    }
}
