package com.npdeas.b1k3labapp.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;

import com.npdeas.b1k3labapp.CardArrayAdapter;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Route.Route;
import com.npdeas.b1k3labapp.Route.RouteUtils;

import java.util.ArrayList;

/**
 * Created by NPDEAS on 17/04/2018.
 */

public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardArrayAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Activity thisActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        thisActivity = this;
        recyclerView = findViewById(R.id.rvCards);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final ArrayList<Route> routes = RouteUtils.listRoutes();

        adapter = CardArrayAdapter.getInstance(routes);
        recyclerView.setAdapter(adapter);

        //seteando a animação de saida
        /*Slide exitSlide = new Slide(Gravity.LEFT);
        exitSlide.setDuration(500);
        getWindow().setExitTransition(exitSlide);*/

        adapter.setOnItemClickListener(new CardArrayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int routeIndex, ImageView image) {
                Intent i = new Intent(RoutesActivity.this, RouteInfoActivity.class);
                i.putExtra(getString(R.string.route_nr), routeIndex);
                ActivityOptions transitionActivityOptions = ActivityOptions
                        .makeSceneTransitionAnimation(RoutesActivity.this, image,image.getTransitionName());
                startActivity(i, transitionActivityOptions.toBundle());
            }
        });

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                routes.get(viewHolder.getAdapterPosition()).remove();
                routes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);

    }

}
