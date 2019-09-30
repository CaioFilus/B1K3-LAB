package com.npdeas.b1k3labapp.UI.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.npdeas.b1k3labapp.R;

public class DividerView extends LinearLayout {

    private View[] views;
    private int pos = 0;
    private int columnsNr;

    public DividerView(Context context) {
        super(context);
        //init(context);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attr){
        this.setMinimumWidth(10);


        //TypedArray a = context.obtainStyledAttributes(attrs);
        setOrientation(HORIZONTAL);


        final TypedArray styleAttributesArray = context.obtainStyledAttributes(attr, R.styleable.DividerView);
        columnsNr = styleAttributesArray.getInteger(R.styleable.DividerView_nrColumns, 2);
        styleAttributesArray.getColor(R.styleable.DividerView_backgroundColor, Color.BLUE);
        styleAttributesArray.recycle();

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                5,
                1.0f
        );
        views = new View[columnsNr];
        for(int i = 0; i < columnsNr; i++){
            views[i] = new View(context);
            views[i].setLayoutParams(param);
            views[i].setBackgroundResource(R.drawable.divider_out);
        }
        views[0].setBackgroundResource(R.drawable.divider_in);
        for(int i = 0; i < columnsNr; i++){
            addView(views[i]);
        }
    }

    public void selectBackground(int pos){
        for(int i = 0; i < columnsNr;i++){
            if(i != pos){
                AnimationDrawable animationDrawable = (AnimationDrawable) views[i].getBackground();
                animationDrawable.start();
            }
        }
    }

}
