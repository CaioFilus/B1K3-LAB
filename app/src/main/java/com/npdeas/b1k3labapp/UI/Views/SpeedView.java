package com.npdeas.b1k3labapp.UI.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * Created by NPDEAS on 5/17/2018.
 */

public class SpeedView extends AppCompatImageView{
    private Path path;
    private Drawable drawable;
    private Bitmap bitmap;
    public SpeedView(Context context, AttributeSet attributeSet){
            super(context,attributeSet);
            /*
            Resources res = getResources();
            this.setImageResource(R.drawable.ic_acelerometro);
            drawable = res.getDrawable(R.drawable.ic_flecha);*/

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawable.draw(canvas);
    }
}
