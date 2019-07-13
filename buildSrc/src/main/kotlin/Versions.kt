/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val clog4j: String = "2.0.7" 

    const val com_eden_common: String = "1.11.4" 

    const val dokka_runner: String = "0.2.3" 

    const val groovydoc_runner: String = "0.2.3" 

    const val javadoc_runner: String = "0.2.3" 

    const val krow: String = "0.1.12" // available: "0.1.13"

    const val american_bible_society_client: String = "0.5.0" 

    const val eden: String = "0.4.0" 

    const val gradle_versions_plugin: String = "0.21.0" 

    const val codacy_coverage_reporter: String = "6.0.0" 

    const val guice: String = "4.2.2" 

    const val javax_inject: String = "1"

    const val gradle_bintray_plugin: String = "1.8.4" 

    const val toml4j: String = "0.7.2" 

    const val com_openhtmltopdf: String = "0.0.1-RC20" 

    const val okhttp: String = "3.14.2" 

    const val univocity_parsers: String = "2.8.2" 

    const val com_vladsch_flexmark: String = "0.42.12" // available: "0.50.4"

    const val commons_io: String = "2.6" 

    const val buildsrcversions: String = "0.3.2" 

    const val task_tree: String = "1.3.1" // available: "1.4"

    const val jsass: String = "5.8.0" 

    const val classgraph: String = "4.8.37" 

    const val pebble: String = "3.0.10" 

    const val strikt_core: String = "0.20.1" 

    const val validation_api: String = "2.0.1.Final" 

    const val jaxb_api: String = "2.3.0" // available: "2.4.0-b180830.0359"

    const val thumbnailator: String = "0.4.8" 

    const val plantuml: String = "1.2019.6" // available: "8059"

    const val gradle_git: String = "1.7.2" 

    const val commons_lang3: String = "3.9" 

    const val asciidoctorj: String = "2.0.0" 

    const val evo_inflector: String = "1.2.2" 

    const val javax_el: String = "3.0.1-b11" 

    const val hamcrest_library: String = "2.1" 

    const val hibernate_validator: String = "6.0.16.Final" 

    const val org_jetbrains_kotlin: String = "1.3.31" 

    const val json: String = "20180813" 

    const val jsoup: String = "1.12.1" 

    const val org_junit_jupiter: String = "5.4.2" 

    const val mockito_core: String = "2.28.2" 

    const val org_nanohttpd: String = "2.3.1" 

    const val pygments: String = "2.4.2" 

    const val jython_standalone: String = "2.7.1" 

    const val snakeyaml: String = "1.24" 

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.2.1"

        const val currentVersion: String = "5.4.1"

        const val nightlyVersion: String = "5.6-20190610000035+0000"

        const val releaseCandidate: String = "5.5-rc-2"
    }
}
