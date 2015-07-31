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
public class CarpaccioRecyclerViewAdapter<T> extends RecyclerView.Adapter<Holder<T>> {

    Carpaccio carpaccio;
    int layoutResId;
    String mappedName;
    OnItemClickListener<T> onItemClickListener;
    RecyclerViewCallback<T> recyclerViewCallback;

    public CarpaccioRecyclerViewAdapter(Carpaccio carpaccio, int layoutResId, String mappedName) {
        this.carpaccio = carpaccio;
        this.layoutResId = layoutResId;
        this.mappedName = mappedName;

        carpaccio.registerAdapter(mappedName, this);
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerViewCallback<T> getRecyclerViewCallback() {
        return recyclerViewCallback;
    }

    public void setRecyclerViewCallback(RecyclerViewCallback<T> recyclerViewCallback) {
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
    public Holder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = null;
        if (recyclerViewCallback != null) {
            int layoutId = recyclerViewCallback.onCreateViewHolder(viewType);
            if(layoutId > 0)
                cellView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }
        if(cellView == null)
            cellView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);

        carpaccio.addChildViews(cellView);

        if(recyclerViewCallback != null) { //customize holder
            Holder<T> holder = recyclerViewCallback.onCreateViewHolder(cellView, viewType);
            if(holder != null)
                return holder;
        }

        return new Holder<T>(cellView);
    }

    @Override
    public void onBindViewHolder(final Holder<T> holder, final int position) {
        final T mappedObject = (T)carpaccio.bindView(holder.itemView, mappedName, position);
        holder.onItemClickListener = onItemClickListener;
        holder.position = position;
        holder.mappedObject = mappedObject;
        holder.onBind(mappedObject);

        if(recyclerViewCallback != null) {
            recyclerViewCallback.onBind(mappedObject, holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        List mappedList = carpaccio.getMappedList(mappedName);
        if (mappedList == null) return 0;
        else return mappedList.size();
    }
}
