dependencies {
    compile(project(":OrchidCore"))
    compile(project(":plugins:OrchidPosts"))
    compile(project(":plugins:OrchidPages"))
    compile(project(":plugins:OrchidForms"))
    compile(project(":plugins:OrchidTaxonomies"))
    (rootProject.ext.get("languageExtensionProjects") as Iterable<Project>).forEach { compile(it) }
}
