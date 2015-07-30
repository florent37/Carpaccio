package com.github.florent37.carpaccio.controllers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.carpaccio.Carpaccio;

import java.util.List;

/**
 * Created by florentchampigny on 27/07/15.
 */
public class CarpaccioRecyclerViewAdapter extends RecyclerView.Adapter<CarpaccioRecyclerViewAdapter.Holder> {

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int position;
        Object mappedObject;
        OnItemClickListener onItemClickListener;

        public Holder(View itemView) {
            super(itemView);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(mappedObject, position, itemView);
        }

    }

    Carpaccio carpaccio;
    int layoutResId;
    String mappedName;
    OnItemClickListener onItemClickListener;
    RecyclerViewCallback recyclerViewCallback;

    public CarpaccioRecyclerViewAdapter(Carpaccio carpaccio, int layoutResId, String mappedName) {
        this.carpaccio = carpaccio;
        this.layoutResId = layoutResId;
        this.mappedName = mappedName;

        carpaccio.registerAdapter(mappedName, this);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerViewCallback getRecyclerViewCallback() {
        return recyclerViewCallback;
    }

    public void setRecyclerViewCallback(RecyclerViewCallback recyclerViewCallback) {
        this.recyclerViewCallback = recyclerViewCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if (recyclerViewCallback != null) {
            int itemType = recyclerViewCallback.getItemViewType(position);
            if (itemType != -1)
                return itemType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = null;
        if (recyclerViewCallback != null) {
            int layoutId = recyclerViewCallback.onCreateViewHolder(viewType);
            if(layoutId != -1)
                cellView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }
        if(cellView == null)
            cellView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);

        carpaccio.addChildViews(cellView);
        return new Holder(cellView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final Object mappedObject = carpaccio.bindView(holder.itemView, mappedName, position);
        holder.onItemClickListener = onItemClickListener;
        holder.position = position;
        holder.mappedObject = mappedObject;

        if(recyclerViewCallback != null)
            recyclerViewCallback.onBind(mappedObject,holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        List mappedList = carpaccio.getMappedList(mappedName);
        if (mappedList == null) return 0;
        else return mappedList.size();
    }
}
