package com.github.florent37.carpaccio.mapping;

import android.view.View;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.CarpaccioHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 23/07/15.
 */
public class MappingManager {
    protected Map<String, Object> mappedObjects = new HashMap<>();
    protected Map<String, List<MappingWaiting>> mappingWaitings = new HashMap<>();

    public interface MappingManagerCallback{
        void callFunctionOnControllers(String function, View view, String[] args);
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
     * Add an object to the mapper
     * When the object is added, call all the mappingWaitings (views which need this object)
     * @param name the mapped object name, ex : for function($user), the name will be "user"
     * @param object the mapped object
     */
    public void mapObject(String name, Object object) {
        mappedObjects.put(name, object);

        //call the waiting objects
        List<MappingWaiting> waitings = mappingWaitings.get(name);
        if (waitings != null) {
            for (MappingWaiting mappingWaiting : waitings) {
                String value = null;
                if(mappingWaiting.call.equals(name)) { //"user"
                    value = object.toString();
                }else if(mappingWaiting.call.startsWith(name)){ //"user.getName()"
                    String functionName = getFunctionName(mappingWaiting.call);
                    value = CarpaccioHelper.callFunction(object, functionName);
                }

                if(value != null && mappingManagerCallback != null) {
                    mappingManagerCallback.callFunctionOnControllers(mappingWaiting.function, mappingWaiting.view, new String[]{value});
                    mappingWaitings.remove(name);
                }
            }
        }
    }

    /**
     * From user.getName() return "getName"
     */
    protected static String getFunctionName(String call){
        if(call.contains("(") && call.contains(")"))
            return call.substring(call.indexOf(".") + 1, call.indexOf("("));
        else
        {
            String[] split = call.split("\\.");
            String firstLetter = split[1].substring(0,1).toUpperCase();
            String lastLetters = split[1].substring(1,split[1].length());

            return "get"+firstLetter+lastLetters;
        }
    }

    /**
     * Called when a view loaded and call a mapping function
     * @param function the function name, eg "setText($user)"
     * @param view the calling view
     * @param args args, eg : [$user] or [$user.getName()]
     */
    public void callMapping(String function, View view, String[] args) {
        if (isCallMapping(args)) {
            String arg = args[0];

            String objectName = null;

            String call;
            if(arg.contains(".")) { //"$user.getName()"
                call = arg.substring(1, arg.length()); // "user.getName()"
                objectName = call.substring(0, arg.indexOf(".")-1); // "user"
            }
            else {
                objectName = arg.substring(1, arg.length()); // "user"
                call = objectName; // "user"
            }


            { //add to waiting
                List<MappingWaiting> waitings = mappingWaitings.get(objectName);
                if (waitings == null)
                    waitings = new ArrayList<>();
                waitings.add(new MappingWaiting(view, function, call, objectName));
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

}
