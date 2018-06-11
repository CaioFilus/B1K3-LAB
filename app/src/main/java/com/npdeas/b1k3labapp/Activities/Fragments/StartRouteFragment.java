package com.npdeas.b1k3labapp.Activities.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.npdeas.b1k3labapp.Npdeas.FileStruct;
import com.npdeas.b1k3labapp.R;

import java.sql.Struct;

/**
 * Created by NPDEAS on 5/17/2018.
 */

public class StartRouteFragment extends Fragment{

    private View v;
    private FileStruct struct;
    private TextView txtViewInfoDis;
    private TextView txtViewInfoGps;
    private TextView txtViewInfoMic;
    private TextView txtViewInfoVelo;
    private TextView txtViewInfoTemp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_route,container,false);
        txtViewInfoDis = v.findViewById(R.id.textInfoDis);
        txtViewInfoGps = v.findViewById(R.id.txtViewInfoGPS);
        txtViewInfoMic = v.findViewById(R.id.textViewInfoMic);
        txtViewInfoVelo = v.findViewById(R.id.textViewInfoVelo);
        txtViewInfoTemp = v.findViewById(R.id.textViewInfoTemp);
         return v;
    }
    public void setRoutePartion(FileStruct struct){
         txtViewInfoGps.setText(struct.getLongetude() + ", " + struct.getLongetude());
         txtViewInfoVelo.setText(struct.getSpeed() + "m/s");
         txtViewInfoMic.setText(struct.getDb() + "db");
         try {
             if (struct.getDistance() == 0) {
                 txtViewInfoDis.setText("B1K3 Lab não conectado");
             } else {
                 txtViewInfoDis.setText(struct.getDistance() + "m");
             }
         }catch (Exception e){
             Log.i("aqui", "deu errro aqui rapaz");
         }

    }
}
