plugins {
    id("org.jlleitschuh.gradle.ktlint")
    id("app.cash.licensee")
}

ktlint {
    debug.set(false)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    additionalEditorconfigFile.set(file("$rootDir/.editorconfig"))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

@Suppress("HttpUrlsUsage")
licensee {
    // LGPL
    allow("LGPL-3.0")

    // Apache
    allow("Apache-2.0")

    // MIT
    allow("MIT")
    allowUrl("http://opensource.org/licenses/MIT") // classgraph
    allowUrl("http://www.opensource.org/licenses/mit-license.php") // toml4j, slf4j
    allowUrl("http://www.opensource.org/licenses/mit-license.html") // thumbnailator
    allowUrl("https://raw.githubusercontent.com/bit3/jsass/master/LICENSE") // jsass
    allowUrl("http://json.org/license.html") // org.json
    allowUrl("https://jsoup.org/license") // jsoup

    // BSD
    allow("BSD-3-Clause")
    allowUrl("http://opensource.org/licenses/BSD-2-Clause") // flexmark
    allowUrl("http://opensource.org/licenses/BSD-3-Clause") // pebble
    allowUrl("http://www.opensource.org/licenses/bsd-license.php") // hamcrest
    ignoreDependencies("io.github.copper-leaf") {
        because("copper leaf core artifacts do not have licenses, for some reason. They're all BSD.")
    }

    // EPL
    allow("EPL-1.0")
    allowUrl("https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt")
}
