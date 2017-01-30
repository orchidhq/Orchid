package com.eden.orchid.options.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;

@AutoRegister
public class ResourcesOption implements Option {

    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    @Override
    public String getFlag() {
        return "resourcesDir";
    }

    @Override
    public String getDescription() {
        return "the directory containing your content files";
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
        return 90;
    }

    @Override
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean required() {
        return false;
    }
}
