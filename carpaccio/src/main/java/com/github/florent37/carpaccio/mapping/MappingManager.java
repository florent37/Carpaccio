package com.github.florent37.carpaccio.mapping;

import android.view.View;

import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpaccio.model.CarpaccioAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class MappingManager {
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

    /**
     * @param name   the mapped object name, ex : for function($user), the name will be "user"
     * @param object the mapped object
     */
    public String evaluateValue(Object object, String call, String name){
        String value = null;

        if (call.equals(name)) { //"user"
            value = object.toString();
        } else if (call.startsWith(name)) { //"user.getName()"
            String functionName = getFunctionName(call); // "getName"
            value = CarpaccioHelper.callFunction(object, functionName);
        }

        return value;
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

        //call the waiting objects
        List<MappingWaiting> waitingsForThisName = mappingWaitings.get(name);
        if (waitingsForThisName != null) {
            for (MappingWaiting mappingWaiting : waitingsForThisName) {
                String value = evaluateValue(object,mappingWaiting.getCall(),name);

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
        mappedLists.put(name, list);
    }

    public List getMappedList(Object adapter, String name) {
        return mappedLists.get(name);
    }

    /**
     * From user.getName() return "getName"
     */
    protected static String getFunctionName(String call) {
        if (call.contains("(") && call.contains(")"))
            return call.substring(call.indexOf(".") + 1, call.indexOf("("));
        else {
            String[] split = call.split("\\.");
            String firstLetter = split[1].substring(0, 1).toUpperCase();
            String lastLetters = split[1].substring(1, split[1].length());

            return "get" + firstLetter + lastLetters;
        }
    }

    /**
     * Called when a view loaded and call a mapping function
     *
     * @param view         the calling view
     * @param mappedObject If available, the object to map with the view. Else add the view to mappingWaitings
     */
    public void callMappingOnView(CarpaccioAction action, View view, Object mappedObject ) {
        if (action.isCallMapping()) {
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
                String value = evaluateValue(mappedObject,call,objectName);

                action.setValues(new String[]{value}); //TODO

                mappingManagerCallback.callActionOnView(action, view);
            }

            else{
                //add to waiting
                List<MappingWaiting> waitings = mappingWaitings.get(objectName); //["user"] = List<MappingWaiting>
                if (waitings == null)
                    waitings = new ArrayList<>();
                waitings.add(new MappingWaiting(view, action,call,objectName));
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

    public void bindListItem(String mapName, int position) {
        mapObject(mapName, mappedLists.get(mapName).get(position));
    }

    public Object getMappedListsObject(String name, int position) {
        return mappedLists.get(name).get(position);
    }
}
