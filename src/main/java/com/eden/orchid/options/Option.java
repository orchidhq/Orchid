package com.eden.orchid.options;


import com.eden.common.json.JSONElement;

public interface Option {
    String getFlag();
    JSONElement parseOption(String[] options);
    JSONElement getDefaultValue();
    int priority();

    int optionLength();
}
