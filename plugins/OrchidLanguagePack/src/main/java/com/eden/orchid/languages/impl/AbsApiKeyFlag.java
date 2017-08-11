package com.eden.orchid.languages.impl;

import com.eden.Eden;
import com.eden.orchid.api.options.OrchidFlag;

public class AbsApiKeyFlag implements OrchidFlag {

    @Override
    public String getFlag() {
        return "absApiKey";
    }

    @Override
    public String getDescription() {
        return "Bible verses can only be looked up when an API key from Bibles.org. Get you key at http://bibles.org/pages/api/signup";
    }

    @Override
    public Object parseFlag(String[] options) {
        Eden eden = Eden.getInstance();
        eden.config().putString("ABS_ApiKey", options[1]);
        eden.config().putString("com.eden.americanbiblesociety.ABSRepository_selectedBibleId", "eng-NASB");
        return options[1];
    }
}
