package com.github.florent37.carpaccio.model;

import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpaccio.mapping.MappingManager;

/**
 * Created by florentchampigny on 28/07/15.
 */
public class CarpaccioAction{
    protected String completeCall;
    protected String function;
    protected String[] args;
    protected boolean isCallMapping;
    protected ObjectAndMethod objectAndMethod;

    protected String[] values;

    public CarpaccioAction(String completeCall) {
        this.completeCall = completeCall;

        setFunction(CarpaccioHelper.getFunctionName(completeCall));
        setArgs(CarpaccioHelper.getAttributes(completeCall));
        setValues(getArgs()); //by default : values = args; if mapping, values will be calculated
        setIsCallMapping(MappingManager.isCallMapping(getArgs()));
    }

    public boolean isCallMapping() {
        return isCallMapping;
    }

    public void setIsCallMapping(boolean isCallMapping) {
        this.isCallMapping = isCallMapping;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getCompleteCall() {
        return completeCall;
    }

    public void setCompleteCall(String completeCall) {
        this.completeCall = completeCall;
    }

    public ObjectAndMethod getObjectAndMethod() {
        return objectAndMethod;
    }

    public void setObjectAndMethod(ObjectAndMethod objectAndMethod) {
        this.objectAndMethod = objectAndMethod;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarpaccioAction)) return false;

        CarpaccioAction that = (CarpaccioAction) o;

        return !(completeCall != null ? !completeCall.equals(that.completeCall) : that.completeCall != null);

    }

    @Override
    public int hashCode() {
        return completeCall != null ? completeCall.hashCode() : 0;
    }

    @Override
    public String toString() {
        return completeCall;
    }
}
