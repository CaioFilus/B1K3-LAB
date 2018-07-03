package com.npdeas.b1k3labapp.Activities;


import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.npdeas.b1k3labapp.CardArrayAdapter;
import com.npdeas.b1k3labapp.R;
import com.jjoe64.graphview.GraphView;
import com.npdeas.b1k3labapp.Route.Route;


/**
 * Created by NPDEAS on 24/04/2018.
 */

public class RouteInfoActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_route);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        imgViewRoute = findViewById(R.id.imgViewMap);
        textViewDate = findViewById(R.id.textViewDateInfo);
        textViewSchedule = findViewById(R.id.textViewScheduleInfo);
        textViewDis = findViewById(R.id.textViewDisInfo);
        textViewTime = findViewById(R.id.textViewTimeInfo);
        textViewMaxSpd = findViewById(R.id.textViewMaxSpdInfo);
        textViewAvarageSpd = findViewById(R.id.textViewAvarageSpd);
        graphView = findViewById(R.id.graphView);

        //obtendo os aquivos de rota e img

        CardArrayAdapter aux = CardArrayAdapter.getInstance();
        int nrRoute = getIntent().getExtras().getInt(getString(R.string.route_nr));
        Route route = aux.getRoute(nrRoute);
        //colocando a imagem no CollapisingToolbar
        imgViewRoute.setImageBitmap(route.getImg());
        collapsingToolbarLayout.setTitle(route.getRouteName());


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        int i = 0;
        int routeSize = route.getRouteSize();
        for (; i < routeSize; i++) {
            series.appendData(new DataPoint(route.getRouteNode(i).getDistance(),
                    route.getRouteNode(i).getSpeed() * 3.6),true, routeSize);
        }
        LineGraphSeries<DataPoint> maxSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0,route.getMaxSpeed()*3.6),
                new DataPoint(route.getDistance(),route.getMaxSpeed()*3.6)
        });
        LineGraphSeries<DataPoint> avarageSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, route.getAvarageSpeed()*3.6),
                new DataPoint(route.getDistance(), route.getAvarageSpeed()*3.6)
        });

        //graphview.setDrawBackground(true);
        graphView.setBackgroundColor(getResources().getColor(android.R.color.white));
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMaxX(500);
        graphView.getViewport().setMaxY(route.getMaxSpeed());
        graphView.getViewport().setScrollable(true);
        graphView.setTitle("Velocidade");
        graphView.setTitleColor(getResources().getColor(android.R.color.black));


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
        paintMax.setStrokeWidth(4);
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

        String[] dateTime = route.getDataTime().split(" ");
        textViewMaxSpd.setText((int) route.getMaxSpeed()*3.6 + "km/h");
        textViewAvarageSpd.setText((int) route.getAvarageSpeed()*3.6 + "km/h");
        textViewDate.setText(dateTime[0]);
        textViewSchedule.setText(dateTime[1]);
        textViewDis.setText(route.getDistance() + "m");
        textViewTime.setText(route.getTime() + "s");



        /*Slide enterSlide = new Slide(Gravity.RIGHT);
        enterSlide.setDuration(500);
        getWindow().setEnterTransition(enterSlide);

        Slide exitSlide = new Slide(Gravity.RIGHT);
        exitSlide.setDuration(500);
        getWindow().setReturnTransition(exitSlide);*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
