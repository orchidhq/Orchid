package com.eden.orchid.api;

import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.generators.GeneratorService;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.OptionsService;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.theme.ThemeService;
import com.google.inject.Injector;

public interface OrchidContext extends
        CompilerService,
        ThemeService,
        EventService,
        IndexService,
        ResourceService,
        TaskService,
        OptionsService,
        GeneratorService
{

    Injector getInjector();

    void start();
    void finish();
}
