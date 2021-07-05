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
    allowUrl("http://www.gnu.org/licenses/lgpl.html") // openhtmltopdf, jnr-posix (transitive dependency of jruby)
    allowUrl("http://www.gnu.org/licenses/lgpl-2.1-standalone.html") // jruby
    allowUrl("http://www.jcraft.com/jzlib/LICENSE.txt") // jzlib (transitive dependency of openhtmltopdf)
    ignoreDependencies("org.jruby") {
        because("jruby publishes multiple licenses, including GPL. Orchid can use its LGPL license, but must ignore " +
                "its GPL license to pass Licensee checks.")
    }
    ignoreDependencies("com.github.jnr") {
        because("a jruby dependency publishes multiple licenses, including GPL. Orchid can use its LGPL license, but " +
                "must ignore its GPL license to pass Licensee checks.")
    }

    // Apache
    allow("Apache-2.0")
    ignoreDependencies("org.atteo", "evo-inflector") {
        because("evo-inflector is Apache-2.0 on its Github page, but does not publish that to MavenCentral")
    }
    ignoreDependencies("xml-apis") {
        because("xml-apis publishes multiple licenses, but we can use it under Apache-2.0.")
    }

    // MIT
    allow("MIT")
    allowUrl("http://opensource.org/licenses/MIT") // classgraph
    allowUrl("http://www.opensource.org/licenses/mit-license.php") // toml4j, slf4j
    allowUrl("http://www.opensource.org/licenses/mit-license.html") // thumbnailator
    allowUrl("https://raw.githubusercontent.com/bit3/jsass/master/LICENSE") // jsass
    allowUrl("https://github.com/mockito/mockito/blob/main/LICENSE") // mockito
    allowUrl("http://json.org/license.html") // org.json
    allowUrl("https://jsoup.org/license") // jsoup

    // BSD
    allow("BSD-3-Clause")
    allowUrl("http://opensource.org/licenses/BSD-2-Clause") // flexmark
    allowUrl("http://opensource.org/licenses/BSD-3-Clause") // pebble
    allowUrl("http://www.opensource.org/licenses/BSD-2-Clause") // pygments
    allowUrl("http://www.opensource.org/licenses/bsd-license.php") // hamcrest
    ignoreDependencies("io.github.copper-leaf") {
        because("copper leaf core artifacts do not have licenses, for some reason. They're all BSD.")
    }

    // EPL
    allow("EPL-1.0")
    allowUrl("https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt")
    allowUrl("https://www.eclipse.org/legal/epl-v20.html") // junit
    allowUrl("http://www.eclipse.org/legal/epl-v20.html") // jruby
    allowUrl("https://www.eclipse.org/legal/epl-2.0/") // junit

    // public domain (no license)
    ignoreDependencies("aopalliance") {
        because("aopalliance (a transitive dependency of Guice) is public domain and has no license.")
    }

    // python
    allowUrl("https://www.jython.org/Project/license.html") // jython

    // CDDL
    allowUrl("https://github.com/javaee/javax.annotation/blob/master/LICENSE") // java.annotation-api

    // GPL
    ignoreDependencies("net.sourceforge.plantuml") {
        because(
            """
            Ignoring to make sure other modules pass validation. PlantUML is GPL which is **NOT** a valid license for
            Orchid under its LGPL license. THIS NEEDS TO BE FIXED BEFORE 1.0.0 RELEASE!
            
            Note that PlantUML also publishes JARs under other licenses (with reduces features that omit its 
            dependencies on other GPL libraries). Orchid may need to try to bundle the LGPL jar directly with itself 
            rather than using the GPL-licensed version on MavenCentral, or something similar.
            """.trimIndent()
        )
    }
}
