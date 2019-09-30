package com.npdeas.b1k3labapp.UI.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 7/26/2018.
 */

public class RemoteFinishActivity extends AppCompatActivity{

    public static final int RESULT_OK = 200;
    public static final int RESULT_DENY = 0;

    private Button buttonConfirmRoute;
    private Button buttonCancelRoute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        buttonCancelRoute = findViewById(R.id.buttonCancelRoute);
        buttonConfirmRoute = findViewById(R.id.buttonConfirmRoute);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int heigh = metrics.heightPixels;

        getWindow().setLayout((int) (width*.8) ,(int) (heigh*.5));

        buttonConfirmRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
            }
        });
        buttonCancelRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_DENY);
            }
        });

    }
}
