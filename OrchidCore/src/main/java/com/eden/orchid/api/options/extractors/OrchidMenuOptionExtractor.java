package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.google.inject.Provider;
import org.json.JSONArray;

import javax.inject.Inject;
import java.lang.reflect.Field;

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

    @Inject
    public OrchidMenuOptionExtractor(Provider<OrchidContext> contextProvider, FlexibleIterableConverter iterableConverter) {
        super(100);
        this.contextProvider = contextProvider;
        this.iterableConverter = iterableConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(OrchidMenu.class);
    }

    @Override
    public OrchidMenu getOption(Field field, Object sourceObject, String key) {
//        Iterable iterableSource = iterableConverter.convert(sourceObject).second;
//        ArrayList<Object> collection = new ArrayList<>();
//
//        for(Object o : iterableSource) {
//            collection.add(o);
//        }
//
//        return new OrchidMenu(contextProvider.get(), new JSONArray(collection));

        if(sourceObject instanceof JSONArray) {
            return new OrchidMenu(contextProvider.get(), (JSONArray) sourceObject);
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
