package com.github.florent37.carpaccio.mapping;

import android.view.View;

import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpaccio.CarpaccioLogger;
import com.github.florent37.carpaccio.model.CarpaccioAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class MappingManager {
    private static final String TAG = "CarpaccioMappingManager";
    protected Map<String, Object> mappedObjects = new HashMap<>();
    protected Map<String, List> mappedLists = new HashMap<>();
    protected Map<String, List<MappingWaiting>> mappingWaitings = new HashMap<>();

    public interface MappingManagerCallback {
        void callActionOnView(CarpaccioAction action, View view);
    }

    protected MappingManagerCallback mappingManagerCallback;

    public MappingManager() {
    }

    /**
     * All mapping call must start with $
     * ex : function($user)   function($user.getText())
     * CAN ONLY MAP 1 OBJECT, function($user1,$user2) will be rejected
     */
    public static boolean isCallMapping(String[] args) {
        return args.length == 1 && args[0].startsWith("$");
    }

    //object.image.getUrl()
    public String evaluate(Object object, String call) {
        if (!call.contains(".")) { //"object"
            CarpaccioLogger.d(TAG, "call " + call + " on " + object.getClass().getName());
            return object.toString();
        } else {
            String function = call.substring(call.indexOf('.') + 1); //image.getUrl(); or //image
            String callToGetObject;
            if (function.contains(".")) {
                callToGetObject = function.substring(0, function.indexOf('.')); //image
            } else {
                callToGetObject = function; //image
            }
            String realCallToGetObject = getFunctionName(callToGetObject);
            Object newObject = CarpaccioHelper.callFunction(object, realCallToGetObject);

            if (newObject != null) {
                CarpaccioLogger.d(TAG, "call " + realCallToGetObject + " return =" + newObject.getClass().getName());

                if (newObject instanceof String) {
                    return (String) newObject;
                } else if (newObject instanceof Number) {
                    return String.valueOf(newObject);
                } else
                    return evaluate(newObject, function);
            } else {
                CarpaccioLogger.d(TAG, "call " + realCallToGetObject + " return = NULL");

                return null;
            }
        }
    }


    /**
     * Add an object to the mapper
     * When the object is added, call all the mappingWaitings (views which need this object)
     *
     * @param name   the mapped object name, ex : for function($user), the name will be "user"
     * @param object the mapped object
     */
    public void mapObject(String name, Object object) {
        mappedObjects.put(name, object);

        CarpaccioLogger.d(TAG, "map object [" + name + "," + object.getClass().getName() + "]");

        //call the waiting objects
        List<MappingWaiting> waitingsForThisName = mappingWaitings.get(name);
        if (waitingsForThisName != null) {
            for (MappingWaiting mappingWaiting : waitingsForThisName) {

                CarpaccioLogger.d(TAG, "call waiting mapped " + mappingWaiting.getCarpaccioAction().getCompleteCall());

                String value = evaluate(object, mappingWaiting.getCall());

                CarpaccioLogger.d(TAG, "call waiting value =  " + value);

                if (value != null && mappingManagerCallback != null) {
                    mappingWaiting.getCarpaccioAction().setValues(new String[]{value}); //TODO

                    mappingManagerCallback.callActionOnView(mappingWaiting.getCarpaccioAction(), mappingWaiting.getView());
                }
            }

            //remove all waitings for this name
            waitingsForThisName.clear();
            mappingWaitings.remove(name);
        }
    }

    public void mapList(String name, List list) {
        CarpaccioLogger.d(TAG, "map list " + name + " size=" + list.size());
        mappedLists.put(name, list);
    }

    public List getMappedList(String name) {
        return mappedLists.get(name);
    }

    /**
     * From getName() return getName()
     * From name return getName()
     */
    protected static String getFunctionName(String call) {
        if (call.contains("(") && call.contains(")"))
            return call.replace("()", "");
        else {
            String firstLetter = call.substring(0, 1).toUpperCase();
            String lastLetters = call.substring(1, call.length());

            return "get" + firstLetter + lastLetters;
        }
    }

    /**
     * Called when a view loaded and call a mapping function
     *
     * @param view         the calling view
     * @param mappedObject If available, the object to map with the view. Else add the view to mappingWaitings
     */
    public void callMappingOnView(CarpaccioAction action, View view, Object mappedObject) {

        if (action.isCallMapping()) {

                CarpaccioLogger.d(TAG, "callMappingOnView mapping=" + mappedObject + " action=" + action.getCompleteCall() + " view=" + view.getClass().getName());

            String arg = action.getArgs()[0]; //only map the first argument

            String objectName = null;

            String call;
            if (arg.contains(".")) { //"$user.getName()"
                call = arg.substring(1, arg.length()); // "user.getName()"
                objectName = call.substring(0, arg.indexOf(".") - 1); // "user"
            } else {
                objectName = arg.substring(1, arg.length()); // "user"
                call = objectName; // "user"
            }

            //if you already have the object
            if (mappedObject != null) {
                String value = evaluate(mappedObject, call);

                CarpaccioLogger.d(TAG, "callMappingOnView evaluate(" + call + ")" + " on " + mappedObject.getClass().getName() + " returned " + value);

                action.setValues(new String[]{value}); //TODO

                mappingManagerCallback.callActionOnView(action, view);
            } else {
                //add to waiting
                List<MappingWaiting> waitings = mappingWaitings.get(objectName); //["user"] = List<MappingWaiting>
                if (waitings == null)
                    waitings = new ArrayList<>();
                waitings.add(new MappingWaiting(view, action, call, objectName));

                CarpaccioLogger.d(TAG, "added to waiting " + call + " for " + view.getClass().getName());

                mappingWaitings.put(objectName, waitings);
            }
        }
    }

    public MappingManagerCallback getMappingManagerCallback() {
        return mappingManagerCallback;
    }

    public void setMappingManagerCallback(MappingManagerCallback mappingManagerCallback) {
        this.mappingManagerCallback = mappingManagerCallback;
    }

    public Object getMappedListsObject(String name, int position) {
        return mappedLists.get(name).get(position);
    }
}
