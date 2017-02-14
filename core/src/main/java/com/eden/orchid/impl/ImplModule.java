package com.eden.orchid.impl;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.api.docParser.OrchidInlineTagHandler;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler;
import com.eden.orchid.impl.compilers.sass.SassCompiler;
import com.eden.orchid.impl.compilers.jtwig.JTwigCompiler;
import com.eden.orchid.impl.docParser.tags.AuthorTag;
import com.eden.orchid.impl.docParser.tags.DeprecatedTag;
import com.eden.orchid.impl.docParser.tags.ExceptionTag;
import com.eden.orchid.impl.docParser.tags.LinkTag;
import com.eden.orchid.impl.docParser.tags.ReturnTag;
import com.eden.orchid.impl.docParser.tags.SeeTag;
import com.eden.orchid.impl.docParser.tags.SinceTag;
import com.eden.orchid.impl.docParser.tags.ThrowsTag;
import com.eden.orchid.impl.docParser.tags.VersionTag;
import com.eden.orchid.impl.generators.AssetsGenerator;
import com.eden.orchid.impl.generators.ExternalIndexGenerator;
import com.eden.orchid.impl.generators.InternalIndexGenerator;
import com.eden.orchid.impl.generators.MetadataGenerator;
import com.eden.orchid.impl.options.BaseUrlOption;
import com.eden.orchid.impl.options.CommentLanguageOption;
import com.eden.orchid.impl.options.ConfigOption;
import com.eden.orchid.impl.options.DataFilesOption;
import com.eden.orchid.impl.options.DestinationOption;
import com.eden.orchid.impl.options.DisabledGeneratorsOption;
import com.eden.orchid.impl.options.EnvironmentOption;
import com.eden.orchid.impl.options.ResourcesOption;
import com.eden.orchid.impl.options.ThemeOption;
import com.eden.orchid.impl.options.VersionOption;
import com.eden.orchid.impl.resources.OrchidFileResources;
import com.eden.orchid.impl.tasks.BuildTask;
import com.eden.orchid.impl.tasks.ListCompilersTask;
import com.eden.orchid.impl.tasks.ListGeneratorsTask;
import com.eden.orchid.impl.tasks.ListOptionsTask;
import com.eden.orchid.impl.tasks.ListResourceSourcesTask;
import com.eden.orchid.impl.tasks.ListTasksTask;
import com.eden.orchid.impl.tasks.ListThemesTask;

public class ImplModule extends OrchidModule {

    @Override
    protected void configure() {
        bind(OrchidResources.class).to(OrchidFileResources.class);
        bind(OrchidContext.class).to(Context.class);

        // Resource Sources
        addToSet(OrchidResourceSource.class,
                OrchidFileResources.class);

        // Compilers
        addToSet(OrchidCompiler.class,
                MarkdownCompiler.class,
                JTwigCompiler.class,
                SassCompiler.class);

        // Precompilers
        addToSet(OrchidPreCompiler.class,
                FrontMatterPrecompiler.class);

        // Block Tag Handlers
        addToSet(OrchidBlockTagHandler.class,
                AuthorTag.class,
                DeprecatedTag.class,
                ExceptionTag.class,
                ReturnTag.class,
                SeeTag.class,
                SinceTag.class,
                ThrowsTag.class,
                VersionTag.class);

        // Inline Tag Handlers
        addToSet(OrchidInlineTagHandler.class,
                LinkTag.class);

        // Generators
        addToSet(OrchidGenerator.class,
                AssetsGenerator.class,
                MetadataGenerator.class,
                ExternalIndexGenerator.class,
                InternalIndexGenerator.class);

        // Options
        addToSet(OrchidOption.class,
                BaseUrlOption.class,
                CommentLanguageOption.class,
                ConfigOption.class,
                DataFilesOption.class,
                DestinationOption.class,
                DisabledGeneratorsOption.class,
                EnvironmentOption.class,
                ResourcesOption.class,
                ThemeOption.class,
                VersionOption.class);

        // Tasks
        addToSet(OrchidTask.class,
                BuildTask.class,
                ListCompilersTask.class,
                ListGeneratorsTask.class,
                ListOptionsTask.class,
                ListResourceSourcesTask.class,
                ListTasksTask.class,
                ListThemesTask.class);
    }
}
