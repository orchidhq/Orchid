package com.eden.orchid.api.converters;

import com.google.inject.ImplementedBy;

@ImplementedBy(ClogStringConverterHelper.class)
public interface StringConverterHelper {

    String convert(String input);

}
