package com.npdeas.b1k3labapp;

/**
 * Created by NPDEAS on 18/04/2018.
 */


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class CardArrayAdapter extends RecyclerView.Adapter<CardArrayAdapter.ViewHolder> {
    private ArrayList<CardMap> mDataset;
    private static ItemClickListener itemClickListener;
    private ViewHolder holder;
    private int position;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView cardViewMap;
        public ImageView imgViewMap;
        public TextView textViewTime;
        public TextView textViewDate;
        public TextView textViewTitle;
        public File imgFile;
        public File routeFile;


        public ViewHolder(View itemView) {
            super(itemView);
            cardViewMap = itemView.findViewById(R.id.carfViewMap);
            imgViewMap = itemView.findViewById(R.id.imgViewMap);
            textViewTime = itemView.findViewById(R.id.txtViewTime);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTitle = itemView.findViewById(R.id.textViewRouteTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(routeFile,imgFile, imgViewMap);
            }
        }


    }

    public CardArrayAdapter(ArrayList<CardMap> myDataset) {
        mDataset = myDataset;
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        CardArrayAdapter.itemClickListener = itemClickListener;
    }

    @Override
    public CardArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        holder.textViewTitle.setText(mDataset.get(position).getRouteTitle());
        holder.imgViewMap.setImageBitmap(mDataset.get(position).getImgMap());
        holder.textViewTime.setText(mDataset.get(position).getTimeMap());
        holder.textViewDate.setText(mDataset.get(position).getDateMap());
        holder.imgFile = mDataset.get(position).getImgFile();
        holder.routeFile = mDataset.get(position).getRouteFile();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface ItemClickListener {
        void onItemClick(File routeFile, File imgFile, ImageView image);
    }

}
