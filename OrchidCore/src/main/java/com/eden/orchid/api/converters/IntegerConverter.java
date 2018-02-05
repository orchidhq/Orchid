package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input  | Result               | Converter |
 * |--------|----------------------|-----------|
 * | number | that number as int   |           |
 * | string | parsed number as int | toString  |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class IntegerConverter implements TypeConverter<Integer> {

    private final StringConverter stringConverter;

    @Inject
    public IntegerConverter(StringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public Class<Integer> resultClass() {
        return Integer.class;
    }

    @Override
    public EdenPair<Boolean, Integer> convert(Object object) {
        try {
            return new EdenPair<>(true, Integer.parseInt(stringConverter.convert(object).second));
        }
        catch (NumberFormatException e) {
            return new EdenPair<>(false, 0);
        }
    }

}
