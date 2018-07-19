package com.npdeas.b1k3labapp.Activities.Popup_Activityes;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 7/16/2018.
 */

public class EasterEggPopup extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_eater_egg);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int heigh = metrics.heightPixels;

        getWindow().setLayout((int) (width*.8) ,(int) (heigh*.5));
       /* BitmapDrawable ob = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.easter_egg));
        imgViewPopup.setImageDrawable(ob);
        imgViewPopup.setBackground(ob);*/
    }
}
