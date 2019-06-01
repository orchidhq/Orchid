package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.archetypes.SharedConfigArchetype;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Archetype(value = SharedConfigArchetype.class, key = "from")
public abstract class Relation<T> implements OptionsHolder {
    protected final OrchidContext context;
    private Map<String, Object> ref;
    @Nullable
    private T item;
    @Nullable
    private T defaultItem;

    @Inject
    public Relation(OrchidContext context) {
        this.context = context;
    }

    @Nullable
    public abstract T load();

    public final void set(@Nullable T item) {
        this.defaultItem = item;
    }

    @Nullable
    public final T get() {
        if (item == null) {
            extractOptions(context, ref);
            item = load();
            if (item == null && defaultItem != null) {
                item = defaultItem;
            }
        }
        return item;
    }

    public boolean exists() {
        return get() != null;
    }

    public Map<String, Object> parseStringRef(String ref) {
        Map<String, Object> objectRef = new HashMap<>();
        objectRef.put("itemId", ref);
        return objectRef;
    }

    public Map<String, Object> getRef() {
        return this.ref;
    }

    public void setRef(final Map<String, Object> ref) {
        this.ref = ref;
    }
}
