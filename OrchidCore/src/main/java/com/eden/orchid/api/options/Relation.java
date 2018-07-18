package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public abstract class Relation<T> implements OptionsHolder {

    protected final OrchidContext context;

    @Getter @Setter
    private Map<String, Object> ref;
    private T item;

    @Inject
    public Relation(OrchidContext context) {
        this.context = context;
    }

    public abstract T load();

    public final T get() {
        if(item == null) {
            extractOptions(context, ref);
            item = load();
        }

        return item;
    }

    public Map<String, Object> parseStringRef(String ref) {
        Map<String, Object> objectRef = new HashMap<>();

        objectRef.put("itemId", ref);

        return objectRef;
    }

}
