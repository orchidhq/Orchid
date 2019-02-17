package com.eden.orchid.testhelpers;

import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.impl.generators.HomepageGenerator;

public class TestGeneratorModule extends OrchidModule {

    private final Class<? extends OrchidGenerator> generatorClass;

    public TestGeneratorModule() {
        this(HomepageGenerator.class);
    }

    public TestGeneratorModule(Class<? extends OrchidGenerator> generatorClass) {
        this.generatorClass = generatorClass;
    }

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, generatorClass);
    }

}
