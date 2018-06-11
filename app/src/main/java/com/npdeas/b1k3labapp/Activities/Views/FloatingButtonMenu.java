package com.npdeas.b1k3labapp.Activities.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.npdeas.b1k3labapp.R;

import java.util.ArrayList;

/**
 * Created by NPDEAS on 5/18/2018.
 */

public class FloatingButtonMenu extends FloatingActionButton{

    private FloatingActionButton fabMenu;
    private ArrayList<FloatingActionButton> listButton;
    private OnFloatingButtonMenuClick menuClick;

    public  FloatingButtonMenu(Context context, AttributeSet attributeSet ){
        super(context, attributeSet);
        listButton =  new ArrayList<>();


    }


    public void setOnClickEvent(OnFloatingButtonMenuClick menuClick){
        this.menuClick = menuClick;
    }
    public void addMenuItem(){

    }

    interface OnFloatingButtonMenuClick{

        void OnFloatingButtonMenuClick();

    }
}
