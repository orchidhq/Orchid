package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

/**
 * | Input    | Result            | Converter |
 * |----------|-------------------|-----------|
 * | anything | object.toString() |           |
 * | null     | empty string      |           |
 *
 * @since v1.0.0
 */
public class StringConverter implements TypeConverter<String> {

    @Override
    public Class<String> resultClass() {
        return String.class;
    }

    @Override
    public EdenPair<Boolean, String> convert(Object object) {
        if(object != null) {
            return new EdenPair<>(true, object.toString());
        }
        return new EdenPair<>(true, "");
    }

}
