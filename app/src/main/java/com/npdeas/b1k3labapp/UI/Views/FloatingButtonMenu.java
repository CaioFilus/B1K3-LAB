package com.npdeas.b1k3labapp.UI.Views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

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
