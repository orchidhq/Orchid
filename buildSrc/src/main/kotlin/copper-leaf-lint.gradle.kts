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
    val allValidLicenses = Config.license.compatibleWith + Config.license
    allValidLicenses.forEach { license ->
        allow(license.spdxIdentifier)
        license.urlAliases.forEach { url ->
            allowUrl(url)
        }
    }

    ignoreDependencies("org.atteo", "evo-inflector") {
        because("evo-inflector is Apache-2.0 on its Github page, but does not publish that to MavenCentral")
    }
    ignoreDependencies("xml-apis") {
        because("xml-apis publishes multiple licenses, but we can use it under Apache-2.0.")
    }
    ignoreDependencies("io.github.copper-leaf") {
        because("copper leaf core artifacts do not have licenses, for some reason. They're all BSD.")
    }
    ignoreDependencies("aopalliance") {
        because("aopalliance (a transitive dependency of Guice) is public domain and has no license.")
    }

    // python
    allowUrl("https://www.jython.org/Project/license.html") // jython
}
