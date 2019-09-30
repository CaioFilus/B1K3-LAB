package com.npdeas.b1k3labapp.UI.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 6/20/2018.
 */

public class TesteFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teste,container,false);
    }
}
