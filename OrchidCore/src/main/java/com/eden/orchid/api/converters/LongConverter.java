package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

/**
 * | Input  | Result                | Converter |
 * |--------|-----------------------|-----------|
 * | number | that number as long   |           |
 * | string | parsed number as long | toString  |
 *
 * @since v1.0.0
 */
public class LongConverter implements TypeConverter<Long> {

    @Override
    public Class<Long> resultClass() {
        return Long.class;
    }

    @Override
    public EdenPair<Boolean, Long> convert(Object object) {
        if(object != null) {
            try {
                return new EdenPair<>(true, Long.parseLong(object.toString()));
            }
            catch (NumberFormatException e) {
                return new EdenPair<>(false, 0L);
            }
        }

        return new EdenPair<>(false, 0L);
    }

}
