package com.github.florent37.carpaccio.controllers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.carpaccio.Carpaccio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 27/07/15.
 */
public class CarpaccioRecyclerViewAdapter<T> extends RecyclerView.Adapter<Holder> {

    Carpaccio carpaccio;
    int layoutResId;
    String mappedName;
    OnItemClickListener<T> onItemClickListener;
    RecyclerViewCallback recyclerViewCallback;

    List<Integer> headerViewTypes = new ArrayList<>();
    List<Header> headers = new ArrayList<>();

    public CarpaccioRecyclerViewAdapter(Carpaccio carpaccio, int layoutResId, String mappedName) {
        this.carpaccio = carpaccio;
        this.layoutResId = layoutResId;
        this.mappedName = mappedName;

        carpaccio.registerAdapter(mappedName, this);
    }

    public CarpaccioRecyclerViewAdapter(String mappedName) {
        this.mappedName = mappedName;
    }

    public void setCarpaccio(Carpaccio carpaccio) {
        this.carpaccio = carpaccio;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
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
        int itemType = -1;
        if (recyclerViewCallback != null)
            itemType = recyclerViewCallback.getItemViewType(position);
        if (itemType != -1)
            return itemType;
        else if (position < getHeaderCount()) {
            int viewType =  Integer.MIN_VALUE + position;
            headerViewTypes.add(viewType);
            return viewType;
        }
        else
            return super.getItemViewType(position);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = null;
        if (recyclerViewCallback != null) {
            int layoutId = recyclerViewCallback.onCreateViewHolder(viewType);
            if (layoutId > 0)
                cellView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }

        if(headerViewTypes.contains(viewType)){
            int layoutId = headers.get(headerViewTypes.indexOf(viewType)).layoutId;
            if (layoutId > 0)
                cellView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }

        if (cellView == null)
            cellView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);

        carpaccio.addChildViews(cellView);

        if (recyclerViewCallback != null) { //customize holder
            Holder holder = recyclerViewCallback.onCreateViewHolder(cellView, viewType);
            if (holder != null)
                return holder;
        }

        return new Holder(cellView);
    }

    public Object getItemForRow(View view, int position) {
        if (Carpaccio.IN_EDIT_MODE) {
            return new Object();
        } else if (position < getHeaderCount()) {
            return carpaccio.bindView(view, headers.get(position).mappedName);
        } else {
            return carpaccio.bindView(view, mappedName, position-getHeaderCount());
        }
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final Object mappedObject = getItemForRow(holder.itemView, position);
        holder.onItemClickListener = onItemClickListener;
        holder.position = position;
        holder.mappedObject = mappedObject;
        holder.onBind(mappedObject);

        if (recyclerViewCallback != null) {
            recyclerViewCallback.onBind(mappedObject, holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        int count = getHeaderCount();

        List mappedList = carpaccio.getMappedList(mappedName);
        if (mappedList != null)
            count += mappedList.size();

        return count;
    }

    public int getHeaderCount() {
        return headers.size();
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

}
