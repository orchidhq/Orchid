package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input    | Result            | Converter |
 * |----------|-------------------|-----------|
 * | anything | object.toString() |           |
 * | null     | empty string      |           |
 *
 * @since v1.0.0
 */
public final class StringConverter implements TypeConverter<String> {

    private final StringConverterHelper helper;

    @Inject
    public StringConverter(StringConverterHelper helper) {
        this.helper = helper;
    }

    @Override
    public Class<String> resultClass() {
        return String.class;
    }

    @Override
    public EdenPair<Boolean, String> convert(Object object) {
        if(object != null) {
            return new EdenPair<>(true, helper.convert(object.toString()));
        }
        return new EdenPair<>(true, "");
    }

}
