package io.sule.gaugeview;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;


import java.util.Random;
import io.sule.gaugelibrary.GaugeView;


public class GaugeActivity extends Activity{

    private GaugeView mGaugeView;
    private final Random RAND = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauge);

        mGaugeView = (GaugeView) findViewById(R.id.gauge_view);
        mTimer.start();

    }

    private final CountDownTimer mTimer = new CountDownTimer(30000, 1000) {

        @Override
        public void onTick(final long millisUntilFinished) {
            mGaugeView.setTargetValue(RAND.nextInt(101));
        }

        @Override
        public void onFinish() {}
    };


}