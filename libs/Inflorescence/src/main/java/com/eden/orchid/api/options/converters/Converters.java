package com.eden.orchid.api.options.converters;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.TypeConverter;

import javax.inject.Inject;
import java.util.Set;

public class Converters {

    private final Set<TypeConverter> converters;

    @Inject
    public Converters(Set<TypeConverter> converters) {
        this.converters = converters;
    }

    public <T> EdenPair<Boolean, T> convert(Object object, Class<T> targetClass) {
        for(TypeConverter converter : converters) {
            if(converter.resultClass().equals(targetClass)) {
                return (EdenPair<Boolean, T>) converter.convert(object);
            }
        }

        return new EdenPair<>(false, null);
    }
}
