package com.eden.orchid.utilities;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class AlwaysSortedTreeSet<T> extends TreeSet<T> {

    public AlwaysSortedTreeSet() {
    }

    public AlwaysSortedTreeSet(Comparator<? super T> comparator) {
        super(comparator);
    }

    public AlwaysSortedTreeSet(Collection<? extends T> c) {
        super(c);
    }

    public AlwaysSortedTreeSet(SortedSet<T> s) {
        super(s);
    }

    @Override
    public Iterator<T> iterator() {
        TreeSet<T> treeSet = new TreeSet<>();

        Iterator<T> iterator = super.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            treeSet.add(item);
        }

        return treeSet.iterator();
    }
}
