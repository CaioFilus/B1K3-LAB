package com.npdeas.b1k3labapp.UI.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.npdeas.b1k3labapp.CardArrayAdapter;
import com.npdeas.b1k3labapp.Database.AppDatabase;
import com.npdeas.b1k3labapp.Database.Daos.RouteDao;
import com.npdeas.b1k3labapp.Database.Daos.RouteNodeDao;
import com.npdeas.b1k3labapp.Database.Structs.Route;
import com.npdeas.b1k3labapp.Database.Structs.RouteNode;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Route.RouteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NPDEAS on 17/04/2018.
 */

public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardArrayAdapter adapter;
    private FrameLayout frameLayoutRoute;
    private RecyclerView.LayoutManager layoutManager;
    private List<Route> routes;

    private RouteDao routeDao;
    private Activity thisActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        thisActivity = this;
        recyclerView = findViewById(R.id.rvCards);
        frameLayoutRoute = findViewById(R.id.frameLayoutRoute);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        routeDao = AppDatabase.getAppDatabase(this).routeDao();

//        adapter = CardArrayAdapter.getInstance(routes);
//        recyclerView.setAdapter(adapter);

        //seteando a animação de saida
        /*Slide exitSlide = new Slide(Gravity.LEFT);
        exitSlide.setDuration(500);
        getWindow().setExitTransition(exitSlide);*/
        new LoadRoutesTask().execute();

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                AlertDialog.Builder builder = new AlertDialog.Builder(RoutesActivity.this);
                builder.setTitle("Deseja apagar esta rota ?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        routeDao.delete(routes.get(viewHolder.getAdapterPosition()));
                        routes.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
    }

    private class LoadRoutesTask extends AsyncTask<Void, Void, List<Route>> {
        @Override
        protected List<Route> doInBackground(Void... voids) {
            RouteNodeDao routeNodeDao = AppDatabase.getAppDatabase(RoutesActivity.this).routeNodeDao();
            List<Route> routes = routeDao.getAll();
            for (Route route : routes) {
                route.routeNodes = routeNodeDao.findRouteNodesForRoute(route.id);
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<Route> routes) {
            adapter = new CardArrayAdapter(RoutesActivity.this, routes);
            RoutesActivity.this.routes = routes;
            adapter.setOnItemClickListener(new CardArrayAdapter.ItemClickListener() {
                @Override
                public void onRouteClick(Route route, ImageView image) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.addToBackStack(null);
                    RouteInfoActivity routeInfoActivity = new RouteInfoActivity();
                    routeInfoActivity.setRoute(route);
                    routeInfoActivity.show(ft, "Info");
//
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
