package com.eden.orchid.api.registration;

public interface Prioritized extends Comparable<Prioritized> {
    default int priority() {
        return 100;
    }

    @Override
    default int compareTo(Prioritized o) {
        if(priority() == o.priority()) {
            return getClass().getName().compareTo(o.getClass().getName());
        }
        else {
            return o.priority() - priority();
        }
    }
}
