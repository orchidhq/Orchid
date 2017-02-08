package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.registration.AutoRegister;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoRegister
public class TwigSortByFilter implements JtwigFunction {

    @Override
    public String name() {
        return "sortBy";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {

        List<Object> fnParams = request.maximumNumberOfArguments(2)
                                     .minimumNumberOfArguments(2)
                                     .getArguments();


        Object value = fnParams.get(0);
        Object[] params = new Object[] { fnParams.get(1) };

        if (value == null) {
            return "";
        }

        if(!isArray(value)) {
            throw new RuntimeException("cannot sort: " + value);
        }

        Object[] array = asArray(value);
        String property = params.length == 0 ? null : asString(params[0]);

        List<Comparable> list = asComparableList(array, property);

        Collections.sort(list);

        return property == null ?
                list.toArray(new Comparable[list.size()]) :
                list.toArray(new SortableMap[list.size()]);
    }

    private List<Comparable> asComparableList(Object[] array, String property) {

        List<Comparable> list = new ArrayList<Comparable>();

        for (Object obj : array) {

            if(obj instanceof Map && property != null) {
                list.add(new SortableMap((Map<String, Comparable>) obj, property));
            }
            else {
                list.add((Comparable) obj);
            }
        }

        return list;
    }

    public boolean isArray(Object value) {

        return value != null && (value.getClass().isArray() || value instanceof List);
    }

    public Object[] asArray(Object value) {

        if(value == null) {
            return null;
        }

        if (value.getClass().isArray()) {
            return (Object[]) value;
        }

        if (value instanceof List) {
            return ((List) value).toArray();
        }

        return new Object[]{value};
    }

    public String asString(Object value) {

        if (value == null) {
            return "";
        }

        if (!this.isArray(value)) {
            return String.valueOf(value);
        }

        Object[] array = this.asArray(value);

        StringBuilder builder = new StringBuilder();

        for (Object obj : array) {
            builder.append(this.asString(obj));
        }

        return builder.toString();
    }

    private class SortableMap extends HashMap<String, Comparable> implements Comparable<SortableMap> {

        final String property;

        SortableMap(Map<String, Comparable> map, String property) {
            super.putAll(map);
            this.property = property;
        }

        @Override
        public int compareTo(SortableMap that) {

            Comparable thisValue = this.get(property);
            Comparable thatValue = that.get(property);

            if(thisValue == null || thatValue == null) {
                throw new RuntimeException("Liquid error: comparison of Hash with Hash failed");
            }

            return thisValue.compareTo(thatValue);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            for(Entry entry : super.entrySet()) {
                builder.append(entry.getKey()).append(entry.getValue());
            }

            return builder.toString();
        }
    }

}
