package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input  | Result                  | Converter |
 * |--------|-------------------------|-----------|
 * | number | that number as double   |           |
 * | string | parsed number as double | toString  |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class DoubleConverter implements TypeConverter<Double> {

    private final StringConverter stringConverter;

    @Inject
    public DoubleConverter(StringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public Class<Double> resultClass() {
        return Double.class;
    }

    @Override
    public EdenPair<Boolean, Double> convert(Object object) {
        try {
            return new EdenPair<>(true, Double.parseDouble(stringConverter.convert(object).second));
        }
        catch (NumberFormatException e) {
            return new EdenPair<>(false, 0.0);
        }
    }

}
