package com.eden.orchid.api;

import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.converters.ClogStringConverterHelper;
import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.FlexibleIterableConverter;
import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.NumberConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.converters.StringConverterHelper;
import com.eden.orchid.api.converters.TimeConverter;
import com.eden.orchid.api.converters.TypeConverter;
import com.eden.orchid.api.options.HibernateValidator;
import com.eden.orchid.api.options.Extractor;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsValidator;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.options.extractors.AnyOptionExtractor;
import com.eden.orchid.api.options.extractors.ArrayOptionExtractor;
import com.eden.orchid.api.options.extractors.BooleanOptionExtractor;
import com.eden.orchid.api.options.extractors.DateOptionExtractor;
import com.eden.orchid.api.options.extractors.DateTimeOptionExtractor;
import com.eden.orchid.api.options.extractors.DoubleOptionExtractor;
import com.eden.orchid.api.options.extractors.EnumOptionExtractor;
import com.eden.orchid.api.options.extractors.FloatOptionExtractor;
import com.eden.orchid.api.options.extractors.IntOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONArrayOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONObjectOptionExtractor;
import com.eden.orchid.api.options.extractors.ListOptionExtractor;
import com.eden.orchid.api.options.extractors.LongOptionExtractor;
import com.eden.orchid.api.options.extractors.ModularListOptionExtractor;
import com.eden.orchid.api.options.extractors.OptionsHolderOptionExtractor;
import com.eden.orchid.api.options.extractors.RelationOptionExtractor;
import com.eden.orchid.api.options.extractors.StringOptionExtractor;
import com.eden.orchid.api.options.extractors.TimeOptionExtractor;
import com.eden.orchid.api.options.globals.ConfigGlobal;
import com.eden.orchid.api.options.globals.DataGlobal;
import com.eden.orchid.api.options.globals.IndexGlobal;
import com.eden.orchid.api.options.globals.SiteGlobal;
import com.eden.orchid.api.options.globals.ThemeGlobal;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.site.OrchidSite;
import com.eden.orchid.api.site.OrchidSiteImpl;
import com.eden.orchid.api.theme.models.Social;
import com.eden.orchid.api.theme.permalinks.PermalinkPathType;
import com.eden.orchid.api.theme.permalinks.pathTypes.DataPropertyPathType;
import com.eden.orchid.api.theme.permalinks.pathTypes.TitlePathType;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.google.inject.Provides;
import com.google.inject.name.Named;

@IgnoreModule
public final class ApiModule extends OrchidModule {

    @Override
    protected void configure() {
        bind(Extractor.class).to(OptionsExtractor.class);
        bind(OptionsValidator.class).to(HibernateValidator.class);

        // Type Converters
        addToSet(StringConverterHelper.class,
                ClogStringConverterHelper.class);

        addToSet(TypeConverter.class,
                BooleanConverter.class,
                DateTimeConverter.class,
                DoubleConverter.class,
                FlexibleIterableConverter.class,
                FlexibleMapConverter.class,
                FloatConverter.class,
                IntegerConverter.class,
                LongConverter.class,
                NumberConverter.class,
                Social.Item.Converter.class,
                FrontMatterPrecompiler.CustomDelimiter.Converter.class,
                StringConverter.class,
                TimeConverter.class
        );

        // Options Extractors
        addToSet(OptionExtractor.class,
                ModularListOptionExtractor.class,
                OptionsHolderOptionExtractor.class,
                RelationOptionExtractor.class,

                AnyOptionExtractor.class,
                ArrayOptionExtractor.class,
                BooleanOptionExtractor.class,
                DateOptionExtractor.class,
                DateTimeOptionExtractor.class,
                DoubleOptionExtractor.class,
                EnumOptionExtractor.class,
                FloatOptionExtractor.class,
                IntOptionExtractor.class,
                JSONArrayOptionExtractor.class,
                JSONObjectOptionExtractor.class,
                ListOptionExtractor.class,
                LongOptionExtractor.class,
                StringOptionExtractor.class,
                TimeOptionExtractor.class
        );

        // Template Globals
        addToSet(TemplateGlobal.class,
                ConfigGlobal.class,
                DataGlobal.class,
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
            (@Named("version") String version,
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
