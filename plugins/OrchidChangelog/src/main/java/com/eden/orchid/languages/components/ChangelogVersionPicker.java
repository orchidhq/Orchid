package com.eden.orchid.languages.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.languages.model.ChangelogModel;
import lombok.Getter;

import javax.inject.Inject;

public final class ChangelogVersionPicker extends OrchidComponent {

    @Getter
    private final ChangelogModel model;

    @Inject
    public ChangelogVersionPicker(OrchidContext context, ChangelogModel model) {
        super(context, "changelogVersionPicker", 100);
        this.model = model;
    }

}
