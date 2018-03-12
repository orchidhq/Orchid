package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;

public abstract class Relation<T> implements OptionsHolder {

    protected final OrchidContext context;

    @Getter @Setter
    private JSONObject ref;
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

    public JSONObject parseStringRef(String ref) {
        JSONObject objectRef = new JSONObject();

        objectRef.put("itemId", ref);

        return objectRef;
    }

}
