package com.eden.orchid.api;

import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.NumberConverter;
import com.eden.orchid.api.converters.TypeConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.options.extractors.BooleanOptionExtractor;
import com.eden.orchid.api.options.extractors.ComponentHolderOptionExtractor;
import com.eden.orchid.api.options.extractors.DateOptionExtractor;
import com.eden.orchid.api.options.extractors.DateTimeOptionExtractor;
import com.eden.orchid.api.options.extractors.DoubleOptionExtractor;
import com.eden.orchid.api.options.extractors.FloatOptionExtractor;
import com.eden.orchid.api.options.extractors.IntOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONArrayOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONObjectOptionExtractor;
import com.eden.orchid.api.options.extractors.LongOptionExtractor;
import com.eden.orchid.api.options.extractors.OptionsHolderOptionExtractor;
import com.eden.orchid.api.options.extractors.OrchidMenuOptionExtractor;
import com.eden.orchid.api.options.extractors.StringArrayOptionExtractor;
import com.eden.orchid.api.options.extractors.StringOptionExtractor;
import com.eden.orchid.api.options.extractors.TimeOptionExtractor;
import com.eden.orchid.api.options.globals.ConfigGlobal;
import com.eden.orchid.api.options.globals.IndexGlobal;
import com.eden.orchid.api.options.globals.SiteGlobal;
import com.eden.orchid.api.options.globals.ThemeGlobal;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.site.OrchidSite;
import com.eden.orchid.api.site.OrchidSiteImpl;
import com.eden.orchid.api.theme.permalinks.PermalinkPathType;
import com.eden.orchid.api.theme.permalinks.pathTypes.DataPropertyPathType;
import com.eden.orchid.api.theme.permalinks.pathTypes.TitlePathType;
import com.google.inject.Provides;
import com.google.inject.name.Named;

@IgnoreModule
public final class ApiModule extends OrchidModule {

    @Override
    protected void configure() {

        // Type Converters
        addToSet(TypeConverter.class,
                BooleanConverter.class,
                NumberConverter.class,
                LongConverter.class,
                DoubleConverter.class,
                IntegerConverter.class,
                FloatConverter.class);

        // Options Extractors
        addToSet(OptionExtractor.class,
                BooleanOptionExtractor.class,
                ComponentHolderOptionExtractor.class,
                DateOptionExtractor.class,
                DateTimeOptionExtractor.class,
                DoubleOptionExtractor.class,
                FloatOptionExtractor.class,
                IntOptionExtractor.class,
                JSONArrayOptionExtractor.class,
                JSONObjectOptionExtractor.class,
                LongOptionExtractor.class,
                OptionsHolderOptionExtractor.class,
                OrchidMenuOptionExtractor.class,
                StringOptionExtractor.class,
                StringArrayOptionExtractor.class,
                TimeOptionExtractor.class
        );

        // Template Globals
        addToSet(TemplateGlobal.class,
                ConfigGlobal.class,
                IndexGlobal.class,
                SiteGlobal.class,
                ThemeGlobal.class);

        // Permalink Path Types
        addToSet(PermalinkPathType.class,
                TitlePathType.class,
                DataPropertyPathType.class);
    }

    @Provides
    OrchidSite provideOrchidSite
            (@Named("v") String version,
             @Named("baseUrl") String baseUrl,
             @Named("environment") String environment,
             @Named("defaultTemplateExtension") String defaultTemplateExtension) {
        try {
            return new OrchidSiteImpl(
                    (String) Class.forName("com.eden.orchid.OrchidVersion").getMethod("getVersion").invoke(null),
                    version,
                    baseUrl,
                    environment,
                    defaultTemplateExtension);
        }
        catch (Exception e) {
            return new OrchidSiteImpl(
                    "",
                    version,
                    baseUrl,
                    environment,
                    defaultTemplateExtension);
        }
    }

}
