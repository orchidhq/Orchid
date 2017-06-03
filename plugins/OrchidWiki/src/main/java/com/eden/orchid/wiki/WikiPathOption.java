package com.eden.orchid.wiki;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WikiPathOption extends OrchidOption {

    private String path;

    @Inject
    public WikiPathOption() {
        this.priority = 100;
    }

    @Override
    public String getFlag() {
        return "wikiPath";
    }

    @Override
    public String getDescription() {
        return "The path used for your wiki. Includes both the path for your source wiki files, and also the output path.";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        path = options[1];

        if (!options[1].endsWith("/")) {
            path += "/";
        }

        return new JSONElement(options[1]);
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int optionLength() {
        return 2;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
