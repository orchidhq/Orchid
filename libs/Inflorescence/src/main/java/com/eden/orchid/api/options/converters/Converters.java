package com.eden.orchid.api.options.converters;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.TypeConverter;

import javax.inject.Inject;
import java.util.Collection;

public class Converters {

    private final Collection<TypeConverter> converters;

    @Inject
    public Converters(Collection<TypeConverter> converters) {
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
