package com.npdeas.b1k3labapp.Activities;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.npdeas.b1k3labapp.Activities.Fragments.MapsFragment;
import com.npdeas.b1k3labapp.Activities.Fragments.StartRouteFragment;
import com.npdeas.b1k3labapp.Activities.Fragments.TesteFragment;
import com.npdeas.b1k3labapp.Bluetooth.Bluetooth;
import com.npdeas.b1k3labapp.Constants;
import com.npdeas.b1k3labapp.Route.Npdeas.FileNames;
import com.npdeas.b1k3labapp.Route.Npdeas.FileStruct;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;
import com.npdeas.b1k3labapp.Route.Npdeas.NpDeasWriter;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Sensors.Microphone;
import com.npdeas.b1k3labapp.Sensors.NpdeasSensorManager;
import com.npdeas.b1k3labapp.WebDb;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

/**
 * Created by NPDEAS on 5/24/2018.
 */

public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, MapsFragment.GpsFragmentEvent {


    private Bluetooth bluetooth;
    private Microphone mic;
    private NpdeasSensorManager sensors = null;
    private NpDeasWriter manager;
    private FileStruct fileStruct;
    private byte db;
    private int dis;
    private boolean fabFlag = false;
    private boolean isFABOpen = false;
    private ModalType modalType;
    private Drawable lastImgResource;

    private NavigationView navigationView;
    private ViewPager viewPager;
    private StartRouteFragment routeFragment;
    private MapsFragment mapsFragment;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Button fabStartRoute;
    private FloatingActionButton fabModal;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;
    private SpaceTabLayout tabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //iniciando os componentes de tela
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        fabStartRoute = findViewById(R.id.fabStartRoute);
        tabLayout = findViewById(R.id.spaceTabLayout);
        //orientação da tela
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //sincronizando o SlindingMenu e adicionando o listener dele
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        List<Fragment> fragmentList = new ArrayList<>();
        mapsFragment = new MapsFragment();
        routeFragment = new StartRouteFragment();
        TesteFragment testeFragment = new TesteFragment();

        fragmentList.add(mapsFragment);
        fragmentList.add(routeFragment);
        fragmentList.add(testeFragment);

        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(this);
        new Intent("start_route");
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                // Add media control buttons that invoke intents in your media service
                .addAction(android.R.drawable.ic_media_play, "Iniciar", resultPendingIntent) // #0
                .addAction(android.R.drawable.ic_media_pause, "Parar", resultPendingIntent)
                .setContentTitle("Wonderful music")
                .setContentText("My Awesome Band")
                .build();// #1*/
                // Apply the media style template

        //Colocando o SlidingView(aquele movimentinho de ir pro lado na tela)
        /*PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //pegando o objeto de cada Fragment
        mapsFragment = (MapsFragment) pagerAdapter.getItem(0);
        routeFragment = (StartRouteFragment) pagerAdapter.getItem(1);*/
        //splashScreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        }, 0);

        //pedindo permissão do usuario
        final int permissionGranted = PackageManager.PERMISSION_GRANTED;
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != permissionGranted) && (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != permissionGranted) &&
                (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != permissionGranted)) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.REQUEST_OK);
        } else {
            inicialize();
        }
    }

    public void inicialize() {

        mic = new Microphone();
        mic.startRecord();
        bluetooth = new Bluetooth(this);
        new WebDb(this);
        fileStruct = new FileStruct();
        fabModal = findViewById(R.id.fabModal);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        modalType = ModalType.BIKE;
        lastImgResource = fabModal.getDrawable();
                View.OnClickListener fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton aux = (FloatingActionButton) view;
                fabModal.setImageDrawable(aux.getDrawable());
                lastImgResource = aux.getDrawable();
                for(int i = 0; i < 4; i++){
                    if(ModalType.values()[i].valor == aux.getId()){
                        modalType = ModalType.values()[i];
                        break;
                    }
                }
                isFABOpen = false;
                fab1.animate().translationY(0);
                fab2.animate().translationY(0);
                fab3.animate().translationY(0);
                fab4.animate().translationY(0);
            }
        };
        fab1.setOnClickListener(fabListener);
        fab2.setOnClickListener(fabListener);
        fab3.setOnClickListener(fabListener);
        fab4.setOnClickListener(fabListener);

        fabModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    isFABOpen = true;
                    fabModal.setImageResource(android.R.drawable.arrow_down_float);
                    fab1.animate().translationY(-getResources().getDimension(R.dimen.position_fab1));
                    fab2.animate().translationY(-getResources().getDimension(R.dimen.position_fab2));
                    fab3.animate().translationY(-getResources().getDimension(R.dimen.position_fab3));
                    fab4.animate().translationY(-getResources().getDimension(R.dimen.position_fab4));
                } else {
                    isFABOpen = false;
                    fabModal.setImageDrawable(lastImgResource);
                    fab1.animate().translationY(0);
                    fab2.animate().translationY(0);
                    fab3.animate().translationY(0);
                    fab4.animate().translationY(0);
                }
            }
        });


        //adiciona a barra de temperatura
        /*if (sensors.isSensorWork(SensorType.TEMPERATURE)) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentTemp, new TemperatureFragment());
            ft.commit();
        }*/

        //seta o evento do botão
        fabStartRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabFlag) {
                    mapsFragment.startRoute();
                    manager = new NpDeasWriter(FileNames.fileName(MainActivity.this));
                    //fabText.setText("Parar");
                    viewPager.setCurrentItem(1);
                    fabStartRoute.setText("Parar");
                    fabFlag = true;
                } else {
                    fabFlag = false;
                    //fabText.setText("Iniciar");
                    fabStartRoute.setText("Iniciar");
                    viewPager.setCurrentItem(0);
                    manager.close(mapsFragment.finishRoute());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensors != null) {
            sensors.onPause();
        }
        /*if (mapFragment.isEnabled()) {
            mapFragment.onPause();
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if (mapFragment.isEnabled()) {
            mapFragment.onStop();
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (mapFragment.isEnabled()) {
            mapFragment.onStart();
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensors != null) {
            sensors.onResume();
        }
        /*if (mapFragment.isEnabled()) {
            mapFragment.onResume();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetooth != null) {
            if (bluetooth.isConnected()) {
                bluetooth.cancelComunication();
            }
        }
        //mapFragment.onDestroy();
    }

    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permission = true;
        if (requestCode == Microphone.MIC_OK) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != Microphone.MIC_OK) {
                    permission = false;
                    break;
                }
            }
            if (permission) {
                inicialize();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.routes: {
                Intent blueActivity = new Intent(this, RoutesActivity.class);
                startActivity(blueActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
            case R.id.bluetooth: {
                Intent blueActivity = new Intent(this, BluetoothActivity.class);
                startActivity(blueActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
            case R.id.about: {
                Intent aboutActivity = new Intent(this, AboutActivity.class);
                startActivity(aboutActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
            case R.id.exit: {
                finish();
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetGpsLocation(FileStruct fileStruct) {
        fileStruct.setDb((byte) mic.getDbMobilAvarage());
        if (bluetooth.isConnected()) {
            fileStruct.setOvertaking((short) bluetooth.getDistance());
        } else {
            fileStruct.setOvertaking((short) 0);
        }
        this.fileStruct = fileStruct;
        if (fabFlag) {
            manager.addNewLine(fileStruct);
        }
    }

    @Override
    public void onFragmentCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        Log.e("MainActivity ", e.getMessage());
                    }
                    db = (byte) mic.getDbMobilAvarage();
                    dis = bluetooth.getDistance();
                    fileStruct.setDb(db);
                    fileStruct.setOvertaking(dis);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (routeFragment != null) {
                                if (fileStruct != null) {
                                    routeFragment.setRoutePartion(fileStruct);
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
