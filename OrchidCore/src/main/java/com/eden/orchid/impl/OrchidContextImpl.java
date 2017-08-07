package com.eden.orchid.impl;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.events.FilterService;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.render.ContentFilter;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Injector;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

@Singleton
public final class OrchidContextImpl implements OrchidContext {

    private Injector injector;

    private JSONObject root;
    private JSONObject optionsData;
    private JSONObject configData;

    private Theme defaultTheme;
    private Stack<Theme> themeStack;

    private OrchidTasks orchidTasks;
    private OrchidFlags flags;
    private OrchidOptions options;
    private OrchidGenerators generators;

    private EventService eventService;
    private FilterService filterService;

    private Set<OrchidCompiler> compilers;
    private Set<OrchidParser> parsers;
    private OrchidPrecompiler precompiler;
    private Set<ContentFilter> filters;

    @Inject
    public OrchidContextImpl(
            Injector injector,
            OrchidTasks orchidTasks,
            OrchidGenerators generators,
            EventService eventService,
            FilterService filterService,
            OrchidOptions options,
            OrchidPrecompiler precompiler,
            Set<OrchidCompiler> compilers,
            Set<OrchidParser> parsers,
            Set<ContentFilter> filters) {
        this.injector = injector;
        this.orchidTasks = orchidTasks;
        this.flags = OrchidFlags.getInstance();
        this.generators = generators;
        this.eventService = eventService;
        this.filterService = filterService;
        this.options = options;

        this.root = new JSONObject();
        this.themeStack = new Stack<>();

        this.precompiler = precompiler;
        this.compilers = new ObservableTreeSet<>(compilers);
        this.parsers = new ObservableTreeSet<>(parsers);
        this.filters = new ObservableTreeSet<>(filters);
    }

    @Override
    public boolean runTask(Theme defaultTheme, String taskName) {
        this.defaultTheme = defaultTheme;
        orchidTasks.run(taskName);
        return true;
    }

    @Override
    public void build() {
        eventService.broadcast(Orchid.Events.BUILD_START);

        getTheme().clearCache();
        for (Theme theme : themeStack) {
            theme.clearCache();
        }

        optionsData = options.loadOptions();

        // Create theme menus
        JSONElement menuElement = query("options.menu");
        if (OrchidUtils.elementIsArray(menuElement)) {
            getTheme().createMenu(null, (JSONArray) menuElement.getElement());
        }
        else if (OrchidUtils.elementIsObject(menuElement)) {
            getTheme().createMenus((JSONObject) menuElement.getElement());
        }

        configData = (optionsData.has("config")) ? optionsData.getJSONObject("config") : new JSONObject();

        generators.startIndexing();

        // Add discovered assets
        // TODO: Leave this up to themes/pages/components to add their own assets rather than do it by force
        // TODO: Alternatively, allow this behavior as a flag?s
        for (OrchidPage style : getIndex().find("assets/js")) {
            getTheme().addJs(style);
        }
        for (OrchidPage style : getIndex().find("assets/css")) {
            getTheme().addCss(style);
        }

        generators.startGeneration();
        eventService.broadcast(Orchid.Events.BUILD_FINISH);
    }

    @Override
    public JSONElement query(String pointer) {
        if (!EdenUtils.isEmpty(pointer)) {
            if (pointer.startsWith("options.")) {
                return new JSONElement(optionsData).query(pointer.replace("options.", ""));
            }
        }

        return new JSONElement(root).query(pointer);
    }

    @Override
    public void broadcast(String event, Object... args) {
        eventService.broadcast(event, args);
    }

    @Override
    public Map<String, Object> getSiteData(Object... data) {
        Map<String, Object> siteData = new HashMap<>();

        Map<String, ?> root = getRoot().toMap();

        for (String key : root.keySet()) {
            siteData.put(key, root.get(key));
        }

        if (data != null && data.length > 0) {
            if (data[0] instanceof OrchidPage) {
                OrchidPage page = (OrchidPage) data[0];
                siteData.put("page", page);
                siteData.put(page.getKey(), page);
            }
            else if (data[0] instanceof OrchidComponent) {
                OrchidComponent component = (OrchidComponent) data[0];
                siteData.put("component", component);
                siteData.put(component.getKey(), component);
            }
            else if (data[0] instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) data[0];
                siteData.put("data", jsonObject);

                for (String key : jsonObject.keySet()) {
                    siteData.put(key, jsonObject.get(key));
                }
            }
            else if (data[0] instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) data[0];
                siteData.put("data", map);

                for (String key : map.keySet()) {
                    siteData.put(key, map.get(key));
                }
            }
            else if (data[0] instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) data[0];
                siteData.put("data", jsonArray);
            }
            else if (data[0] instanceof Collection) {
                Collection collection = (Collection) data[0];
                siteData.put("data", collection);
            }
        }

        siteData.put("index", getIndex());
        siteData.put("options", optionsData.toMap());

        return siteData;
    }

    public OrchidIndex getIndex() {
        return this.generators.getInternalIndex();
    }

    public OrchidRootInternalIndex getInternalIndex() {
        return this.generators.getInternalIndex();
    }

    public OrchidExternalIndex getExternalIndex() {
        return this.generators.getExternalIndex();
    }

    public OrchidCompositeIndex getCompositeIndex() {
        return this.generators.getCompositeIndex();
    }

    @Override
    public Theme getTheme() {
        return (themeStack.size() > 0) ? themeStack.peek() : getDefaultTheme();
    }

    @Override
    public void pushTheme(Theme theme) {
        themeStack.push(theme);
    }

    @Override
    public void popTheme() {
        themeStack.pop();
    }

    public OrchidCompiler compilerFor(String extension) {
        return compilers
                .stream()
                .filter(compiler -> Arrays.stream(compiler.getSourceExtensions()).anyMatch(s -> s.equalsIgnoreCase(extension)))
                .findFirst()
                .orElseGet(() -> null);
    }

    public OrchidParser parserFor(String extension) {
        return parsers
                .stream()
                .filter(parser -> Arrays.stream(parser.getSourceExtensions()).anyMatch(s -> s.equalsIgnoreCase(extension)))
                .findFirst()
                .orElseGet(() -> null);
    }

    public String compile(String extension, String input, Object... data) {
        OrchidCompiler compiler = compilerFor(extension);

        String compiledContent = (compiler != null) ? compiler.compile(extension, input, data) : input;

        for (ContentFilter filter : filters) {
            compiledContent = filter.apply(compiledContent);
        }

        return compiledContent;
    }

    public JSONObject parse(String extension, String input) {
        OrchidParser parser = parserFor(extension);

        return (parser != null) ? parser.parse(extension, input) : null;
    }

    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        return precompiler.getEmbeddedData(input);
    }

    public String precompile(String input, Object... data) {
        return precompiler.precompile(input, data);
    }

    public String getOutputExtension(String extension) {
        OrchidCompiler compiler = compilerFor(extension);

        return (compiler != null) ? compiler.getOutputExtension() : extension;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    @Override
    public JSONObject getRoot() {
        return root;
    }

    public JSONObject getOptionsData() {
        return optionsData;
    }

    public JSONObject getConfigData() {
        return configData;
    }

    @Override
    public Theme getDefaultTheme() {
        return defaultTheme;
    }

    public Stack<Theme> getThemeStack() {
        return themeStack;
    }

    public OrchidTasks getOrchidTasks() {
        return orchidTasks;
    }

    public OrchidFlags getFlags() {
        return flags;
    }

    public OrchidOptions getOptions() {
        return options;
    }

    public OrchidGenerators getGenerators() {
        return generators;
    }

    public EventService getEventService() {
        return eventService;
    }

    @Override
    public FilterService getFilterService() {
        return filterService;
    }

    public Set<OrchidCompiler> getCompilers() {
        return compilers;
    }

    public Set<OrchidParser> getParsers() {
        return parsers;
    }

    public OrchidPrecompiler getPrecompiler() {
        return precompiler;
    }

    public Set<ContentFilter> getFilters() {
        return filters;
    }
}
