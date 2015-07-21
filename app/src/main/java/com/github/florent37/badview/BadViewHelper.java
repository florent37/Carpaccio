package com.github.florent37.badview;

import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by florentchampigny on 21/07/15.
 */
class BadViewHelper {

    protected static Object construct(String name){
        try {
            return Class.forName(name).newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Class[] getClasses(Object[] args){
        Class[] classes = new Class[args.length+1];
        classes[0] = View.class;
        for(int i=0;i<args.length;++i)
            classes[i+1] = args[i].getClass();
        return classes;
    }

    protected static Object[] getArguments(View view, Object[] args){
        Object[] out = new Object[args.length+1];
        out[0] = view;
        for(int i=0;i<args.length;++i)
            out[i+1] = args[i];
        return out;
    }

    protected static String getFunctionName(String tag){
        return tag.substring(0, tag.indexOf('('));
    }

    protected static String[] getAttributes(String tag){
        String attributes = tag.substring(tag.indexOf('(') + 1, tag.indexOf(')'));
        return attributes.split(",");
    }

    protected static void callFunction(Object object, String name, View view, Object[] args){
        Method method = null;

        try {
            method = object.getClass().getMethod(name, getClasses(args));
        }catch (Exception e){
            e.printStackTrace();
        }

        if(method != null) {
            try {
                method.invoke(object, getArguments(view, args));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
