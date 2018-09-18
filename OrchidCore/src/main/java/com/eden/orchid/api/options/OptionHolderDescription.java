package com.eden.orchid.api.options;

import lombok.Value;

import java.util.List;

@Value
public class OptionHolderDescription {
    private String descriptiveName;
    private String classDescription;
    private List<OptionsDescription> optionsDescriptions;
}
