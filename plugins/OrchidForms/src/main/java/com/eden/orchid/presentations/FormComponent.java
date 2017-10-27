package com.eden.orchid.presentations;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.components.OrchidComponent;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class FormComponent extends OrchidComponent {

    @Getter @Setter @Option
    private Form form;

    @Inject
    public FormComponent(OrchidContext context) {
        super(context, "form", 20);
    }

    @Override
    public void addAssets() {
        super.addAssets();
        addCss("assets/css/form.scss");
    }
}
