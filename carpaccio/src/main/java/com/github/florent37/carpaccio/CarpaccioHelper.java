package com.github.florent37.carpaccio;

import android.support.v4.util.LruCache;
import android.view.View;

import com.github.florent37.carpaccio.model.CarpaccioAction;
import com.github.florent37.carpaccio.model.ObjectAndMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
            CarpaccioLogger.e(TAG, "Cannot construct " + name, e);
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
    public static Object[] getArgumentsWithView(View view, Class[] parametersType, Object[] args) {
        Object[] out = new Object[args.length + 1];

        //add the view on the first parameter
        try {
            out[0] = parametersType[0].cast(view);
        } catch (ClassCastException e) {
            if (LOG_FAILURES)
                CarpaccioLogger.e(TAG, view.getClass().toString() + " cannot be cast to " + parametersType[0].getClass().toString(), e);
            out[0] = view;
        }

        for (int i = 0; i < args.length; ++i) {
            Class paramClass = parametersType[i + 1];
            Object param = args[i];

            if (param instanceof String && isNumber(paramClass)) {
                out[i + 1] = stringToNumber((String) param, paramClass);
            } else {
                try {
                    out[i + 1] = paramClass.cast(param);
                } catch (ClassCastException e) {
                    if (LOG_FAILURES)
                        CarpaccioLogger.e(TAG, param.getClass().toString() + " cannot be cast to " + paramClass.toString(), e);
                    out[i + 1] = param;
                }
            }
        }
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
        String attributes = tag.substring(tag.indexOf('(') + 1, tag.lastIndexOf(')'));
        if (attributes.isEmpty())
            return new String[0];
        return trim(attributes.split(","));
    }

    public static Object removeTag(View view, String actionName) {
        if (view.getTag() != null && view.getTag() instanceof List && actionName!= null) {
            List<CarpaccioAction> actions = (List<CarpaccioAction>) view.getTag();
            List<CarpaccioAction> newActions = new ArrayList<>();

            for (int i = 0, count = actions.size(); i < count; ++i)
                if (!actions.get(i).getCompleteCall().equals(actionName))
                    newActions.add(actions.get(i));

            return newActions;
        }

        return view.getTag();
    }

    /**
     * Trim an array of String (each element)
     */
    public static String[] trim(String[] strings) {
        for (int i = 0; i < strings.length; ++i)
            strings[i] = strings[i].trim();
        return strings;
    }

    public static ObjectAndMethod findObjectWithThisMethod(List<Object> objects, String function, int numberOfParams) {
        if (objects != null && function != null) {
            Method method;
            Object object;
            int numberOfObjects = objects.size();
            for (int j = 0; j < numberOfObjects; ++j) {
                object = objects.get(j);
                int methodCount = object.getClass().getMethods().length;
                for (int i = 0; i < methodCount; ++i) {
                    method = object.getClass().getMethods()[i];
                    if (function.equals(method.getName()) && method.getParameterTypes().length == numberOfParams) {
                        return new ObjectAndMethod(object, method);
                    }
                }
            }

            CarpaccioLogger.v(TAG, "can't find controller with the method " + function + " , controllers=" + objects.toString());
        }

        return null;
    }

    /**
     *
     */
    public static Method callFunction(Object object, String name, View view, Object[] args) {
        if (object != null && name != null && view != null && args != null) {

            Method method = null;
            Class viewClass = View.class;

            //if name = font(Roboto.ttf) with a TextView
            //try to find the font(TextView,String)
            for (Method containedMethods : object.getClass().getMethods()) {
                if (name.equals(containedMethods.getName()) && containedMethods.getParameterTypes().length == args.length + 1) { //+1 for the view
                    method = containedMethods;
                    break;
                }
            }

            //try {
            //    method = object.getClass().getMethod(name, getClasses(args));
            //}catch (Exception e){
            //    Log.v(TAG,object.getClass()+" does not contains the method "+name);
            //}

            return callMethod(object, method, name, view, args);
        }

        return null;
    }

    public static Method callMethod(Object object, Method method, String name, View view, Object[] args) {

        if (method != null && object != null) {

            CarpaccioLogger.d(TAG, view.getClass().getName() + " call method " + name + " on " + object);

            try {
                method.invoke(object, getArgumentsWithView(view, method.getParameterTypes(), args));
                return method;
            } catch (Exception e) {
                CarpaccioLogger.e(TAG, object.getClass() + " cannot invoke method " + name);
            }
        }

        return null;
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
                CarpaccioLogger.v(TAG, object.getClass() + " does not contains the method " + name);
        }

        if (method != null) {
            try {
                return (T) method.invoke(object);
            } catch (Exception e) {
                CarpaccioLogger.e(TAG, object.getClass() + " cannot invoke method " + name);
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
            method = object.getClass().getMethod(name, getClasses(args));
        } catch (Exception e) {
            if (LOG_FAILURES)
                CarpaccioLogger.v(TAG, object.getClass() + " does not contains the method " + name);
        }

        if (method != null) {
            try {
                return (T) method.invoke(object, args);
            } catch (Exception e) {
                CarpaccioLogger.e(TAG, object.getClass() + " cannot invoke method " + name);
            }
        }
        return null;
    }

    public static Integer stringToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            CarpaccioLogger.e(TAG, s + " is not an integer", e);
            return null;
        }
    }

    public static Double stringToDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            CarpaccioLogger.e(TAG, s + " is not an double", e);
            return null;
        }
    }

    public static Long stringToLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            CarpaccioLogger.e(TAG, s + " is not a long", e);
            return null;
        }
    }

    public static Float stringToFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            CarpaccioLogger.e(TAG, s + " is not a long", e);
            return null;
        }
    }

    public static boolean isNumber(Class destinationClass) {
        return
                Integer.class.equals(destinationClass) ||
                        destinationClass.getName().equals("int") ||

                        Float.class.equals(destinationClass) ||
                        destinationClass.getName().equals("float") ||

                        Long.class.equals(destinationClass) ||
                        destinationClass.getName().equals("long") ||

                        Double.class.equals(destinationClass) ||
                        destinationClass.getName().equals("double");
    }

    public static Object stringToNumber(String s, Class destinationClass) {
        if (Integer.class.equals(destinationClass))
            return stringToInt(s);
        else if (destinationClass.getName().equals("int"))
            return stringToInt(s).intValue();
        else if (Float.class.equals(destinationClass))
            return stringToFloat(s);
        else if (destinationClass.getName().equals("float"))
            return stringToFloat(s).floatValue();
        else if (Long.class.equals(destinationClass))
            return stringToLong(s);
        else if (destinationClass.getName().equals("long"))
            return stringToLong(s).longValue();
        else if (Double.class.equals(destinationClass))
            return stringToDouble(s);
        else if (destinationClass.getName().equals("double"))
            return stringToDouble(s).doubleValue();
        else
            return null;
    }

    public static <T extends View> T findParentOfClass(View view, Class<T> theClass) {
        if (theClass.isAssignableFrom(view.getClass()))
            return (T) view;
        else if (view.getParent() != null && view.getParent() instanceof View)
            return findParentOfClass((View) view.getParent(), theClass);
        else
            return null;
    }

    public static Carpaccio findParentCarpaccio(View view) {
        return findParentOfClass(view, Carpaccio.class);
    }

    public static Carpaccio registerToParentCarpaccio(View view) {
        Carpaccio carpaccio = findParentOfClass(view, Carpaccio.class);
        if (carpaccio != null) {
            carpaccio.addCarpaccioView(view);
            return carpaccio;
        }
        return null;
    }
}
