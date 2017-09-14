package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

/**
 * | Input  | Result               | Converter |
 * |--------|----------------------|-----------|
 * | number | that number as int   |           |
 * | string | parsed number as int | toString  |
 *
 * @since v1.0.0
 */
public class IntegerConverter implements TypeConverter<Integer> {

    @Override
    public Class<Integer> resultClass() {
        return Integer.class;
    }

    @Override
    public EdenPair<Boolean, Integer> convert(Object object) {
        if(object != null) {
            try {
                return new EdenPair<>(true, Integer.parseInt(object.toString()));
            }
            catch (NumberFormatException e) {
                return new EdenPair<>(false, 0);
            }
        }

        return new EdenPair<>(false, 0);
    }

}
