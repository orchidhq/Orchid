package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input  | Result                 | Converter |
 * |--------|------------------------|-----------|
 * | number | that number as float   |           |
 * | string | parsed number as float | toString  |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class FloatConverter implements TypeConverter<Float> {

    private final StringConverter stringConverter;

    @Inject
    public FloatConverter(StringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public Class<Float> resultClass() {
        return Float.class;
    }

    @Override
    public EdenPair<Boolean, Float> convert(Object object) {
        try {
            return new EdenPair<>(true, Float.parseFloat(stringConverter.convert(object).second));
        }
        catch (NumberFormatException e) {
            return new EdenPair<>(false, 0.0f);
        }
    }

}
