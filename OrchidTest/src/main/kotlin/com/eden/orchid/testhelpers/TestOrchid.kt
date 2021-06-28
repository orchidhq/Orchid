package com.eden.orchid.testhelpers

import clog.Clog
import com.eden.orchid.StandardModule
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.OrchidSecurityManager
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.impl.compilers.markdown.FlexmarkModule
import com.eden.orchid.impl.compilers.pebble.PebbleModule
import com.google.inject.Guice

class TestOrchid {

    private lateinit var context: OrchidContext

    fun getContext(): OrchidContext {
        return context
    }

    fun executeTest(): TestResults {
        return try {
            Clog.i("Running Orchid test")
            context.start()
            context.finish()

            TestResults(
                context,
                context.resolve(TestRenderer::class.java).renderedPageMap,
                context.collections.map { TestRenderer.TestIndexedCollection(it) },
                true,
                null
            )
        } catch (t: Throwable) {
            Clog.e("An exception was thrown while running Orchid integration test: {}", t.message)
            t.printStackTrace()
            TestResults(null, emptyMap(), emptyList(), false, t)
        }
    }

    fun initializeModules(
        flags: MutableMap<String, Any>,
        config: Map<String, Any>,
        resources: List<(OrchidContext)->OrchidResource>,
        extraModules: List<OrchidModule>,
        serve: Boolean
    ) = apply {
        val actualFlags = HashMap(flags)
        if (!actualFlags.containsKey("environment")) {
            actualFlags["environment"] = "test"
        }
        if (!actualFlags.containsKey("baseUrl")) {
            actualFlags["baseUrl"] = "http://orchid.test"
        }
        if (!actualFlags.containsKey("dest")) {
            actualFlags["dest"] =  "./build/orchid/test"
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
        modules.add(TestResourceSource(resources).toModule())
        modules.add(TestConfigResourceSource(config).toModule())

        context = createContext(modules)
    }

    private fun createContext(modules: List<OrchidModule>): OrchidContext {
        var moduleLog = "\n--------------------"
        for (module in modules) {
            moduleLog += "\n * " + module.javaClass.name
        }
        moduleLog += "\n"
        Clog.d("Running test with the following modules")
        Clog.d(moduleLog)

        val injector = Guice.createInjector(modules)

        var flagLog = ""
        flagLog += "\n--------------------\n"
        flagLog += OrchidFlags.getInstance().printFlags()
        flagLog += "\n"
        Clog.d("Flag values")
        Clog.d(flagLog)

        val context = injector.getInstance(OrchidContext::class.java)

        try {
            val manager = injector.getInstance(OrchidSecurityManager::class.java)
            System.setSecurityManager(manager)
        } catch (e: Exception) {

        }
        return context
    }

}
