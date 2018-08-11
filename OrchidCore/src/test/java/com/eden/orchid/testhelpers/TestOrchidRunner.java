package com.eden.orchid.testhelpers;

import com.eden.orchid.Orchid;
import com.eden.orchid.StandardModule;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.impl.compilers.markdown.FlexmarkModule;
import com.eden.orchid.impl.compilers.pebble.PebbleModule;
import com.google.inject.Module;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOrchidRunner {

    public TestResults runTest(Map<String, Object> flags, Map<String, Object> config, Map<String, Pair<String, Map<String, Object>>> resources, List<OrchidModule> extraModules) {
        List<Module> modules = new ArrayList<>();
        if(!flags.containsKey("environment")) {
            flags = new HashMap<>(flags);
            flags.put("environment", "test");
        }
        modules.add(StandardModule.builder()
                .flags(flags)
                .includeClasspath(false)
                .includeCoreApi(true)
                .includeCoreImpl(false)
                .build());
        modules.add(new TestImplModule());
        modules.add(new PebbleModule());
        modules.add(new FlexmarkModule());

        if(extraModules != null) {
            modules.addAll(extraModules);
        }

        Orchid.getInstance().startForUnitTest(modules, orchidContextProvider -> {
            ArrayList<OrchidModule> contextDependantModules = new ArrayList<>();

            contextDependantModules.add(new TestResourceSource(orchidContextProvider, resources).toModule());
            contextDependantModules.add(new TestConfigResourceSource(orchidContextProvider, config).toModule());

            return contextDependantModules;
        });

        TestRenderer renderer = Orchid.getInstance().getInjector().getInstance(TestRenderer.class);

        return new TestResults(renderer.getRenderedPageMap());
    }

}
