package com.eden.orchid.testhelpers

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.SuppressedWarnings
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.HashMap

open class OrchidIntegrationTest(
    vararg standardAdditionalModules: OrchidModule
) : BaseOrchidTest() {

    private var serve = false

    private lateinit var flags: MutableMap<String, Any>
    private lateinit var config: MutableMap<String, Any>
    private lateinit var resources: MutableMap<String, Pair<String, Map<String, Any>>>

    private val standardAdditionalModules: Set<OrchidModule> = setOf(*standardAdditionalModules)

    @BeforeEach
    fun integrationTestSetUp() {
        flags = HashMap()
        config = HashMap()
        resources = HashMap()
        serve = false
    }

    @AfterEach
    fun integrationTestTearDown() {
        flags = mutableMapOf()
        config = mutableMapOf()
        resources = mutableMapOf()
        serve = false
    }

    protected fun flag(flag: String, value: Any) {
        flags[flag] = value
    }

    @Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
    protected fun configObject(flag: String, json: String) {
        if (config.containsKey(flag)) {
            val o = config[flag]
            if (o is Map<*, *>) {
                config[flag] = EdenUtils.merge(o as Map<String, *>, JSONObject(json).toMap())
            } else {
                config[flag] = JSONObject(json).toMap()
            }
        } else {
            config[flag] = JSONObject(json).toMap()
        }
    }

    protected fun configArray(flag: String, json: String) {
        config[flag] = JSONArray(json).toList()
    }

    protected fun resource(path: String, content: String, json: String) {
        resource(path, content, JSONObject(json).toMap())
    }

    @JvmOverloads
    protected fun resource(path: String, content: String = "", data: Map<String, Any> = HashMap()) {
        resources[path] = Pair(content, data)
    }

    protected fun serveOn(port: Int) {
        enableLogging()

        flag("task", "serve")
        flag("port", port)
        flag("src", "./src")
        flag("dest", "./build/orchid/test")
        this.serve = true
    }

// Execute test runner
//----------------------------------------------------------------------------------------------------------------------

    protected fun execute(vararg modules: OrchidModule): TestResults {
        return TestOrchid().runTest(
            flags,
            config,
            resources,
            listOf(*modules, *standardAdditionalModules.toTypedArray()),
            serve
        )
    }

}
