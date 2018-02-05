package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input          | Result                | Converter       |
 * |----------------|-----------------------|-----------------|
 * | decimal number | that number as double | DoubleConverter |
 * | integer number | that number as long   | LongConverter   |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class NumberConverter implements TypeConverter<Number> {

    private final LongConverter longConverter;
    private final DoubleConverter doubleConverter;

    @Inject
    public NumberConverter(LongConverter longConverter, DoubleConverter doubleConverter) {
        this.longConverter = longConverter;
        this.doubleConverter = doubleConverter;
    }

    @Override
    public Class<Number> resultClass() {
        return Number.class;
    }

    @Override
    public EdenPair<Boolean, Number> convert(Object object) {
        if(object != null) {
            EdenPair<Boolean, Long> longValue = longConverter.convert(object);
            if(longValue.first) {
                return new EdenPair<>(true, longValue.second);
            }
            EdenPair<Boolean, Double> doubleValue = doubleConverter.convert(object);
            if(doubleValue.first) {
                return new EdenPair<>(true, doubleValue.second);
            }
        }

        return new EdenPair<>(false, 0);
    }

}
