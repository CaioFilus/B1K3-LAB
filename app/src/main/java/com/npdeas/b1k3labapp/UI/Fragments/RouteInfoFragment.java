package com.npdeas.b1k3labapp.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.npdeas.b1k3labapp.Database.Structs.RouteNode;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Route.RouteInfo;

/**
 * Created by NPDEAS on 5/17/2018.
 */

public class RouteInfoFragment extends Fragment {

    private View v;
    private boolean iotDeviceStatus = false;

    private ViewGroup layoutTemperature;
    private ViewGroup layoutHumidity;
    private ViewGroup layoutOvertaking;
    private TextView txtViewInfoDis;
    private TextView txtViewInfoGps;
    private TextView txtViewInfoMic;
    private TextView textViewMaxDB;
    private TextView textViewAvrgSpd;
    private TextView textViewMaxSpd;
    private TextView txtViewInfoTemp;
    private TextView textViewHumidity;
    private TextView textViewTemperature;
    private PointerSpeedometer velocimeter;
    private RouteInfo routeInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_monitorament, container, false);
        layoutTemperature = v.findViewById(R.id.layoutTemperature);
        layoutHumidity = v.findViewById(R.id.layoutHumidity);
        layoutOvertaking = v.findViewById(R.id.layoutOvertaking);
        txtViewInfoDis = v.findViewById(R.id.textViewDisInfo);
        txtViewInfoGps = v.findViewById(R.id.txtViewInfoGPS);
        txtViewInfoMic = v.findViewById(R.id.textViewInfoMic);
        textViewMaxDB = v.findViewById(R.id.textViewMaxDB);
        textViewAvrgSpd = v.findViewById(R.id.textViewAvrgSpd);
        textViewMaxSpd = v.findViewById(R.id.textViewMaxSpd);
        txtViewInfoTemp = v.findViewById(R.id.textViewInfoTemp);
        textViewHumidity = v.findViewById(R.id.textViewHumidity);
        textViewTemperature = v.findViewById(R.id.textViewTemperature);
        velocimeter = v.findViewById(R.id.velocimeter);
        routeInfo = new RouteInfo();
        layoutHumidity.setVisibility(View.GONE);
        layoutTemperature.setVisibility(View.GONE);

        return v;
    }

    public void setRoutePartion(RouteNode struct) {
        txtViewInfoGps.setText(struct.longetude + ", " + struct.longetude);
        txtViewInfoMic.setText(struct.db + "db");
        velocimeter.speedTo((float) (struct.speed * 3.6), 300);
        routeInfo.loadInfo(struct);
        textViewAvrgSpd.setText(String.format("%.2f", routeInfo.getAvarageSpeed() * 3.6) + " Km/h");
        textViewMaxSpd.setText(String.format("%.2f", routeInfo.getMaxSpeed() * 3.6) + " Km/h");
        textViewMaxDB.setText(routeInfo.getMaxDB() + " db");
        if (iotDeviceStatus) {
            txtViewInfoDis.setText(struct.overtaking + " cm");
            textViewTemperature.setText(struct.temperature + " C");
            textViewHumidity.setText(struct.humidity + " %");
        }
    }

    public void setIotDeviceStatus(boolean status) {
        if (!status) {
            layoutOvertaking.setVisibility(View.GONE);
            layoutHumidity.setVisibility(View.GONE);
            layoutTemperature.setVisibility(View.GONE);
        } else {
            layoutOvertaking.setVisibility(View.VISIBLE);
            layoutHumidity.setVisibility(View.VISIBLE);
            layoutTemperature.setVisibility(View.VISIBLE);
        }
        this.iotDeviceStatus = status;

    }

}
