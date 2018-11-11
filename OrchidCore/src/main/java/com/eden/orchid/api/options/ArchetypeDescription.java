package com.eden.orchid.api.options;

import lombok.Value;

@Value
public class ArchetypeDescription {
    private String key;
    private Class<? extends OptionArchetype> archetypeType;
    private String displayName;
    private String description;
}
