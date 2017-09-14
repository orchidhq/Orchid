package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

/**
 * | Input  | Result                 | Converter |
 * |--------|------------------------|-----------|
 * | number | that number as float   |           |
 * | string | parsed number as float | toString  |
 *
 * @since v1.0.0
 */
public class FloatConverter implements TypeConverter<Float> {

    @Override
    public Class<Float> resultClass() {
        return Float.class;
    }

    @Override
    public EdenPair<Boolean, Float> convert(Object object) {
        if(object != null) {
            try {
                return new EdenPair<>(true, Float.parseFloat(object.toString()));
            }
            catch (NumberFormatException e) {
                return new EdenPair<>(false, 0.0f);
            }
        }

        return new EdenPair<>(false, 0.0f);
    }

}
