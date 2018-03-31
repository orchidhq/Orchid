package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.LongDefault;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * ### Source Types
 *
 * | Item Type | Coercion |
 * |-----------|----------|
 * | number    | direct   |
 * | string    | parse as number |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation   | Default Value            |
 * |------------|--------------|--------------------------|
 * | long       | @LongDefault | Annotation value() or 0L |
 * | Long       | @LongDefault | Annotation value() or 0L |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class LongOptionExtractor extends OptionExtractor<Long> {

    private final LongConverter converter;

    @Inject
    public LongOptionExtractor(LongConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(long.class) || clazz.equals(Long.class);
    }

    @Override
    public Long getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public Long getDefaultValue(Field field) {
        if(field.isAnnotationPresent(LongDefault.class)) {
            return field.getAnnotation(LongDefault.class).value();
        }
        else {
            return 0L;
        }
    }

//    @Override
//    public List<Long> getList(Field field, JSONObject options, String key) {
//        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
//        List<Long> list = new ArrayList<>();
//        for (int i = 0; i < array.length(); i++) {
//            list.add(array.getLong(i));
//        }
//        return list;
//    }
//
//    @Override
//    public Object getArray(Field field, JSONObject options, String key) {
//        List<Long> list = this.getList(field, options, key);
//
//        if (field.getType().equals(long[].class)) {
//            long[] array = new long[list.size()];
//            for (int i = 0; i < list.size(); i++) {
//                array[i] = list.get(i);
//            }
//            return array;
//        }
//        else if (field.getType().equals(Long[].class)) {
//            Long[] array = new Long[list.size()];
//            list.toArray(array);
//            return array;
//        }
//
//        return null;
//    }
}
