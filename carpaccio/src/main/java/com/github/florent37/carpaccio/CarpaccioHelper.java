package com.github.florent37.carpaccio;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by florentchampigny on 21/07/15.
 */
class CarpaccioHelper {

    protected static String TAG = "CarpaccioHelper";

    protected static Object construct(String name){
        try {
            Object object = Class.forName(name).newInstance();
            if( object instanceof CarpaccioViewController) //filter only CarpaccioViewControllers
                return object;
        }catch (Exception e){
            Log.e(TAG, "Cannot construct " + name, e);
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
        return tag.substring(0, tag.indexOf('(')).trim();
    }

    protected static String[] getAttributes(String tag){
        String attributes = tag.substring(tag.indexOf('(') + 1, tag.indexOf(')'));
        if(attributes.isEmpty())
            return new String[0];
        return trim(attributes.split(","));
    }

    protected static String[] trim(String[] strings){
        for(int i=0;i<strings.length;++i)
            strings[i]=strings[i].trim();
        return strings;
    }

    protected static void callFunction(Object object, String name, View view, Object[] args){
        Method method = null;

        try {
            method = object.getClass().getMethod(name, getClasses(args));
        }catch (Exception e){
            Log.v(TAG,object.getClass()+" does not contains the method "+name);
        }

        if(method != null) {
            try {
                method.invoke(object, getArguments(view, args));
            }catch (Exception e){
                Log.e(TAG,object.getClass()+" cannot invoke method "+name);
            }
        }
    }

}
