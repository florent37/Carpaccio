package com.github.florent37.carpaccio.mapping;

import android.view.View;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class MappingWaiting { //for setText($user.getName())
    public View view;
    public String function; //the function name : setText
    public String call; //the complete call : user.getName()
    public String objectName; //the mapped name : user

    public MappingWaiting(View view, String function, String call, String objectName) {
        this.view = view;
        this.function = function;
        this.call = call;
        this.objectName = objectName;
    }
}
