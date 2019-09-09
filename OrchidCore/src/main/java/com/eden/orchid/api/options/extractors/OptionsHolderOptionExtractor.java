package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.converters.TypeConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsHolder;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ### Source Types
 *
 * | Item Type  | Coercion |
 * |------------|----------|
 * | JSONObject | direct   |
 * | Map        | new JSONObject from map |
 * | JSONArray  | direct   |
 * | anything[] | new JSONArray from array |
 * | List       | new JSONArray from list |
 *
 *
 * ### Destination Types
 *
 * | Field Type                    | Annotation   | Default Value            |
 * |-------------------------------|--------------|--------------------------|
 * | ? extends OptionsHolder       | none         | null                     |
 * | List<? extends OptionsHolder> | none         | null                     |
 *
 * ### _Notes_
 *
 * This can deserialize any JSONObject into any class that implements OptionsHolder, and can also handle any generic
 * List of OptionsHolders of the same Class.
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class OptionsHolderOptionExtractor extends OptionExtractor<OptionsHolder> {

    private final OptionsHolderOptionExtractor.Converter converter;

    @Inject
    public OptionsHolderOptionExtractor(OptionsHolderOptionExtractor.Converter converter) {
        super(25);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return OptionsHolder.class.isAssignableFrom(clazz);
    }

    @Override
    public OptionsHolder getOption(Field field, Object sourceObject, String key) {
        return converter.convert(field.getType(), sourceObject).second;
    }

    @Override
    public OptionsHolder getDefaultValue(Field field) {
        OptionsHolder holder = converter.convert(field.getType(), new HashMap<>()).second;

        if(holder == null) {
            Clog.e("Could not create instance of [{}] to extract into class {}", field.getType().getName(), field.getDeclaringClass().getName());
        }

        return holder;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "a new instance of " + field.getType().getSimpleName();
    }

    public static class Converter implements TypeConverter<OptionsHolder> {
        private final OrchidContext context;
        private final FlexibleMapConverter mapConverter;

        @Inject
        public Converter(OrchidContext context, FlexibleMapConverter mapConverter) {
            this.context = context;
            this.mapConverter = mapConverter;
        }

        @Override
        public boolean acceptsClass(Class clazz) {
            return OptionsHolder.class.isAssignableFrom(clazz);
        }

        @Override
        public EdenPair<Boolean, OptionsHolder> convert(Class clazz, Object o) {
            try {
                OptionsHolder holder = (OptionsHolder) context.resolve(clazz);
                EdenPair<Boolean, Map> config = mapConverter.convert(clazz, o);
                holder.extractOptions(context, config.second);
                return new EdenPair<>(true, holder);
            }
            catch (Exception e) { }

            return null;
        }
    }

}
