package com.eden.orchid.api.registration;

import java.util.Observable;

public abstract class Prioritized extends Observable implements Comparable<Prioritized>, Contextual {

    protected int priority = 100;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (this.priority != priority) {
            setChanged();
        }

        this.priority = priority;

        if (hasChanged()) {
            notifyObservers();
        }
    }

    @Override
    public int compareTo(Prioritized o) {
        if(getPriority() == o.getPriority()) {
            return getClass().getName().compareTo(o.getClass().getName());
        }
        else {
            return o.getPriority() - getPriority();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Prioritized that = (Prioritized) o;

        if(getPriority() == that.getPriority()) {
            return getClass().getName().equals(that.getClass().getName());
        }
        else {
            return that.getPriority() == getPriority();
        }
    }

    @Override
    public int hashCode() {
        return getPriority();
    }
}
