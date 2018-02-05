package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input  | Result                | Converter |
 * |--------|-----------------------|-----------|
 * | number | that number as long   |           |
 * | string | parsed number as long | toString  |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class LongConverter implements TypeConverter<Long> {

    private final StringConverter stringConverter;

    @Inject
    public LongConverter(StringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public Class<Long> resultClass() {
        return Long.class;
    }

    @Override
    public EdenPair<Boolean, Long> convert(Object object) {
        try {
            return new EdenPair<>(true, Long.parseLong(stringConverter.convert(object).second));
        }
        catch (NumberFormatException e) {
            return new EdenPair<>(false, 0L);
        }
    }

}
