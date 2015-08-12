package com.github.florent37.carpaccio.controllers;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpaccio.CarpaccioManager;
import com.github.florent37.carpaccio.controllers.adapter.CarpaccioRecyclerViewAdapter;
import com.github.florent37.carpaccio.controllers.adapter.Header;
import com.github.florent37.carpaccio.controllers.adapter.Holder;
import com.github.florent37.carpaccio.controllers.adapter.RecyclerViewCallbackAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 12/08/15.
 */
public class RecyclerViewController {

    public void columns(View view, int number) {
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), number));
        }
    }

    List<Header> headers = new ArrayList<>();
    public void addHeader(View view, String mappedName, String layoutName){
        final int layoutResId = ControllerHelper.getLayoutIdentifierFromString(view.getContext(), layoutName);
        Header header =  new Header(mappedName, layoutResId);
        if (layoutResId != -1 && !headers.contains(header)) {
            headers.add(header);
        }else{
            throw new CarpaccioManager.CarpaccioException("can't find layout "+layoutName);
        }
    }

    public void setAdapterForRecyclerView(View view, String mappedName, String layoutName, CarpaccioRecyclerViewAdapter adapter){
        final int layoutResId = ControllerHelper.getLayoutIdentifierFromString(view.getContext(), layoutName);
        if (layoutResId != -1) {
            final Carpaccio carpaccio = CarpaccioHelper.findParentCarpaccio(view);
            if (carpaccio != null) {
                if (view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    if (recyclerView.getLayoutManager() == null)
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                    adapter.setCarpaccio(carpaccio);
                    adapter.setLayoutResId(layoutResId);

                    adapter = carpaccio.registerAdapter(mappedName, adapter); //carpaccio register only 1 adapter;

                    recyclerView.setAdapter(adapter);

                    if (Carpaccio.IN_EDIT_MODE) {
                        CommonViewController commonViewController = new CommonViewController();
                        LinearLayout linearLayout = commonViewController.replace(recyclerView, LinearLayout.class.getName());
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                        for(Header header : headers){
                            View subView = LayoutInflater.from(linearLayout.getContext()).inflate(header.getLayoutId(), linearLayout, false);
                            carpaccio.addCarpaccioView(subView);
                            linearLayout.addView(subView);
                        }

                        for (int i = 0; i < 10; ++i) {
                            View subView = LayoutInflater.from(linearLayout.getContext()).inflate(layoutResId, linearLayout, false);
                            carpaccio.addCarpaccioView(subView);
                            linearLayout.addView(subView);
                        }
                    }
                }
            }
        }else{
            throw new CarpaccioManager.CarpaccioException("can't find layout "+layoutName);
        }
    }

    public void adapter(View view, String mappedName, String layoutName) {
        CarpaccioRecyclerViewAdapter adapter = new CarpaccioRecyclerViewAdapter(mappedName);

        for(Header header : headers)
            adapter.addHeader(header);

        setAdapterForRecyclerView(view,mappedName,layoutName,adapter);
    }

}
