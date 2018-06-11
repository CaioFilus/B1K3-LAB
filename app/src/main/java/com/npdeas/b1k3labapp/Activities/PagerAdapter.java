package com.npdeas.b1k3labapp.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.npdeas.b1k3labapp.Activities.Fragments.MapsFragment;
import com.npdeas.b1k3labapp.Activities.Fragments.StartRouteFragment;
import com.npdeas.b1k3labapp.Maps.Maps;

/**
 * Created by NPDEAS on 5/23/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    MapsFragment mapsFragment;
    StartRouteFragment startRouteFragment;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
        mapsFragment  = new MapsFragment();
        startRouteFragment = new StartRouteFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mapsFragment;
            case 1:
                return startRouteFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
