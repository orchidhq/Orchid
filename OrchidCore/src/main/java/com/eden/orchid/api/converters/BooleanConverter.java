package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;

/**
 * | Input          | Result | Converter       |
 * |----------------|--------|-----------------|
 * | null           | false  |                 |
 * | true, "true"   | true   |                 |
 * | false, "false" | true   |                 |
 * | number 0       | false  | NumberConverter |
 * | number non-0   | true   | NumberConverter |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class BooleanConverter implements TypeConverter<Boolean> {

    private final StringConverter stringConverter;
    private final NumberConverter numberConverter;

    @Inject
    public BooleanConverter(StringConverter stringConverter, NumberConverter numberConverter) {
        this.stringConverter = stringConverter;
        this.numberConverter = numberConverter;
    }

    @Override
    public Class<Boolean> resultClass() {
        return Boolean.class;
    }

    @Override
    public EdenPair<Boolean, Boolean> convert(Object object) {
        if (object == null) {
            return new EdenPair<>(true, false);
        }

        if (object instanceof Boolean) {
            return new EdenPair<>(true, (Boolean) object);
        }
        if (object instanceof String) {
            String s = stringConverter.convert(object).second;
            if (s.equalsIgnoreCase("true")) {
                return new EdenPair<>(true, true);
            }
            else if (s.equalsIgnoreCase("false")) {
                return new EdenPair<>(true, false);
            }
        }

        EdenPair<Boolean, Number> numberValue = numberConverter.convert(object);
        if (numberValue.first) {
            if (numberValue.second.doubleValue() == 0) {
                return new EdenPair<>(true, false);
            }
            else {
                return new EdenPair<>(true, true);
            }
        }

        return new EdenPair<>(false, false);
    }

}
