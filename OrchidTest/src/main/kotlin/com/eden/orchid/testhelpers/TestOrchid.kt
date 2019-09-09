package com.eden.orchid.testhelpers

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.StandardModule
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.OrchidSecurityManager
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import com.eden.orchid.impl.compilers.markdown.FlexmarkModule
import com.eden.orchid.impl.compilers.pebble.PebbleModule
import com.google.inject.Guice
import java.util.HashMap

class TestOrchid {

    fun runTest(
        flags: MutableMap<String, Any>,
        config: Map<String, Any>,
        resources: Map<String, Pair<String, Map<String, Any>>>,
        extraModules: List<OrchidModule>,
        serve: Boolean
    ): TestResults {
        val actualFlags = HashMap(flags)
        if (!actualFlags.containsKey("environment")) {
            actualFlags["environment"] = "test"
        }
        if (!actualFlags.containsKey("baseUrl")) {
            actualFlags["baseUrl"] = "http://orchid.test"
        }

        // setup original modules
        val modules = mutableListOf(
            StandardModule.builder()
                .flags(actualFlags)
                .includeClasspath(false)
                .includeCoreApi(true)
                .includeCoreImpl(includeCoreImpl = true, useTestCoreImpl = true)
                .build(),
            TestImplModule(serve),
            PebbleModule(),
            FlexmarkModule(),
            *extraModules.toTypedArray()
        )

        // set up the context to use resources from the test class
        modules.add(ResourceOverridingModule(modules))
        modules.add(TestResourceSource(resources).toModule())
        modules.add(TestConfigResourceSource(config).toModule())

        return try {
            val testContext = startForUnitTest(modules)
            TestResults(
                testContext,
                testContext.resolve(TestRenderer::class.java).getRenderedPageMap(),
                testContext.collections,
                true,
                null
            )
        } catch (t: Throwable) {
            Clog.e("An exception was thrown while running Orchid integration test: {}", t.message)
            t.printStackTrace()
            TestResults(null, emptyMap(), emptyList(), false, t)
        }

    }

    private fun startForUnitTest(modules: List<OrchidModule>): OrchidContext {
        var moduleLog = "\n--------------------"
        for (module in modules) {
            moduleLog += "\n * " + module.javaClass.name
        }
        moduleLog += "\n"
        Clog.tag("Running test with the following modules").log(moduleLog)

        val injector = Guice.createInjector(modules)

        var flagLog = ""
        flagLog += "\n--------------------\n"
        flagLog += OrchidFlags.getInstance().printFlags()
        flagLog += "\n"
        Clog.tag("Flag values").log(flagLog)

        val context = injector.getInstance(OrchidContext::class.java)

        try {
            val manager = injector.getInstance(OrchidSecurityManager::class.java)
            System.setSecurityManager(manager)
        } catch (e: Exception) {

        }

        Clog.i("Running Orchid test")
        context.start()
        context.finish()
        return context
    }

    class ResourceOverridingModule(
        private val modules: List<OrchidModule>
    ) : OrchidModule() {

        override fun configure() {
            // replace each plugin's normal resource source with one that also looks in the project directories, since
            // Intellij or Gradle may use the raw classfiles and resources on disk rather than bundling the test module
            // into a Jarfile.
            modules
                .filter { it.isHasResources }
                .forEach {
                    Clog.d("overriding resources from {}", it.javaClass.simpleName)
                    addToSet(
                        PluginResourceSource::class.java,
                        PluginFileResourceSource(
                            it.javaClass,
                            it.resourcePriority + 1
                        )
                    )
                }
        }
    }

}
