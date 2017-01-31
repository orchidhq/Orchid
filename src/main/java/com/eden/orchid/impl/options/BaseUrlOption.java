package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;

@AutoRegister
public class BaseUrlOption implements Option {

    @Override
    public String getFlag() {
        return "baseUrl";
    }

    @Override
    public String getDescription() {
        return "the base URL to append to generated URLs.";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return new JSONElement(options[1]);
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 60;
    }

    @Override
    public boolean required() {
        return false;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
