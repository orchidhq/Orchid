package com.eden.orchid.utilities;

import java.util.Collection;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

public class ObservableTreeSet<T extends Observable> extends TreeSet<T> implements Observer {

    public ObservableTreeSet() {
    }

    public ObservableTreeSet(Comparator<? super T> comparator) {
        super(comparator);
    }

    public ObservableTreeSet(Collection<? extends T> c) {
        super(c);
    }

    public ObservableTreeSet(SortedSet<T> s) {
        super(s);
    }

    @Override
    synchronized public boolean add(T element) {
        element.addObserver(this);
        return super.add(element);
    }

    @Override
    @SuppressWarnings("unchecked")
    synchronized public void update(Observable element, Object arg) {
        remove(element);
        add((T) element);
    }
}
