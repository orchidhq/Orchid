rootProject.name = "buildSrc"

include(":orchidPlugin")

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("./../libs.versions.toml"))
        }
        create("testLibs") {
            from(files("./../test-libs.versions.toml"))
        }
        create("buildscriptLibs") {
            from(files("./../buildscript-libs.versions.toml"))
        }
    }
}
