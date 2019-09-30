package com.npdeas.b1k3labapp.UI.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.npdeas.b1k3labapp.R;

public class RouteControllButton extends AppCompatButton {


    private static int TEXT_SIZE = 25;
    private static int SMALL_TEXT_SIZE = 10;
    private static String START_TEXT = "Iniciar";
    private static String STOP_TEXT = "Iniciar";

    private TransitionDrawable mGradientDrawable;
    private AnimatorSet mMorphingAnimatorSet;
    private boolean mIsMorphingInProgress = false;
    private boolean buttonFlag = false;


    public RouteControllButton(Context context) {
        super(context);
        init(context);
    }

    public RouteControllButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RouteControllButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mGradientDrawable = (TransitionDrawable) getResources().getDrawable(R.drawable.button_animation);
        setBackground(mGradientDrawable);
        setElevation(20);
        setText("Iniciar");
        setTextColor(Color.WHITE);
        setTextSize((float) TEXT_SIZE);
//        setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
    }

    public void startAnimation() {
        /*if(mState != State.IDLE){
            return;
        }*/

        int initialWidth = getWidth();
        int initialHeight = getHeight();

        //mState = State.PROGRESS;
        mIsMorphingInProgress = true;
        this.setText(null);
        setClickable(false);

        int toWidth = 40; //some random value...
        int toHeight = toWidth; //make it a perfect circle
        final TransitionDrawable drawable = (TransitionDrawable) getBackground();

        ValueAnimator inTextAnimation = ValueAnimator.ofInt(TEXT_SIZE, SMALL_TEXT_SIZE);
        inTextAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                RouteControllButton.this.setTextSize(val);
            }
        });
        ValueAnimator outTextAnimation = ValueAnimator.ofInt(SMALL_TEXT_SIZE, TEXT_SIZE);
        outTextAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                RouteControllButton.this.setTextSize(val);
            }
        });

        ValueAnimator inWidthAnimation = ValueAnimator.ofInt(initialWidth, toWidth);
        inWidthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = val;
                setLayoutParams(layoutParams);
            }
        });

        ValueAnimator inHeightAnimation = ValueAnimator.ofInt(initialHeight, toHeight);
        inHeightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = val;
                setLayoutParams(layoutParams);
            }
        });


        ValueAnimator outWidthAnimation = ValueAnimator.ofInt(toWidth, initialWidth);
        outWidthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = val;
                setLayoutParams(layoutParams);
            }
        });

        ValueAnimator outHeightAnimation = ValueAnimator.ofInt(toHeight, initialHeight);
        outHeightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = val;
                setLayoutParams(layoutParams);
            }
        });


        mMorphingAnimatorSet = new AnimatorSet();
        mMorphingAnimatorSet.setDuration(300);

        mMorphingAnimatorSet.play(outHeightAnimation).with(outWidthAnimation).with(outTextAnimation).after(inWidthAnimation);
        mMorphingAnimatorSet.play(inWidthAnimation).with(inHeightAnimation).with(inTextAnimation);

        mMorphingAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setClickable(true);
                mIsMorphingInProgress = false;
            }
        });

        mMorphingAnimatorSet.start();
        if (!buttonFlag) {
            drawable.startTransition(300);
        }else{
            drawable.reverseTransition(300);
        }
        buttonFlag = !buttonFlag;
    }


    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        OnClickListener mClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
                l.onClick(view);
            }
        };
        super.setOnClickListener(mClickListener);
    }


    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}

