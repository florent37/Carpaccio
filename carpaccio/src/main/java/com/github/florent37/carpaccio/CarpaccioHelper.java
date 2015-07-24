package com.github.florent37.carpaccio;

import android.support.v4.util.LruCache;
import android.view.View;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class CarpaccioHelper {

    public static String TAG = "CarpaccioHelper";

    public static boolean LOG_FAILURES = false;

    protected static LruCache<String, Class> classesCache = new LruCache<>(15);

    public static Object construct(String name) {
        try {
            Class objectClass = classesCache.get(name);
            if (objectClass == null) {
                objectClass = Class.forName(name);
                classesCache.put(name, objectClass);
            }
            return objectClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "Cannot construct " + name, e);
        }
        return null;
    }

    /**
     * Return an array of classes from the args[], with headerClass on the first position
     * From [object1,object2,object3] with headerClass=View.class return [View.class, object1.class, object2.class, object3.class]
     */
    public static Class[] getClassesWithHeaderClass(Object[] args, Class headerClass) {
        Class[] classes = new Class[args.length + 1];
        classes[0] = headerClass;
        for (int i = 0; i < args.length; ++i)
            classes[i + 1] = args[i].getClass();
        return classes;
    }

    /**
     * Return an array of classes from the args[]
     * From [object1,object2,object3] return [object1.class, object2.class, object3.class]
     */
    public static Class[] getClasses(Object[] args) {
        Class[] classes = new Class[args.length];
        for (int i = 0; i < args.length; ++i)
            classes[i] = args[i].getClass();
        return classes;
    }

    /**
     * From arg=["arg1","arg2"] and viewClass= TextView.class and view instance of TextView (but given as a view)
     * return [(TextView)view,"arg1","arg2")];
     */
    public static Object[] getArgumentsWithView(Class viewClass, View view, Object[] args) {
        Object[] out = new Object[args.length + 1];

        //add the view on the first parameter
        try {
            out[0] = viewClass.cast(view);
        } catch (ClassCastException e) {
            if (LOG_FAILURES)
                Log.e(TAG, view.getClass().toString() + " cannot be cast to " + viewClass.toString(), e);
            out[0] = view;
        }

        for (int i = 0; i < args.length; ++i)
            out[i + 1] = args[i];

        return out;
    }

    /**
     * from "myFunction(arg1,arg2)", return "myFunction"
     */
    public static String getFunctionName(String tag) {
        return tag.substring(0, tag.indexOf('(')).trim();
    }

    /**
     * from "myFunction(arg1,arg2)", return ["arg1","arg2"]
     */
    public static String[] getAttributes(String tag) {
        String attributes = tag.substring(tag.indexOf('(') + 1, tag.indexOf(')'));
        if (attributes.isEmpty())
            return new String[0];
        return trim(attributes.split(","));
    }

    /**
     * Trim an array of String (each element)
     */
    public static String[] trim(String[] strings) {
        for (int i = 0; i < strings.length; ++i)
            strings[i] = strings[i].trim();
        return strings;
    }

    public static boolean callFunctionOnObjects(List<Object> objects, final String function, final View view, final String[] args) {
        if (objects != null && function != null && view != null && args != null) {
            for (Object registerObject : objects) {
                if (registerObject != null) {
                    boolean called = CarpaccioHelper.callFunction(registerObject, function, view, args);
                    if (called) {
                            Log.d(TAG, "Called " + function + " on " + registerObject.getClass().getName());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     */
    public static boolean callFunction(Object object, String name, View view, Object[] args) {
        if (object != null && name != null && view != null && args != null) {

            Method method = null;
            Class viewClass = View.class;

            //if name = font(Roboto.ttf) with a TextView
            //try to find the font(TextView,String)
            for (Method containedMethods : object.getClass().getMethods()) {
                if (name.equals(containedMethods.getName()) && containedMethods.getParameterTypes().length == args.length + 1) { //+1 for the view
                    method = containedMethods;
                    viewClass = method.getParameterTypes()[0];
                    break;
                }
            }

            //try {
            //    method = object.getClass().getMethod(name, getClasses(args));
            //}catch (Exception e){
            //    Log.v(TAG,object.getClass()+" does not contains the method "+name);
            //}

            if (method != null) {
                try {
                    method.invoke(object, getArgumentsWithView(viewClass, view, args));
                    return true;
                } catch (Exception e) {
                        Log.e(TAG, object.getClass() + " cannot invoke method " + name);
                }
            } else {
                if (LOG_FAILURES)
                    Log.v(TAG, object.getClass() + " does not contains the method " + name);
            }
        }

        return false;
    }

    /**
     * Invoke the function object.name() with no arguments
     * Then return the result (with cast)
     */
    public static <T> T callFunction(Object object, String name) {
        Method method = null;

        try {
            method = object.getClass().getMethod(name);
        } catch (Exception e) {
            if (LOG_FAILURES)
                Log.v(TAG, object.getClass() + " does not contains the method " + name);
        }

        if (method != null) {
            try {
                return (T) method.invoke(object);
            } catch (Exception e) {
                    Log.e(TAG, object.getClass() + " cannot invoke method " + name);
            }
        }
        return null;
    }

    /**
     * Invoke the function object.name() with no arguments
     * Then return the result (with cast)
     */
    public static <T> T callFunction(Object object, String name, Object[] args) {
        Method method = null;

        try {
            method = object.getClass().getMethod(name,getClasses(args));
        } catch (Exception e) {
            if (LOG_FAILURES)
                Log.v(TAG, object.getClass() + " does not contains the method " + name);
        }

        if (method != null) {
            try {
                return (T) method.invoke(object,args);
            } catch (Exception e) {
                    Log.e(TAG, object.getClass() + " cannot invoke method " + name);
            }
        }
        return null;
    }

}
