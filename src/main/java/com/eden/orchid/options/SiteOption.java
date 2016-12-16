package com.eden.orchid.options;

public abstract class SiteOption {
    boolean wasFound;

    public abstract String getFlag();
    public abstract boolean parseOption(String[] options);
    public abstract void setDefault();
}
