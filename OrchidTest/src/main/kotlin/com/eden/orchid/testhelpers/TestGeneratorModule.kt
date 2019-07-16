package com.eden.orchid.testhelpers

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule

class TestGeneratorModule(
    private val generatorClass: Class<out OrchidGenerator<*>>
) : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java, generatorClass)
    }
}

inline fun <reified T : OrchidGenerator<*>> withGenerator(): OrchidModule {
    return TestGeneratorModule(T::class.java)
}