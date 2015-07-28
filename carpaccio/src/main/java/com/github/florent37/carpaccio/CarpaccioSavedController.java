package com.github.florent37.carpaccio;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by florentchampigny on 28/07/15.
 */
public class CarpaccioSavedController {

    String name;
    Object controller;
    Method method;

    public CarpaccioSavedController(String name, Object controller, Method method) {
        this.name = name;
        this.controller = controller;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return controller;
    }

    public String getName() {
        return name;
    }
}
