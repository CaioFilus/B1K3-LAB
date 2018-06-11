package com.npdeas.b1k3labapp.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.npdeas.b1k3labapp.CardArrayAdapter;
import com.npdeas.b1k3labapp.CardMap;
import com.npdeas.b1k3labapp.Npdeas.FileConstants;
import com.npdeas.b1k3labapp.Npdeas.NpDeasReader;
import com.npdeas.b1k3labapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by NPDEAS on 17/04/2018.
 */

public class RoutesActivity extends AppCompatActivity {

    private ArrayList<CardMap> cardMaps;

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
        cardMaps = new ArrayList<>();

        final NpDeasReader reader = new NpDeasReader();
        final ArrayList<File> routeFiles = reader.getRouteFiles();
        String[] nameRoute = reader.getRouteNamesFiles(routeFiles);
        final ArrayList<File> imgFiles = reader.getImage();
        final Bitmap imgs[] = new Bitmap[imgFiles.size()];
        for (int i = 0; i < imgs.length;i++){
            imgs [i] = BitmapFactory.decodeFile(imgFiles.get(i).getAbsolutePath());
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FileConstants.DATA_FORMAT);
        for(int i  =0;i<nameRoute.length;i++){
            String[] dateTime =  sdf.format(routeFiles.get(i).lastModified()).split(" ");
            CardMap aux = new CardMap();
            aux.setRouteTitle(nameRoute[i]);
            aux.setDateMap(dateTime[0]);
            aux.setTimeMap(dateTime[1]);
            aux.setImgMap(imgs[i]);
            aux.setRouteFile(routeFiles.get(i));
            aux.setImgFile(imgFiles.get(i));
            cardMaps.add(aux);
        }

        adapter = new CardArrayAdapter(cardMaps);
        recyclerView.setAdapter(adapter);

        //seteando a animação de saida
        Slide exitSlide = new Slide(Gravity.LEFT);
        exitSlide.setDuration(500);
        getWindow().setExitTransition(exitSlide);

        adapter.setOnItemClickListener(new CardArrayAdapter.ItemClickListener(){
            @Override
            public void onItemClick(File routeFile,File imgFile, ImageView image) {
                Intent i = new Intent(RoutesActivity.this, RouteInfoActivity.class);
                /*Bitmap routeImg = ((BitmapDrawable)image.getDrawable()).getBitmap();

                routeImg = NpdeasUtils.getResizedBitmap(routeImg,500);*/
                i.putExtra(getString(R.string.file_route),routeFile.getAbsolutePath());
                i.putExtra(getString(R.string.file_image),imgFile.getAbsolutePath());

                Pair<View,String> pair1 = Pair.create((View)image,image.getTransitionName());
                ActivityOptions transitionActivityOptions = ActivityOptions
                        .makeSceneTransitionAnimation(RoutesActivity.this, pair1);
                startActivity(i, transitionActivityOptions.toBundle());
            }
        });

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT , ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                NpDeasReader.removeFile(routeFiles.get(viewHolder.getAdapterPosition()));
                NpDeasReader.removeFile(imgFiles.get(viewHolder.getAdapterPosition()));
                cardMaps.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);

    }

}
