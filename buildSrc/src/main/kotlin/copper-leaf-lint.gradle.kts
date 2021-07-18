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
    // GPL
    allow("GPL-2.0")
    allow("GPL-3.0")
    allowUrl("http://www.gnu.org/copyleft/gpl.html") // jnr-posix
    allowUrl("http://www.gnu.org/licenses/gpl.txt") // plantuml
    allowUrl("http://www.gnu.org/licenses/gpl-2.0-standalone.html") // jruby

    // LGPL
    allow("LGPL-3.0")
    allowUrl("http://www.gnu.org/licenses/lgpl.html") // openhtmltopdf, jnr-posix (transitive dependency of jruby)
    allowUrl("http://www.gnu.org/licenses/lgpl-2.1-standalone.html") // jruby
    allowUrl("http://www.jcraft.com/jzlib/LICENSE.txt") // jzlib (transitive dependency of openhtmltopdf)

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
}
