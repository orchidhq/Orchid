package com.eden.orchid.api.registration;

import lombok.Getter;

public abstract class Prioritized implements Comparable<Prioritized> {

    @Getter protected final int priority;

    public Prioritized(int priority) {
        this.priority = priority;
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
