package com.github.florent37.carpaccio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.florent37.carpaccio.mapping.MappingManager;

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
        executeActionsOnViews(carpaccioViews);
    }

    public void executeActionsOnViews(List<View> views) {
        for (View view : views) {
            executeActionsOnView(view);
        }
    }

    public void executeActionsOnView(View view) {
        if (view.getTag() != null) {
            String tag = view.getTag().toString().trim();
            String[] calls = CarpaccioHelper.trim(tag.split(";"));
            for (String call : calls) {
                if (!call.startsWith("//")) {
                    String function = CarpaccioHelper.getFunctionName(call);
                    String[] args = CarpaccioHelper.getAttributes(call);

                    //if it's a mapped function ex: setText($user)
                    if (mappingManager != null && mappingManager.isCallMapping(args))
                        mappingManager.callMapping(function, view, args);
                    else
                        //an usual function setText(florent)
                        callFunctionOnControllers(function, view, args);
                }
            }
        }

        if (view instanceof TextView && ((TextView) view).getText() != null) {
            String text = ((TextView) view).getText().toString().trim();

            if (text.startsWith("$")) {
                mappingManager.callMapping("setText", view, new String[]{text});
            }
        }
    }


    public void callFunctionOnControllers(final String function, final View view, final String[] args) {
        CarpaccioHelper.callFunctionOnObjects(this.registerControllers, function, view, args);
    }

    public void mapObject(String name, Object object) {
        if (mappingManager != null)
            mappingManager.mapObject(name, object);
    }

    //region mapList

    protected Map<View,List<View>> carpaccioSubViews = new HashMap<>();

    public void mapList(String mappedName, List list) {
        if (mappingManager != null) {
            mappingManager.mapList(mappedName, list);
            notifyAdapterDataSetChanded(registerAdapters.get(mappedName));
        }
    }

    public List getMappedList(Object adapter, String name) {
        if (mappingManager != null)
            return mappingManager.getMappedList(adapter, name);
        return null;
    }

    public void registerAdapter(String mapName, Object adapter) {
        if (adapter instanceof RecyclerView.Adapter || adapter instanceof BaseAdapter) {
            this.registerAdapters.put(mapName, adapter);
        }
    }

    public void bindChildViews(View view) {
        if (mappingManager != null) {
            List<View> subViews = carpaccioSubViews.get(view);
            if(subViews == null) {
                subViews = new ArrayList<>();
                carpaccioSubViews.put(view,subViews);
            }
            findCarpaccioControlledViews(view,subViews);
            executeActionsOnViews(subViews);
        }
    }

    public void bindView(View view, String mapName, int position) {
        if (mappingManager != null) {
            List<View> subViews = carpaccioSubViews.get(view);
            mappingManager.bindListItem(mapName, position);
            executeActionsOnViews(subViews);
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
        findCarpaccioControlledViews(view,this.carpaccioViews);
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
