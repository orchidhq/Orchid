package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

public interface TypeConverter<T> {

    Class<T> resultClass();

    EdenPair<Boolean, T> convert(Object object);

}
