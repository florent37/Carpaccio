package com.github.florent37.carpaccio.mapping;

import android.view.View;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class MappingWaiting {
    public View view;
    public String function;
    public String arg;
    public String objectName;

    public MappingWaiting(View view, String function, String arg, String objectName) {
        this.view = view;
        this.function = function;
        this.arg = arg;
        this.objectName = objectName;
    }
}
