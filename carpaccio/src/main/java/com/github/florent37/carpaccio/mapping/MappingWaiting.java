package com.github.florent37.carpaccio.mapping;

import android.view.View;

import com.github.florent37.carpaccio.model.CarpaccioAction;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class MappingWaiting {
    public View view;
    public CarpaccioAction carpaccioAction;
    public String call; // "user.getName()"
    public String objectName; //"user"

    public MappingWaiting(View view, CarpaccioAction carpaccioAction, String call, String objectName) {
        this.view = view;
        this.carpaccioAction = carpaccioAction;
        this.call = call;
        this.objectName = objectName;
    }

    public View getView() {
        return view;
    }

    public CarpaccioAction getCarpaccioAction() {
        return carpaccioAction;
    }

    public String getCall() {
        return call;
    }

    public String getObjectName() {
        return objectName;
    }
}
