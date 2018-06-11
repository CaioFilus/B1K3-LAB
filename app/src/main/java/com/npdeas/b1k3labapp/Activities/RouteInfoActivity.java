package com.npdeas.b1k3labapp.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.npdeas.b1k3labapp.Maps.MapsUtils;
import com.npdeas.b1k3labapp.Npdeas.FileStruct;
import com.npdeas.b1k3labapp.Npdeas.NpDeasReader;
import com.npdeas.b1k3labapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.util.ArrayList;

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
    private GraphView graph;

    private float avarageSpeed = 0;
    private float maxSpeed = 0;

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
        graph = findViewById(R.id.graphView);

        //obtendo os aquivos de rota e img
        File routeFile = new File(getIntent().getStringExtra(getString(R.string.file_route)));
        File imgFile = new File(getIntent().getStringExtra(getString(R.string.file_image)));

        //colocando a imagem no CollapisingToolbar
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imgViewRoute.setImageBitmap(bitmap);

        ArrayList<FileStruct> fileStructs = new ArrayList<>();
        NpDeasReader reader = new NpDeasReader(routeFile);
        float distance = 0;
        long time = 0;
        double oldLat = 0;
        double oldLng = 0;

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        int i = 0;
        FileStruct aux;
        while ((aux = reader.getFileStruct()) != null) {
            fileStructs.add(aux);
            if ((oldLat != 0) && (oldLng != 0)) {
                distance += MapsUtils.getDistance(oldLat, oldLng, aux.getLatitude(),
                        aux.getLongetude());
                if (aux.getSpeed() != 0) {
                    time += (distance / aux.getSpeed());
                }
            }
            series.appendData(new DataPoint(distance,
                    fileStructs.get(i).getSpeed()), true, i + 1);
            //pegando a velociadade maxima e a o somatorio da velocidade media
            if (maxSpeed < aux.getSpeed()) {
                maxSpeed = aux.getSpeed();
            }
            avarageSpeed += aux.getSpeed();

            oldLat = aux.getLatitude();
            oldLng = aux.getLongetude();
            i++;
        }
        //series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series.setDrawDataPoints(false);
        // custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        paint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.redNpDeas));
        series.setCustomPaint(paint);
        graph.addSeries(series);

        String[] dateTime = routeFile.getParentFile().getName().split(" ");
        avarageSpeed = avarageSpeed / i;
        textViewMaxSpd.setText((int) maxSpeed + "m/s");
        textViewAvarageSpd.setText((int) avarageSpeed + "m/s");
        textViewDate.setText(dateTime[0]);
        textViewSchedule.setText(dateTime[1]);
        textViewDis.setText((int) distance + "m");
        textViewTime.setText(time + "s");
        collapsingToolbarLayout.setTitle(routeFile.getName());


        Slide enterSlide = new Slide(Gravity.RIGHT);
        enterSlide.setDuration(500);
        getWindow().setEnterTransition(enterSlide);

        Slide exitSlide = new Slide(Gravity.RIGHT);
        exitSlide.setDuration(500);
        getWindow().setReturnTransition(exitSlide);

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
