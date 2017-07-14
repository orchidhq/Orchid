package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.List;

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
 * It is also worth noting that templates can be provided by OrchidGenerator extensions apart from a Theme, and when using
 * Twig as the main template engine, can be injected into the site's content by extending one of the Theme's layouts,
 * which should typically be 'templates/layouts/index.twig'.
 *
 */
public abstract class OrchidGenerator extends Prioritized {

    protected final String key;

    protected OrchidContext context;
    protected OrchidResources resources;
    protected OrchidRenderer renderer;

    @Inject
    public OrchidGenerator(int priority, String key, OrchidContext context, OrchidResources resources, OrchidRenderer renderer) {
        setPriority(priority);
        this.key = key;
        this.context = context;
        this.resources = resources;
        this.renderer = renderer;
    }

    /**
     * Return the name of the OrchidGenerator. The index created by this OrchidGenerator is scoped under this name.
     *
     * @return this generator's name
     */
    public final String getKey() {
        return key;
    }

    /**
     * Return a description of this OrchidGenerator, which is displayed when listing available Generators.
     *
     * @return this generator's description
     */
    public abstract String getDescription();

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
    public abstract void startGeneration(List<? extends OrchidPage> pages);

}
