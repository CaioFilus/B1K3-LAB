package com.npdeas.b1k3labapp;

/**
 * Created by NPDEAS on 18/04/2018.
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.npdeas.b1k3labapp.Database.Structs.Route;
import com.npdeas.b1k3labapp.Utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class CardArrayAdapter extends RecyclerView.Adapter<CardArrayAdapter.ViewHolder> {


    private final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    private Context context;
    private List<Route> mDataset;
    private ItemClickListener itemClickListener;


    public CardArrayAdapter(Context context, List<Route> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    public Route getRoute(int index) {
        return mDataset.get(index);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Route route = mDataset.get(position);
//        String[] dataTime = mDataset.get(position).finishDate.toString().split(" ");
        holder.textViewTitle.setText(route.name);
        holder.imgViewMap.setImageBitmap(FileUtils.readImg(context, route.img));
        holder.textViewTime.setText(formatTime.format(route.startDate));
        holder.textViewDate.setText(formatDate.format(route.startDate));
//        holder.position = position;
        if (route.webId == 0) {
            holder.imgViewSendCloud.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onRouteClick(mDataset.get(holder.getLayoutPosition()),
                            holder.imgViewMap);
                }
            }
        });

        /*holder.imgFile = mDataset.get(position).getImgFile();
        holder.routeFile = mDataset.get(position).getRouteFile();*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardViewMap;

        public ImageView imgViewMap;
        public TextView textViewTime;
        public TextView textViewDate;
        public TextView textViewTitle;
        public ImageView imgViewSendCloud;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewMap = itemView.findViewById(R.id.carfViewMap);
            imgViewMap = itemView.findViewById(R.id.imgViewMap);
            textViewTime = itemView.findViewById(R.id.txtViewTime);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTitle = itemView.findViewById(R.id.textViewRouteTitle);
            imgViewSendCloud = itemView.findViewById(R.id.imgViewSendCloud);
        }

    }

    public interface ItemClickListener {
        void onRouteClick(Route route, ImageView image);
    }
}
