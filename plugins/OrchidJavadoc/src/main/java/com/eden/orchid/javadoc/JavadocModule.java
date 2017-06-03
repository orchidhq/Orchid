package com.eden.orchid.javadoc;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.eden.orchid.javadoc.api.JavadocInlineTagHandler;
import com.eden.orchid.javadoc.impl.generators.JavadocClassesGenerator;
import com.eden.orchid.javadoc.impl.generators.JavadocPackagesGenerator;
import com.eden.orchid.javadoc.impl.jtwig.ConstructorsFilter;
import com.eden.orchid.javadoc.impl.jtwig.DirectSubclassesFilter;
import com.eden.orchid.javadoc.impl.jtwig.FieldsFilter;
import com.eden.orchid.javadoc.impl.jtwig.MethodsFilter;
import com.eden.orchid.javadoc.impl.tags.AuthorTag;
import com.eden.orchid.javadoc.impl.tags.DeprecatedTag;
import com.eden.orchid.javadoc.impl.tags.ExceptionTag;
import com.eden.orchid.javadoc.impl.tags.LinkTag;
import com.eden.orchid.javadoc.impl.tags.ReturnTag;
import com.eden.orchid.javadoc.impl.tags.SeeTag;
import com.eden.orchid.javadoc.impl.tags.SinceTag;
import com.eden.orchid.javadoc.impl.tags.ThrowsTag;
import com.eden.orchid.javadoc.impl.tags.VersionTag;
import org.jtwig.functions.JtwigFunction;

public class JavadocModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class,
                JavadocClassesGenerator.class,
                JavadocPackagesGenerator.class);

        // Block Tag Handlers
        addToSet(JavadocBlockTagHandler.class,
                AuthorTag.class,
                DeprecatedTag.class,
                ExceptionTag.class,
                ReturnTag.class,
                SeeTag.class,
                SinceTag.class,
                ThrowsTag.class,
                VersionTag.class);

        // Inline Tag Handlers
        addToSet(JavadocInlineTagHandler.class,
                LinkTag.class);

        addToSet(DefaultResourceSource.class,
                JavadocResourceSource.class);

        addToSet(JtwigFunction.class,
                FieldsFilter.class,
                ConstructorsFilter.class,
                MethodsFilter.class,
                DirectSubclassesFilter.class
                );
    }
}
