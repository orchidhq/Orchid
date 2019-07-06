package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidSecurityManager;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.events.OrchidEvent;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This is the main entry point to the Orchid build process. It does little more than create a OrchidContextImpl for Orchid to runTask
 * within, and then set that context into motion. It is the single point-of-entry for starting the Orchid process; both
 * Javadoc's `start` method and the Java `main` method are in here, which create the appropriate OrchidContext and then
 * runTask a single Orchid task.
 */
public final class Orchid {

// Make main Orchid object a singleton
//----------------------------------------------------------------------------------------------------------------------

    private static Orchid instance;

    private OrchidContext context;

    private Orchid.State state = State.BOOTSTRAP;

    public static Orchid getInstance() {
        if (instance == null) {
            instance = new Orchid();
        }

        return instance;
    }

    private Orchid() { }

// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        boolean success = Orchid.getInstance().start(
                StandardModule.builder().args(args).build()
        );
        System.exit((success) ? 0 : 1);
    }

    public boolean start(Module... modules) {
        List<Module> modulesList = new ArrayList<>();
        Collections.addAll(modulesList, modules);
        return start(modulesList);
    }

    public boolean start(List<Module> modules) {
        try {
            String moduleLog = "\n--------------------";
            for (Module module : modules) {
                if (!module.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                    moduleLog += "\n * " + module.getClass().getName();
                }
            }
            moduleLog += "\n";
            Clog.tag("Using the following modules").log(moduleLog);

            Injector injector = Guice.createInjector(modules);

            String flagLog = "";
            flagLog += "\n--------------------\n";
            flagLog += OrchidFlags.getInstance().printFlags();
            flagLog += "\n";
            Clog.tag("Flag values").log(flagLog);

            context = injector.getInstance(OrchidContext.class);

            try {
                OrchidSecurityManager manager = injector.getInstance(OrchidSecurityManager.class);
                System.setSecurityManager(manager);
            }
            catch (Exception e) {

            }

            Clog.i("Running Orchid version {}, site version {} in {} environment", context.getOrchidVersion(), context.getVersion(), context.getEnvironment());
            context.start();
            context.finish();
            return true;
        }
        catch (Exception e) {
            Clog.e("Something went wrong running Orchid", e);
            e.printStackTrace();

            context.broadcast(Orchid.Lifecycle.Shutdown.fire(this));
            return false;
        }
    }

    public OrchidContext getContext() {
        return this.context;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

// Events fired by Orchid Core
//----------------------------------------------------------------------------------------------------------------------

    public static class Lifecycle {

        /**
         * Orchid has finished the bootstrapping process and is fully initialized and ready to continue. No task has
         * been started yet.
         */
        public static class InitComplete extends OrchidEvent {
            private InitComplete(Object sender) { super(sender); }

            public static InitComplete fire(Object sender) { return new InitComplete(sender); }
        }

        /**
         * Orchid is still bootstrapping, but all services have been started and are ready for use. Catch this event to
         * do any kind of one-time initialization on singletons that are not {@link OrchidService }s.
         */
        public static class OnStart extends OrchidEvent {
            private OnStart(Object sender) { super(sender); }

            public static OnStart fire(Object sender) { return new OnStart(sender); }
        }

        /**
         * Orchid is shutting down normally, and should be caught for non-critical cleanup. Listen for the
         * {@link Orchid.Lifecycle.Shutdown } event for any critical cleanup or to release system resources.
         */
        public static class OnFinish extends OrchidEvent {
            private OnFinish(Object sender) { super(sender); }

            public static OnFinish fire(Object sender) { return new OnFinish(sender); }
        }

        /**
         * Indicates that Orchid is shutting down, either by a a fatal crash or when the process is forcibly killed by
         * the system, or when Orchid finishes normally after a task completes. This should be caught to clean up all
         * system resources, kill threads, etc. Listen for the {@link Orchid.Lifecycle.OnFinish } for non-critical
         * cleanup tasks.
         */
        public static class Shutdown extends OrchidEvent {
            private Shutdown(Object sender) { super(sender); }

            public static Shutdown fire(Object sender) {
                Orchid.getInstance().setState(Orchid.State.SHUTDOWN);
                return new Shutdown(sender);
            }
        }

        /**
         * A valid task has been identified and is about to be run.
         */
        public static class TaskStart extends OrchidEvent {
            private TaskStart(Object sender) { super(sender); }

            public static TaskStart fire(Object sender) { return new TaskStart(sender); }
        }

        /**
         * A task has finished execution and Orchid is about to shut down normally. This may not ever be called if
         * Orchid shuts down unexpectedly.
         */
        public static class TaskFinish extends OrchidEvent {
            private TaskFinish(Object sender) { super(sender); }

            public static TaskFinish fire(Object sender) { return new TaskFinish(sender); }
        }

        /**
         * A build phase has begun, and we are now in the {@link Orchid.State#BUILD_PREP } state.
         */
        public static class BuildStart extends OrchidEvent {
            private BuildStart(Object sender) { super(sender); }

            public static BuildStart fire(Object sender) { return new BuildStart(sender); }
        }

        /**
         * A build phase has completed, and we are now at the end of the {@link Orchid.State#BUILDING } state.
         */
        public static class BuildFinish extends OrchidEvent {
            private BuildFinish(Object sender) { super(sender); }

            public static BuildFinish fire(Object sender) { return new BuildFinish(sender); }
        }

        /**
         * The indexing step of the build phase has begun.
         */
        public static class IndexingStart extends OrchidEvent {
            private IndexingStart(Object sender) { super(sender); }

            public static IndexingStart fire(Object sender) { return new IndexingStart(sender); }
        }

        /**
         * The indexing step of the build phase has completed.
         */
        public static class IndexingFinish extends OrchidEvent {
            private IndexingFinish(Object sender) { super(sender); }

            public static IndexingFinish fire(Object sender) { return new IndexingFinish(sender); }
        }

        /**
         * The indexing step for a single Generator has begun.
         */
        public static class IndexGeneratorStart extends OrchidEvent {
            private IndexGeneratorStart(OrchidGenerator sender) { super(sender); }

            public static IndexGeneratorStart fire(OrchidGenerator sender) { return new IndexGeneratorStart(sender); }
        }

        /**
         * The indexing step for a single Generator has completed. The list of pages indexed by this generator are
         * passed with the event, and listeners are free to add, remove, or changes the pages as needed. The result of
         * firing this event is what is actually stored as this generator's pages, so this builds extensibility into
         * the indexing phase of each Generator.
         */
        public static class IndexGeneratorExtend extends OrchidEvent<OrchidGenerator> {
            private final List<? extends OrchidPage> generatorPages;

            private IndexGeneratorExtend(OrchidGenerator sender, List<? extends OrchidPage> generatorPages) {
                super(sender);
                this.generatorPages = (generatorPages != null) ? generatorPages : new ArrayList<>();
            }

            public static IndexGeneratorExtend fire(OrchidGenerator sender, List<? extends OrchidPage> generatorPages) {
                return new IndexGeneratorExtend(sender, generatorPages);
            }

            public List<? extends OrchidPage> getGeneratorPages() {
                return this.generatorPages;
            }
        }

        /**
         * The indexing step for a single Generator has completed, its pages and collections have been indexed.
         */
        public static class IndexGeneratorFinish extends OrchidEvent<OrchidGenerator> {
            private IndexGeneratorFinish(OrchidGenerator sender) {
                super(sender);
            }

            public static IndexGeneratorFinish fire(OrchidGenerator sender) {
                return new IndexGeneratorFinish(sender);
            }
        }

        /**
         * The generating step of the build phase has begun.
         */
        public static class GeneratingStart extends OrchidEvent {
            private GeneratingStart(Object sender) { super(sender); }

            public static GeneratingStart fire(Object sender) { return new GeneratingStart(sender); }
        }

        /**
         * The generating step of the build phase has completed.
         */
        public static class GeneratingFinish extends OrchidEvent {
            private GeneratingFinish(Object sender) { super(sender); }

            public static GeneratingFinish fire(Object sender) { return new GeneratingFinish(sender); }
        }

        /**
         * A deployment phase has begun, and we are now in the {@link Orchid.State#DEPLOYING } state.
         */
        public static class DeployStart extends OrchidEvent {
            private DeployStart(Object sender) { super(sender); }

            public static DeployStart fire(Object sender) { return new DeployStart(sender); }
        }

        /**
         * A deployment phase has completed, and we are now at the end of the {@link Orchid.State#DEPLOYING } state.
         */
        public static class DeployFinish extends OrchidEvent {
            private final boolean success;
            private DeployFinish(Object sender, boolean success) {
                super(sender);
                this.success = success;
            }

            @Override
            public String toString() {
                JSONObject data = new JSONObject();

                data.put("success", success);

                return data.toString();
            }

            public static DeployFinish fire(Object sender, boolean success) { return new DeployFinish(sender, success); }
        }

        /**
         * A generic event to indicate some form of global progress. This is mostly used for display purposes, and is
         * caught and displayed in the admin panel, with the progress shown as a progress bar and the 'progress type'
         * displayed to the end-user.
         */
        public static final class ProgressEvent extends OrchidEvent {
            protected final String progressType;
            protected final int currentProgress;
            protected final int maxProgress;
            protected final long millis;

            private ProgressEvent(Object sender, String progressType, int currentProgress, int maxProgress, long millis) {
                super("progress", sender);
                this.progressType = progressType;
                this.currentProgress = currentProgress;
                this.maxProgress = maxProgress;
                this.millis = millis;
            }

            @Override
            public String toString() {
                JSONObject data = new JSONObject();

                data.put("progressType", progressType);
                data.put("currentProgress", currentProgress);
                data.put("maxProgress", maxProgress);
                data.put("millis", millis);

                return data.toString();
            }

            public static ProgressEvent fire(Object sender, String progressType, int currentProgress, int maxProgress) { return new ProgressEvent(sender, progressType, currentProgress, maxProgress, 0); }
            public static ProgressEvent fire(Object sender, String progressType, int currentProgress, int maxProgress, long millis) { return new ProgressEvent(sender, progressType, currentProgress, maxProgress, millis); }
        }

        /**
         * Files have been changed and the site is about to be rebuilt. Orchid is usually in the
         * {@link Orchid.State#IDLE } state at the time this event if first fired, and internally Orchid catches this
         * event to start a build.
         */
        public static class FilesChanged extends OrchidEvent {
            private FilesChanged(Object sender) { super(sender); }

            public static FilesChanged fire(Object sender) { return new FilesChanged(sender); }
        }

        /**
         * Indicates that _all_ caches should be cleared. Always called during the {@link Orchid.State#BUILD_PREP }
         * state (at the start of every build), but may be called at any other time as needed.
         */
        public static class ClearCache extends OrchidEvent {
            private ClearCache(Object sender) { super(sender); }

            public static ClearCache fire(Object sender) { return new ClearCache(sender); }
        }

        /**
         * Fired when a long-running task has been terminated by the user, such as with the `quit` command.
         */
        public static class EndSession extends OrchidEvent {
            private EndSession(Object sender) { super(sender); }

            public static EndSession fire(Object sender) { return new EndSession(sender); }
        }

        /**
         * Fired as a shim in the options loading process to allow options to be dynamically loaded as needed. This is
         * fired from the {@link ConfigArchetype }, and catching this event allows listeners to add additional values
         * into the archetype options for this object. This archetype is added to Pages, Generators, and Publishers by
         * default, but other classes are free to use it as needed.
         */
        public static class ArchetypeOptions extends OrchidEvent {
            private Map<String, Object> config;
            public ArchetypeOptions(String archetypeKey, Object sender) {
                super(archetypeKey, sender);
                config = new HashMap<>();
            }

            public Map<String, Object> getConfig() {
                return config;
            }

            public void addConfig(Map<String, Object> config) {
                if(config != null) {
                    this.config = EdenUtils.merge(this.config, config);
                }
            }
        }
    }

    public enum State {

        /**
         * From the time Orchid is first instantiated until after the initial injection is complete. This phase ends
         * with the {@link Orchid.Lifecycle.InitComplete } event, after which the selected task is run.
         */
        BOOTSTRAP(false, false),

        /**
         * The Orchid shutdown process. This is set anytime {@link Orchid.Lifecycle.Shutdown } event is fired, such as
         * when Orchid experiences a fatal crash or when the process is forcibly killed by the system, but may also be
         * set sooner during a 'normal' shutdown as prompted by the completion and exit of a task. When the shutdown is
         * sudden, it is marked only by the {@link Orchid.Lifecycle.Shutdown } event, but a normal shutdown starts with
         * the {@link Orchid.Lifecycle.OnFinish } event and all services get a change to shutdown cleanly, and is then
         * followed by the {@link Orchid.Lifecycle.OnFinish } event.
         */
        SHUTDOWN(false, false),

        /**
         * From the start of a 'build' until indexing begins. Starts with the {@link Orchid.Lifecycle.BuildStart }
         * event, and includes clearing the cache, re-loading config options, re-initializing the theme, and extracting
         * options into the services. This is the first of the 'build states', which are 'working states.'
         */
        BUILD_PREP(true,  false),

        /**
         * Orchid is indexing all Generators. During this state, {@link Orchid.Lifecycle.ProgressEvent } of type
         * 'indexing' are broadcast, one for each generator. This is the second of the 'build states', which are
         * 'working states.'
         */
        INDEXING(true,  false),

        /**
         * Orchid is generating all pages. During this state, {@link Orchid.Lifecycle.ProgressEvent } of type 'building'
         * are broadcast, one for each page. This is the third and final of the 'build states', which are 'working
         * states.'
         */
        BUILDING(true,  false),

        /**
         * Orchid is deploying your site with all configured publishers. During this state,
         * {@link Orchid.Lifecycle.ProgressEvent } of type 'deploying' are broadcast, one for each publisher. This is
         * the only 'deploy state', which is a 'working states.'
         */
        DEPLOYING(false, true),

        /**
         * Orchid is idle, not currently indexing, building, or deploying. Orchid is listening for changes to your files
         * to rebuild, or is waiting to respond to a command.
         */
        IDLE(false, false)
        ;

        private final boolean isBuildState;
        private final boolean isDeployState;
        private final boolean isWorkingState;

        State(boolean isBuildState, boolean isDeployState) {
            this.isBuildState = isBuildState;
            this.isDeployState = isDeployState;
            this.isWorkingState = isDeployState || isBuildState;
        }

        public boolean isBuildState() {
            return this.isBuildState;
        }

        public boolean isDeployState() {
            return this.isDeployState;
        }

        public boolean isWorkingState() {
            return this.isWorkingState;
        }
    }

//
//----------------------------------------------------------------------------------------------------------------------


    public EdenPair<Boolean, Throwable> startForUnitTest(List<Module> modules, Function<Provider<OrchidContext>, List<OrchidModule>> contextDependantModulesFunction) {
        try {
            String moduleLog = "\n--------------------";
            for (Module module : modules) {
                if (!module.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                    moduleLog += "\n * " + module.getClass().getName();
                }
            }
            moduleLog += "\n";
            Clog.tag("Using the following modules").log(moduleLog);

            modules.add(new OrchidModule() {
                @Override
                protected void configure() {
                    Provider<OrchidContext> contextProvider = getProvider(OrchidContext.class);
                    List<OrchidModule> contextDependantModules = contextDependantModulesFunction.apply(contextProvider);
                    if(contextDependantModules != null) {
                        for(OrchidModule module : contextDependantModules) {
                            install(module);
                        }
                    }
                }
            });

            Injector injector = Guice.createInjector(modules);

            String flagLog = "";
            flagLog += "\n--------------------\n";
            flagLog += OrchidFlags.getInstance().printFlags();
            flagLog += "\n";
            Clog.tag("Flag values").log(flagLog);

            context = injector.getInstance(OrchidContext.class);

            try {
                OrchidSecurityManager manager = injector.getInstance(OrchidSecurityManager.class);
                System.setSecurityManager(manager);
            }
            catch (Exception e) {

            }

            Clog.i("Running Orchid version {}, site version {} in {} environment", context.getOrchidVersion(), context.getVersion(), context.getEnvironment());
            context.start();
            context.finish();
            return new EdenPair<>(true, null);
        }
        catch (Exception e) {
            Clog.e("Something went wrong running Orchid: {}", e, e.getMessage());
            e.printStackTrace();

            context.broadcast(Orchid.Lifecycle.Shutdown.fire(this));
            return new EdenPair<>(true, e);
        }
    }

}
