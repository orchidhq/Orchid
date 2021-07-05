package com.eden.orchid.swiftdoc

import com.eden.orchid.sourcedoc.SourceDocModule
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Javadoc generator")
@EnabledOnOs(OS.MAC)
class NewSwiftdocGeneratorTest : OrchidIntegrationTest(SwiftdocModule(), SourceDocModule()) {

    @Test
    @DisplayName("Java files are parsed, and pages are generated for each class and package.")
    fun test01() {
        configObject(
            "swiftdoc",
            """
            |{
            |    "sourceDirs": "mockSwift",
            |    "showRunnerLogs": true
            |}
            |""".trimMargin()
        )

        expectThat(execute())
            // Module readme
            .pageWasRendered("/swiftdoc/index.html") { }

            // Swift sources
            .pageWasRendered("/swiftdoc/privateswiftclass/index.html") { }
            .pageWasRendered("/swiftdoc/swiftclass/index.html") { }
            .pageWasRendered("/swiftdoc/swiftclass/swiftclass/index.html") { }
            .pageWasRendered("/swiftdoc/swiftclasswithprivatemembers/index.html") { }
            .pageWasRendered("/swiftdoc/swiftclasswithprivatemembers/swiftclasswithprivatemembers/index.html") { }
            .pageWasRendered("/swiftdoc/swiftclasswithsuppressedmembers/index.html") { }
            .pageWasRendered("/swiftdoc/swiftclasswithsuppressedmembers/swiftclasswithsuppressedmembers/index.html") { }
            .pageWasRendered("/swiftdoc/swiftstruct/index.html") { }
            .pageWasRendered("/swiftdoc/swiftstruct/swiftstruct/index.html") { }
            .pageWasRendered("/swiftdoc/swiftsuppressedclass/index.html") { }

            // other
            .pageWasRendered("/assets/css/orchidSourceDoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }
}
