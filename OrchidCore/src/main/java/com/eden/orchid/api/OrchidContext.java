package com.eden.orchid.api;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.generators.GeneratorService;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.OptionsService;
import com.eden.orchid.api.publication.PublicationService;
import com.eden.orchid.api.registration.InjectionService;
import com.eden.orchid.api.render.RenderService;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.site.OrchidSite;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.theme.ThemeService;
import com.google.inject.ImplementedBy;

import java.util.Collection;

@ImplementedBy(OrchidContextImpl.class)
public interface OrchidContext extends
        OrchidSite,
        CompilerService,
        ThemeService,
        EventService,
        IndexService,
        ResourceService,
        TaskService,
        OptionsService,
        GeneratorService,
        RenderService,
        PublicationService,
        InjectionService
{

    default String getKey() { return "context"; }

    void extractServiceOptions();

    OrchidSite getSite();

    Collection<OrchidService> getServices();

    void start();
    void finish();

    Orchid.State getState();
    void setState(Orchid.State state);
}
