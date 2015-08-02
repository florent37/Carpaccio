package com.github.florent37.carpaccio.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpaccio.controllers.adapter.CarpaccioRecyclerViewAdapter;

import java.lang.reflect.Constructor;

import static com.github.florent37.carpaccio.controllers.ControllerHelper.getLayoutIdentifierFromString;
import static com.github.florent37.carpaccio.controllers.ControllerHelper.pxFromDp;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class CommonViewController {

    private static final String TAG = "CommonViewController";

    public void forInclude(ViewGroup viewGroup, String layoutName, int number) throws Exception {
        int layoutResId = ControllerHelper.getLayoutIdentifierFromString(viewGroup.getContext(), layoutName);
        if (layoutResId == 0) {
            Log.e(TAG, "failed to find view layout " + layoutName);
            return;
        }

        if (layoutResId != -1) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            for (int i = 0; i < number; ++i) {
                layoutInflater.inflate(layoutResId, viewGroup, true);
            }
        }
    }

    public void padding(View view, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        view.setPadding(
                (int) pxFromDp(view.getContext(), paddingLeft),
                (int) pxFromDp(view.getContext(), paddingTop),
                (int) pxFromDp(view.getContext(), paddingRight),
                (int) pxFromDp(view.getContext(), paddingBottom)
        );
    }

    public void margin(View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.setMargins(
                    (int) pxFromDp(view.getContext(), marginLeft),
                    (int) pxFromDp(view.getContext(), marginTop),
                    (int) pxFromDp(view.getContext(), marginRight),
                    (int) pxFromDp(view.getContext(), marginBottom)
            );
            view.setLayoutParams(lp);
        }
    }

    public void columns(View view, int number) {
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), number));
        }
    }

    public void adapter(View view, String mappedName, String layoutName) {
        final int layoutResId = getLayoutIdentifierFromString(view.getContext(), layoutName);
        if (layoutResId != -1) {
            final Carpaccio carpaccio = CarpaccioHelper.findParentCarpaccio(view);
            if (carpaccio != null) {
                if (view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    if (recyclerView.getLayoutManager() == null)
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    CarpaccioRecyclerViewAdapter adapter = new CarpaccioRecyclerViewAdapter(carpaccio, layoutResId, mappedName);
                    adapter = carpaccio.registerAdapter(mappedName, adapter); //carpaccio register only 1 adapter;
                    recyclerView.setAdapter(adapter);

                    if (Carpaccio.IN_EDIT_MODE) {
                        LinearLayout linearLayout = replace(recyclerView, LinearLayout.class.getName());
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        for (int i = 0; i < 10; ++i) {
                            View subView = LayoutInflater.from(linearLayout.getContext()).inflate(layoutResId, linearLayout, false);
                            carpaccio.addCarpaccioView(subView);
                            linearLayout.addView(subView);
                        }
                    }
                }
            }
        }
    }

    public void clickStartActivity(final View view, final String activityName) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<?> c = null;
                if (activityName != null) {

                    String acivityPath = activityName;

                    if (acivityPath.startsWith(".")) {
                        acivityPath = view.getContext().getPackageName() + acivityPath;
                    }

                    try {
                        c = Class.forName(acivityPath);

                        view.getContext().startActivity(new Intent(view.getContext(), c));
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public <T extends View> T replace(View view, String viewIdentifier) {
        return replaceViewithTagToRemove(view, viewIdentifier, "replace(" + viewIdentifier + ")");
    }

    public <T extends View> T replaceViewithTagToRemove(View view, String viewIdentifier, String tagToRemove) {
        Context context = view.getContext();
        View newView = null;
        if (viewIdentifier.startsWith("R.layout.") || !viewIdentifier.contains("."))
            newView = replaceByLayout(context, view, viewIdentifier);
        else
            newView = replaceByViewClass(context, view, viewIdentifier);

        if (newView != null) {
            ViewGroup parent = (ViewGroup) view.getParent();

            newView.setLayoutParams(view.getLayoutParams());
            newView.setId(view.getId());

            newView.setTag(CarpaccioHelper.removeTag(view, tagToRemove));

            if (view.getBackground() != null)
                newView.setBackgroundDrawable(view.getBackground());

            if (view instanceof ImageView && newView instanceof ImageView) {
                ImageView imgView = (ImageView) view;
                ImageView imgNewView = (ImageView) newView;

                if (imgView.getDrawable() != null)
                    imgNewView.setImageDrawable(imgView.getDrawable());
            }

            if (view instanceof TextView && newView instanceof TextView) {
                TextView txtView = (TextView) view;
                TextView txtNewView = (TextView) newView;

                if (txtView.getText() != null)
                    txtNewView.setText(txtView.getText());
                if (txtView.getHint() != null)
                    txtNewView.setHint(txtView.getText());
            }

            int index = parent.indexOfChild(view);
            parent.addView(newView, index);
            parent.removeView(view);
        }

        Carpaccio carpaccio = CarpaccioHelper.registerToParentCarpaccio(newView);
        if(carpaccio != null){
            carpaccio.getCarpaccioManager().executeActionsOnView(newView);
        }


        return (T) newView;
    }

    protected View replaceByViewClass(Context context, View view, String viewClassName) { //com.github.florent37.materialviewpager.MaterialViewPager
        View newView = null;
        try {
            Class viewClass = Class.forName(viewClassName);
            Constructor<View> constructor = viewClass.getDeclaredConstructor(Context.class);
            newView = constructor.newInstance(context);
        } catch (Exception e) {
            Log.e(TAG, "failed to find view class " + viewClassName, e);
        }

        return newView;
    }

    protected View replaceByLayout(Context context, View view, String layout) { //R.layout.XXX
        int layoutResId = ControllerHelper.getLayoutIdentifierFromString(context, layout);
        if (layoutResId == 0) {
            Log.e(TAG, "failed to find view layout " + layout);
            return null;
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            return LayoutInflater.from(view.getContext()).inflate(layoutResId, parent, false);
        }
    }

}
