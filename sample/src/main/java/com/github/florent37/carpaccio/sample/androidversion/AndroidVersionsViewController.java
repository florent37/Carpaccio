package com.github.florent37.carpaccio.sample.androidversion;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class AndroidVersionsViewController {

    public void displayAndroidVersions(View view) {
        if (view instanceof RecyclerView) {
            getAndroidVersions((RecyclerView) view);
        }
    }

    protected void getAndroidVersions(final RecyclerView recyclerView) {

        final AndroidVersionAdapter androidVersionAdapter = new AndroidVersionAdapter();
        recyclerView.setAdapter(androidVersionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        AndroidService androidService = new RestAdapter.Builder()
                .setEndpoint(AndroidService.ENDPOINT)
                .build().create(AndroidService.class);

        androidService.getElements(new Callback<List<AndroidVersion>>() {
            @Override
            public void success(List<AndroidVersion> androidVersions, Response response) {
                androidVersionAdapter.addAndroidVersions(androidVersions);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

    }


}
