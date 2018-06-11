package com.npdeas.b1k3labapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 10/04/2018.
 */

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener{

    ImageView imgViewUfpr;
    ImageView imgViewNpdeas;
    TextView textViewPresnt;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgViewUfpr = findViewById(R.id.imgViewUfpr);
        imgViewNpdeas = findViewById(R.id.imgViewNpdeas);
        textViewPresnt = findViewById(R.id.textViewPresent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        animate(imgViewNpdeas);
        animate(textViewPresnt);
        animate(imgViewUfpr);


    }

    private void animate(final View imageView) {

        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        int fadeInDuration = 1000; // Configure time values here
        int timeBetween = 1000;
        int fadeOutDuration = 1000;

        imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends

        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);

        animation.setAnimationListener(this);
        imageView.startAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        imgViewUfpr.setAnimation(null);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
