package com.github.florent37.carpacciocontrollers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.carpaccio.Carpaccio;

import java.util.List;

/**
 * Created by florentchampigny on 27/07/15.
 */
public class CarpaccioRecyclerViewAdapter extends RecyclerView.Adapter {

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }

    Carpaccio carpaccio;
    int layoutResId;
    String mappedName;

    public CarpaccioRecyclerViewAdapter(Carpaccio carpaccio, int layoutResId, String mappedName) {
        this.carpaccio = carpaccio;
        this.layoutResId = layoutResId;
        this.mappedName = mappedName;

        carpaccio.registerAdapter(mappedName,this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        carpaccio.getCarpaccioManager().bindChildViews(cellView);
        return new Holder(cellView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        carpaccio.getCarpaccioManager().bindView(holder.itemView, mappedName, position);
    }

    @Override
    public int getItemCount() {
        List mappedList = carpaccio.getMappedList(this,mappedName);
        if (mappedList == null) return 0;
        else return mappedList.size();
    }
}
