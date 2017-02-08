package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.api.docParser.OrchidInlineTagHandler;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.registration.OrchidRegistrationProvider;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.impl.docParser.docs.AnnotationParser;
import com.eden.orchid.impl.docParser.docs.ClassDocParser;
import com.eden.orchid.impl.docParser.docs.CommentParser;
import com.eden.orchid.impl.docParser.docs.ParameterParser;
import com.eden.orchid.impl.docParser.docs.TypeParser;
import com.sun.javadoc.RootDoc;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.Map;

public final class OrchidContext implements OrchidResourceSource {

    private JSONObject root;
    private RootDoc rootDoc;
    private Theme theme;
    private OrchidResources resources;
    private Map<String, String[]> optionsMap;

    private OrchidTasks orchidTasks;
    private OrchidOptions options;
    private OrchidGenerators generators;

    private CommentParser commentParser;
    private ClassDocParser classDocParser;
    private TypeParser typeParser;
    private AnnotationParser annotationParser;
    private ParameterParser parameterParser;


    public OrchidContext(Map<String, String[]> optionsMap) {

        this.root = new JSONObject();
        this.optionsMap = optionsMap;

        this.rootDoc          = getRegistrar().resolve(RootDoc.class);
        this.resources        = getRegistrar().resolve(OrchidResources.class);

        this.orchidTasks      = getRegistrar().resolve(OrchidTasks.class);
        this.options          = getRegistrar().resolve(OrchidOptions.class);
        this.generators       = getRegistrar().resolve(OrchidGenerators.class);

        this.commentParser    = getRegistrar().resolve(CommentParser.class);
        this.classDocParser   = getRegistrar().resolve(ClassDocParser.class);
        this.typeParser       = getRegistrar().resolve(TypeParser.class);
        this.annotationParser = getRegistrar().resolve(AnnotationParser.class);
        this.parameterParser  = getRegistrar().resolve(ParameterParser.class);

        getRegistrar().registerObject(this);
    }

    public boolean run(String taskName) {
        Clog.v("Bootstrapping Orchid");
        bootstrap();

        Clog.v("#{$1} providers",         getRegistrar().resolveSet(OrchidRegistrationProvider.class).size());
        Clog.v("#{$1} resourceSources",   getRegistrar().resolveSet(OrchidResourceSource.class).size());
        Clog.v("#{$1} compilers",         getRegistrar().resolveSet(OrchidCompiler.class).size());
        Clog.v("#{$1} precompilers",      getRegistrar().resolveSet(OrchidPreCompiler.class).size());
        Clog.v("#{$1} blockTagHandlers",  getRegistrar().resolveSet(OrchidBlockTagHandler.class).size());
        Clog.v("#{$1} inlineTagHandlers", getRegistrar().resolveSet(OrchidInlineTagHandler.class).size());
        Clog.v("#{$1} generators",        getRegistrar().resolveSet(OrchidGenerator.class).size());
        Clog.v("#{$1} optionsMap",        getRegistrar().resolveSet(OrchidOption.class).size());
        Clog.v("#{$1} tasks",             getRegistrar().resolveSet(OrchidTask.class).size());

        if (shouldContinue()) {
            getRegistrar().reorderResourceSources();
            theme.onThemeSet();
            orchidTasks.run(taskName);
            return true;
        }
        else {
            return false;
        }
    }

    public void bootstrap() {
        providerScan();
        pluginScan();
        optionsScan();
    }

    public void build() {
        indexingScan();
        generationScan();
        theme.generateHomepage();
    }

    private void providerScan() {
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesImplementing(OrchidRegistrationProvider.class, (matchingClass) -> {
            for(Annotation annotation : matchingClass.getAnnotations()) {
                if(annotation.annotationType().equals(AutoRegister.class)) {
                    OrchidRegistrationProvider provider = getRegistrar().resolve(matchingClass);
                    if(provider != null) {
                        getRegistrar().registerProvider(provider);
                    }
                }
            }
        });
        scanner.scan();
    }

    private void pluginScan() {
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesWithAnnotation(AutoRegister.class, (matchingClass) -> {
            Object object = getRegistrar().resolve(matchingClass);
            if(object != null) {
                getRegistrar().registerObject(object);
            }
        });
        scanner.scan();
    }

    private void optionsScan() {
        root.put("options", new JSONObject());
        options.parseOptions(optionsMap, root.getJSONObject("options"));
    }

    private boolean shouldContinue() {
        return options.shouldContinue() && (theme != null) && theme.shouldContinue();
    }

    private void indexingScan() {
        root.put("index", new JSONObject());
        generators.startIndexing(root.getJSONObject("index"));
    }

    private void generationScan() {
        generators.startGeneration();
    }

    @Override
    public int getResourcePriority() {
        return 10;
    }

    @Override
    public void setResourcePriority(int priority) {

    }

    public JSONElement query(String pointer) { return new JSONElement(root).query(pointer); }
    public JSONObject getRoot() { return root; }
    public RootDoc getRootDoc() { return rootDoc; }
    public Theme getTheme() { return theme; }
    public OrchidResources getResources() { return resources; }
    public Map<String, String[]> getOptionsMap() { return optionsMap; }
    public void setRoot(JSONObject root) { this.root = root; }
    public void setRootDoc(RootDoc rootDoc) { this.rootDoc = rootDoc; }
    public void setTheme(Theme theme) { this.theme = theme; }
    public void setResources(OrchidResources resources) { this.resources = resources; }
    public void setOptionsMap(Map<String, String[]> optionsMap) { this.optionsMap = optionsMap; }

    public OrchidOptions getOptions() {
        return options;
    }

    public void setOptions(OrchidOptions options) {
        this.options = options;
    }

    public OrchidGenerators getGenerators() {
        return generators;
    }

    public void setGenerators(OrchidGenerators generators) {
        this.generators = generators;
    }

    public CommentParser getCommentParser() {
        return commentParser;
    }

    public void setCommentParser(CommentParser commentParser) {
        this.commentParser = commentParser;
    }

    public ClassDocParser getClassDocParser() {
        return classDocParser;
    }

    public void setClassDocParser(ClassDocParser classDocParser) {
        this.classDocParser = classDocParser;
    }

    public OrchidTasks getOrchidTasks() {
        return orchidTasks;
    }

    public void setOrchidTasks(OrchidTasks orchidTasks) {
        this.orchidTasks = orchidTasks;
    }

    public TypeParser getTypeParser() {
        return typeParser;
    }

    public void setTypeParser(TypeParser typeParser) {
        this.typeParser = typeParser;
    }

    public AnnotationParser getAnnotationParser() {
        return annotationParser;
    }

    public void setAnnotationParser(AnnotationParser annotationParser) {
        this.annotationParser = annotationParser;
    }

    public ParameterParser getParameterParser() {
        return parameterParser;
    }

    public void setParameterParser(ParameterParser parameterParser) {
        this.parameterParser = parameterParser;
    }
}
