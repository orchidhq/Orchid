package com.eden.orchid.options;

import com.eden.orchid.JSONElement;

public interface Option {
    String getFlag();
    JSONElement parseOption(String[] options);
    JSONElement getDefaultValue();
    int priority();
}
