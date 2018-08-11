package com.eden.orchid.testhelpers;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.registration.OrchidModule;
import kotlin.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrchidIntegrationTest {

    protected TestOrchidRunner runner;
    protected TestResults testResults;

    protected Map<String, Object> flags;
    protected Map<String, Object> config;
    protected Map<String, Pair<String, Map<String, Object>>> resources;

    protected final Set<OrchidModule> standardAdditionalModules;

    public OrchidIntegrationTest(OrchidModule... standardAdditionalModules) {
        Set<OrchidModule> standardModules = new HashSet<>();
        Collections.addAll(standardModules, standardAdditionalModules);
        this.standardAdditionalModules = Collections.unmodifiableSet(standardModules);
    }

    @BeforeEach
    public void setUp() {
        disableLogging();
        runner = new TestOrchidRunner();
        flags = new HashMap<>();
        config = new HashMap<>();
        resources = new HashMap<>();
    }

    @AfterEach
    public void tearDown() {
        enableLogging();
        runner = null;
        testResults = null;
        flags = null;
        config = null;
        resources = null;
    }

    protected void disableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    protected void enableLogging() {
        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE);
    }

    protected TestResults execute(OrchidModule... modules) {
        List<OrchidModule> testedModules = new ArrayList<>();
        if(modules != null) {
            Collections.addAll(testedModules, modules);
        }
        testedModules.addAll(standardAdditionalModules);

        testResults = runner.runTest(flags, config, resources, testedModules);
        return testResults;
    }

    protected void flag(String flag, Object value) {
        flags.put(flag, value);
    }

    protected void config(String flag, Object value) {
        config.put(flag, value);
    }

    protected void resource(String path) {
        resource(path, "");
    }

    protected void resource(String path, String content) {
        resource(path, content, new HashMap<>());
    }

    protected void resource(String path, String content, Map<String, Object> data) {
        resources.put(path, new Pair<>(content, data));
    }

}
