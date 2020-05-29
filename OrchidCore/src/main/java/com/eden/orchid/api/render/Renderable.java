package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import kotlin.text.StringsKt;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Renderable {

    @Nonnull
    String getTemplateBase();

    @Nonnull
    default List<String> getPossibleTemplates() {
        return new ArrayList<>();
    }

    @Nullable
    default OrchidResource resolveTemplate(OrchidContext context, OrchidPage orchidPage) {
        return context
                .getTemplateResourceSource(null, context.getTheme())
                .getResourceEntry(context, getTemplateBase(), getPossibleTemplates());
    }

    @Nonnull
    default String renderContent(OrchidContext context, OrchidPage orchidPage) {
        OrchidResource resource = resolveTemplate(context, orchidPage);
        if (resource != null) {
            return resource.compileContent(context, this);
        }
        return "";
    }

}
