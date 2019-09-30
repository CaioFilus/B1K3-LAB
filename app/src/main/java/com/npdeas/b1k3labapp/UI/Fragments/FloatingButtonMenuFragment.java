package com.npdeas.b1k3labapp.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npdeas.b1k3labapp.R;


/**
 * Created by NPDEAS on 6/7/2018.
 */

public class FloatingButtonMenuFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  v1 = inflater.inflate(R.layout.view_floating_button_menu,container,false);
        /*FloatingActionButton fab1 = v1.findViewById(R.id.fabModalT);
        FloatingActionButton fab2 = v1.findViewById(R.id.fabT1);
        FloatingActionButton fab3 = v1.findViewById(R.id.fabT2);*/
        return v1;

    }
}
