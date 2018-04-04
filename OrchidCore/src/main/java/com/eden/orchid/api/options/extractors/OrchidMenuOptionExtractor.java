package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;
import com.eden.orchid.api.options.converters.FlexibleMapConverter;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class OrchidMenuOptionExtractor extends OptionExtractor<OrchidMenu> {

    private final Provider<OrchidContext> contextProvider;
    private final FlexibleIterableConverter iterableConverter;
    private final FlexibleMapConverter mapConverter;

    @Inject
    public OrchidMenuOptionExtractor(Provider<OrchidContext> contextProvider, FlexibleIterableConverter iterableConverter, FlexibleMapConverter mapConverter) {
        super(100);
        this.contextProvider = contextProvider;
        this.iterableConverter = iterableConverter;
        this.mapConverter = mapConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(OrchidMenu.class);
    }

    @Override
    public OrchidMenu getOption(Field field, Object sourceObject, String key) {
        Iterable iterable = iterableConverter.convert(sourceObject, "type").second;

        JSONArray jsonArray = new JSONArray();
        for(Object o : iterable) {
            Map map = mapConverter.convert(o).second;
            jsonArray.put(new JSONObject(map));
        }

        if(jsonArray.length() > 0) {
            return new OrchidMenu(contextProvider.get(), jsonArray);
        }

        return null;
    }

    @Override
    public OrchidMenu getDefaultValue(Field field) {
        return new OrchidMenu(contextProvider.get(), new JSONArray());
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "Empty OrchidMenu";
    }

}
