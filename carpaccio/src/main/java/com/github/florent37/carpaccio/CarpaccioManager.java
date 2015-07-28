package com.github.florent37.carpaccio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.florent37.carpaccio.mapping.MappingManager;
import com.github.florent37.carpaccio.model.CarpaccioAction;
import com.github.florent37.carpaccio.model.ObjectAndMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioManager implements MappingManager.MappingManagerCallback {

    protected List<View> carpaccioViews = new ArrayList<>();
    protected Map<String, Object> registerAdapters = new HashMap<>();
    protected List<Object> registerControllers = new ArrayList<>();
    protected MappingManager mappingManager;
    protected Map<String, ObjectAndMethod> savedControllers = new HashMap<>();


    public CarpaccioManager(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
        this.mappingManager.setMappingManagerCallback(this);
    }

    public void addView(View view) {
        carpaccioViews.add(view);
    }

    public boolean isCarpaccioControlledView(View view) {
        if (view.getTag() != null) {
            String tag = view.getTag().toString();
            return !tag.isEmpty() && tag.contains("(") && tag.contains(")"); //TODO use matcher
        } else if (view instanceof TextView && ((TextView) view).getText() != null) {
            String text = ((TextView) view).getText().toString();
            return !text.isEmpty() && text.trim().startsWith("$");
        } else
            return false;
    }

    public void registerController(Object controller) {
        if (controller != null)
            registerControllers.add(controller);
    }

    public void registerControllers(String registerString) {
        String[] registers = registerString.split(";");
        for (String s : registers) {
            String reg = s.trim().replace(";", "");
            if (!reg.isEmpty())
                registerController(CarpaccioHelper.construct(reg));
        }
    }

    public void executeActionsOnViews() {
        executeActionsOnViews(carpaccioViews, null);
    }

    public void executeActionsOnViews(List<View> views, Object mappedObject) {
        for (View view : views) {
            executeActionsOnView(view, mappedObject);
        }
    }

    public void executeActionsOnView(View view) {
        executeActionsOnView(view, null);
    }


    public void executeActionsOnView(View view, Object mappedObject) {
        if (view.getTag() != null) {

            List<CarpaccioAction> actions;

            if (view.getTag() instanceof List) { //if already splitted
                actions = (List<CarpaccioAction>) view.getTag();
            } else { //only do this work the first time
                actions = new ArrayList<>();

                String tag = view.getTag().toString().trim();
                String[] tagCalls = CarpaccioHelper.trim(tag.split(";"));
                for (String call : tagCalls) {
                    if (!call.startsWith("//")) { //skip the "//function(args)"
                        CarpaccioAction carpaccioAction = new CarpaccioAction(call);

                        actions.add(carpaccioAction);
                    }
                }

                view.setTag(actions); //save into view tags, replace the string with the list
            }

            if (actions != null)
                for (CarpaccioAction action : actions) {

                    //if it's a mapped function ex: setText($user)
                    if (mappingManager != null && action.isCallMapping())
                        mappingManager.callMappingOnView(action,view, mappedObject);
                    else //an usual function setText(florent)
                        callActionOnView(action, view);
                }

        }

        if (view instanceof TextView && ((TextView) view).getText() != null) {
            String text = ((TextView) view).getText().toString().trim();

            if (text.startsWith("$")) {
                if(mappingManager != null) {
                    CarpaccioAction carpaccioAction = new CarpaccioAction("setText("+text+")");
                    mappingManager.callMappingOnView(carpaccioAction, view, null);
                }
            }
        }
    }

    public void callActionOnView(CarpaccioAction action, View view) {
        //find the controller for this call
        ObjectAndMethod objectAndMethod = action.getObjectAndMethod();
        if(objectAndMethod == null){
            //check if cached it
            String key = action.getFunction() + (action.getArgs().length + 1);

            objectAndMethod = savedControllers.get(key);

            if(objectAndMethod == null) { //if not cached,
                objectAndMethod = CarpaccioHelper.findObjectWithThisMethod(this.registerControllers, action.getFunction(), action.getArgs().length + 1); //+1 for the view
                savedControllers.put(key,objectAndMethod);
            }
            action.setObjectAndMethod(objectAndMethod);
        }

        if(objectAndMethod != null){
            //call !
            CarpaccioHelper.callMethod(action.getObjectAndMethod().getObject(), action.getObjectAndMethod().getMethod(), action.getFunction(), view, action.getValues());
        }
    }

    public void mapObject(String name, Object object) {
        if (mappingManager != null)
            mappingManager.mapObject(name, object);
    }

    //region mapList

    protected Map<View, List<View>> carpaccioSubViews = new HashMap<>();

    public void mapList(String mappedName, List list) {
        if (mappingManager != null) {
            mappingManager.mapList(mappedName, list);
            notifyAdapterDataSetChanded(registerAdapters.get(mappedName));
        }
    }

    public List getMappedList(String name) {
        if (mappingManager != null)
            return mappingManager.getMappedList(name);
        return null;
    }

    public void registerAdapter(String mapName, Object adapter) {
        if (adapter instanceof RecyclerView.Adapter || adapter instanceof BaseAdapter) {
            this.registerAdapters.put(mapName, adapter);
        }
    }

    public void addChildViews(View view) {
        if (mappingManager != null) {
            List<View> subViews = carpaccioSubViews.get(view);
            if (subViews == null) {
                subViews = new ArrayList<>();
                carpaccioSubViews.put(view, subViews);
            }
            findCarpaccioControlledViews(view, subViews);
        }
    }

    public void bindView(View view, String mapName, int position) {
        if (mappingManager != null) {
            List<View> subViews = carpaccioSubViews.get(view);
            Object mappedObject = mappingManager.getMappedListsObject(mapName, position);
            executeActionsOnViews(subViews, mappedObject);
        }
    }

    public void notifyAdapterDataSetChanded(Object adapter) {
        if (adapter != null) {
            if (adapter instanceof RecyclerView.Adapter) {
                ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
            } else if (adapter instanceof BaseAdapter) {
                ((BaseAdapter) adapter).notifyDataSetChanged();
            }
        }
    }

    public void findCarpaccioControlledViews(View view) {
        findCarpaccioControlledViews(view, this.carpaccioViews);
    }

    public void findCarpaccioControlledViews(View view, List<View> listAddTo) {
        if (isCarpaccioControlledView(view))
            listAddTo.add(view);

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ViewGroup.class.cast(view);
            for (int i = 0; i < viewGroup.getChildCount(); ++i)
                findCarpaccioControlledViews(viewGroup.getChildAt(i), listAddTo);
        }
    }

    //endregion
}
