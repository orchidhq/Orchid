
dependencies {

}

project.tasks.getByName("assemble").dependsOn("widget:assemble")

project.tasks.processResources {
    from("${project("widget").buildDir}/libs/Orchid-widget.js") {
        into("assets/js")
        rename(".*", "orchidSearch-kt.js")
    }
}
