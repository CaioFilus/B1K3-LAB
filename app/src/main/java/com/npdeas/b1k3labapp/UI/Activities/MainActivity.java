package com.npdeas.b1k3labapp.UI.Activities;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.npdeas.b1k3labapp.Bluetooth.IotDevice;
import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.Database.UserTask;
import com.npdeas.b1k3labapp.UI.Fragments.MapsFragment;
import com.npdeas.b1k3labapp.UI.Fragments.RouteInfoFragment;
import com.npdeas.b1k3labapp.UI.Fragments.TesteFragment;
import com.npdeas.b1k3labapp.UI.Views.ModalFloatingMenu;
import com.npdeas.b1k3labapp.UI.Views.RouteControllButton;
import com.npdeas.b1k3labapp.Constants;
import com.npdeas.b1k3labapp.Notification.RouteNotification;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Sensors.Microphone;
import com.npdeas.b1k3labapp.Services.RouteService;
import com.npdeas.b1k3labapp.Webserver.WebserviceDAO;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

/**
 * Created by NPDEAS on 5/24/2018.
 */

public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, MapsFragment.GpsFragmentEvent {

    private Intent routeIntent;
    private boolean serviceBind = false;
    private boolean bluetoothState = false;

    private RouteService routeService;
    //private WebDb webDb;
    private boolean fabFlag = false;
    private WebserviceDAO webservice;

    private NavigationView navigationView;
    private ViewPager viewPager;
    private RouteInfoFragment routeFragment;
    private MapsFragment mapsFragment;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RouteControllButton fabStartRoute;
    private SpaceTabLayout tabLayout;
    private ModalFloatingMenu floatingMenu;


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
        floatingMenu = findViewById(R.id.fabMenuModal);
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
        routeFragment = new RouteInfoFragment();
        TesteFragment testeFragment = new TesteFragment();

        fragmentList.add(mapsFragment);
        fragmentList.add(routeFragment);
        fragmentList.add(testeFragment);

        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

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

//        webservice = WebserviceDAO.getInstance(this);

        routeIntent = new Intent(this, RouteService.class);
        bindService(routeIntent, mConnection, 0);
        startService(routeIntent);

        floatingMenu.setModal(User.getCurrentUser().modalType);
        fabStartRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabFlag) {
                    if (serviceBind) {
                        routeService.startTracking(floatingMenu.getModal());
                        switchButton();

                    } else {
                        Log.i("Main Activityy", "Serviço não criado");
                    }

                } else {
                    routeService.stopTracking(mapsFragment.getScreenshot());
                    switchButton();

                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_reconnect: {
                if (serviceBind) {

                    if (!bluetoothState) {
                        routeService.connectBluetooth();
                    } else {
                        routeService.disconnectBluetooth();
                    }
                    bluetoothState = !bluetoothState;
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //libera o  microphone para ser usado em outro app
//        if (serviceBind) {
//            if (!routeService.isRouting()) {
//                routeService.releaseMicrophone();
//            }
//        }

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
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            unbindService(mConnection);
        }
        stopService(routeIntent);
    }

    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
            case R.id.form: {
                Intent formActivity = new Intent(this, FormActivity.class);
                startActivity(formActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }

            case R.id.about: {
                Intent aboutActivity = new Intent(this, AboutActivity.class);
                startActivity(aboutActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
            case R.id.logout: {
                UserTask userTask = new UserTask(MainActivity.this);
                userTask.deleteAllTask();
                User.setCurrentUser(null);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.exit: {
                finish();
            }
        }
        return false;
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (routeService != null) {
                                routeFragment.setRoutePartion(routeService.getRouteNode());
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void switchButton() {
        if (!fabFlag) {
            viewPager.setCurrentItem(1);
            fabStartRoute.setText("Parar");
            fabFlag = true;
        } else {
            fabFlag = false;
            fabStartRoute.setText("Iniciar");
            viewPager.setCurrentItem(0);
        }
    }

    public ModalType getModalType() {
        return floatingMenu.getModal();
    }


    /**
     * Escuta os serviços conecetados ao Main
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            RouteService.LocalBinder binder = (RouteService.LocalBinder) service;
            routeService = binder.getService();
            routeService.getIotDevice().setConnHandler(new IotDevice.ConnHandler() {
                @Override
                public void onConnected(BluetoothDevice device) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "B1k3 Lab Conectado",
                                    Toast.LENGTH_SHORT).show();
                            routeFragment.setIotDeviceStatus(true);
                        }
                    });
                }

                @Override
                public void onConnError(BluetoothDevice device, String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "B1k3 Lab Desconectado",
                                    Toast.LENGTH_SHORT).show();
                            routeFragment.setIotDeviceStatus(false);
                        }
                    });
                }
            });

            serviceBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBind = false;
        }
    };
}
