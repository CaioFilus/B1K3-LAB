package com.npdeas.b1k3labapp.UI.Activities;


import android.app.Dialog;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.npdeas.b1k3labapp.CardArrayAdapter;
import com.npdeas.b1k3labapp.Database.Structs.Route;
import com.npdeas.b1k3labapp.R;
import com.jjoe64.graphview.GraphView;
import com.npdeas.b1k3labapp.Route.RouteInfo;
import com.npdeas.b1k3labapp.Utils.FileUtils;

import java.text.SimpleDateFormat;


/**
 * Created by NPDEAS on 24/04/2018.
 */

public class RouteInfoActivity extends DialogFragment {

    private Route route;
    private final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imgViewRoute;
    private TextView textViewDate;
    private TextView textViewSchedule;
    private TextView textViewDis;
    private TextView textViewTime;
    private TextView textViewMaxSpd;
    private TextView textViewAvarageSpd;
    private GraphView graphView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.collapsable_toolbar, container, false);
        collapsingToolbarLayout = v.findViewById(R.id.collapsingToolbar);
        imgViewRoute = v.findViewById(R.id.imgViewMap);
        textViewDate = v.findViewById(R.id.textViewDateInfo);
        textViewSchedule = v.findViewById(R.id.textViewScheduleInfo);
        textViewDis = v.findViewById(R.id.textViewDisInfo);
        textViewTime = v.findViewById(R.id.textViewTimeInfo);
        textViewMaxSpd = v.findViewById(R.id.textViewMaxSpdInfo);
        textViewAvarageSpd = v.findViewById(R.id.textViewAvarageSpd);
        graphView = v.findViewById(R.id.graphView);

        imgViewRoute.setImageBitmap(null);
        if (route != null) {

            textViewDate.setText(formatDate.format(route.startDate));
            textViewSchedule.setText(formatTime.format(route.startDate));

            imgViewRoute.setImageBitmap(FileUtils.readImg(getContext(), route.img));
            collapsingToolbarLayout.setTitle(route.name);
            RouteInfo routeInfo = new RouteInfo(route);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            int i = 0;
            int routeSize = route.routeNodes.size();
            for (; i < routeSize; i++) {
                series.appendData(new DataPoint(i,
                        route.routeNodes.get(i).speed * 3.6), true, routeSize);
            }
            LineGraphSeries<DataPoint> maxSeries = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, routeInfo.getMaxSpeed() * 3.6),
                    new DataPoint(routeInfo.getDistance(), routeInfo.getMaxSpeed() * 3.6)
            });
            LineGraphSeries<DataPoint> avarageSeries = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, routeInfo.getAvarageSpeed() * 3.6),
                    new DataPoint(routeInfo.getDistance(), routeInfo.getAvarageSpeed() * 3.6)
            });

            //graphview.setDrawBackground(true);
            graphView.setBackgroundColor(getResources().getColor(android.R.color.white));
            graphView.getViewport().setScalable(true);
            graphView.getViewport().setMaxX(200);
            graphView.getViewport().setMaxY(200);
            graphView.getViewport().setScrollable(true);
            graphView.setTitle("Velocidade");
            graphView.setTitleColor(getResources().getColor(android.R.color.black));


            textViewMaxSpd.setText((int) routeInfo.getMaxSpeed() * 3.6 + "km/h");
            textViewAvarageSpd.setText((int) routeInfo.getAvarageSpeed() * 3.6 + "km/h");
            textViewDis.setText(String.valueOf(routeInfo.getDistance()) + " m");


//            textViewDate.setText(route.startDate);
//            textViewSchedule.setText(dateTime[1]);
//            textViewDis.setText(route.getDistance() + "m");

            // legend
            series.setTitle("foo");
            series.setThickness(10);
            series.setColor(R.color.blueLightNpDeas);
            series.setDrawBackground(true);
            series.setAnimated(true);
            //series.setBackgroundColor(R.color.redNpDeas);

            Paint paintMax = new Paint();
            paintMax.setStyle(Paint.Style.FILL_AND_STROKE);
            paintMax.setColor(getResources().getColor(R.color.redNpDeas));

            maxSeries.setTitle("Velocidade Maxima");
            maxSeries.setColor(paintMax.getColor());
            maxSeries.setCustomPaint(paintMax);

            Paint paintAvarege = new Paint();
            paintAvarege.setStyle(Paint.Style.FILL_AND_STROKE);
            paintAvarege.setColor(getResources().getColor(R.color.greenNpDeas));
            paintAvarege.setStrokeWidth(4);
            avarageSeries.setTitle("Velocidade Média");
            avarageSeries.setColor(paintAvarege.getColor());
            avarageSeries.setCustomPaint(paintAvarege);

//   series2.setTitle("bar");
//            series2.setColor(R.color.red_bg);

            graphView.getLegendRenderer().setVisible(true);
            graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


            //series.setDrawDataPoints(true);
        /*series.setDataPointsRadius(5);
        series.setDrawDataPoints(false);
        // custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        paint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.redNpDeas));
        series.setCustomPaint(paint);*/
            graphView.addSeries(series);
            graphView.addSeries(maxSeries);
            graphView.addSeries(avarageSeries);


        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void setRoute(Route route) {

        this.route = route;
    }


//
//            String[] dateTime = route.getDataTime().split(" ");
//            textViewMaxSpd.setText((int) route.getMaxSpeed() * 3.6 + "km/h");
//            textViewAvarageSpd.setText((int) route.getAvarageSpeed() * 3.6 + "km/h");
//            textViewDate.setText(dateTime[0]);
//            textViewSchedule.setText(dateTime[1]);
//            textViewDis.setText(route.getDistance() + "m");
//            textViewTime.setText(route.getTime() + "s");
//
//
//
//        /*Slide enterSlide = new Slide(Gravity.RIGHT);
//        enterSlide.setDuration(500);
//        getWindow().setEnterTransition(enterSlide);
//
//        Slide exitSlide = new Slide(Gravity.RIGHT);
//        exitSlide.setDuration(500);
//        getWindow().setReturnTransition(exitSlide);*/


//        @Override
//        public boolean onOptionsItemSelected (MenuItem item){
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                    supportFinishAfterTransition();
//                    return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//    }
}
