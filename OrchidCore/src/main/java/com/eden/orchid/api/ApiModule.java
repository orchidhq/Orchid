package com.eden.orchid.api;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.compilers.CompilerServiceImpl;
import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.converters.ClogStringConverterHelper;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.NumberConverter;
import com.eden.orchid.api.converters.StringConverterHelper;
import com.eden.orchid.api.converters.TypeConverter;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.events.EventServiceImpl;
import com.eden.orchid.api.generators.GeneratorService;
import com.eden.orchid.api.generators.GeneratorServiceImpl;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.indexing.IndexServiceImpl;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsService;
import com.eden.orchid.api.options.OptionsServiceImpl;
import com.eden.orchid.api.options.extractors.BooleanOptionExtractor;
import com.eden.orchid.api.options.extractors.DoubleOptionExtractor;
import com.eden.orchid.api.options.extractors.FloatOptionExtractor;
import com.eden.orchid.api.options.extractors.IntOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONArrayOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONObjectOptionExtractor;
import com.eden.orchid.api.options.extractors.LongOptionExtractor;
import com.eden.orchid.api.options.extractors.OptionsHolderOptionExtractor;
import com.eden.orchid.api.options.extractors.StringOptionExtractor;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.resources.ResourceServiceImpl;
import com.eden.orchid.api.site.OrchidSite;
import com.eden.orchid.api.site.OrchidSiteImpl;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.tasks.TaskServiceImpl;
import com.eden.orchid.api.theme.ThemeService;
import com.eden.orchid.api.theme.ThemeServiceImpl;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.eden.orchid.OrchidVersion;

public class ApiModule extends OrchidModule {

    @Override
    protected void configure() {
        bind(CompilerService.class).to(CompilerServiceImpl.class);
        bind(ThemeService.class).to(ThemeServiceImpl.class);
        bind(EventService.class).to(EventServiceImpl.class);
        bind(IndexService.class).to(IndexServiceImpl.class);
        bind(ResourceService.class).to(ResourceServiceImpl.class);
        bind(TaskService.class).to(TaskServiceImpl.class);
        bind(OptionsService.class).to(OptionsServiceImpl.class);
        bind(GeneratorService.class).to(GeneratorServiceImpl.class);

        bind(OrchidContext.class).to(OrchidContextImpl.class);

        // TypeConverters
        bind(StringConverterHelper.class).to(ClogStringConverterHelper.class);
        addToSet(TypeConverter.class,
                BooleanConverter.class,
                NumberConverter.class,
                LongConverter.class,
                DoubleConverter.class,
                IntegerConverter.class,
                FloatConverter.class);

        // OptionsExtractors
        addToSet(OptionExtractor.class,
                BooleanOptionExtractor.class,
                StringOptionExtractor.class,
                IntOptionExtractor.class,
                LongOptionExtractor.class,
                FloatOptionExtractor.class,
                DoubleOptionExtractor.class,
                OptionsHolderOptionExtractor.class,
                JSONObjectOptionExtractor.class,
                JSONArrayOptionExtractor.class);
    }

    @Provides
    OrchidSite provideOrchidSite(@Named("v") String version, @Named("baseUrl") String baseUrl, @Named("environment") String environment) {
        OrchidSite site = new OrchidSiteImpl(OrchidVersion.getVersion(), version, baseUrl, environment);
        return site;
    }
}
