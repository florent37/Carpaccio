package com.github.florent37.carpaccio.model;

import java.lang.reflect.Method;

/**
 * Created by florentchampigny on 28/07/15.
 */
public class ObjectAndMethod {
    Object object;
    Method method;

    public ObjectAndMethod(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
