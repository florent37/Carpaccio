package com.github.florent37.carpaccio.controllers.adapter;

/**
 * Created by florentchampigny on 12/08/15.
 */
public class Header {

    protected String mappedName;
    protected int layoutId;

    public Header(String mappedName, int layoutId) {
        this.mappedName = mappedName;
        this.layoutId = layoutId;
    }

    public String getMappedName() {
        return mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;

        Header header = (Header) o;

        if (layoutId != header.layoutId) return false;
        return !(mappedName != null ? !mappedName.equals(header.mappedName) : header.mappedName != null);

    }

    @Override
    public int hashCode() {
        int result = mappedName != null ? mappedName.hashCode() : 0;
        result = 31 * result + layoutId;
        return result;
    }
}
