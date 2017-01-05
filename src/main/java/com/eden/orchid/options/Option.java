package com.eden.orchid.options;

import com.eden.orchid.utilities.JSONElement;

public interface Option {
    String getFlag();
    JSONElement parseOption(String[] options);
    JSONElement getDefaultValue();
    int priority();

    int optionLength();
}
