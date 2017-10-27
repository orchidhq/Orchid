package com.eden.orchid.presentations;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.presentations.fields.TextField;

public class FormsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(PluginResourceSource.class,
                FormsResourceSource.class);

        addToSet(OrchidGenerator.class,
                FormsGenerator.class);

        addToSet(OrchidComponent.class,
                FormComponent.class);

        addToSet(OptionExtractor.class,
                FormOptionExtractor.class);

        addToSet(FormField.class,
                TextField.class);
    }

}
