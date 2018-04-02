package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.DoubleDefault;
import com.eden.orchid.api.options.annotations.FloatDefault;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.LongDefault;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.converters.Converters;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ### Source Types
 *
 * | Item Type  | Coercion |
 * |------------|----------|
 * | JSONArray  | direct   |
 * | anything[] | new JSONArray from array |
 * | List       | new JSONArray from list |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation  | Default Value |
 * |------------|-------------|---------------|
 * | JSONArray  | none        | null          |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class ListOptionExtractor extends OptionExtractor<List> {

    private final FlexibleIterableConverter converter;
    private final Converters converters;

    @Inject
    public ListOptionExtractor(FlexibleIterableConverter converter, Converters converters) {
        super(2);
        this.converter = converter;
        this.converters = converters;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public List getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, Iterable> value = converter.convert(sourceObject);

        List<Object> list = new ArrayList<>();

        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> listClass = (Class<?>) stringListType.getActualTypeArguments()[0];

        for(Object item : value.second) {
            EdenPair<Boolean, ?> converted = converters.convert(item, listClass);

            if(converted.first) {
                list.add(converted.second);
            }
        }

        return list;
    }

    @Override
    public List getDefaultValue(Field field) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> listClass = (Class<?>) stringListType.getActualTypeArguments()[0];

        if(listClass.equals(Boolean.class)) {
            if(field.isAnnotationPresent(BooleanDefault.class)) {
                List<Boolean> list = new ArrayList<>();
                for(boolean val : field.getAnnotation(BooleanDefault.class).value()) {
                    list.add(val);
                }
                return list;
            }
        }
        else if(listClass.equals(Double.class)) {
            if(field.isAnnotationPresent(DoubleDefault.class)) {
                List<Double> list = new ArrayList<>();
                for(double val : field.getAnnotation(DoubleDefault.class).value()) {
                    list.add(val);
                }
                return list;
            }
        }
        else if(listClass.equals(Float.class)) {
            if(field.isAnnotationPresent(FloatDefault.class)) {
                List<Float> list = new ArrayList<>();
                for(float val : field.getAnnotation(FloatDefault.class).value()) {
                    list.add(val);
                }
                return list;
            }
        }
        else if(listClass.equals(Integer.class)) {
            if(field.isAnnotationPresent(IntDefault.class)) {
                List<Integer> list = new ArrayList<>();
                for(int val : field.getAnnotation(IntDefault.class).value()) {
                    list.add(val);
                }
                return list;
            }
        }
        else if(listClass.equals(Long.class)) {
            if(field.isAnnotationPresent(LongDefault.class)) {
                List<Long> list = new ArrayList<>();
                for(long val : field.getAnnotation(LongDefault.class).value()) {
                    list.add(val);
                }
                return list;
            }
        }
        else if(listClass.equals(String.class)) {
            if(field.isAnnotationPresent(StringDefault.class)) {
                return Arrays.asList(field.getAnnotation(StringDefault.class).value());
            }
        }

        return new ArrayList();
    }

    @Override
    public String describeDefaultValue(Field field) {
        List<?> value = getDefaultValue(field);

        if(value.size() > 0) {
            return "[" + value
                    .stream()
                    .map(item -> converters.convert(item, String.class).second)
                    .collect(Collectors.joining(", ")) + "]";
        }

        return "empty list";
    }

}
