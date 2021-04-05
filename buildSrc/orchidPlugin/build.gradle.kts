plugins {
    id("groovy")
    id("com.gradle.plugin-publish") version "0.11.0"
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}

group = rootProject.group
version = rootProject.version

pluginBundle {
    website = "https://orchid.run/"
    vcsUrl = "https://github.com/orchid-revival/Orchid"
    description = "A convenient DSL for setting up Orchid with your Gradle projects"
    tags = listOf("orchid", "javadoc")

    plugins {
        create("orchidPlugin") {
            id = "${project.group}.${project.name}"
            displayName = "Orchid Plugin"
        }
    }
}
