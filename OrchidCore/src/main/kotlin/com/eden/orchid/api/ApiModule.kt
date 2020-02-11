package com.eden.orchid.api

import com.eden.orchid.api.converters.BooleanConverter
import com.eden.orchid.api.converters.ClogStringConverterHelper
import com.eden.orchid.api.converters.DateTimeConverter
import com.eden.orchid.api.converters.DoubleConverter
import com.eden.orchid.api.converters.FlexibleIterableConverter
import com.eden.orchid.api.converters.FlexibleMapConverter
import com.eden.orchid.api.converters.FloatConverter
import com.eden.orchid.api.converters.IntegerConverter
import com.eden.orchid.api.converters.LongConverter
import com.eden.orchid.api.converters.NumberConverter
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.converters.StringConverterHelper
import com.eden.orchid.api.converters.TimeConverter
import com.eden.orchid.api.converters.TypeConverter
import com.eden.orchid.api.options.Extractor
import com.eden.orchid.api.options.HibernateValidator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.OptionsValidator
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.options.extractors.AnyOptionExtractor
import com.eden.orchid.api.options.extractors.BooleanOptionExtractor
import com.eden.orchid.api.options.extractors.DateOptionExtractor
import com.eden.orchid.api.options.extractors.DateTimeOptionExtractor
import com.eden.orchid.api.options.extractors.DoubleOptionExtractor
import com.eden.orchid.api.options.extractors.EnumOptionExtractor
import com.eden.orchid.api.options.extractors.FloatOptionExtractor
import com.eden.orchid.api.options.extractors.IntOptionExtractor
import com.eden.orchid.api.options.extractors.JSONArrayOptionExtractor
import com.eden.orchid.api.options.extractors.JSONObjectOptionExtractor
import com.eden.orchid.api.options.extractors.ListOptionExtractor
import com.eden.orchid.api.options.extractors.LongOptionExtractor
import com.eden.orchid.api.options.extractors.ModularListOptionExtractor
import com.eden.orchid.api.options.extractors.ModularTypeOptionExtractor
import com.eden.orchid.api.options.extractors.OptionsHolderOptionExtractor
import com.eden.orchid.api.options.extractors.RelationOptionExtractor
import com.eden.orchid.api.options.extractors.StringArrayOptionExtractor
import com.eden.orchid.api.options.extractors.StringOptionExtractor
import com.eden.orchid.api.options.extractors.TimeOptionExtractor
import com.eden.orchid.api.options.globals.ConfigGlobal
import com.eden.orchid.api.options.globals.DataGlobal
import com.eden.orchid.api.options.globals.IndexGlobal
import com.eden.orchid.api.options.globals.SiteGlobal
import com.eden.orchid.api.options.globals.ThemeGlobal
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.site.OrchidSite
import com.eden.orchid.api.site.OrchidSiteImpl
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.api.theme.permalinks.pathTypes.DataPropertyPathType
import com.eden.orchid.api.theme.permalinks.pathTypes.TitlePathType
import com.google.inject.Provides
import com.google.inject.name.Named

@IgnoreModule
class ApiModule : OrchidModule() {

    override fun configure() {
        bind(Extractor::class.java).to(OptionsExtractor::class.java)
        bind(OptionsValidator::class.java).to(HibernateValidator::class.java)

        // Type Converters
        addToSet(
            StringConverterHelper::class.java,
            ClogStringConverterHelper::class.java
        )

        addToSet(
            TypeConverter::class.java,
            BooleanConverter::class.java,
            DateTimeConverter::class.java,
            DoubleConverter::class.java,
            FlexibleIterableConverter::class.java,
            FlexibleMapConverter::class.java,
            FloatConverter::class.java,
            IntegerConverter::class.java,
            LongConverter::class.java,
            NumberConverter::class.java,
            OptionsHolderOptionExtractor.Converter::class.java,
            StringConverter::class.java,
            TimeConverter::class.java
        )

        // Options Extractors
        addToSet(
            OptionExtractor::class.java,
            ModularListOptionExtractor::class.java,
            ModularTypeOptionExtractor::class.java,
            OptionsHolderOptionExtractor::class.java,
            RelationOptionExtractor::class.java,

            AnyOptionExtractor::class.java,
            StringArrayOptionExtractor::class.java,
            BooleanOptionExtractor::class.java,
            DateOptionExtractor::class.java,
            DateTimeOptionExtractor::class.java,
            DoubleOptionExtractor::class.java,
            EnumOptionExtractor::class.java,
            FloatOptionExtractor::class.java,
            IntOptionExtractor::class.java,
            JSONArrayOptionExtractor::class.java,
            JSONObjectOptionExtractor::class.java,
            ListOptionExtractor::class.java,
            LongOptionExtractor::class.java,
            StringOptionExtractor::class.java,
            TimeOptionExtractor::class.java
        )

        // Template Globals
        addToSet(
            TemplateGlobal::class.java,
            ConfigGlobal::class.java,
            DataGlobal::class.java,
            IndexGlobal::class.java,
            SiteGlobal::class.java,
            ThemeGlobal::class.java
        )

        // Permalink Path Types
        addToSet(
            PermalinkPathType::class.java,
            TitlePathType::class.java,
            DataPropertyPathType::class.java
        )
    }

    @Provides
    internal fun provideOrchidSite(
        @Named("version") version: String,
        @Named("baseUrl") baseUrl: String,
        @Named("environment") environment: String,
        @Named("defaultTemplateExtension") defaultTemplateExtension: String,
        @Named("src") sourceDir: String,
        @Named("dest") destinationDir: String
    ): OrchidSite {
        try {
            return OrchidSiteImpl(
                Class.forName("com.eden.orchid.OrchidVersion").getMethod("getVersion").invoke(null) as String,
                version,
                baseUrl,
                environment,
                defaultTemplateExtension,
                sourceDir,
                destinationDir
            )
        } catch (e: Exception) {
            return OrchidSiteImpl(
                "",
                version,
                baseUrl,
                environment,
                defaultTemplateExtension,
                sourceDir,
                destinationDir
            )
        }

    }

}
