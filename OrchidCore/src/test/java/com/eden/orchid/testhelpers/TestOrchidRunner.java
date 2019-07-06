package com.eden.orchid.testhelpers;

import com.eden.common.util.EdenPair;
import com.eden.orchid.Orchid;
import com.eden.orchid.StandardModule;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource;
import com.google.inject.Module;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOrchidRunner {

    public Pair<OrchidContext, TestResults> runTest(
            Map<String, Object> flags,
            Map<String, Object> config,
            Map<String, Pair<String, Map<String, Object>>> resources,
            List<OrchidModule> extraModules
    ) {
        List<Module> modules = new ArrayList<>();
        if (!flags.containsKey("environment")) {
            flags = new HashMap<>(flags);
            flags.put("environment", "test");
        }
        modules.add(StandardModule.builder()
                .flags(flags)
                .includeClasspath(false)
                .includeCoreApi(true)
                .includeCoreImpl(false)
                .build());
        modules.addAll(extraModules);

        EdenPair<Boolean, Throwable> result = Orchid.getInstance().startForUnitTest(modules, orchidContextProvider -> {
            ArrayList<OrchidModule> contextDependantModules = new ArrayList<>();

            contextDependantModules.add(new TestResourceSource(orchidContextProvider, resources).toModule());
            contextDependantModules.add(new TestConfigResourceSource(orchidContextProvider, config).toModule());

            for (Module module : modules) {
                if (module instanceof OrchidModule) {
                    OrchidModule orchidModule = (OrchidModule) module;
                    if (orchidModule.isHasResources()) {

                        contextDependantModules.add(new OrchidModule() {
                            @Override
                            protected void configure() {
                                addToSet(
                                        PluginResourceSource.class,
                                        new PluginFileResourceSource(
                                                orchidContextProvider,
                                                orchidModule.getClass(),
                                                orchidModule.getResourcePriority() + 1
                                        )
                                );
                            }
                        });
                    }
                }
            }

            return contextDependantModules;
        });

        TestRenderer renderer = Orchid.getInstance().getContext().resolve(TestRenderer.class);

        return new Pair<>(
                Orchid.getInstance().getContext(),
                new TestResults(
                        renderer.getRenderedPageMap(),
                        result.first,
                        result.second
                )
        );
    }

}
