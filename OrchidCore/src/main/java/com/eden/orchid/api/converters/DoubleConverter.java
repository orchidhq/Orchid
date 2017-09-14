package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

/**
 * | Input  | Result                  | Converter |
 * |--------|-------------------------|-----------|
 * | number | that number as double   |           |
 * | string | parsed number as double | toString  |
 *
 * @since v1.0.0
 */
public class DoubleConverter implements TypeConverter<Double> {

    @Override
    public Class<Double> resultClass() {
        return Double.class;
    }

    @Override
    public EdenPair<Boolean, Double> convert(Object object) {
        if(object != null) {
            try {
                return new EdenPair<>(true, Double.parseDouble(object.toString()));
            }
            catch (NumberFormatException e) {
                return new EdenPair<>(false, 0.0);
            }
        }

        return new EdenPair<>(false, 0.0);
    }

}
