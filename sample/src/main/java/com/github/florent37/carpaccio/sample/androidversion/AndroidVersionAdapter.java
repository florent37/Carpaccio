package com.github.florent37.carpaccio.sample.androidversion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.carpaccio.sample.R;
import com.github.florent37.carpaccio.sample.model.AndroidVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class AndroidVersionAdapter extends RecyclerView.Adapter<AndroidVersionViewHolder> {

    List<AndroidVersion> androidVersionsList = new ArrayList<>();

    public AndroidVersionAdapter() {
    }

    public void addAndroidVersions(List<AndroidVersion> androidVersionsList) {
        this.androidVersionsList.addAll(androidVersionsList);
        this.notifyDataSetChanged();
    }

    @Override
    public AndroidVersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_androidversion, viewGroup, false);
        return new AndroidVersionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AndroidVersionViewHolder androidVersionViewHolder, int i) {
        androidVersionViewHolder.bind(androidVersionsList.get(i));
    }

    @Override
    public int getItemCount() {
        return androidVersionsList.size();
    }
}