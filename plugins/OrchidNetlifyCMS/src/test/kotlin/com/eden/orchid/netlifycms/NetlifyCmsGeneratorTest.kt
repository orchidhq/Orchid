package com.eden.orchid.netlifycms

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of NetlifyCMS generator")
class NetlifyCmsGeneratorTest : OrchidIntegrationTest(NetlifyCmsModule(), withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Kotlin and Java files are parsed, and pages are generated for each class and package.")
    fun test01() {
        configObject("netlifyCms", """ {"config": {"backend": {"name":"git-gateway"}}} """)

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/admin/index.html")
        expectThat(testResults).pageWasRendered("/admin/config.yml")
    }

}
