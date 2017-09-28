package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 */
public final class OrchidMenuOptionExtractor extends OptionExtractor<OrchidMenu> {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public OrchidMenuOptionExtractor(Provider<OrchidContext> contextProvider) {
        super(100);
        this.contextProvider = contextProvider;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(OrchidMenu.class);
    }

    @Override
    public OrchidMenu getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof JSONArray) {
            return new OrchidMenu(contextProvider.get(), options.getJSONArray(key));
        }

        return new OrchidMenu(contextProvider.get(), new JSONArray());
    }

    @Override
    public List<OrchidMenu> getList(Field field, JSONObject options, String key) {
        return null;
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        return null;
    }
}
