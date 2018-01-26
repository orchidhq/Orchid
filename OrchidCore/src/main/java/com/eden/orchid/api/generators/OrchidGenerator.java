package com.eden.orchid.api.generators;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.OptionsData;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generators are what create the output pages within Orchid. Generators are run after all Options have been processed
 * as part of a OrchidTask. When a OrchidTask is chosen that builds the output site, it happens in two phases: the **indexing
 * phase** and the **generation phase**.
 *
 * ### Indexing Phase
 * During the indexing phase, Generators should determine exactly the output pages they intend to generate, returning an
 * index of those pages. This index should consist of a JSONArray or JSONObject, wrapped in a JSONElement. The content
 * of either should be JSONObjects (either as a list or as a hierarchy) which contain at least the following properties:
 *
 * * 'name' - the display name of the page
 * * 'url' - the url of the target page. This should be absolute using the set baseUrl OrchidOption, which is done automatically
 * by OrchidReference
 *
 * This JSONElement is then placed into the root JSONObject under the key specified by `getKey()`. At generation time,
 * this index will be used to create deep-links throughout the site, typically through the page navigation or from
 * manually creating links, either through Javadoc 'see' or 'link' tags, or using an output filter in the main template.
 *
 * It is important that **no pages be written during the Indexing phase**. The sites's index is not completed at this
 * point, so it is likely that navigation among pages will not work as expected.
 *
 * ### Generation Phase
 * During the Generation phase, Generators can start to write their pages. Typically, this step involves picking a
 * template for an OrchidPage and generating a page according to that template. This template should be provided by the
 * Theme, but if the Theme did not implement the desired template, Orchid will attempt to find a suitable fallback.
 *
 * It is also worth noting that templates can be provided by OrchidGenerator extensions apart from a Theme, and when
 * using Pebble as the main template engine, can be injected into the site's content by extending one of the Theme's
 * layouts, which should typically be 'templates/layouts/index.peb'.
 *
 */
public abstract class OrchidGenerator extends Prioritized implements OptionsHolder {

    /**
     * Typically used for Generators that produce content pages.
     */
    public static final int PRIORITY_EARLY = 1000;

    /**
     * Typically used for Generators that produce pages based on data contained in content pages or just index content
     * for use in components, menus, or collections.
     */
    public static final int PRIORITY_DEFAULT = 100;

    /**
     * Typically used for Generators that produce assets or data files.
     */
    public static final int PRIORITY_LATE = 10;

    @Getter
    protected final String key;

    protected final OrchidContext context;

    @Getter @Setter
    @Option
    @Description("Set a theme to be used only when rendering pages from this Generator.")
    private String theme;

    @Getter @Setter
    @Option
    @Description("Set the default layout to be used for all Pages from this Generator. Pages can specify their own " +
            "layouts, which take precedence over the Generator layout."
    )
    public String layout;

    @Getter @Setter
    @Option @BooleanDefault(false)
    @Description("Improve site generation performance by rendering the pages from this Generator in parallel. (There " +
            "are currently thread-safety issues that may cause deadlocks, and this should be left false until " +
            "these issues can be addressed.)"
    )
    public boolean parallel;

    @Getter @Setter @OptionsData private JSONElement allData;

    @Inject
    public OrchidGenerator(OrchidContext context, String key, int priority) {
        super(priority);
        this.key = key;
        this.context = context;
    }

    /**
     * A callback to build the index of content this OrchidGenerator intends to create.
     *
     * @return a list of pages that will be built by this generator
     */
    public abstract List<? extends OrchidPage> startIndexing();

    /**
     * A callback to begin generating content. The index is fully built and should not be changed at this time. The
     * list of pages returned by `startIndexing` is passed back in as an argument to the method.
     *
     * @param pages the pages to render
     */
    public abstract void startGeneration(Stream<? extends OrchidPage> pages);

    /**
     * Get a list of the collections that are indexed by this Generator.
     *
     * @return the list of OrchidCollections
     */
    public List<? extends OrchidCollection> getCollections() {
        List<OrchidPage> pages = context.getGeneratorPages(key);
        if(!EdenUtils.isEmpty(pages)) {
            return Collections.singletonList(new FileCollection(this, this.getKey(), pages));
        }

        return null;
    }

}
